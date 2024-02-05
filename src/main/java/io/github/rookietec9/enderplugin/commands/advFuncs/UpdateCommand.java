package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 25.7.5
 * @since 18.1.5
 */
public class UpdateCommand implements EndExecutor {
    private static File plugFile;
    private static final File dir;
    private static int ver;

    static {
        Method getFileMethod;
        try {
            getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            plugFile = (File) getFileMethod.invoke(EnderPlugin.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File newFile = null;

    //Modified code from
    //https://github.com/r-clancy/PlugMan/blob/master/src/main/java/com/rylinaux/plugman/util/PluginUtil.java
    public static boolean unload(Plugin plugin) {

        String name = plugin.getName();

        PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;

        boolean reloadlisteners = true;

        if (pluginManager != null) {

            pluginManager.disablePlugin(plugin);

            try {

                Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
                pluginsField.setAccessible(true);
                plugins = (List<Plugin>) pluginsField.get(pluginManager);

                Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
                lookupNamesField.setAccessible(true);
                names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

                try {
                    Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                    listenersField.setAccessible(true);
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
                } catch (Exception e) {
                    reloadlisteners = false;
                }

                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(commandMap);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Objects.requireNonNull(pluginManager).disablePlugin(plugin);

        if (plugins != null) plugins.remove(plugin);
        if (names != null) names.remove(name);

        if (listeners != null && reloadlisteners) {
            for (SortedSet<RegisteredListener> set : listeners.values()) {
                set.removeIf(value -> value.getPlugin() == plugin);
            }
        }

        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = Objects.requireNonNull(commands).entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }

        ClassLoader cl = plugin.getClass().getClassLoader();

        if (cl instanceof URLClassLoader) {

            try {
                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);

                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);

                ((URLClassLoader) cl).close();
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        }
        System.gc();
        return true;
    }

    static {
        File file = new File(".").getAbsoluteFile();

        File root = file.getParentFile();
        while (root.getParentFile() != null) {
            root = root.getParentFile();
        }
        dir = root;
    }


    public static File newestJar() {
        ver = (Integer.parseInt(plugFile.toString().substring(plugFile.toString().indexOf(".") - 2).replace(".", "").replace("jar", "")) + 1);

        HashMap<Integer, Pair<File, File>> verFileMap = new HashMap<>();

        File verDir = new File(dir.getPath() + "\\cMC\\[" + ver / 100 + "] " + Minecraft.getVersions().get(ver / 100) + "\\");
        File targetDir = new File(dir.getPath() + "\\cmC\\EnderPlugin\\target\\");
        File pluginDir = new File(dir.getPath() + "\\cMC\\[BUKKIT] 1.8\\plugins\\");

        for (File file : new File[]{verDir, targetDir, pluginDir}) {
            //Targets the correct jar files
            List<File> fileList = new ArrayList<>();
            for (File f : Objects.requireNonNull(file.listFiles())) if (f.getName().endsWith(".jar")) fileList.add(f);
            if (fileList.size() == 0) continue;

            if (file.equals(verDir)) {
                File toAdd = fileList.get(fileList.size() - 1);
                fileList.clear();
                fileList.add(toAdd);
            }

            //finds the versions by reading the pom.xml
            for (File plugin : fileList) {
                JarFile jarFile;
                try {
                    jarFile = new JarFile(plugin);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                ZipEntry pom = jarFile.getEntry("META-INF/maven/io.github.rookietec9/EnderPlugin/pom.xml");
                if (pom == null) continue;
                InputStream is;
                try {
                    is = jarFile.getInputStream(pom);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                Scanner scanner = new Scanner(is);

                scanner.findAll("\\d\\d.\\d.\\d").forEach(x -> verFileMap.put(Integer.parseInt(x.group().replace(".", "")), Pair.of(plugin, file)));
            }
        }
        for (Map.Entry<Integer, Pair<File, File>> value : verFileMap.entrySet()) {
            if (value.getValue().getValue() != verDir) {
                try {
                    Files.copy(value.getValue().getKey().toPath(), Path.of(verDir.toPath() + File.separator + value.getValue().getKey().getName()));
                } catch (IOException ignore) {
                }
                verFileMap.put(value.getKey(), Pair.of(value.getValue().getKey(), verDir));
            }
        }
        int newVer = 0;
        for (int key : verFileMap.keySet()) if (key > newVer) newVer = key;

        Path p;

        try {
            p = Files.copy(verFileMap.get(newVer).getKey().toPath(), Path.of(pluginDir.toPath() + File.separator + verFileMap.get(newVer).getKey().getName()));
        } catch (IOException ignore) {
            p = Path.of(pluginDir.toPath() + File.separator + verFileMap.get(newVer).getKey().getName());
        }
        return verFileMap.get(newVer).getKey().getAbsoluteFile().equals(plugFile.getAbsoluteFile()) ? null : new File(p.toUri());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        newFile = newestJar();

        if (this.newFile == null || plugFile.getAbsoluteFile().equals(newFile.getAbsoluteFile())) return msg(sender, serverLang().getErrorMsg() + "No available update!");

        String updatedMsg = serverLang().getPlugMsg() + "Updated to version " + ver + "!", deletedMsg = serverLang().getPlugMsg() + "Deleted the old version.", failMsg = serverLang().getPlugMsg() + "Failed to delete the old version.", reloaded = serverLang().getPlugMsg() + "Reloaded the server.";

        EnderPlugin.scheduler().runSingleTask(() -> {
            unload(EnderPlugin.getInstance());

            System.gc();
            try {
                Plugin plugin = Bukkit.getServer().getPluginManager().loadPlugin(this.newFile);
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);

                boolean deleted = UpdateCommand.plugFile.delete();
                Bukkit.broadcastMessage(updatedMsg);
                Bukkit.reload();
                msg(sender, reloaded);
                if (deleted) msg(sender, deletedMsg);
                else msg(sender, failMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "ENDERPLUGIN_UPDATE", 0, serverLang().getPlugMsg());
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