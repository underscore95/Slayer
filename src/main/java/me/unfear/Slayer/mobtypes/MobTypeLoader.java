package me.unfear.Slayer.mobtypes;

import me.unfear.Slayer.Main;
import me.unfear.Slayer.mobtypes.mythic.MythicMobsLoader;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MobTypeLoader {

    private final Set<MobType> mobTypes = new HashSet<>();

    public MobTypeLoader() {
        reloadConfig();
    }

    public void reloadConfig() {
        mobTypes.clear();

        final File file = new File(Main.inst.getDataFolder(), "mob-types.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Main.inst.saveResource(file.getName(), false);
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (config.getConfigurationSection("mob-types") == null) {
            Main.inst.getLogger().severe("No mob types registered in mob-types.yml, disabling...");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return;
        }

        ConfigurationSection mobTypesSection = config.getConfigurationSection("mob-types");
        if (mobTypesSection != null) {
            for (String key : mobTypesSection.getKeys(false)) {
                final ConfigurationSection section = config.getConfigurationSection("mob-types." + key);

                try {
                    EnumTypes.valueOf(Objects.requireNonNull(section).getString("type"));
                } catch (NullPointerException e) {
                    Main.inst.getLogger().severe("Unable to load mob-type, no type defined (id: " + key + ")");
                    Main.inst.getPluginLoader().disablePlugin(Main.inst);
                    return;
                } catch (IllegalArgumentException e) {
                    Main.inst.getLogger().severe("Unable to load mob-type, type is invalid (id: " + key + ")");
                    Main.inst.getPluginLoader().disablePlugin(Main.inst);
                    return;
                }

                final EnumTypes type = EnumTypes.valueOf(section.getString("type"));
                mobTypes.add(load(section, type, key));
            }
        }
    }

    public Set<MobType> getMobTypes() {
        return this.mobTypes;
    }

    public MobType getMobType(Integer id) {
        for (MobType type : getMobTypes()) {
            if (type.getId() == id)
                return type;
        }
        return null;
    }

    private MobType load(ConfigurationSection section, EnumTypes type, String key) {
        switch (type) {
            case VANILLA:
                return loadVanilla(section, key);
            case MYTHIC_MOBS:
                if (Main.inst.getServer().getPluginManager().getPlugin("MythicMobs") == null) {
                    Main.inst.getLogger()
                            .severe("Unable to load mob-type, type was MYTHIC_MOBS but MythicMobs isn't installed. (id: "
                                    + key + ")");
                    Main.inst.getPluginLoader().disablePlugin(Main.inst);
                    return null;
                }
                return MythicMobsLoader.load(section, key);
        }
        Main.inst.getLogger()
                .severe("Something went really wrong! Please contact unfear_#8046 and send a copy of your configuration files.");
        Main.inst.getPluginLoader().disablePlugin(Main.inst);
        return null; // this shouldn't ever be called, but eclipse was unhappy without it
    }

    private VanillaMobType loadVanilla(ConfigurationSection section, String key) {
        try {
            Integer.parseInt(key);
        } catch (NumberFormatException e) {
            Main.inst.getLogger().severe("Unable to load mob-type, id isn't an integer (id: " + key + ")");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return null;
        }

        final int id = Integer.parseInt(key);

        final String name = section.getString("name");
        final String entityTypeString = section.getString("entity");

        if (name == null || entityTypeString == null) {
            Main.inst.getLogger().severe("Unable to load VANILLA mob-type, missing value (id: " + key + ")");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return null;
        }

        try {
            EntityType.valueOf(entityTypeString);
        } catch (IllegalArgumentException e) {
            Main.inst.getLogger().severe("Unable to load VANILLA mob-type, entity is invalid (id: " + key + ")");
            Main.inst.getPluginLoader().disablePlugin(Main.inst);
            return null;
        }

        final String materialString = section.getString("material");
        try {
            return new VanillaMobType(id, name, Material.valueOf(materialString), EntityType.valueOf(entityTypeString));
        } catch (IllegalArgumentException | NullPointerException e) {
            Main.inst.getLogger().warning(
                    "Invalid material for VANILLA mob-type, defaulting to skeleton skull (id: " + key + ")");
            return new VanillaMobType(id, name, Material.SKELETON_SKULL, EntityType.valueOf(entityTypeString));
        }
    }
}
