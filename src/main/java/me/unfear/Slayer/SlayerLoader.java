package me.unfear.Slayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.unfear.Slayer.mobtypes.MobType;

public class SlayerLoader {
	private ArrayList<SlayerTask> slayerTasks;
	private HashSet<PlayerData> playerData;
	private boolean allowSpawners;
	private int saveTimer;
	private ArrayList<ShopItem> shopItems;

	public SlayerLoader() {
		this.slayerTasks = new ArrayList<>();

		final FileConfiguration config = Slayer.inst.getConfig();

		allowSpawners = config.getBoolean("allow-spawners");
		saveTimer = config.getInt("save-timer");

		// load the tasks
		final ConfigurationSection tasksConfig = config.getConfigurationSection("tasks");
		if (tasksConfig == null) {
			Slayer.inst.getLogger().severe("Failed to load slayer tasks.");
			Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
			return;
		}

		for (String idString : tasksConfig.getKeys(false)) {

			Integer id = null;
			try {
				id = Integer.parseInt(idString);
			} catch (NumberFormatException e) {
				Slayer.inst.getLogger().severe("Failed to load a slayer task, id isn't a number (id=" + idString + ")");
				Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
				return;
			}

			String mobTypeString = tasksConfig.getString(id + ".mob-type");
			final Integer kills = tasksConfig.getInt(id + ".kills");
			final String name = tasksConfig.getString(id + ".name");
			final ArrayList<String> description = (ArrayList<String>) tasksConfig.getStringList(id + ".description");
			final Integer reward = tasksConfig.getInt(id + ".reward");

			if (mobTypeString == null || kills == null || name == null || description == null || reward == null) {
				Slayer.inst.getLogger().severe("Failed to load a slayer task, missing value (id=" + id + ")");
				Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
				return;
			}

			try {
				Integer.parseInt(mobTypeString);
			} catch (NumberFormatException e) {
				Slayer.inst.getLogger().severe("Failed to load a slayer task, mob type isn't an integer (id=" + id
						+ ", mob type: " + mobTypeString + ")");
				Slayer.inst.getLogger().severe("Just updated to v0.1.0? Take a look at this: https://github.com/unfear-underscore/Slayer/wiki/Updating-to-0.1.0");
				Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
				return;
			}

			final MobType mobType = Slayer.inst.getMobTypeLoader().getMobType(Integer.parseInt(mobTypeString));

			if (mobType == null) {
				Slayer.inst.getLogger().severe("Failed to load a slayer task, mob type not a mob (id=" + id
						+ ", mob type id: " + mobTypeString + ")");
				Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
				return;
			}

			if (kills < 1) {
				Slayer.inst.getLogger().severe("Failed to load a slayer task, kills required < 1 (id=" + id + ")");
				Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
				return;
			}

			if (reward < 0) {
				Slayer.inst.getLogger().severe("Failed to load a slayer task, reward < 0 (id=" + id + ")");
				Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
				return;
			}

			slayerTasks.add(new SlayerTask(id, mobType, kills, name, description, reward));
		}

		this.playerData = new HashSet<>();
		this.shopItems = new ArrayList<>();

		ConfigurationSection shopSection = config.getConfigurationSection("shop");
		if (shopSection != null) {
			for (String key : shopSection.getKeys(false)) {
				Integer id = null;
				try {
					id = Integer.parseInt(key);
				} catch (NumberFormatException e) {
					Slayer.inst.getLogger().severe("Failed to load a shop item, id isn't a number (id=" + key + ")");
					Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
					return;
				}

				key += ".";
				String name = shopSection.getString(key + "name");
				ArrayList<String> description = (ArrayList<String>) shopSection.getStringList(key + "description");
				Integer cost = shopSection.getInt(key + "cost");
				ArrayList<String> commands = (ArrayList<String>) shopSection.getStringList(key + "commands");
				String materialString = shopSection.getString(key + "material");
				Integer itemAmount = shopSection.getInt(key + "itemAmount");
				Integer purchases = shopSection.getInt(key + "purchases");

				if (name == null || description == null || cost == null || commands == null || materialString == null
						|| itemAmount == null || purchases == null) {
					Slayer.inst.getLogger().severe("Failed to load a shop item, missing value (id=" + key + ")");
					Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
					return;
				}

				Material material = Material.valueOf(materialString);
				if (material == null) {
					Slayer.inst.getLogger()
							.severe("Failed to load a shop item, material is an invalid item (id=" + key + ")");
					Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
					return;
				}

				if (itemAmount < 1 || itemAmount > 64) {
					Slayer.inst.getLogger().severe(
							"Failed to load a shop item, item amount must be between 1 and 64 (id=" + key + ")");
					Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
					return;
				}

				if (purchases != -1 && purchases < 1) {
					Slayer.inst.getLogger()
							.severe("Failed to load a shop item, purchases must be >0 or -1 (id=" + key + ")");
					Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
					return;
				}

				if (cost < 0) {
					Slayer.inst.getLogger().severe("Failed to load a shop item, cost must be >= 0 (id=" + key + ")");
					Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
					return;
				}

				shopItems.add(new ShopItem(id, name, description, cost, commands, material, itemAmount, purchases));
			}
		}

		if (shopSection == null || shopItems.size() == 0)
			Slayer.inst.getLogger()
					.warning("No shop items found in config.yml, players will be unable to spend slayer points.");

	}

