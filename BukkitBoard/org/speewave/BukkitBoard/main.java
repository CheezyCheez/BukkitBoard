package org.speewave.BukkitBoard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import lib.PatPeter.SQLibrary.*;

public final class main extends JavaPlugin {
		public MySQL db;
		
		public void onEnable()
		{	
			//Lets Load up The Config File:
			getLogger().info("Starting Up BukkitBoard");
			getLogger().info("Connecting to MySQL Database");
			
			db = new MySQL(this.getLogger(),"[BukkitBoard]", getConfig().getString("hostname"), 3306, getConfig().getString("db"), getConfig().getString("user"), getConfig().getString("password"));
			
			if (db==null)
			{
				getLogger().severe("Couldn't Connect to MySQL Database.. Check your Config Files and Server and try again");
			}
			else
			{
				getLogger().info("Connected to MySQL Database");
				getLogger().info("BukkitBoard Ready!");
			}
			
			
			
		}
		
		 public boolean onCommand(CommandSender sender, Command command, String c, String[] args)
		 {
	            Player send = (Player) sender;
	            Player target = null;
	            
	            if(command.getName().equalsIgnoreCase("commandhere") && send.hasPermission("permissionhereifneeded"))
	            {
	            
	            	
	            }
	            return false;
		 }

		public void onDisable()
		{
			getLogger().info("BukkitBoard Unloaded");
		}
}