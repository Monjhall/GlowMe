package io.github.monjhall.glowme;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;

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

		// If using the command removeglowconfig...
		if (cmd.getName().equalsIgnoreCase("removeglowconfig")) {

			// If there are less than two arguments, return false.
			if (args.length < 2) {
				sender.sendMessage("Too few arguments, proper usage below.");
				return false;
			}

			// If there are more than two arguments, return false.
			if (args.length > 2) {
				sender.sendMessage("Too many arguments, proper usage below.");
				return false;
			}

			// Variables for the method.
			String argumentEventType, argumentEventCause;
			argumentEventType = args[0];
			argumentEventCause = args[1];

			// If the configuration path exists, remove the configuration.
			if (plugin.removeConfig(argumentEventType + "." + argumentEventCause, sender) == false)
				return false;

			return true;
		}

		// If using the command configGlow...
		if (cmd.getName().equalsIgnoreCase("configGlow")) {

			// If there are less than four arguments, return false.
			if (args.length < 4) {
				sender.sendMessage("Too few arguments, proper usage below.");
				return false;
			}

			// Assign the arguments to variable to verify.
			String argumentEventType, argumentEventCause, argumentSetting, argumentSettingValue;
			argumentEventType = args[0].toUpperCase();
			argumentEventCause = args[1].toUpperCase();
			argumentSetting = args[2].toUpperCase();
			argumentSettingValue = args[3].toUpperCase();
			EventType verifiedEventType;
			Setting verifiedSetting;

			// Verify the event type is correct, otherwise return false.
			try {
				verifiedEventType = EventType.valueOf(argumentEventType);
			} catch (IllegalArgumentException e) {
				sender.sendMessage("EventType provided doesn't exist.");
				return false;
			}

			// Verify the eventCause using an EventCauseVerification.
			EventCauseVerification eventCauseVerification = new EventCauseVerification(verifiedEventType,
					argumentEventCause);

			// If the duration isn't valid, then post an error message and return false.
			if (!eventCauseVerification.getIsValid()) {
				sender.sendMessage(eventCauseVerification.getErrorMessage());
				return false;
			}

			// Verify the Setting is correct.
			try {
				verifiedSetting = Setting.valueOf(argumentSetting);
			} catch (IllegalArgumentException e) {
				sender.sendMessage("Setting provided doesn't exist.");
				return false;
			}

			// If there are more than four arguments and GlowSetting isn't message.
			if (args.length > 4 && !(verifiedSetting == Setting.MESSAGE)) {
				sender.sendMessage("Too many arguments, proper usage below.");
			}

			// Verify the Setting value.
			SettingValueVerification settingValueVerification = new SettingValueVerification(
					verifiedSetting, argumentSettingValue);

			if (!settingValueVerification.getIsValid()) {
				sender.sendMessage(settingValueVerification.getErrorMessage());
				return false;
			}

			// All values are verified, so set them in the config!
			String path = "";
			if (verifiedSetting == Setting.DURATION || verifiedSetting == Setting.COLOR) {
				path = verifiedEventType + "." + eventCauseVerification.getVerifiedEventCause() + ".GLOWSETTINGS."
						+ verifiedSetting;
			} else {
				path = verifiedEventType + "." + eventCauseVerification.getVerifiedEventCause() + "."
						+ verifiedSetting;
			}


			// If the SettingValue is an integer, set it as one.
			if (verifiedSetting == Setting.DURATION) {
				plugin.getConfig().set(path,
						Integer.parseInt(settingValueVerification.getverifiedSettingValue()));
				sender.sendMessage(
						path + " is now set to " + settingValueVerification.getverifiedSettingValue() + "!");
			}

			// If the GlowSettingValue is a color or action, set it as a string.
			else if (verifiedSetting == Setting.COLOR || verifiedSetting == Setting.ACTION) {
				plugin.getConfig().set(path, settingValueVerification.getverifiedSettingValue());
				sender.sendMessage(
						path + " is now set to " + settingValueVerification.getverifiedSettingValue() + "!");
			}

			// Otherwise, it's a message, set it as one.
			else {
				String value = "";
				for (int i = 3; i < args.length; i++) {
					value = value + args[i] + " ";
				}
				plugin.getConfig().set(path, value.trim());
				sender.sendMessage(path + " is now set to " + value.trim());
			}

			// Save the config file.
			plugin.saveConfig();
			return true;
		}

		// Return false if the function failed.
		plugin.getLogger().info("No command found.");
		return false;
	}
}
