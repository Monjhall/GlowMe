package io.github.monjhall.glowme;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffectType;

public class GlowMeCommandExecutor implements CommandExecutor {

	// Class variables.
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

			// If there are more than three arguments, return false.
			if (args.length > 3) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}

			// Variables for the method.
			Player target = null;
			String argumentColor, argumentDuration;
			argumentColor = argumentDuration = "";

			// If there are only 2 arguments, verify the sender and set a target.
			if (args.length == 2) {

				// If the sender is the console, return false.
				if (!(sender instanceof Player)) {
					sender.sendMessage("Using the command through the console requires all three arguments.");
					return false;
				}

				// Otherwise, set the parameters.
				else {
					target = (Player) sender;
					argumentColor = args[0];
					argumentDuration = args[1];
				}
			}

			// If there are 3 arguments, verify the target and set it.
			if (args.length == 3) {

				// If the target is not online, return false!
				if (Bukkit.getServer().getPlayer(args[0]) == null) {
					sender.sendMessage(args[0] + " is not online!");
					return false;
				}

				// Otherwise, set the parameters.
				else {
					target = Bukkit.getServer().getPlayer(args[0]);
					argumentColor = args[1];
					argumentDuration = args[2];
				}
			}

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(argumentColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				sender.sendMessage(colorVerification.getErrorMessage());
				return false;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(argumentDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				sender.sendMessage(durationVerification.getErrorMessage());
				return false;
			}

			plugin.setGlow(target, durationVerification.getVerifiedDuration(),
					ChatColor.valueOf(colorVerification.getVerifiedColor()));
			return true;
		}

		// If using the command clearglow...
		if (cmd.getName().equalsIgnoreCase("clearglow")) {

			// If there is more than 1 argument, return false.
			if (args.length > 1) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}

			// Variables for the method.
			Player target = null;

			// If there are no arguments, verify and set the target.
			if (args.length == 0) {

				// If the sender is the console, return false.
				if (!(sender instanceof Player)) {
					sender.sendMessage("Using the command through the console requires a specified player.");
					return false;
				}

				// Otherwise, set the target.
				else {
					target = (Player) sender;
				}
			}

			// If there is 1 argument, verify and set the target.
			if (args.length == 1) {

				// If the target is not online, return false!
				if (Bukkit.getServer().getPlayer(args[0]) == null) {
					sender.sendMessage(args[0] + " is not online!");
					return false;
				}

				// Otherwise, set the target.
				else {
					target = Bukkit.getServer().getPlayer(args[0]);
				}

			}

			// If the player has no glow, warn the user.
			if (!target.hasPotionEffect(PotionEffectType.getById(24))) {

				// If the sender is also the target, use "you."
				if (target == sender) {
					sender.sendMessage("You have no active glow!");
				}

				// Otherwise, say "player."
				else {
					sender.sendMessage(args[0] + " has no active glow!");
				}

				return false;
			}

			plugin.clearGlow(target);
			return true;
		}

		// If using the command setglowconfig...
		if (cmd.getName().equalsIgnoreCase("setglowconfig")) {

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

			// Constant array of event types.
			Collection<String> eventTypes = Arrays.asList("ITEMCRAFT", "ITEMCONSUME", "ITEMPICKUP", "TELEPORTCAUSE",
					"ENTITYKILLED");

			// Variables for the method.
			String argumentEventType, argumentEventCause, argumentColor, argumentDuration, verifiedEventType,
					verifiedEventCause;
			argumentEventType = args[0];
			argumentEventCause = args[1];
			argumentColor = args[2];
			argumentDuration = args[3];
			verifiedEventType = verifiedEventCause = "";

			// If the event type is invalid, return false.
			if (!eventTypes.contains(argumentEventType.toUpperCase())) {
				sender.sendMessage("The event must be a valid event type from the config!");
				return false;
			}

			// Set the event type, then continue.
			if (argumentEventType.toUpperCase().equals("ITEMCRAFT"))
				verifiedEventType = "ItemCraft";
			if (argumentEventType.toUpperCase().equals("ITEMCONSUME"))
				verifiedEventType = "ItemConsume";
			if (argumentEventType.toUpperCase().equals("ITEMPICKUP"))
				verifiedEventType = "ItemPickup";
			if (argumentEventType.toUpperCase().equals("TELEPORT"))
				verifiedEventType = "Teleport";
			if (argumentEventType.toUpperCase().equals("ENTITYKILLED"))
				verifiedEventType = "EntityKilled";

			// Verify the second argument (Material, Teleport Cause, or Entity).
			if (verifiedEventType == "ItemCraft" || verifiedEventType == "ItemConsume"
					|| verifiedEventType == "ItemPickup") {

				// If the argument is not a material, return false.
				try {
					verifiedEventCause = Material.matchMaterial(argumentEventCause).toString();
				} catch (NullPointerException e) {
					sender.sendMessage(
							"The material you're trying to add doesn't exist! Check the list of valid materials.");
					return false;
				}
			} else if (verifiedEventType == "Teleport") {

				// If the argument is not a teleport cause, return false.
				try {
					verifiedEventCause = TeleportCause.valueOf(args[1]).toString();
				} catch (IllegalArgumentException e) {
					sender.sendMessage(
							"The teleport cause you're trying to add doesn't exist! Check the list of valid teleport causes.");
					return false;
				}
			} else {

				// If the argument is not an entity type, return false.
				try {
					verifiedEventCause = EntityType.valueOf(args[1]).toString();
				} catch (IllegalArgumentException e) {
					sender.sendMessage(
							"The entity you're trying to add doesn't exist! Check the list of valid entities.");
					return false;
				}
			}

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(argumentColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				sender.sendMessage(colorVerification.getErrorMessage());
				return false;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(argumentDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				sender.sendMessage(durationVerification.getErrorMessage());
				return false;
			}

			// Set the new config!
			sender.sendMessage("New configuration has been set!");
			plugin.getConfig().set(verifiedEventType + "." + verifiedEventCause + "." + "Color",
					colorVerification.getVerifiedColor());
			plugin.getConfig().set(verifiedEventType + "." + verifiedEventCause + "." + "Duration",
					durationVerification.getVerifiedDuration());
			plugin.saveConfig();

			return true;
		}

		// Return false if the function failed.
		plugin.getLogger().info("No command found.");
		return false;
	}

}
