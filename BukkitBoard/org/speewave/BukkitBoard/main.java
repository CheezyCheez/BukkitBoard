package org.speewave.BukkitBoard;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {
	
		public void onEnable()
		{
			getLogger().info("BukkitBoard BUILD 1 active");
		}
		public void onDisable()
		{
			getLogger().info("BukkitBoard Unloaded");
		}

}
