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
package com.spicean.ipbot

import org.bukkit.plugin.java.JavaPlugin
import net.dv8tion.jda.api.entities.TextChannel
import org.bukkit.Bukkit
import javax.security.auth.login.LoginException
import java.lang.Runnable
import java.io.BufferedReader
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.logging.Level

class IPBot : JavaPlugin() {

    private val discord = Discord(this)
    private var delay: Long = 0
    private val thing: Long = 1
    private val time: Long = 200
    private var channel: TextChannel? = null
    private var msgID: String? = null

    override fun onEnable() {
        displayStartup()
        saveDefaultConfig()

        Thread {
            try {
                discord.init()
            } catch (e: LoginException) {
                logger.log(Level.SEVERE, "Invalid discord token", e)
                return@Thread
            }
            delay = config.getLong("delay")
            if (delay == 0L) {
                delay = 18000
            }
            channel = discord.bot.getTextChannelById(config.getString("target-channel")!!)
            if (channel == null) {
                logger.log(Level.SEVERE, "Please specify a channel to send the Server IP")
                return@Thread
            }
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, {
                msgID = config.getString("msgid")
                var systemipaddress = ""
                try {
                    val urlName = URL("http://checkip.amazonaws.com/")
                    val sc = BufferedReader(InputStreamReader(urlName.openStream()))
                    systemipaddress = sc.readLine().trim { it <= ' ' }
                } catch (e: Exception) {
                    logger.log(Level.SEVERE, "Failed to reach http://checkip.amazonaws.com/", e)
                }
                val embed = EmbedBuilder()
                embed.setAuthor("Server IP: $systemipaddress")
                embed.setColor(Color(0x696969))
                if (msgID != null && channel!!.retrieveMessageById(msgID!!) != null) {
                    channel!!.editMessageEmbedsById(msgID!!, embed.build()).queue()
                } else {
                    channel!!.sendMessageEmbeds(embed.build()).queue()
                    fixMsgId()
                }
            }, thing, delay)
        }

    }

    private fun fixMsgId() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, Runnable {
            msgID = channel!!.latestMessageId
            config["msgid"] = msgID
            saveConfig()
        }, time)
    }

    private fun displayStartup() {
        // Start cewl AF startup message
        Bukkit.getLogger().log(Level.INFO, "      §3 __")
        Bukkit.getLogger().log(
            Level.INFO,
            "  §9| §3|__)   §2IPBot §a ${description.version}"
        )
        Bukkit.getLogger().log(
            Level.INFO,
            "  §9| §3|      §8Running on ${server.bukkitVersion}"
        )
        Bukkit.getLogger().log(Level.INFO, "")
    }
}