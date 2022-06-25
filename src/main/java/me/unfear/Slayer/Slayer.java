package me.unfear.Slayer;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.unfear.Slayer.commands.SlayerCommand;
import me.unfear.Slayer.listeners.EntityDeathListener;
import me.unfear.Slayer.listeners.SpawnerSpawnListener;
import me.unfear.Slayer.mobtypes.MobTypeLoader;

public class Slayer extends JavaPlugin {
	
	public static Slayer inst;
	
	private MobTypeLoader mobTypeLoader;
	private SlayerLoader slayerLoader;

	public void onEnable() {
		inst = this;
		
		this.saveDefaultConfig();
		
		this.mobTypeLoader = new MobTypeLoader();
		this.slayerLoader = new SlayerLoader();
		
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new EntityDeathListener(), this);
		pm.registerEvents(new SpawnerSpawnListener(), this);
		
		getCommand("slayer").setExecutor(new SlayerCommand());
		
		new BukkitRunnable() {
			public void run() {
				slayerLoader.save();
			}
		}.runTaskTimer(this, slayerLoader.getSaveTimer() * 20, slayerLoader.getSaveTimer() * 20);
	}
	
	public void onDisable() { 
		if (slayerLoader != null) // its null if something fails to load and the plugin is disabled
		slayerLoader.save();
	}
	
	public MobTypeLoader getMobTypeLoader() {
		return mobTypeLoader;
	}

	public SlayerLoader getSlayerLoader() {
		return slayerLoader;
	}
}
