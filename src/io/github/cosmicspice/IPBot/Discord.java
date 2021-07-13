package io.github.cosmicspice.IPBot;

import java.util.logging.Level;

import javax.security.auth.login.LoginException;

import org.bukkit.plugin.Plugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Discord {

    private Plugin plugin;
    private JDA bot;
    private String token;

    public Discord(Plugin plugin) {
        this.plugin = plugin;
    }

    public void Init() throws LoginException {
        token = plugin.getConfig().getString("token");

        JDABuilder builder = JDABuilder.createDefault(token);
        
        bot = builder.build();

        try {
            bot.awaitReady();
        } catch (InterruptedException e) {
            plugin.getLogger().log(Level.SEVERE, "Thread interrupted", e);
        }
    }

    public JDA getBot() {
        return bot;
    }

}
