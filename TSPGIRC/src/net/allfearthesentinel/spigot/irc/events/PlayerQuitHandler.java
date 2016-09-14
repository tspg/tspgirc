package net.allfearthesentinel.spigot.irc.events;

import net.allfearthesentinel.spigot.irc.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jibble.pircbot.Colors;

/**
 * Created by sean on 13/09/16.
 */
public class PlayerQuitHandler implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (Plugin.getInstance().bot.ignoredPlayers.contains(player.getName())) {
            return;
        }

        String playerName = event.getPlayer().getName();

        String channel = Plugin.getInstance().getConfig().getString("irc.channel");

        Plugin.getInstance().bot.sendMessage(channel, String.format(
                Colors.RED + "<-- " + Colors.NORMAL + "%s%s%s has quit the server",
                (player.isOp() ? Colors.CYAN : ""),
                playerName,
                Colors.NORMAL
        ));
    }
}
