package me.unfear.Slayer;

import org.apache.commons.text.WordUtils;
import org.bukkit.entity.EntityType;

public class Utils {

	public static String prettyEntityType(EntityType type, boolean makePlural) {
		return WordUtils.capitalizeFully(type.toString().toLowerCase().replace("_", " ") + (makePlural ? "s" : ""));
	}
}
