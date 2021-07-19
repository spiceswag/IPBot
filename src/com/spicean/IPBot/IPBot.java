/*
 * This file is part of SpiceUtils, licensed under the MIT license.
 * 
 * Copyright (c) 2021 CosmicSpice
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.spicean.IPBot;

import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import java.net.*;
import java.io.*;
import java.awt.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class IPBot extends JavaPlugin {
    
    private Discord discord = new Discord(this);
    private long delay;
    private long thing = 1;
    private long time = 200;
    private TextChannel channel;
    private String msgID;

    @Override
    public void onEnable() {
        // Start cewl AF startup message
        Bukkit.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "      &3 __"));
        Bukkit.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "  &9| &3|__)   &2IPBot &a" + getDescription().getVersion()));
        Bukkit.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "  &9| &3|      &8Running on " + getServer().getBukkitVersion()));
        Bukkit.getLogger().log(Level.INFO, "");

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
                    getLogger().log(Level.SEVERE, "spice is bad at coding", e);
                }

                EmbedBuilder embed = new EmbedBuilder();
                
                embed.setAuthor("Server IP: " + systemipaddress);
                embed.setColor(new Color(0x696969));

                if (msgID != null && channel.retrieveMessageById(msgID) != null) {
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
