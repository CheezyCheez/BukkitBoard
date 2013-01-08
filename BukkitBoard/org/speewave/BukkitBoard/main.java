package org.speewave.BukkitBoard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class main extends JavaPlugin
{
		//public MySQL db;
		public Connection db;
		public void onEnable()
		{	
			//Lets Load up The Config File:
			getLogger().info("Starting Up BukkitBoard");
			getLogger().info("Connecting to MySQL Database");
			
			
			//ISSUE (Or potential one at that): Get a better way to detect and work with the config... 
			//write a config.yml file <Not sure if this automatically checks for one when saving...
			getConfig().options().copyDefaults(true);
			saveConfig();
			
			//Set up the DB
			
			//Build a Connection String: (jdbc:mysql://<hostname>/<database>?user=<user>+&password=<password>
			String cs = "jdbc:mysql://"+
						getConfig().get("hostname")+"/"+
						getConfig().get("database")+"?user="+
						getConfig().get("user")+"&password="+
						getConfig().get("password");
			try {
				db = DriverManager.getConnection(cs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//See if it was set up or not...
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
	            
	            if(command.getName().equalsIgnoreCase("commandhere")) //&& send.hasPermission("permissionhereifneeded"))
	            {
	            	
	            }
	            return false;
		 }

		public void onDisable()
		{
			getLogger().info("BukkitBoard Unloaded");
		}
}