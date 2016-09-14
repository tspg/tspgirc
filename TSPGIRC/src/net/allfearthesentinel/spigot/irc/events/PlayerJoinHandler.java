package net.allfearthesentinel.spigot.irc.events;

import net.allfearthesentinel.spigot.irc.Plugin;
import net.allfearthesentinel.spigot.irc.Utility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class PlayerJoinHandler implements Listener {
    @EventHandler
    public void onChat(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Plugin.getInstance().bot.ignoredPlayers.contains(player.getName())) {
            return;
        }

        String playerName = event.getPlayer().getName();

        String channel = Plugin.getInstance().getConfig().getString("irc.channel");

        Plugin.getInstance().bot.sendMessage(channel, String.format(
                Colors.GREEN + "-->" + Colors.NORMAL + " %s%s%s has joined the server to world %s",
                (player.isOp() ? Colors.CYAN : ""),
                playerName,
                Colors.NORMAL,
                Utility.ircColourWorld(player.getWorld())
        ));
    }
}
