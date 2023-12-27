package me.unfear.Slayer.mobtypes.mythic;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.mobtypes.MobType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class MythicMobsLoader {

    public static final MythicBukkit mythicMobs = (MythicBukkit) Main.inst.getServer().getPluginManager()
            .getPlugin("MythicMobs");

    public static MobType load(ConfigurationSection section, String key) {
        if (mythicMobs == null || !mythicMobs.isEnabled()) {
            Main.inst.getLogger().severe("Failed to hook into MythicMobs");
            return null;
        }

        try {
            Integer.parseInt(key);
        } catch (NumberFormatException e) {
            Main.inst.getLogger().severe("Unable to load mob-type, id isn't an integer (id: " + key + ")");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return null;
        }

        final int id = Integer.parseInt(key);

        final String name = section.getString("name");
        final String mythicMob = section.getString("mythicmob");

        if (name == null || mythicMob == null) {
            Main.inst.getLogger().severe("Unable to load MYTHIC_MOBS mob-type, missing value (id: " + key + ")");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return null;
        }

        boolean validMob = false;
        for (MythicMob mob : mythicMobs.getMobManager().getMobTypes()) {
            if (mob.getInternalName().equalsIgnoreCase(mythicMob)) {
                validMob = true;
                break;
            }
        }

        if (!validMob) {
            Main.inst.getLogger()
                    .severe("Unable to load MYTHIC_MOBS mob-type, mythicmob does not exist (id: " + key + ")");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return null;
        }

        final String materialString = section.getString("material");
        Material material = Material.SKELETON_SKULL;
        try {
            if (materialString != null) {
                material = Material.valueOf(materialString);
            }
        } catch (IllegalArgumentException e) {
            Main.inst.getLogger().warning(
                    "Invalid material for MYTHIC_MOBS mob-type, defaulting to skeleton skull (id: " + key + ")");
        }

        return new MythicMobType(id, name, material, mythicMob);
    }

}
