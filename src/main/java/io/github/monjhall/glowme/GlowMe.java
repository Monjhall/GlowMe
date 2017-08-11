package io.github.monjhall.glowme;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GlowMe extends JavaPlugin {

	@Override
	public void onEnable() {
		// This method should contain all the logic to be performed when the plugin is enabled.
		
		// Simple log of onEnable being invoked.
		getLogger().info("onEnable has been invoked!");
	}
	
	@Override 
	public void onDisable() {
		// This method should contain all the logic to be performed when the plugin is enabled.
		
		// Simple log of onDisable being invoked.
		getLogger().info("onDisable has been invoked!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// If using the command glow...
		if (cmd.getName().equalsIgnoreCase("glow")) {
			
			// TODO: Fill out command functionality.
			getLogger().info("Command used properly...");
			
			return true;
		}
		
		// Return false if the function failed.
		getLogger().info("Command used improperly...");
		return false;
		
	}
}
