package net.allfearthesentinel.spigot.irc;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 13/09/16.
 */
public class Tasks extends BukkitRunnable {
    public final Plugin plugin;

    public Tasks(Plugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        if (plugin.getBroadcastQueue().size() > 0) {
            int i = 0;
            while (i < plugin.getBroadcastQueue().size()) {
                Bukkit.getServer().broadcastMessage(plugin.getBroadcastQueue().get(i));
                plugin.getBroadcastQueue().remove(i);
                i++;
            }
        }
    }
}
