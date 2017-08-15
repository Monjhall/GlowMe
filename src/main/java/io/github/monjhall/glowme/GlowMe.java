package io.github.monjhall.glowme;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

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

		// Create the glow effect type.
		PotionEffectType glowEffectType = PotionEffectType.getById(24);

		// Create the glow effect.
		PotionEffect glowEffect = new PotionEffect(glowEffectType, (duration * 20), 1);

		// If the player has a glow effect already, remove it.
		if (player.hasPotionEffect(PotionEffectType.getById(24))) {
			player.removePotionEffect(PotionEffectType.getById(24));
			removePlayerFromTeam(player);
		}

		// Add the player to the specified team.
		addPlayerToTeam(player, color);

		// Apply the glow to the provided player.
		glowEffect.apply(player);
	}

	protected void clearGlow(Player player) {

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

	public void loadConfiguration() {

		this.saveDefaultConfig();
	}

}
