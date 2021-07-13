package io.github.cosmicspice.IPBot;

import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import java.net.*;
import java.io.*;
import java.awt.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class IPBot extends JavaPlugin {
    
    private Discord discord = new Discord(this);
    private long delay;
    private long thing = 1;
    private long time = 20;
    private TextChannel channel;
    private String msgID;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            discord.Init();
        } catch (LoginException e) {
            getLogger().log(Level.SEVERE, "Invalid discord token", e);
            return;
        }
        
        delay = getConfig().getLong("delay");
        if (delay == 0) {
            delay = 18000;
        }

        channel = discord.getBot().getTextChannelById(getConfig().getString("target-channel"));
        if (channel == null) {
            getLogger().log(Level.SEVERE, "Please specify a channel to send the Server IP");
            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                msgID = getConfig().getString("msgid");

                String systemipaddress = "";
                try {
                    URL url_name = new URL("http://bot.whatismyipaddress.com");
  
                    BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));
  
                    systemipaddress = sc.readLine().trim();
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "how r u so bad at coding", e);
                }

                EmbedBuilder embed = new EmbedBuilder();
                
                embed.setAuthor("Server IP: " + systemipaddress);
                embed.setColor(new Color(0x696969));

                if (msgID != null)  {
                    channel.editMessageEmbedsById(msgID, embed.build()).queue();
                } else {
                    channel.sendMessageEmbeds(embed.build()).queue();
                    fixmsgid();
                }
            }
        }, thing, delay);
    }

    public void fixmsgid() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                msgID = channel.getLatestMessageId();
                getConfig().set("msgid", msgID);
                saveConfig();
            }
        }, time);
    }

}
