package io.github.monjhall.glowme;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
	
	/*
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// If using the command glow...
		if (cmd.getName().equalsIgnoreCase("glow")) {
			
			// If there are less than 2 arguments, return false.
			if (args.length < 2) {
				sender.sendMessage("Too few arguments, proper usage below.");
				return false;
			}
			
			// If there are only 2 arguments, set the glow on the sender.
			if (args.length == 2) {
				
				// If the sender is the console, return false.
				if (!(sender instanceof Player)) {
					sender.sendMessage("Using the command through the console requires all three arguments.");
					return false;
				}
				
				// If the sender is a player, set the glow on the sender.
				
				// Verify duration is an integer, otherwise catch the exception.
				int duration;
				try {
					duration = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					sender.sendMessage("Duration must be a number, proper usage below.");
					return false;
				}
				
				// Verify that a proper color was provided.
				ChatColor teamColor;
				try {
					teamColor = ChatColor.valueOf(args[1].toUpperCase());
				} catch (IllegalArgumentException e) {
					sender.sendMessage("Color must be one of the allowed scoreboard colors.");
					return false;
				}
				
				if (teamColor.isFormat()) {
					sender.sendMessage("Color must be a color, not a format code.");
					return false;
				}
				
				setGlow((Player) sender, duration * 20, teamColor);
			}
			
			// If there are three arguments, set the glow on the specified player.
			if (args.length == 3) {
				
				// Record the target player.
				Player target = (Bukkit.getServer().getPlayer(args[0]));
				
				// If the target is not online, return false!
				if (target == null) {
					sender.sendMessage(args[0] + " is not online!");
			        return false;
				}
				
				// Verify duration is an integer, otherwise catch the exception.
				int duration;
				try {
					duration = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					sender.sendMessage("Duration must be a number, proper usage below.");
					return false;
				}
				
				// Verify that a proper color was provided.
				ChatColor teamColor;
				try {
					teamColor = ChatColor.valueOf(args[2].toUpperCase());
				} catch (IllegalArgumentException e) {
					sender.sendMessage("Color must be one of the allowed scoreboard colors.");
					return false;
				}
				
				if (teamColor.isFormat()) {
					sender.sendMessage("Color must be a color, not a format code.");
					return false;
				}
				
				setGlow(target, duration * 20, teamColor);
				
			}
			
			// If there are more than three arguments, return false.
			if (args.length > 3) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}
			
			return true;
		}
		
		// If using the command clearglow...
		if (cmd.getName().equalsIgnoreCase("clearglow")) {
			if (args.length == 0) {
				
				// If the sender is the console, return false.
				if (!(sender instanceof Player)) {
					sender.sendMessage("Using the command through the console requires a specified player.");
					return false;
				}
				
				// If the player has no glow, warn the user.
				if (!(((Player) sender).hasPotionEffect(PotionEffectType.getById(24)))) {
					sender.sendMessage("You have no active glow!");
					return false;
				}
				
				// Otherwise, clear their glow.
				clearGlow((Player) sender);
			}
			
			if (args.length == 1) {
				
				// Record the target player.
				Player target = (Bukkit.getServer().getPlayer(args[0]));
				
				// If the target is not online, return false!
				if (target == null) {
					sender.sendMessage(args[0] + " is not online!");
			        return false;
				}
				
				// If the player has no glow, warn the user.
				if (!(((Player) sender).hasPotionEffect(PotionEffectType.getById(24)))) {
					sender.sendMessage(args[0] + " has no active glow!");
					return false;
				}
				
				// Otherwise, clear that player's glow.
				clearGlow(target);
				
			}
			
			// If there is more than one arguments, return false.
			if (args.length > 1) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}
			
			return true;
		}
		
		// Return false if the function failed.
		getLogger().info("No command found.");
		return false;
		
	}
	*/
	
	protected void setGlow(Player player, int duration, ChatColor color) {
		
		// Create the glow effect type.
		PotionEffectType glowEffectType = PotionEffectType.getById(24);
		
		// Create the glow effect.
		PotionEffect glowEffect = new PotionEffect(glowEffectType, duration, 1);
		
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
