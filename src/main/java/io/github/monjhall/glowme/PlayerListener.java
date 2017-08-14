package io.github.monjhall.glowme;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
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

			// Set the duration and color. Resorts to default values if it fails.
			int duration = plugin.getConfig()
					.getInt("ItemCraft." + event.getCurrentItem().getType().toString() + ".Duration");
			ChatColor color = null;

			// Notify the user of any duration mistakes.
			if (duration < -1 || duration == 0 || duration > 1500) {
				plugin.getLogger().log(Level.CONFIG,
						"The duration for " + event.getCurrentItem() + " was incorrect! Fix this in the config!");
				return;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}

			// Try-Catch statement to make sure that the chat color is not null or
			// incorrect.
			try {
				color = ChatColor.valueOf((plugin.getConfig()
						.getString("ItemCraft." + event.getCurrentItem().getType().toString() + ".Color"))
								.toUpperCase());
			} catch (NullPointerException e) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for " + event.getCurrentItem() + " was null! Fix this in the config!");
				return;
			} catch (IllegalArgumentException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getCurrentItem()
						+ " was not an allowed color! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getWhoClicked(), duration, color);
		}

	}

	// When an item is eaten, check if a glow should be applied and apply it!
	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("ItemConsume." + event.getItem().getType().toString())) {

			// Set the duration and color. Resorts to default values if it fails.
			int duration = plugin.getConfig()
					.getInt("ItemConsume." + event.getItem().getType().toString() + ".Duration");
			ChatColor color = null;

			// Notify the user of any duration mistakes.
			if (duration < -1 || duration == 0 || duration > 1500) {
				plugin.getLogger().log(Level.CONFIG,
						"The duration for " + event.getItem() + " was incorrect! Fix this in the config!");
				return;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}

			// Try-Catch statement to make sure that the chat color is not null or
			// incorrect.
			try {
				color = ChatColor.valueOf(
						(plugin.getConfig().getString("ItemConsume." + event.getItem().getType().toString() + ".Color"))
								.toUpperCase());
			} catch (NullPointerException e) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for " + event.getItem() + " was null! Fix this in the config!");
				return;
			} catch (IllegalArgumentException e) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for " + event.getItem() + " was not an allowed color! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getPlayer(), duration, color);
		}

	}

	// When an item is eaten, check if a glow should be applied and apply it!
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("ItemPickup." + event.getItem().getItemStack().getType().toString())) {

			// Set the duration and color. Resorts to default values if it fails.
			int duration = plugin.getConfig()
					.getInt("ItemPickup." + event.getItem().getItemStack().getType().toString() + ".Duration");
			ChatColor color = null;

			// Notify the user of any duration mistakes.
			if (duration < -1 || duration == 0 || duration > 1500) {
				plugin.getLogger().log(Level.CONFIG, "The duration for " + event.getItem().getItemStack().getType()
						+ " was incorrect! Fix this in the config!");
				return;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}

			// Try-Catch statement to make sure that the chat color is not null or
			// incorrect.
			try {
				color = ChatColor.valueOf((plugin.getConfig()
						.getString("ItemPickup." + event.getItem().getItemStack().getType().toString() + ".Color"))
								.toUpperCase());
			} catch (NullPointerException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getItem().getItemStack().getType()
						+ " was null! Fix this in the config!");
				return;
			} catch (IllegalArgumentException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getItem().getItemStack().getType()
						+ " was not an allowed color! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getPlayer(), duration, color);
		}
	}

	// When an entity dies, check if a glow should be applied and apply it!
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("EntityKilled." + event.getEntity().getType().toString())) {

			// Set the duration and color. Resorts to default values if it fails.
			int duration = plugin.getConfig()
					.getInt("EntityKilled." + event.getEntity().getType().toString() + ".Duration");
			ChatColor color = null;

			// Notify the user of any duration mistakes.
			if (duration < -1 || duration == 0 || duration > 1500) {
				plugin.getLogger().log(Level.CONFIG, "The duration for " + event.getEntity().getType().toString()
						+ " was incorrect! Fix this in the config!");
				return;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}

			// Try-Catch statement to make sure that the chat color is not null or
			// incorrect.
			try {
				color = ChatColor.valueOf((plugin.getConfig()
						.getString("EntityKilled." + event.getEntity().getType().toString() + ".Color")).toUpperCase());
			} catch (NullPointerException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getEntity().getType().toString()
						+ " was null! Fix this in the config!");
				return;
			} catch (IllegalArgumentException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getEntity().getType().toString()
						+ " was not an allowed color! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getEntity().getKiller(), duration, color);
		}

	}

	// When a player throws an ender pearl, check if a glow should be applied and
	// apply it!
	@EventHandler
	public void onEnderPearlTeleport(PlayerTeleportEvent event) {

		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("TeleportCause." + event.getCause().toString())) {

			// Set the duration and color. Resorts to default values if it fails.
			int duration = plugin.getConfig().getInt("TeleportCause." + event.getCause().toString() + ".Duration");
			ChatColor color = null;

			// Notify the user of any duration mistakes.
			if (duration < -1 || duration == 0 || duration > 1500) {
				plugin.getLogger().log(Level.CONFIG,
						"The duration for " + event.getCause().toString() + " was incorrect! Fix this in the config!");
				return;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}

			// Try-Catch statement to make sure that the chat color is not null or
			// incorrect.
			try {
				color = ChatColor.valueOf(
						(plugin.getConfig().getString("TeleportCause." + event.getCause().toString() + ".Color"))
								.toUpperCase());
			} catch (NullPointerException e) {
				plugin.getLogger().log(Level.CONFIG,
						"The color for " + event.getCause().toString() + " was null! Fix this in the config!");
				return;
			} catch (IllegalArgumentException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getCause().toString()
						+ " was not an allowed color! Fix this in the config!");
				return;
			}

			// Set the glow for the player.
			plugin.setGlow((Player) event.getPlayer(), duration, color);
		}

	}

	// This just prints a message on login as a test.
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		plugin.getLogger().log(Level.INFO, "This is an event test. Please ignore.");
	}
}
