/*
 * 						BukkitBoard
 * 		The Bulletin Board Plugin for Bukkit Servers!
 * ==========================================================
 * 	    Copyright (C) 2013 The BukkitBoard Development Team
 * 
 * 		This Code is Licensed under the FreeBSD License
 *			 Please read the LICENCE.txt file!
 * 		For a list of Authors & Contributors see AUTHORS.TXT
 * 
 * 		For More info. Check out our Project on GitHub!
 * 			https://github.com/speewave/BukkitBoard
 * 
 * 			Also Please check out the README.md
 */

package org.speewave.BukkitBoard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.base.Joiner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Please Note this is not a final product just yet.. (i'll probably always be doing something to it) anyway... if you have a Feature request or Bug:
//Please go to : https://github.com/speewave/BukkitBoard/issues... someone on the dev team will do something about it...

public final class main extends JavaPlugin
{
		
		public String tbd = "Sorry... This Function not yet implemented";
		public Connection db;
		public PreparedStatement ps;
		//public MessageType[] mt;
		
		public void onEnable()
		{	
			
			getLogger().info("Starting Up BukkitBoard");
			//getConfig().options().copyDefaults(true);
			saveConfig();
			
			getLogger().info("Connecting to MySQL Database");
			SQLConnect();
			TableCheck();
						
		}
		
		public boolean SQLConnect()
		{
			//Set up the DB	
			//Build a Connection String: (jdbc:mysql://<hostname>/<database>?user=<user>+&password=<password>
			String cs = "jdbc:mysql://"+
						getConfig().get("hostname")+"/"+
						getConfig().get("database")+"?user="+
						getConfig().get("user")+"&password="+
						getConfig().get("password");
			
			//See if we could connect or not
			try 
			{
				db = DriverManager.getConnection(cs);
			} 
			catch (SQLException e) 
			{
				getLogger().severe("Couldn't Connect to MySQL Database.. Check your Config Files and Server and try again");
				getLogger().severe(e.getMessage());
				return false;
			}
			return true;
		}
		
		//Initialization code : Checks for the main table... Creates it if there isn't one
		public boolean TableCheck()
		{
				getLogger().info("Connected to MySQL Database");
				//Now that it's set up Check to see if there is at least a bukkitboard table exists (bktbrd_main)
				getLogger().info("Checking for BukkitBoard Main Table (bktbrd_maisn)");
				try
				{
					ps = db.prepareStatement("Select * from bktbrd_main;");
					ps.executeQuery();
				}
				catch (SQLException e)
				{
					int s = e.getErrorCode();
					//DEBUG Code: getLogger().info(String.valueOf(s));
					//1146 = Table doesn't exist
					if (s == 1146)
					{
						getLogger().info("Main Table not found, Creating it");
						try
						{
							ps= db.prepareStatement("CREATE TABLE bktbrd_main (ID smallint NOT NULL AUTO_INCREMENT,player varchar(16),type char,msg varchar(120),PRIMARY KEY (id));");
							ps.execute();
						}
						catch(SQLException e2)
						{
							getLogger().severe("Could Not Create Table! Please check database permissions and try again..");
							getLogger().severe(e2.getMessage());
							return false;
						}
						getLogger().info("Table Created!");
						return true;
					}
					else
					{
						getLogger().info("Table Found");
						return true;
					}
				}
				return true;
		}
		
		//Aliases for types
		public char TypeAlias (String instring)

		{
			switch(instring.toLowerCase())
			{
				//info (i)
				case ("i"): return 'i'; 
				case ("info"): return 'i'; 
				//Stores (s)
				case ("s"): return 's';
				case ("store"): return 's'; 
				//Work or Jobs (w)
				case ("w"):return 'w';
				case ("j"):return 'w'; 
				case ("work"):return 'w';
				//Clans (c)
				case ("c"):return 'c'; 
				case ("clan"):return 'c'; 
				//Matches (m)
				case ("m"):return 'm'; 
				case ("matches"):return 'm';
				//Tournaments (t)
				case ("t"):return 't';
				case ("tournaments"):return 't';
				case ("tourneys"):return 't';
				default: return '0';
			}
		
		}
		
		//BukkitBoard Functionality
		
		//Listing Information on the database
		public boolean list (Player player, String[] args)
		{
			player.sendMessage(tbd);
			return true;
		}
		
		//Help Command
		public boolean help(Player player, String [] args)
		{
			switch (args[1])
			{
			default: return Help_Usage(player);
			}
		}
		
		public boolean Help_Usage(Player player)
		{
			player.sendMessage(ChatColor.RED  + "=====================================================");
			player.sendMessage(ChatColor.BLUE + "BukkitBoard Usage List");
			player.sendMessage(ChatColor.BLUE + " Use /bb help <subcommand> For More Help");
			player.sendMessage(ChatColor.RED  + "=====================================================");
			player.sendMessage(ChatColor.BLUE + "/bb post <type> <msg>");
			player.sendMessage(ChatColor.BLUE + "/bb list -t<type> -a<amount> -p<page>");
			player.sendMessage(ChatColor.BLUE + "/bb remove <id>");
			player.sendMessage(ChatColor.RED  + "=====================================================");
			return true;
		}
		
		//Listing Types command
		public boolean ShowTypes(Player player)
		{
			player.sendMessage(tbd);
			return true;
		}
		
		//Posting command /bb post
		public boolean Post (Player player, String[] args)
		{
			boolean success= true;
			
			int startindex = args[0].length() + 1;
			String msg = Joiner.on(" ").join(args).substring(startindex).trim();
			
			String inType = args[0];
			
			char t = TypeAlias(inType);
			if (t=='0')
			{
				player.sendMessage("You didn't use a recognized type, use /bb for a listing of them");
				return false;
			}
			
			try
			{
				ps.execute("INSERT INTO bktbrd_main (player,type,msg) VALUES('"+player.getName()+"','"+t+"','"+msg+"');");
			}
			catch (SQLException e)
			{
				player.sendMessage("Something Went Wrong... Have an admin look it to it.. your error will be on the console");
				getLogger().severe(e.getMessage());
				success=false;
			}
			if (success = true)
			{
				Bukkit.broadcastMessage("Someone has made another post! Type /b to view it!");
				//TODO: Charge the player money (for Vault Support)
			}
			return success;
		}
		
		
		public boolean onCommand(CommandSender sender, Command command, String c, String[] args)
		 {
	            Player send = (Player) sender;
	            Player target = null;
	            
	            if (command.getName().equalsIgnoreCase("bbp")) return Post(send,args);
	            else return false;
	            
		 }

		public void onDisable()
		{
			getLogger().info("BukkitBoard Unloaded");
		}
}

