package io.github.monjhall.glowme;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class GlowMeCommandExecutor implements CommandExecutor {
	private final GlowMe plugin;

	// Constructor.
	public GlowMeCommandExecutor(GlowMe plugin) {
		this.plugin = plugin;
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

				// Verify that a proper color was provided.
				ChatColor teamColor;
				try {
					teamColor = ChatColor.valueOf(args[0].toUpperCase());
				} catch (IllegalArgumentException e) {
					sender.sendMessage("Color must be one of the allowed scoreboard colors.");
					return false;
				}

				if (teamColor.isFormat()) {
					sender.sendMessage("Color must be a color, not a format code.");
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

				// Notify the user of any duration mistakes.
				if (duration < -1) {
					sender.sendMessage("Duration must be a value between 1 and 1500 or a value of -1.");
					return false;
				} else if (duration == 0) {
					sender.sendMessage("Duration must be a value between 1 and 1500 or a value of -1."
							+ "\nIf you are trying to clear a glow, use the /clearglow command.");
					return false;
				} else if (duration > 1500) {
					sender.sendMessage("Duration must be a value between 1 and 1500 or a value of -1."
							+ "\nIf you are trying to apply infinite glow, use a duration of -1.");
					return false;
				} else if (duration == -1) {
					// Set duration to a large number so it'll be infinite.
					duration = 100000;
				} else {
					// Do nothing...
				}

				plugin.setGlow((Player) sender, duration, teamColor);
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
				
				// Verify duration is an integer, otherwise catch the exception.
				int duration;
				try {
					duration = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage("Duration must be a number, proper usage below.");
					return false;
				}
				
				// Notify the user of any duration mistakes.
				if (duration < -1) {
					sender.sendMessage("Duration must be a value between 1 and 1500 or a value of -1.");
					return false;
				} else if (duration == 0) {
					sender.sendMessage("Duration must be a value between 1 and 1500 or a value of -1."
							+ "\nIf you are trying to clear a glow, use the /clearglow command.");
					return false;
				} else if (duration > 1500) {
					sender.sendMessage("Duration must be a value between 1 and 1500 or a value of -1."
							+ "\nIf you are trying to apply infinite glow, use a duration of -1.");
					return false;
				} else if (duration == -1) {
					// Set duration to a large number so it'll be infinite.
					duration = 100000;
				} else {
					// Do nothing...
				}

				plugin.setGlow(target, duration, teamColor);

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
				plugin.clearGlow((Player) sender);
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
				plugin.clearGlow(target);

			}

			// If there is more than one arguments, return false.
			if (args.length > 1) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}

			return true;
		}
		
		// If using the command setglowconfig...
		if (cmd.getName().equalsIgnoreCase("setglowconfig")) {
			
			// Constant array of event types.
			Collection<String> eventTypes = Arrays.asList("ITEMCRAFT", "ITEMCONSUME", "ITEMPICKUP", "TELEPORTCAUSE", "ENTITYKILLED");
			
			// Variables for the final config setting.
			String eventType = "";
			String eventCause = "";
			String color = null;
			int duration = 0;
			
			// If there are less than four arguments, return false.
			if (args.length < 4) {
				sender.sendMessage("Too few arguments, proper usage below.");
				return false;
			}
			
			// If there are more than four arguments, return false.
			if (args.length > 4) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}
			
			// If the event type is invalid, return false.
			if (!eventTypes.contains(args[0].toUpperCase())) {
				sender.sendMessage("The event must be a valid event type from the config!");
				return false;
			}
			
			// Set the event type, then continue.
			if (args[0].toUpperCase().equals("ITEMCRAFT")) eventType = "ItemCraft";
			if (args[0].toUpperCase().equals("ITEMCONSUME")) eventType = "ItemConsume";
			if (args[0].toUpperCase().equals("ITEMPICKUP")) eventType = "ItemPickup";
			if (args[0].toUpperCase().equals("TELEPORT")) eventType = "Teleport";
			if (args[0].toUpperCase().equals("ENTITYKILLED")) eventType = "EntityKilled";
			
			// Verify the second argument (Material, Teleport Cause, or Entity).
			if (eventType == "ItemCraft" || eventType == "ItemConsume" || eventType == "ItemPickup") {
				
				// If the argument is not a material, return false.
				try {
					eventCause = Material.matchMaterial(args[1]).toString();
				} catch (NullPointerException e) {
					sender.sendMessage("The material you're trying to add doesn't exist! Check the list of valid materials.");
					return false;
				}
			} else if (eventType == "Teleport") {
				
				// If the argument is not a teleport cause, return false.
				try {
					eventCause = TeleportCause.valueOf(args[1]).toString();
				} catch (IllegalArgumentException e) {
					sender.sendMessage("The teleport cause you're trying to add doesn't exist! Check the list of valid teleport causes.");
					return false;
				}		
			} else {
				
				// If the argument is not an entity type, return false.
				try {
					eventCause = EntityType.valueOf(args[1]).toString();
				} catch (IllegalArgumentException e) {
					sender.sendMessage("The entity you're trying to add doesn't exist! Check the list of valid entities.");
					return false;
				}
			}
			
			// Verify that a proper color was provided.
			try {
				ChatColor.valueOf(args[2].toUpperCase());
			} catch (IllegalArgumentException e) {
				sender.sendMessage("Color must be one of the allowed scoreboard colors.");
				return false;
			}

			if (ChatColor.valueOf(args[2].toUpperCase()).isFormat()) {
				sender.sendMessage("Color must be a color, not a format code.");
				return false;
			}
			
			// Reset the color to the string, since it works!
			color = args[2].toUpperCase();
			
			// Verify duration is an integer, otherwise catch the exception.
			try {
				duration = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				sender.sendMessage("Duration must be a number, proper usage below.");
				return false;
			}
			
			// Notify the user of any duration mistakes.
			if (duration < -1) {
				sender.sendMessage("Duration must be a value between 0 and 1500 or a value of -1.");
				return false;
			} else if (duration > 1500) {
				sender.sendMessage("Duration must be a value between 0 and 1500 or a value of -1."
						+ "\nIf you are trying to apply infinite glow, use a duration of -1.");
				return false;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}
			
			// Set the new config!
			sender.sendMessage("New configuration has been set!");
			plugin.getConfig().set(eventType + "." + eventCause + "." + "Color", color.toString());
			plugin.getConfig().set(eventType + "." + eventCause + "." + "Duration", duration);
			plugin.saveConfig();
			
			return true;
		}

		// Return false if the function failed.
		plugin.getLogger().info("No command found.");
		return false;
	}

}
