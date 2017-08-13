package io.github.monjhall.glowme;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class checkActiveEffects extends BukkitRunnable {
	
	private final GlowMe plugin;
	
	public checkActiveEffects(GlowMe plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		
		// Collection of all online players.
		Collection<Player> onlinePlayers = (Collection<Player>) Bukkit.getServer().getOnlinePlayers();
		
		// Iterate through the collection of players, looking at their active effects.
		for (Iterator<Player> playerIterator = onlinePlayers.iterator();
				playerIterator.hasNext();) {
			
			// Create a collection of the current player's active effects.
			Player activePlayer = playerIterator.next();
			Collection<PotionEffect> playerActiveEffects = activePlayer.getActivePotionEffects();
			
			// If the active player isn't glowing, ignore them.
			if (!(activePlayer.hasPotionEffect(PotionEffectType.getById(24)))) continue;
			
			// If they are glowing, check the time on the effect.
			for (Iterator<PotionEffect> effectsIterator = playerActiveEffects.iterator();
					effectsIterator.hasNext();) {
				
				// Verify the active effect is the glow.
				PotionEffect activeEffect = effectsIterator.next();
				if (activeEffect.getType() != PotionEffectType.getById(24)) continue;
				
				// If the glow is about to run out, remove the player from the team.
				if (activeEffect.getDuration() <= 15) plugin.removePlayerFromTeam(activePlayer);
				
			}
		}
	}

}
