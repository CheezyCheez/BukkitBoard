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
		//TODO: Add more commands!
		//TODO: Add a help menu.
		//TODO: Load types externally instead of having them hard-coded
	
		public String tbd = "Sorry... This Function not yet implemented";
		public Connection db;
		public PreparedStatement ps;

		public void onEnable()
		{	
			//Checks everywhere;
			//Step 1: Connection Check
			//Step 2: Main Table Check\Creation
			//TODO: Make Error Messages a bit more helpful.. (Nobody wants to read a stack trace #justsaying)
			boolean isGood = true;
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
			
			//See if we could connect or not
			try 
			{
				db = DriverManager.getConnection(cs);
			} 
			catch (SQLException e) 
			{
				getLogger().severe("Couldn't Connect to MySQL Database.. Check your Config Files and Server and try again");
				getLogger().severe(e.getMessage());
				isGood = false;
			}
			//looks like we're good!
			//Step 2!
			if (isGood)
			{
				getLogger().info("Connected to MySQL Database");
				//Now that it's set up Check to see if there is at least a bukkitboard table exists (bktbrd_main)
				getLogger().info("Checking for BukkitBoard Main Table (bktbrd_main)");
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
						getLogger().info("Main Table not existant... Creating");
						getLogger().info("Generating Main Table : bktbrd_main");
						
						//Lets Create a Table!
						//more exception Catching...
						try
						{
							ps= db.prepareStatement("CREATE TABLE bktbrd_main (ID smallint NOT NULL AUTO_INCREMENT,player varchar(16),type char,msg varchar(120),PRIMARY KEY (id));");
							ps.execute();
							isGood=true;
						}
						catch(SQLException e2)
						{
							getLogger().severe("Could Not Create Table! Please check database permissions and try again..");
							getLogger().severe(e2.getMessage());
							isGood=false;
						}
					
					}
					else
					{
						//TODO: This Doesn't Show up
						getLogger().info("Table Found!");
						isGood=true;
					}
				}
					if (isGood)
					{
						getLogger().info("BukkitBoard Ready!"); 
					}
					else
					{
						getLogger().severe("BukkitBoard had Errors");
					}
				}
		}
	
		//NOT WORKING : 
		public void join(PlayerJoinEvent event)
		{
			event.getPlayer().sendMessage(ChatColor.BLUE + "This server is running BukkitBoard!");
			//TODO: Add an option to toggle messages on and off.
			String player = event.getPlayer().getDisplayName();
			if( player == "speewave")
			{
				Bukkit.broadcastMessage(ChatColor.RED + "The developer of BukkitBoard has joined!");
			}
			else if(player == "football70500")
			{
				Bukkit.broadcastMessage(ChatColor.RED + "A developer of BukkitBoard has joined!");
			}
			// Shameless plug is ... rather interesting : Thanks @football70500
		}
		
		//Aliases for types
		public char TypeAlias (String instring)

		{
			switch(instring.toLowerCase())
			{
				//TODO: add config customizable types
			
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
			player.sendMessage(tbd);
			return true;
		}
		
		//Listing Types command
		public boolean ShowTypes(Player player)
		{
			player.sendMessage(tbd);
			return true;
		}
		
		//Posting command /bb post
		public boolean Post (Player player, String inType, String[] Message)
		{
			boolean success= true;
			
			int startindex = Message[0].length() + Message [1].length() + 1;
			String msg = Joiner.on(" ").join(Message).substring(startindex).trim();//TODO: Add this to fix apostrophes causing errors...replaceAll("'", "")
			
			char t = TypeAlias(inType);
			if (t=='0')
			{
				player.sendMessage("You didn't use a recognized type, use /bb types for a listing of them");
				player.sendMessage("Except not now because that feature isn't implemented yet");
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
				//TODO: Print a status out
				//TODO: Charge the player money (for Vault Support)
			}
			return success;
		}
		
		
		public boolean onCommand(CommandSender sender, Command command, String c, String[] args)
		 {
	            Player send = (Player) sender;
	            Player target = null;
	            
	            //TODO: Write something to handle all these command
	            //TODO: Add Some Commands:
	            //Command List:
	            // /bb <command>
	            // [DONE] : post <type> <msg> - Post a Message, needs the Type + Message
	            // list <page>- List Messages
	            // read <id> <msg> - Respond to a message (sends to personal mailboxes)
	            // remove <id> - removes a post
	            // help
	            //TODO: Add Staff Commands:
	            //Command List: 
	            // /bba (bukkitboard for admins)
	            // remove <id> <reason> - Removes a post, Give a reason (Message will be removed and player will be notified)
	           if (command.getName().equalsIgnoreCase("bb"))
	           {
	        	   switch (args[0])
	        	   {
	        	   case "post": return Post(send,args[1],args);
	        		   default:
	           			 break;		  
	        	   }
	           }
	            
	            return false;
		 }

		public void onDisable()
		{
			getLogger().info("BukkitBoard Unloaded");
		}
}
