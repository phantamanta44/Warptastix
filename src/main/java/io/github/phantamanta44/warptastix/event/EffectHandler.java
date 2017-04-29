package io.github.phantamanta44.warptastix.event;

import io.github.phantamanta44.warptastix.Warptastix;
import io.github.phantamanta44.warptastix.config.WTXConfig;
import io.github.phantamanta44.warptastix.util.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectHandler implements Listener {

    public static final MaterialData TP_EFF_MAT = new MaterialData(Material.PORTAL);

    private final Map<UUID, Pair<GameMode, BukkitTask>> effected;

    public EffectHandler() {
        effected = new HashMap<>();
    }

    public void teleport(Player player, Location dest) {
        UUID id = player.getUniqueId();
        BukkitTask task = Bukkit.getServer().getScheduler().runTaskLater(Warptastix.INSTANCE, () -> {
            Pair<GameMode, BukkitTask> eff = effected.remove(id);
            if (eff != null)
                player.setGameMode(eff.getA());
            dest.getWorld().playSound(dest, Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
            dest.getWorld().spawnParticle(Particle.SMOKE_NORMAL, dest, 12);
            dest.getWorld().spawnParticle(Particle.BLOCK_CRACK, dest, 18, TP_EFF_MAT);
        }, WTXConfig.EFFECT.getDuration());
        if (effected.containsKey(id)) {
            Pair<GameMode, BukkitTask> eff = effected.get(id);
            eff.getB().cancel();
            effected.put(id, Pair.of(eff.getA(), task));
        } else {
            effected.put(id, Pair.of(player.getGameMode(), task));
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
        player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 12);
        player.getWorld().spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 18, TP_EFF_MAT);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.addPotionEffect(
                new PotionEffect(PotionEffectType.BLINDNESS, WTXConfig.EFFECT.getDuration() + 20, 9, true, false));
        player.teleport(dest);
        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (WTXConfig.EFFECT.isEnabled()) {
            if (effected.containsKey(event.getPlayer().getUniqueId()))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (WTXConfig.EFFECT.isEnabled()) {
            Pair<GameMode, BukkitTask> eff = effected.remove(event.getPlayer().getUniqueId());
            if (eff != null) {
                event.getPlayer().setGameMode(eff.getA());
                eff.getB().cancel();
            }
        }
    }

    public void clear() {
        effected.forEach((id, eff) -> {
            Player pl = Bukkit.getServer().getPlayer(id);
            if (pl != null) {
                pl.setGameMode(eff.getA());
                pl.getWorld().playSound(pl.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
                pl.getWorld().spawnParticle(Particle.SMOKE_NORMAL, pl.getLocation(), 12);
                pl.getWorld().spawnParticle(Particle.BLOCK_CRACK, pl.getLocation(), 18, TP_EFF_MAT);
            }
            eff.getB().cancel();
        });
        effected.clear();
    }

}
