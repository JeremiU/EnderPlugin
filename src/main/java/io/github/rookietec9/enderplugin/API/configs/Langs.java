package io.github.rookietec9.enderplugin.API.configs;

import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * An enum of all aviable Languages.
 *
 * @author TheEnderCrafter9
 * @version 13.4.4
 * @since 2.5.1
 */
public enum Langs {
    ENGLISH_US("EN_US"),
    PIRATE_SPEAK("PR_US");

    String friendly;

    Langs(String friendly) {
        this.friendly = friendly;
    }

    public static Langs fromSender(CommandSender sender) {
        Config langAssignment = new Config(true,"","langAssignment.yml", EnderPlugin.getInstance());
        if (sender instanceof Player) {
            if (langAssignment.getYaml().get("lang." + ((Player) sender).getUniqueId().toString()) == null) {
                langAssignment.getYaml().createSection("lang." + ((Player) sender).getUniqueId().toString());
                langAssignment.getYaml().set("lang." + ((Player) sender).getUniqueId().toString(), "EN_US");
                langAssignment.modifyYaml();
                return Langs.ENGLISH_US;
            } //MAKE SECTION

            for (int i = 0; i < Langs.values().length; i++) {
                if (Langs.values()[i].toString().equalsIgnoreCase(langAssignment.getYaml().getString("lang." + ((Player) sender).getUniqueId().toString()))) {
                    return Langs.values()[i];
                }
            }
        } else {
            for (int i = 0; i < Langs.values().length; i++) {
                if (Langs.values()[i].toString().equalsIgnoreCase(langAssignment.getYaml().getString("lang.default"))) {
                    return Langs.values()[i];
                }
            }
        }
        return Langs.ENGLISH_US;
    }

    @Override
    public String toString() {
        return friendly;
    }
}