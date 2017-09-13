package io.github.monjhall.glowme;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class PlayerListener implements Listener {

	private final GlowMe plugin;

	// Constructor.
	public PlayerListener(GlowMe plugin) {
		this.plugin = plugin;
	}

	// When an item is crafted, check if a glow should be applied and apply it!
	@EventHandler
	public void onItemCraft(CraftItemEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("ITEMCRAFT." + event.getCurrentItem().getType().toString())) {

			// Set the main path for convenience, and other variables.
			String easyPath = "ITEMCRAFT." + event.getCurrentItem().getType().toString();
			String configColor, configDuration, configMessage, configAction = "";

			// Action handling here. Check for an action.
			if (plugin.getConfig().contains(easyPath + ".ACTION")) {

				// Determine whether the action is valid.
				try {
					configAction = ActionType.valueOf(plugin.getConfig().getString(easyPath + ".ACTION")).toString();
				} catch (IllegalArgumentException | NullPointerException exception) {
					if (exception instanceof IllegalArgumentException) {
						plugin.getLogger().log(Level.WARNING, "The action listed in the config file for " + easyPath
								+ " is not a viable action. Fix in the config.");
						return;
					} else if (exception instanceof NullPointerException) {
						plugin.getLogger().log(Level.WARNING, "Action setting exists for " + easyPath
								+ " in the config, but has no action. Fix in the config.");
						return;
					}
				}

				// Based on the action listed, react appropriately.
				if (ActionType.valueOf(configAction).equals(ActionType.GLOW)
						|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

					// Check if color is listed in the glow settings.
					if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.COLOR")) {
						plugin.getLogger().log(Level.WARNING, "There is no color listed in the glow settings for "
								+ easyPath + " in the config file. Fix in the config.");
						return;
					}

					// Check if duration is listed in the glow settings.
					if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.DURATION")) {
						plugin.getLogger().log(Level.WARNING, "There is no duration listed in the glow settings for "
								+ easyPath + " in the config file. Fix in the config.");
						return;
					}

					// Verify the duration and color.
					configColor = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.COLOR");
					configDuration = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.DURATION");

					// Verify the color using a ColorVerification.
					ColorVerification colorVerification = new ColorVerification(configColor);

					// If the color isn't valid, the post an error message and return false.
					if (!colorVerification.getIsValid()) {
						plugin.getLogger().log(Level.WARNING,
								"The color for " + easyPath + " was incorrect! Fix this in the config!");
						return;
					}

					// Verify the duration using a DurationVerification.
					DurationVerification durationVerification = new DurationVerification(configDuration);

					// If the duration isn't valid, then post an error message and return false.
					if (!durationVerification.getIsValid()) {
						plugin.getLogger().log(Level.WARNING,
								"The duration for " + easyPath + " was incorrect! Fix this in the config!");
						return;
					}

					// Set the glow for the player.
					plugin.setGlow((Player) event.getWhoClicked(), durationVerification.getVerifiedDuration(),
							ChatColor.valueOf(colorVerification.getVerifiedColor()));

				}

			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.CLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)) {

				// Remove the glow from the player.
				plugin.clearGlow((Player) event.getWhoClicked());
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.ANNOUNCE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// If there is no message, put a warning in the log.
				if (!plugin.getConfig().contains(easyPath + ".MESSAGE")) {
					plugin.getLogger().log(Level.WARNING,
							easyPath + " was set to announce, but no message has been set! Fix in the config.");
					return;
				}

				// Verify the message.
				configMessage = plugin.getConfig().getString(easyPath + ".MESSAGE");

				// Replace components of the message.
				if (configMessage.contains("<player>") || configMessage.contains("<p>")) {
					configMessage = configMessage.replaceAll("<player>", event.getWhoClicked().getName());
					configMessage = configMessage.replaceAll("<p>", event.getWhoClicked().getName());
				}

				if (configMessage.contains("<cause>") || configMessage.contains("<c>")) {
					configMessage = configMessage.replaceAll("<cause>", event.getCurrentItem().getType().toString());
					configMessage = configMessage.replaceAll("<c>", event.getCurrentItem().getType().toString());
				}

				// Broadcast the message.
				plugin.getServer().broadcastMessage(configMessage);
			}
		}
	}

	// When an item is eaten, check if a glow should be applied and apply it!
	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {

		// Set the main path for convenience, and other variables.
		String easyPath = "ITEMCONSUME." + event.getItem().getType().toString();
		String configColor, configDuration, configMessage, configAction = "";

		// Action handling here. Check for an action.
		if (plugin.getConfig().contains(easyPath + ".ACTION")) {

			// Determine whether the action is valid.
			try {
				configAction = ActionType.valueOf(plugin.getConfig().getString(easyPath + ".ACTION")).toString();
			} catch (IllegalArgumentException | NullPointerException exception) {
				if (exception instanceof IllegalArgumentException) {
					plugin.getLogger().log(Level.WARNING, "The action listed in the config file for " + easyPath
							+ " is not a viable action. Fix in the config.");
					return;
				} else if (exception instanceof NullPointerException) {
					plugin.getLogger().log(Level.WARNING, "Action setting exists for " + easyPath
							+ " in the config, but has no action. Fix in the config.");
					return;
				}
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.GLOW)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// Verify that the glow settings exist.
				// if (plugin.getConfig().contains(easyPath + ".GLOWSETTINGS")) {
				// plugin.getLogger().log(Level.WARNING,
				// "There are no glow settings listed in the config file for " + easyPath
				// + ". Fix in the config.");
				// return;
				// }

				// Check if color is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.COLOR")) {
					plugin.getLogger().log(Level.WARNING, "There is no color listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Check if duration is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.DURATION")) {
					plugin.getLogger().log(Level.WARNING, "There is no duration listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Verify the duration and color.
				configColor = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.COLOR");
				configDuration = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.DURATION");

				// Verify the color using a ColorVerification.
				ColorVerification colorVerification = new ColorVerification(configColor);

				// If the color isn't valid, the post an error message and return false.
				if (!colorVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The color for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Verify the duration using a DurationVerification.
				DurationVerification durationVerification = new DurationVerification(configDuration);

				// If the duration isn't valid, then post an error message and return false.
				if (!durationVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The duration for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Set the glow for the player.
				plugin.setGlow(event.getPlayer(), durationVerification.getVerifiedDuration(),
						ChatColor.valueOf(colorVerification.getVerifiedColor()));

			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.CLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)) {

				// Remove the glow from the player.
				plugin.clearGlow(event.getPlayer());
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.ANNOUNCE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// If there is no message, put a warning in the log.
				if (!plugin.getConfig().contains(easyPath + ".MESSAGE")) {
					plugin.getLogger().log(Level.WARNING,
							easyPath + " was set to announce, but no message has been set! Fix in the config.");
					return;
				}

				// Verify the message.
				configMessage = plugin.getConfig().getString(easyPath + ".MESSAGE");

				// Replace components of the message.
				if (configMessage.contains("<player>") || configMessage.contains("<p>")) {
					configMessage = configMessage.replaceAll("<player>", event.getPlayer().getDisplayName());
					configMessage = configMessage.replaceAll("<p>", event.getPlayer().getDisplayName());
				}

				if (configMessage.contains("<cause>") || configMessage.contains("<c>")) {
					configMessage = configMessage.replaceAll("<cause>", event.getItem().getType().toString());
					configMessage = configMessage.replaceAll("<c>", event.getItem().getType().toString());
				}

				// Broadcast the message.
				plugin.getServer().broadcastMessage(configMessage);
			}
		}
	}

	// When an item is eaten, check if a glow should be applied and apply it!
	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		
		// Prevent other entities from glowing for picked up items.
		if (!event.getEntityType().equals(EntityType.PLAYER)) return;

		// Set the main path for convenience, and other variables.
		String easyPath = "ITEMPICKUP." + event.getItem().getItemStack().getType().toString();
		String configColor, configDuration, configMessage, configAction = "";

		// Action handling here. Check for an action.
		if (plugin.getConfig().contains(easyPath + ".ACTION")) {
			
			// Determine whether the action is valid.
			try {
				configAction = ActionType.valueOf(plugin.getConfig().getString(easyPath + ".ACTION")).toString();
			} catch (IllegalArgumentException | NullPointerException exception) {
				if (exception instanceof IllegalArgumentException) {
					plugin.getLogger().log(Level.WARNING, "The action listed in the config file for " + easyPath
							+ " is not a viable action. Fix in the config.");
					return;
				} else if (exception instanceof NullPointerException) {
					plugin.getLogger().log(Level.WARNING, "Action setting exists for " + easyPath
							+ " in the config, but has no action. Fix in the config.");
					return;
				}
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.GLOW)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// Check if color is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.COLOR")) {
					plugin.getLogger().log(Level.WARNING, "There is no color listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Check if duration is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.DURATION")) {
					plugin.getLogger().log(Level.WARNING, "There is no duration listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Verify the duration and color.
				configColor = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.COLOR");
				configDuration = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.DURATION");

				// Verify the color using a ColorVerification.
				ColorVerification colorVerification = new ColorVerification(configColor);

				// If the color isn't valid, the post an error message and return false.
				if (!colorVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The color for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Verify the duration using a DurationVerification.
				DurationVerification durationVerification = new DurationVerification(configDuration);

				// If the duration isn't valid, then post an error message and return false.
				if (!durationVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The duration for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Set the glow for the player.
				plugin.setGlow((Player) event.getEntity(), durationVerification.getVerifiedDuration(),
						ChatColor.valueOf(colorVerification.getVerifiedColor()));

			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.CLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)) {

				// Remove the glow from the player.
				plugin.clearGlow((Player) event.getEntity());
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.ANNOUNCE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// If there is no message, put a warning in the log.
				if (!plugin.getConfig().contains(easyPath + ".MESSAGE")) {
					plugin.getLogger().log(Level.WARNING,
							easyPath + " was set to announce, but no message has been set! Fix in the config.");
					return;
				}

				// Verify the message.
				configMessage = plugin.getConfig().getString(easyPath + ".MESSAGE");

				// Replace components of the message.
				if (configMessage.contains("<player>") || configMessage.contains("<p>")) {
					configMessage = configMessage.replaceAll("<player>", event.getEntity().getName());
					configMessage = configMessage.replaceAll("<p>", event.getEntity().getName());
				}

				if (configMessage.contains("<cause>") || configMessage.contains("<c>")) {
					configMessage = configMessage.replaceAll("<cause>", event.getItem().getItemStack().getType().toString());
					configMessage = configMessage.replaceAll("<c>", event.getItem().getItemStack().getType().toString());
				}

				// Broadcast the message.
				plugin.getServer().broadcastMessage(configMessage);

			}
		}
	}

	// When a player teleports, check if a glow should be applied and apply it!
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		// Set the main path for convenience, and other variables.
		String easyPath = "TELEPORT." + event.getCause().toString();
		String configColor, configDuration, configMessage, configAction = "";

		// Action handling here. Check for an action.
		if (plugin.getConfig().contains(easyPath + ".ACTION")) {

			// Determine whether the action is valid.
			try {
				configAction = ActionType.valueOf(plugin.getConfig().getString(easyPath + ".ACTION")).toString();
			} catch (IllegalArgumentException | NullPointerException exception) {
				if (exception instanceof IllegalArgumentException) {
					plugin.getLogger().log(Level.WARNING, "The action listed in the config file for " + easyPath
							+ " is not a viable action. Fix in the config.");
					return;
				} else if (exception instanceof NullPointerException) {
					plugin.getLogger().log(Level.WARNING, "Action setting exists for " + easyPath
							+ " in the config, but has no action. Fix in the config.");
					return;
				}
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.GLOW)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// Check if color is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.COLOR")) {
					plugin.getLogger().log(Level.WARNING, "There is no color listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Check if duration is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.DURATION")) {
					plugin.getLogger().log(Level.WARNING, "There is no duration listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Verify the duration and color.
				configColor = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.COLOR");
				configDuration = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.DURATION");

				// Verify the color using a ColorVerification.
				ColorVerification colorVerification = new ColorVerification(configColor);

				// If the color isn't valid, the post an error message and return false.
				if (!colorVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The color for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Verify the duration using a DurationVerification.
				DurationVerification durationVerification = new DurationVerification(configDuration);

				// If the duration isn't valid, then post an error message and return false.
				if (!durationVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The duration for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Set the glow for the player.
				plugin.setGlow(event.getPlayer(), durationVerification.getVerifiedDuration(),
						ChatColor.valueOf(colorVerification.getVerifiedColor()));

			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.CLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)) {

				// Remove the glow from the player.
				plugin.clearGlow(event.getPlayer());
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.ANNOUNCE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// If there is no message, put a warning in the log.
				if (!plugin.getConfig().contains(easyPath + ".MESSAGE")) {
					plugin.getLogger().log(Level.WARNING,
							easyPath + " was set to announce, but no message has been set! Fix in the config.");
					return;
				}

				// Verify the message.
				configMessage = plugin.getConfig().getString(easyPath + ".MESSAGE");

				// Replace components of the message.
				if (configMessage.contains("<player>") || configMessage.contains("<p>")) {
					configMessage = configMessage.replaceAll("<player>", event.getPlayer().getDisplayName());
					configMessage = configMessage.replaceAll("<p>", event.getPlayer().getDisplayName());
				}

				if (configMessage.contains("<cause>") || configMessage.contains("<c>")) {
					configMessage = configMessage.replaceAll("<cause>", event.getCause().toString());
					configMessage = configMessage.replaceAll("<c>", event.getCause().toString());
				}

				// Broadcast the message.
				plugin.getServer().broadcastMessage(configMessage);
			}
		}
	}

	// When an entity dies, check if a glow should be applied and apply it!
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

		// Set the main path for convenience, and other variables.
		String easyPath = "ENTITYKILLED." + event.getEntityType().toString();
		plugin.getLogger().log(Level.INFO, "Entity killed had a tpye of " + event.getEntityType());
		String configColor, configDuration, configMessage, configAction = "";

		// Action handling here. Check for an action.
		if (plugin.getConfig().contains(easyPath + ".ACTION")) {

			// Determine whether the action is valid.
			try {
				configAction = ActionType.valueOf(plugin.getConfig().getString(easyPath + ".ACTION")).toString();
			} catch (IllegalArgumentException | NullPointerException exception) {
				if (exception instanceof IllegalArgumentException) {
					plugin.getLogger().log(Level.WARNING, "The action listed in the config file for " + easyPath
							+ " is not a viable action. Fix in the config.");
					return;
				} else if (exception instanceof NullPointerException) {
					plugin.getLogger().log(Level.WARNING, "Action setting exists for " + easyPath
							+ " in the config, but has no action. Fix in the config.");
					return;
				}
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.GLOW)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// Check if color is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.COLOR")) {
					plugin.getLogger().log(Level.WARNING, "There is no color listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Check if duration is listed in the glow settings.
				if (!plugin.getConfig().contains(easyPath + ".GLOWSETTINGS.DURATION")) {
					plugin.getLogger().log(Level.WARNING, "There is no duration listed in the glow settings for "
							+ easyPath + " in the config file. Fix in the config.");
					return;
				}

				// Verify the duration and color.
				configColor = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.COLOR");
				configDuration = plugin.getConfig().getString(easyPath + ".GLOWSETTINGS.DURATION");

				// Verify the color using a ColorVerification.
				ColorVerification colorVerification = new ColorVerification(configColor);

				// If the color isn't valid, the post an error message and return false.
				if (!colorVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The color for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Verify the duration using a DurationVerification.
				DurationVerification durationVerification = new DurationVerification(configDuration);

				// If the duration isn't valid, then post an error message and return false.
				if (!durationVerification.getIsValid()) {
					plugin.getLogger().log(Level.WARNING,
							"The duration for " + easyPath + " was incorrect! Fix this in the config!");
					return;
				}

				// Set the glow for the player.
				plugin.setGlow(event.getEntity().getKiller(), durationVerification.getVerifiedDuration(),
						ChatColor.valueOf(colorVerification.getVerifiedColor()));

			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.CLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)) {

				// Remove the glow from the player.
				plugin.clearGlow(event.getEntity().getKiller());
			}

			// Based on the action listed, react appropriately.
			if (ActionType.valueOf(configAction).equals(ActionType.ANNOUNCE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCECLEANSE)
					|| ActionType.valueOf(configAction).equals(ActionType.ANNOUNCEGLOW)) {

				// If there is no message, put a warning in the log.
				if (!plugin.getConfig().contains(easyPath + ".MESSAGE")) {
					plugin.getLogger().log(Level.WARNING,
							easyPath + " was set to announce, but no message has been set! Fix in the config.");
					return;
				}

				// Verify the message.
				configMessage = plugin.getConfig().getString(easyPath + ".MESSAGE");

				// Replace components of the message.
				if (configMessage.contains("<player>") || configMessage.contains("<p>")) {
					configMessage = configMessage.replaceAll("<player>", event.getEntity().getKiller().getDisplayName());
					configMessage = configMessage.replaceAll("<p>", event.getEntity().getKiller().getDisplayName());
				}

				if (configMessage.contains("<cause>") || configMessage.contains("<c>")) {
					configMessage = configMessage.replaceAll("<cause>", event.getEntityType().toString());
					configMessage = configMessage.replaceAll("<c>", event.getEntityType().toString());
				}

				// Broadcast the message.
				plugin.getServer().broadcastMessage(configMessage);
			}
		}
	}
}
