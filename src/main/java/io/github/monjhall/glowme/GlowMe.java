package io.github.monjhall.glowme;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

public class GlowMe extends JavaPlugin {

	@Override
	public void onEnable() {
		
		// This method should contain all the logic to be performed when the plugin is enabled.
		
		// Set the commands to be executed by the command executor.
		GlowMeCommandExecutor commandExecutor = new GlowMeCommandExecutor(this);
		this.getCommand("glow").setExecutor(commandExecutor);
		this.getCommand("clearglow").setExecutor(commandExecutor);
		
		// Create a scheduler to run tasks.
		BukkitScheduler scheduler = getServer().getScheduler();
		
		// Schedule a task to be repeatedly run and create it.
		// This task will check if player's need to be removed from teams, since their glow is running out.
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			
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
						if (activeEffect.getDuration() <= 15) removePlayerFromTeam(activePlayer);
						
					}
				}
			}
		}, 0L, 10L);
	}
	
	@Override 
	public void onDisable() {
		// This method should contain all the logic to be performed when the plugin is disabled.
	}
	
	protected void setGlow(Player player, int duration, ChatColor color) {
		
		// Create the glow effect type.
		PotionEffectType glowEffectType = PotionEffectType.getById(24);
		
		// Create the glow effect.
		PotionEffect glowEffect = new PotionEffect(glowEffectType, duration, 1);
		
		// If the player has a glow effect already, remove it.
		if (player.hasPotionEffect(PotionEffectType.getById(24))) {
			player.removePotionEffect(PotionEffectType.getById(24));
			removePlayerFromTeam(player);
		}
		
		// Add the player to the specified team.
		addPlayerToTeam(player, color);
		
		// Apply the glow to the provided player.
		glowEffect.apply(player);
	}
	
	protected void clearGlow(Player player) {
		
		if (player.hasPotionEffect(PotionEffectType.getById(24))) {
			player.removePotionEffect(PotionEffectType.getById(24));
			removePlayerFromTeam(player);
		}
		
	}
	
	private void removePlayerFromTeam(Player player) {
		
		// Get the scoreboard manager, get the main scoreboard, and set the new teams.
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard mainBoard = manager.getMainScoreboard();
		Team playerTeam = mainBoard.getPlayerTeam(player);
		playerTeam.removePlayer(player);
	}
	
	private void addPlayerToTeam(Player player, ChatColor color) {
		
		// Get the scoreboard manager, create a scoreboard, and set the new teams.
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard mainBoard = manager.getMainScoreboard();
		
		// Check if the specific colored team already exists.
		Team playerTeam;
		if (mainBoard.getTeam(color.toString()) == null) {
			playerTeam = mainBoard.registerNewTeam(color.toString());
		} else {
			playerTeam = mainBoard.getTeam(color.toString());
		}
		playerTeam.addPlayer(player);
		playerTeam.setPrefix(color + "");
	}
	
}
