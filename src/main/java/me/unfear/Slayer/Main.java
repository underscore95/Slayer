package me.unfear.Slayer;

import me.unfear.Slayer.commands.*;
import me.unfear.Slayer.listeners.EntityDeathListener;
import me.unfear.Slayer.listeners.SpawnerSpawnListener;
import me.unfear.Slayer.mobtypes.MobTypeLoader;
import me.unfear.Slayer.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Main extends JavaPlugin {

    public static Main inst;

    private MobTypeLoader mobTypeLoader;
    private SlayerLoader slayerLoader;
    private Language language;

    public void onEnable() {
        inst = this;

        this.saveDefaultConfig();

        language = new Language();

        this.mobTypeLoader = new MobTypeLoader();
        this.slayerLoader = new SlayerLoader();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PlaceholderHook().hook(getDescription(), getLogger());

        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new EntityDeathListener(language, slayerLoader), this);
        pm.registerEvents(new SpawnerSpawnListener(), this);

        Objects.requireNonNull(getCommand("slayer")).setExecutor(new SlayerCommand());
        Objects.requireNonNull(getCommand("canceltask")).setExecutor(new CancelTaskCommand());
        Objects.requireNonNull(getCommand("collectrewards")).setExecutor(new CollectRewardCommand());
        Objects.requireNonNull(getCommand("starttask")).setExecutor(new StartTaskCommand());
        Objects.requireNonNull(getCommand("slayerbuy")).setExecutor(new SlayerBuyCommand(language, slayerLoader));
        Objects.requireNonNull(getCommand("reloadslayer")).setExecutor(new ReloadSlayerCommand(mobTypeLoader, language, slayerLoader));

        new BukkitRunnable() {
            public void run() {
                slayerLoader.save();
            }
        }.runTaskTimer(this, slayerLoader.getSaveTimer() * 20L, slayerLoader.getSaveTimer() * 20L);
    }

    public void onDisable() {
        if (slayerLoader != null) {
			// its null if something fails to load and the plugin is disabled
            slayerLoader.save();
        }
    }

    public MobTypeLoader getMobTypeLoader() {
        return mobTypeLoader;
    }

    public SlayerLoader getSlayerLoader() {
        return slayerLoader;
    }

    public Language getLanguage() {
        return language;
    }
}
