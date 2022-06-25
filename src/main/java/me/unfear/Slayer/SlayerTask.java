package me.unfear.Slayer;

import java.util.ArrayList;

import me.unfear.Slayer.mobtypes.MobType;

public class SlayerTask {

	private int id;
	private MobType mobType;
	private int kills;
	private String name;
	private ArrayList<String> description;
	private int reward;

	public SlayerTask(int id, MobType mobType, int kills, String name, ArrayList<String> description, int reward) {
		super();
		this.id = id;
		this.mobType = mobType;
		this.kills = kills;
		this.name = name;
		this.description = description;
		this.reward = reward;
	}

	public int getId() {
		return id;
	}

	public MobType getMobType() {
		return mobType;
	}

	public int getKills() {
		return kills;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public int getReward() {
		return reward;
	}
}
