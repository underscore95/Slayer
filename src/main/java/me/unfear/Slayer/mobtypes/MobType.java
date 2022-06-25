package me.unfear.Slayer.mobtypes;

import org.bukkit.entity.Entity;

public abstract class MobType {
	
	private int id;
	private String name;
	public MobType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract boolean isThis(Entity entity);
}
