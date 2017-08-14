package io.github.monjhall.glowme;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class PlayerListener implements Listener {
	
	private final GlowMe plugin;
	
	// Constructor.
	public PlayerListener (GlowMe plugin) {
		this.plugin = plugin;
	}
	
	// When an item is crafted, check if a glow should be applied and apply it!
	@EventHandler
	public void onItemCraft(CraftItemEvent event) {
		
//		plugin.getLogger().log(Level.INFO, event.getCurrentItem() + " has been made!");
//		if (event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
//			plugin.setGlow((Player) event.getWhoClicked(), 30, ChatColor.AQUA);
//		}
		
		// Check if the item is contained in the config file...
		if (plugin.getConfig().contains("ItemCraft." + event.getCurrentItem().getType().toString())) {
			
			// Set the duration and color. Resorts to default values if it fails.
			int duration = plugin.getConfig().getInt("ItemCraft." + event.getCurrentItem().getType().toString() + ".Duration");
			ChatColor color = null;
			
			// Notify the user of any duration mistakes.
			if (duration < -1 || duration == 0 || duration > 1500) {
				plugin.getLogger().log(Level.CONFIG, "The duration for " + event.getCurrentItem() + " was incorrect! Fix this in the config!");
				return;
			} else if (duration == -1) {
				// Set duration to a large number so it'll be infinite.
				duration = 100000;
			}
			
			// Try-Catch statement to make sure that the chat color is not null or incorrect.
			try {
				color = ChatColor.valueOf((plugin.getConfig().getString("ItemCraft." + event.getCurrentItem().getType().toString() + ".Color")).toUpperCase());
			} catch (NullPointerException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getCurrentItem() + " was null! Fix this in the config!");
				return;
			} catch (IllegalArgumentException e) {
				plugin.getLogger().log(Level.CONFIG, "The color for " + event.getCurrentItem() + " was not an allowed color! Fix this in the config!");
				return;
			}
			
			// Set the glow for the player.
			plugin.setGlow((Player) event.getWhoClicked(), duration, color);
		}
		
	}
	
	// When an item is eaten, check if a glow should be applied and apply it! 
	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent event) {
		plugin.getLogger().log(Level.INFO, event.getItem() + " has been eaten!");
		if (event.getItem().getType() == Material.GOLDEN_APPLE) {
			plugin.setGlow((Player) event.getPlayer(), 30, ChatColor.GOLD);
		}
	}
	
	// When an item is eaten, check if a glow should be applied and apply it! 
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		plugin.getLogger().log(Level.INFO, event.getItem() + " has been eaten!");
		if (event.getItem().getItemStack().getType() == Material.DIAMOND) {
			plugin.setGlow((Player) event.getPlayer(), 30, ChatColor.AQUA);
		}
	}
	
	// When a player dies, check if a glow should be applied and apply it!
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		plugin.getLogger().log(Level.INFO, event.getEntity().getDisplayName() + " has been killed!");
		if (event.getEntity().getType() == EntityType.PLAYER) {
			plugin.setGlow(event.getEntity().getKiller(), 30, ChatColor.RED);
		}
	}
	
	// When a player throws an ender pearl, check if a glow should be applied and apply it!
	@EventHandler
	public void onEnderPearlTeleport(PlayerTeleportEvent event) {
		plugin.getLogger().log(Level.INFO, event.getPlayer() + " has been teleported!");
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			plugin.setGlow(event.getPlayer(), 30, ChatColor.DARK_PURPLE);
		}
	} 
	
	// This just prints a message on login as a test.
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		plugin.getLogger().log(Level.INFO, "This is an event test. Please ignore.");
	}
}
