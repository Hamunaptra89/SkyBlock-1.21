package fr.halbrand.skyblock.addons.custom.mecanics.mining;

import fr.halbrand.skyblock.Main;
import fr.halbrand.skyblock.utils.tools.enums.EnumStats;
import fr.halbrand.skyblock.utils.files.data.PStats;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.*;
import java.util.*;
import java.util.concurrent.*;

public class MiningManager implements Listener {

    private final ConcurrentMap<Player, BukkitTask> miningTasks = new ConcurrentHashMap<>();
    private final ConcurrentMap<Player, Block> blockBeingMined = new ConcurrentHashMap<>();
    private final ConcurrentMap<Player, Long> blockFinishMined = new ConcurrentHashMap<>();
    private final ConcurrentMap<Player, Long> nextPhase = new ConcurrentHashMap<>();
    private final Map<Location, Integer> animationEntityIds = new HashMap<>();
    private final Map<Material, Integer> hardness = Map.of(
            Material.STONE, 15,
            Material.COBBLESTONE, 20,
            Material.OBSIDIAN, 500
    );

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private final Random random = new Random();

    public MiningManager() {
        manager.addPacketListener(new PacketAdapter(Main.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                if (e.getPlayer() == null || e.getPlayer().getGameMode() != GameMode.SURVIVAL) {
                    return;
                }

                Player p = e.getPlayer();
                PacketContainer packet = e.getPacket();
                EnumWrappers.PlayerDigType type = packet.getPlayerDigTypes().read(0);
                BlockPosition pos = packet.getBlockPositionModifier().read(0);
                Block b = p.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());

                switch (type) {
                    case START_DESTROY_BLOCK -> start(p, b);
                    case ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK -> cancel(p);
                    default -> {}
                }
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        cancel(e.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        cancel(e.getPlayer());
    }

    public void cancel(Player p) {
        Block block = blockBeingMined.remove(p);
        if (block != null) {
            removeBlockStage(block.getLocation(), p);
        }
        blockFinishMined.remove(p);
        nextPhase.remove(p);
        BukkitTask task = miningTasks.remove(p);
        if (task != null) {
            task.cancel();
        }
    }

    public void start(Player p, Block b) {
        PStats stats = new PStats(p);
        Material blockType = b.getType();

        if (b.getType() == Material.AIR || p.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        double blockHardness = hardness.getOrDefault(blockType, 100);
        double MINING_SPEED = stats.get(EnumStats.MINING_SPEED);
        double delaySeconds = (blockHardness) / (MINING_SPEED);

        if (delaySeconds <= 0) {
            b.breakNaturally();
            return;
        }

        blockBeingMined.put(p, b);
        long finishTime = System.currentTimeMillis() + (long) (delaySeconds * 1000L);
        blockFinishMined.put(p, finishTime);
        nextPhase.put(p, System.currentTimeMillis() + 100L);

        int animationEntityId = generateUniqueEntityId(b.getLocation());
        animationEntityIds.put(b.getLocation(), animationEntityId);

        BukkitTask miningTask = new BukkitRunnable() {
            private int lastStage = -1;

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long timeLeft = finishTime - currentTime;

                if (!blockBeingMined.containsKey(p) || b.getType() == Material.AIR) {
                    terminateAnimation(b.getLocation(), p);
                    cancel();
                    return;
                }

                double progress = 1.0 - ((double) timeLeft / (delaySeconds * 1000));
                int currentStage = Math.min((int) (progress * 10), 9);

                if (currentStage != lastStage) {
                    sendBlockDamage(p, b.getLocation(), currentStage, animationEntityId);
                    lastStage = currentStage;
                }

                if (timeLeft <= 0) {
                    b.breakNaturally();
                    p.playSound(b.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3f, 1f);
                    b.getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation().add(0.5, 0.5, 0.5), 5, 0.2, 0.2, 0.2, 0.05, b.getBlockData());
                    terminateAnimation(b.getLocation(), p);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

        miningTasks.put(p, miningTask);
    }

    private int generateUniqueEntityId(Location loc) {
        return loc.hashCode() ^ random.nextInt(1000);
    }

    private void sendBlockDamage(Player player, Location location, int stage, int entityId) {
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packet.getIntegers().write(0, entityId).write(1, stage);
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.toVector()));

        try {
            manager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void terminateAnimation(Location location, Player player) {
        Integer entityId = animationEntityIds.remove(location);
        if (entityId != null) {
            sendBlockDamage(player, location, -1, entityId);
        }
    }

    private void removeBlockStage(Location loc, Player player) {
        terminateAnimation(loc, player);
    }
}