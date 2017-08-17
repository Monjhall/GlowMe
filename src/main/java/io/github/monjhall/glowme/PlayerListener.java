package io.github.monjhall.glowme;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
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
		if (plugin.getConfig().contains("ItemCraft." + event.getCurrentItem().getType().toString())) {

			// Variables for the method.
			String configColor, configDuration;
			configColor = plugin.getConfig()
					.getString("ItemCraft." + event.getCurrentItem().getType().toString() + ".Color");
			configDuration = Integer.toString(plugin.getConfig()
					.getInt("ItemCraft." + event.getCurrentItem().getType().toString() + ".Duration"));

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(configColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG, "The color for ItemCraft." + event.getCurrentItem()
						+ " was incorrect! Fix this in the config!");
				return;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(configDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG, "The duration for ItemCraft." + event.getCurrentItem()
						+ " was incorrect! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getWhoClicked(), durationVerification.getVerifiedDuration(),
					ChatColor.valueOf(colorVerification.getVerifiedColor()));
		}

	}

	// When an item is eaten, check if a glow should be applied and apply it!
	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("ItemConsume." + event.getItem().getType().toString())) {

			// Variables for the method.
			String configColor, configDuration;
			configColor = plugin.getConfig()
					.getString("ItemConsume." + event.getItem().getType().toString() + ".Color");
			configDuration = Integer.toString(
					plugin.getConfig().getInt("ItemConsume." + event.getItem().getType().toString() + ".Duration"));

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(configColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for ItemConsume." + event.getItem() + " was incorrect! Fix this in the config!");
				return;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(configDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The duration for ItemConsume." + event.getItem() + " was incorrect! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getPlayer(), durationVerification.getVerifiedDuration(),
					ChatColor.valueOf(colorVerification.getVerifiedColor()));
		}

	}

	// When an item is eaten, check if a glow should be applied and apply it!
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("ItemPickup." + event.getItem().getItemStack().getType().toString())) {

			// Variables for the method.
			String configColor, configDuration;
			configColor = plugin.getConfig()
					.getString("ItemPickup." + event.getItem().getItemStack().getType().toString() + ".Color");
			configDuration = Integer.toString(plugin.getConfig()
					.getInt("ItemPickup." + event.getItem().getItemStack().getType().toString() + ".Duration"));

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(configColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for ItemPickup." + event.getItem() + " was incorrect! Fix this in the config!");
				return;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(configDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The duration for ItemPickup." + event.getItem() + " was incorrect! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getPlayer(), durationVerification.getVerifiedDuration(),
					ChatColor.valueOf(colorVerification.getVerifiedColor()));
		}
	}

	// When an entity dies, check if a glow should be applied and apply it!
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("EntityKilled." + event.getEntity().getType().toString())) {

			// Variables for the method.
			String configColor, configDuration;
			configColor = plugin.getConfig()
					.getString("EntityKilled." + event.getEntity().getType().toString() + ".Color");
			configDuration = Integer.toString(
					plugin.getConfig().getInt("EntityKilled." + event.getEntity().getType().toString() + ".Duration"));

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(configColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for EntityKilled." + event.getEntity() + " was incorrect! Fix this in the config!");
				return;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(configDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG, "The duration for EntityKilled." + event.getEntity()
						+ " was incorrect! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getEntity().getKiller(), durationVerification.getVerifiedDuration(),
					ChatColor.valueOf(colorVerification.getVerifiedColor()));
		}

	}

	// When a player throws an ender pearl, check if a glow should be applied and
	// apply it!
	@EventHandler
	public void onEnderPearlTeleport(PlayerTeleportEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("Teleport." + event.getCause().toString())) {

			// Variables for the method.
			String configColor, configDuration;
			configColor = plugin.getConfig().getString("Teleport." + event.getCause().toString() + ".Color");
			configDuration = Integer
					.toString(plugin.getConfig().getInt("Teleport." + event.getCause().toString() + ".Duration"));

			// Verify the color using a ColorVerification.
			ColorVerification colorVerification = new ColorVerification(configColor);

			// If the color isn't valid, the post an error message and return false.
			if (!colorVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for Teleport." + event.getCause() + " was incorrect! Fix this in the config!");
				return;
			}

			// Verify the duration using a DurationVerification.
			DurationVerification durationVerification = new DurationVerification(configDuration);

			// If the duration isn't valid, then post an error message and return false.
			if (!durationVerification.getIsValid()) {
				plugin.getLogger().log(Level.CONFIG,
						"The duration for Teleport." + event.getCause() + " was incorrect! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getPlayer(), durationVerification.getVerifiedDuration(),
					ChatColor.valueOf(colorVerification.getVerifiedColor()));
		}
	}
}
