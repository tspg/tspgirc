package net.allfearthesentinel.spigot.irc;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jibble.pircbot.ConnectionSettings;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sean on 12/09/16.
 */
public class Bot extends PircBot {
    private Plugin plugin = Plugin.getInstance();
    public List<String> ignoredUsers = new ArrayList<String>();
    public List<String> ignoredPlayers = new ArrayList<String>();
    public ConnectionSettings settings;

    public Bot() {
        this.setName(plugin.getConfig().getString("irc.nickname"));
        this.setUserName(plugin.getConfig().getString("irc.ident"));

        String server = plugin.getConfig().getString("irc.server");
        int port = plugin.getConfig().getInt("irc.port");

        this.setVerbose(plugin.getConfig().getBoolean("irc.verbose"));

        ConnectionSettings connectionSettings = new ConnectionSettings(server);
        connectionSettings.port = port;
        connectionSettings.useSSL = plugin.getConfig().getBoolean("irc.ssl");
        connectionSettings.verifySSL = plugin.getConfig().getBoolean("irc.verify_ssl");

        settings = connectionSettings;

        try {
            plugin.getLogger().info("Connecting...");
            connect(connectionSettings);
        } catch (IOException e) {
            plugin.getLogger().warning("IOException!!");
            e.printStackTrace();
        } catch (IrcException e) {
            plugin.getLogger().warning("IRCException!!");
            e.printStackTrace();
        }
    }

    protected void onConnect() {
        this.sendRawLine("MODE " + getNick() + " +B");
        this.sendRawLine("NICKSERV IDENTIFY " +
                plugin.getConfig().getString("irc.nickserv.username") + " " +
                plugin.getConfig().getString("irc.nickserv.password"));
        this.joinChannel(plugin.getConfig().getString("irc.channel"), plugin.getConfig().getString("irc.channel_key"));
    }

    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (ignoredUsers.contains(sender)) {
            return;
        }

        String userLevel = "";
        User user = null;

        for (User u : this.getUsers(channel)) {
            if (u.equals(sender)) {
                user = u;
            }
        }

        if (user != null) {
            if (user.isOp()) {
                userLevel = "\u00A7a@\u00A7r";
            } else if (user.isHalfOp()) {
                userLevel = "\u00A7d%\u00A7r";
            } else if (user.hasVoice()) {
                userLevel = "\u00A7e+\u00A7r";
            }
        }

        plugin.addToBroadcastQueue(
                String.format(
                        "\u00A78[\u00A77%s\u00A78] \u00A72<%s\u00A7a%s\u00A72> \u00A7r%s",
                        channel,
                        userLevel,
                        sender,
                        message
                )
        );
    }

    protected void onAction(String sender, String login, String hostname, String target, String action) {
        if (ignoredUsers.contains(sender)) {
            return;
        }

        if (!target.startsWith("#")) {
            return;
        }

        plugin.addToBroadcastQueue(
                String.format(
                        "\u00A78[\u00A77%s\u00A78] \u00A72* \u00A7a%s \u00A7r%s",
                        target,
                        sender,
                        action
                )
        );
    }

    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        if (ignoredUsers.contains(oldNick)) {
            if (!ignoredUsers.contains(newNick)) {
                ignoredUsers.add(newNick);
            }

            ignoredUsers.remove(oldNick);
        }
    }

    protected void onDisconnect() {
        if (Plugin.getInstance().pluginUnloading) {
            return;
        }

        try {
            connect(settings);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }
}
