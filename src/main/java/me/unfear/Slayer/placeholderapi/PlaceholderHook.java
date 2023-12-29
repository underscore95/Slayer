package me.unfear.Slayer.placeholderapi;

import org.bukkit.plugin.PluginDescriptionFile;

import java.util.logging.Logger;

public class PlaceholderHook {

    public void hook(PluginDescriptionFile pluginDescriptionFile, Logger logger) {
        SlayerPlaceholderExpansion expansion = new SlayerPlaceholderExpansion(pluginDescriptionFile);
        expansion.register();
        logger.info("Hooked into PlaceholderAPI!");
    }
}
