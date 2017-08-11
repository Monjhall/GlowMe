package io.github.monjhall.glowme;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

public class GlowMe extends JavaPlugin {

	@Override
	public void onEnable() {
		// This method should contain all the logic to be performed when the plugin is enabled.
		
		// Simple log of onEnable being invoked.
		getLogger().info("onEnable has been invoked!");
	}
	
	@Override 
	public void onDisable() {
		// This method should contain all the logic to be performed when the plugin is enabled.
		
		// Simple log of onDisable being invoked.
		getLogger().info("onDisable has been invoked!");
	}
	
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
		
		// Return false if the function failed.
		getLogger().info("Failure reached after argument checking.");
		return false;
		
	}
	
	private void setGlow(Player player, int duration, ChatColor color) {
		
		// Create the glow effect type.
		PotionEffectType glowEffectType = PotionEffectType.getById(24);
		
		// Create the glow effect.
		PotionEffect glowEffect = new PotionEffect(glowEffectType, duration, 1);
		
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
		
		// Apply the glow to the provided player.
		glowEffect.apply(player);
	}
}
