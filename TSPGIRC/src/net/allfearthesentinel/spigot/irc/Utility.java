package net.allfearthesentinel.spigot.irc;

import org.bukkit.World;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class Utility {
    public static String ircColourWorld(World world) {
        World.Environment environment = world.getEnvironment();
        String name = world.getName();

        if (name.endsWith("_nether")) {
            name = name.substring(0, name.length() - "_nether".length());
        }

        if (name.endsWith("_the_end")) {
            name = name.substring(0, name.length() - "_the_end".length());
        }

        if (environment == World.Environment.NORMAL) {
            name = Colors.GREEN + Colors.BOLD + name + Colors.NORMAL;
        } else if (environment == World.Environment.NETHER) {
            name = Colors.RED + Colors.BOLD + name + Colors.BOLD + " (Nether)" + Colors.NORMAL;
        } else if (environment == World.Environment.THE_END) {
            name = Colors.YELLOW + Colors.BOLD + name + Colors.BOLD + " (The End)" + Colors.NORMAL;
        }

        return name;
    }
}
