package me.unfear.Slayer;

import me.unfear.Slayer.mobtypes.MobType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

public class PlayerData {

    public static final Random RANDOM = new Random();

    private final UUID player;
    private int kills;
    private SlayerTask currentTask;
    private int points;
    private int tasksCompleted;
    private boolean hasSentMessage = false; // this isn't saved intentionally, incase player forgets about the task
    private final HashMap<Integer, Integer> shopItemsPurchased; // shop item id, purchases
    private final HashMap<Integer, Integer> entityKills;

    public PlayerData(UUID player, int kills, SlayerTask currentTask, int points, int tasksCompleted, HashMap<Integer, Integer> shopItemsPurchased, HashMap<Integer, Integer> entityKills) {
        super();
        this.player = player;
        this.kills = kills;
        this.currentTask = currentTask;
        this.points = points;
        this.tasksCompleted = tasksCompleted;
        this.shopItemsPurchased = shopItemsPurchased;
        this.entityKills = entityKills;
        for (MobType type : Main.inst.getMobTypeLoader().getMobTypes()) {
            this.entityKills.putIfAbsent(type.getId(), 0);
        }
    }

    public PlayerData(UUID player) {
        this(player, 0, null, 0, 0, new HashMap<>(), new HashMap<>());
    }

    public HashMap<Integer, Integer> getEntityKills() {
        return this.entityKills;
    }

    public void incrementEntityKills(int id) {
        this.entityKills.putIfAbsent(id, 0);
        this.entityKills.put(id, this.entityKills.get(id) + 1);
    }

    public HashMap<Integer, Integer> getShopItemsPurchased() {
        return this.shopItemsPurchased;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;

        if (this.getCurrentTask() == null || this.kills < this.getCurrentTask().getKills() || hasSentMessage)
            return;

        Player p = Bukkit.getPlayer(player);
        if (p == null) return;
        p.sendMessage(Main.inst.getLanguage().slayerComplete());
        this.hasSentMessage = true;
    }

    public SlayerTask getCurrentTask() {
        return currentTask;
    }

    public boolean completedCurrentTask() {
        return this.getCurrentTask() != null && this.getCurrentTask().getKills() <= this.kills;
    }

    public void setCurrentTask(SlayerTask currentTask) {
        this.currentTask = currentTask;
        this.setKills(0);
        this.hasSentMessage = false;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public UUID getPlayer() {
        return player;
    }

    private Integer getCurrentTaskId() {
        SlayerTask task = getCurrentTask();
        if (task == null)
            return null;
        return task.getId();
    }

    public int getReward() {
        if (currentTask == null)
            return 0;
        return currentTask.getReward();
    }

    public void save() {

        if (Bukkit.getPlayer(player) == null)
            this.hasSentMessage = false;

        final File file = new File(Main.inst.getDataFolder(), "data" + File.separator + player + ".yml");

        // clear old data
        if (file.exists()) {
            file.delete();
        }

        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("kills", kills);
        config.set("current-task", getCurrentTaskId());
        config.set("points", points);
        config.set("tasks-completed", tasksCompleted);

        for (Entry<Integer, Integer> entry : shopItemsPurchased.entrySet()) {
            config.set("shop-items-purchased." + entry.getKey(), entry.getValue());
        }

        for (Entry<Integer, Integer> entry : entityKills.entrySet()) {
            config.set("entity-kills." + entry.getKey(), entry.getValue());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveTask(Player player) {
        this.kills = 0;

        ArrayList<SlayerTask> tasks = Main.inst.getSlayerLoader().getSlayerTasks();
        int i = -1;
        while (i == -1 || (tasks.get(i) == this.currentTask && tasks.size() > 1)) {
            i = RANDOM.nextInt(tasks.size()); // so the player doesn't get the same task 2x in a row
        }
        SlayerTask task = tasks.get(i);
        this.setCurrentTask(task);

        player.sendMessage(Main.inst.getLanguage().slayerTask(task.getKills(), task.getMobType().getName()));
    }
}