	public ArrayList<SlayerTask> getSlayerTasks() {
		return slayerTasks;
	}

	public boolean isAllowSpawners() {
		return allowSpawners;
	}

	public PlayerData getPlayerData(UUID uuid) {
		// is it already in memory?
		for (PlayerData data : this.playerData) {
			if (data.getPlayer().equals(uuid))
				return data;
		}

		// is it on disk?
		final File file = new File(Slayer.inst.getDataFolder(), "data" + File.separator + uuid + ".yml");
		if (file.exists()) {
			final FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(file);

			Integer kills = dataConfig.getInt("kills");
			SlayerTask slayerTask = getSlayerTask(dataConfig.getInt("current-task"));
			if (!dataConfig.isSet("current-task"))
				slayerTask = null;
			Integer points = dataConfig.getInt("points");
			Integer tasksCompleted = dataConfig.getInt("tasks-completed");

			if (kills == null || points == null || tasksCompleted == null) {
				Slayer.inst.getLogger().severe("Failed to load player data, missing value (uuid=" + uuid + ")");
				return create(uuid);
			}

			// shop items
			final HashMap<Integer, Integer> shopItemsPurchased = new HashMap<>();
			if (dataConfig.isSet("shop-items-purchased")) {
				for (String key : dataConfig.getConfigurationSection("shop-items-purchased").getKeys(false)) {
					try {
						int id = Integer.parseInt(key);
						int amount = dataConfig.getInt("shop-items-purchased." + key);
						if (!dataConfig.isSet("shop-items-purchased." + key))
							amount = 0;
						shopItemsPurchased.put(id, amount);
					} catch (NumberFormatException e) {
						Slayer.inst.getLogger().severe("Failed to load player data, shop id is not a number (uuid="
								+ uuid + ", id=" + key + ")");
						return create(uuid);
					}
				}
			}

			// entity kills
			final HashMap<Integer, Integer> entityKills = new HashMap<>();
			if (dataConfig.isSet("entity-kills")) {
				for (String key : dataConfig.getConfigurationSection("entity-kills").getKeys(false)) {
					try {
						int id = Integer.parseInt(key);
						int amount = dataConfig.getInt("entity-kills." + key);
						if (!dataConfig.isSet("entity-kills." + key))
							amount = 0;
						entityKills.put(id, amount);
					} catch (NumberFormatException e) {
						Slayer.inst.getLogger().severe("Failed to load player data, mob type id is not a number (uuid="
								+ uuid + ", id=" + key + ")");
						return create(uuid);
					}
				}
			}

			// loaded
			final PlayerData data = new PlayerData(uuid, kills, slayerTask, points, tasksCompleted, shopItemsPurchased,
					entityKills);
			this.playerData.add(data);
			return data;
		}

		return create(uuid);
	}

	private SlayerTask getSlayerTask(Integer id) {
		if (id == null)
			return null;
		for (SlayerTask task : this.slayerTasks) {
			if (task.getId() == id)
				return task;
		}
		return null;
	}

	public int getSaveTimer() {
		return saveTimer;
	}

	private PlayerData create(UUID uuid) {
		final PlayerData data = new PlayerData(uuid);
		playerData.add(data);
		return data;
	}

	public void save() {
		for (PlayerData data : this.playerData) {
			data.save();
		}
	}

	public ShopItem getShopItem(int id) {
		for (ShopItem shopItem : this.shopItems) {
			if (shopItem.getId() == id)
				return shopItem;
		}
		return null;
	}

	public ArrayList<ShopItem> getShopItems() {
		return this.shopItems;
	}
}
