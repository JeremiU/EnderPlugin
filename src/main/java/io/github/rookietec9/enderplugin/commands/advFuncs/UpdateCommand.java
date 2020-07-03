package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 18.1.5
 */
public class UpdateCommand implements EndExecutor {
    private File newFile = null;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        File loc;
        int ver = Integer.parseInt(Minecraft.versionInfo(Minecraft.VerType.NUM_NO_DOTS));
        List<File> oldFiles = new ArrayList<>();
        System.out.println("" + ver + " ver");
        try {
            loc = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            loc = new File(loc.getPath().substring(0, loc.getPath().lastIndexOf("\\")));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return msg(sender, EnderPlugin.serverLang().getPlugMsg() + "Error with the path of the server file!");
        }
        int newVer = 0;
        System.out.println(loc.getPath());
        File dir = new File(loc.getPath());
        for (File file : dir.listFiles()) {
            if (!file.isDirectory() &&
                    file.getName().startsWith("EnderPlugin")) {
                try {
                    newVer = Integer.parseInt(Minecraft.versionInfo(file.getPath(), Minecraft.VerType.NUM_NO_DOTS));
                } catch (NumberFormatException ex) {
                    return msg(sender, EnderPlugin.serverLang().getNumFormatMsg());
                }
                if (newVer > ver) {
                    System.out.println("" + newVer + " newer ver");
                    this.newFile = file;
                }
                if (ver > newVer)
                    oldFiles.add(file);
            }
        }
        int verN = newVer;
        if (this.newFile == null) return msg(sender, EnderPlugin.serverLang().getErrorMsg() + "No available update!");

        EnderPlugin.scheduler().runSingleTask(() -> {
            File f;
            ClassLoader classLoader = EnderPlugin.class.getClassLoader();
            try {
                f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
            System.out.println(f.getPath());
            Bukkit.getServer().getPluginManager().disablePlugin(EnderPlugin.getInstance());
            if (classLoader instanceof URLClassLoader) {
                try {
                    ((URLClassLoader) classLoader).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("L->");
            }
            System.gc();
            try {
                Plugin plugin = Bukkit.getServer().getPluginManager().loadPlugin(this.newFile);
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);
                msg(sender, "Updated to version " + verN + "!");
                Bukkit.reload();
                msg(sender, "Reloaded.");
                System.out.println(f.getAbsolutePath());
                if (f.delete()) {
                    msg(sender, "Deleted the old version!");
                } else {
                    msg(sender, "Failed to delete the old version.");
                }
                System.out.println(oldFiles);
                for (File file : oldFiles) {
                    System.out.println(!file.renameTo(file));
                    System.out.println(file.delete());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "ENDERPLUGIN_UPDATE", 1.0D);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("update");
    }
}