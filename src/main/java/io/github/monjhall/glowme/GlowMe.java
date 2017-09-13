package io.github.monjhall.glowme;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

enum EventType {
	ITEMCRAFT, ITEMCONSUME, ITEMPICKUP, TELEPORT, ENTITYKILLED
}

enum Setting {
	COLOR, DURATION, MESSAGE, ACTION
}

enum ActionType {
	CLEANSE, GLOW, ANNOUNCEGLOW, ANNOUNCECLEANSE, ANNOUNCE
}

public class GlowMe extends JavaPlugin {

	// This method should contain all the logic to be performed when the plugin is
	// enabled.
	@Override
	public void onEnable() {

		// Load the configuration file.
		this.loadConfiguration();

		// Set the commands to be executed by the command executor.
		GlowMeCommandExecutor commandExecutor = new GlowMeCommandExecutor(this);
		this.getCommand("glow").setExecutor(commandExecutor);
		this.getCommand("clearglow").setExecutor(commandExecutor);
		this.getCommand("setglowconfig").setExecutor(commandExecutor);
		this.getCommand("removeglowconfig").setExecutor(commandExecutor);
		this.getCommand("configGlow").setExecutor(commandExecutor);

		// Create the active effects task and repeatedly run it.
		checkActiveEffects checkActiveEffectsTask = new checkActiveEffects(this);
		checkActiveEffectsTask.runTaskTimer(this, 0L, 5L);

		// Register the events.
		this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}

	// This method should contain all the logic to be performed when the plugin is
	// disabled.
	@Override
	public void onDisable() {

	}

	protected void setGlow(Player player, int duration, ChatColor color) {

		// If duration is 0, clear the glow instead.
		if (duration == 0) {
			clearGlow(player);
		}

		// Otherwise, set the new glow for the player.
		else {

			// Create the glow effect.
			PotionEffectType glowEffectType = PotionEffectType.getById(24);
			PotionEffect glowEffect = new PotionEffect(glowEffectType, (duration * 20), 1);

			// If the player has a glow effect already, clear it.
			if (player.hasPotionEffect(PotionEffectType.getById(24))) {
				clearGlow(player);
			}

			// Apply the glow to the provided player.
			addPlayerToTeam(player, color);
			glowEffect.apply(player);
		}
	}

	protected void clearGlow(Player player) {

		// If the player has a glow effect, remove it and remove them from their team.
		if (player.hasPotionEffect(PotionEffectType.getById(24))) {
			player.removePotionEffect(PotionEffectType.getById(24));
			removePlayerFromTeam(player);
		}
	}

	protected void removePlayerFromTeam(Player player) {

		// Get the scoreboard manager, get the main scoreboard, and set the new teams.
		Scoreboard mainBoard = this.getServer().getScoreboardManager().getMainScoreboard();
		Team playerTeam = mainBoard.getPlayerTeam(player);
		playerTeam.removePlayer(player);
	}

	protected void addPlayerToTeam(Player player, ChatColor color) {

		// Get the scoreboard manager, create a scoreboard, and set the new teams.
		Scoreboard mainBoard = this.getServer().getScoreboardManager().getMainScoreboard();

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
	
	protected boolean removeConfig(String path, CommandSender sender) {
		
		// If the configuration path exists, remove the configuration.
		if (getConfig().contains(path)) {
			getConfig().set(path, null);
			saveConfig();
			sender.sendMessage("Configuration was removed.");
			return true;
		}
		
		// Otherwise, return false.
		else {
			sender.sendMessage("Configuration doesn't exist, cannot be removed.");
			return false;
		}
	}

	public void loadConfiguration() {

		this.saveDefaultConfig();
	}

}
