package net.allfearthesentinel.spigot.irc.events;

import net.allfearthesentinel.spigot.irc.Plugin;
import net.allfearthesentinel.spigot.irc.Utility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class PlayerDeathHandler implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = event.getEntity();

        if (Plugin.getInstance().bot.ignoredPlayers.contains(player.getName())) {
            return;
        }

        Player killer = player.getKiller();

        if (killer == null) {
            String channel = Plugin.getInstance().getConfig().getString("irc.channel");
            Plugin.getInstance().bot.sendMessage(channel, String.format(
                    Colors.YELLOW + "%%-%% " + Colors.NORMAL + "%s%s" + Colors.NORMAL + " died in world %s",
                    (player.isOp() ? Colors.CYAN : ""),
                    player.getName(),
                    Utility.ircColourWorld(player.getWorld())
            ));
        } else {
            String channel = Plugin.getInstance().getConfig().getString("irc.channel");
            Plugin.getInstance().bot.sendMessage(channel, String.format(
                    Colors.YELLOW + "%%-%% " + Colors.NORMAL + "%s%s" + Colors.NORMAL + " was killed in world %s by %s",
                    (player.isOp() ? Colors.CYAN : ""),
                    player.getName(),
                    Utility.ircColourWorld(player.getWorld()),
                    (killer.isOp() ? Colors.CYAN : ""),
                    killer.getName()
            ));
        }
    }
}
