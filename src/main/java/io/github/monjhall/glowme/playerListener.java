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

public final class playerListener implements Listener {
	
	private final GlowMe plugin;
	
	// Constructor.
	public playerListener (GlowMe plugin) {
		this.plugin = plugin;
	}
	
	// When an item is crafted, check if a glow should be applied and apply it!
	@EventHandler
	public void onItemCraft(CraftItemEvent event) {
		plugin.getLogger().log(Level.INFO, event.getCurrentItem() + " has been made!");
		if (event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
			plugin.setGlow((Player) event.getWhoClicked(), 30, ChatColor.AQUA);
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
