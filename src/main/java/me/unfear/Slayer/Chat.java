package me.unfear.Slayer;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    public static String format(String string) {
        string = string.replace("&#", "#");
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String color = string.substring(matcher.start(), matcher.end());
            string = string.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }
        string = ChatColor.translateAlternateColorCodes('&', string); // Translates any & codes too
        return string;
    }
}
