/*
 * This file is part of IPBot, licensed under the MIT license.
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
package spicesw.ipbot

import net.dv8tion.jda.api.JDA
import kotlin.Throws
import javax.security.auth.login.LoginException
import net.dv8tion.jda.api.JDABuilder
import org.bukkit.plugin.Plugin
import java.lang.InterruptedException
import java.util.logging.Level

class Discord(private val plugin: Plugin) {
    lateinit var bot: JDA
        private set

    private lateinit var token: String

    @Throws(LoginException::class)
    fun init() {
        token = plugin.config.getString("token").toString()
        val builder = JDABuilder.createDefault(token)
        bot = builder.build()
        try {
            bot.awaitReady()
        } catch (e: InterruptedException) {
            plugin.logger.log(Level.SEVERE, "Thread interrupted", e)
        }
    }

}