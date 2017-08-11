package io.github.monjhall.glowme;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
			
			if (args.length != 3) {
				sender.sendMessage("Wrong number of arguments.");
				return false;
			}
			
			Player target = (Bukkit.getServer().getPlayer(args[0]));
			if (target == null) {
				sender.sendMessage(args[0] + " is not online!");
		        return false;
			}
			
			// TODO: Fill out command functionality.
			sender.sendMessage("Functionality needs to be added to the method body.");
			
			return true;
		}
		
		// Return false if the function failed.
		getLogger().info("Command used improperly...");
		return false;
		
	}
}
