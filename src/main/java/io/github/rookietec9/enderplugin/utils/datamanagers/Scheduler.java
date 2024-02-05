package io.github.rookietec9.enderplugin.utils.datamanagers;

import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * @author Jeremi
 * @version 25.7.3
 */
public class Scheduler {

    private final HashMap<String, Integer> schedulerList;
    private final HashMap<String, String> prefixList;

    public Scheduler() {
        schedulerList = new HashMap<>();
        prefixList = new HashMap<>();
    }

    public void runRepeatingTask(Runnable runnable, String id, double secDelay, double secLoopFrequency, double totalDuration, String prefix) {
        runRepeatingTask(runnable, id, secDelay, secLoopFrequency, prefix);
        Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> EnderPlugin.scheduler().cancel(id.toUpperCase()), (int) ((totalDuration + secDelay + 0.05) * 20));
    }

    public void runSingleTask(Runnable runnable, String id, double secDelay, String prefix) {
        putData(id.toUpperCase(), prefix, Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), runnable, (int) (secDelay * 20)));
    }

    public void runRepeatingTask(Runnable runnable, String id, double secDelay, double secLoopFrequency, String prefix) {
        putData(id.toUpperCase(), prefix, Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), runnable, (int) (secDelay * 20), (int) (secLoopFrequency * 20)));
    }

    public boolean isRunning(String id) {
        return schedulerList.get(id.toUpperCase()) != null && Bukkit.getScheduler().isQueued(schedulerList.get(id.toUpperCase()));
    }

    public void cancel(String id) {
        if (schedulerList.get(id.toUpperCase()) == null) {
            System.out.println("ERROR : " + id.toUpperCase() + " ID NOT FOUND!");
            return;
        }
        Bukkit.getScheduler().cancelTask(schedulerList.get(id.toUpperCase()));
    }

    public int bukkitID(String id) {
        return schedulerList.get(id.toUpperCase());
    }

    public String prefix(String id) {
        return prefixList.get(id.toUpperCase());
    }

    public void runMarker(String id, double totalDuration, String prefix) {
        putData(id.toUpperCase(), prefix, Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {}, (int) (totalDuration * 20)));
    }

    public List<String> currentRunningIDs() {
        List<String> currentTasks = new ArrayList<>();

        //Loops through all bukkit tasks, sees if the schedulerList contains a bukkit task, adds that entry from the hashmap to the current task list
        Bukkit.getScheduler().getPendingTasks().stream().filter(x -> schedulerList.containsValue(x.getTaskId())).
                forEach(t -> schedulerList.keySet().stream().filter(s -> schedulerList.get(s) == t.getTaskId()).forEach(currentTasks :: add));
        return currentTasks;
    }

    public void putData(String id, String prefix, int bukkitTask) {
        if (schedulerList.get(id.toUpperCase()) != null) schedulerList.remove(id);
        if (prefixList.get(id.toUpperCase()) != null) prefixList.remove(id);
        prefixList.put(id.toUpperCase(), prefix);
        schedulerList.put(id.toUpperCase(), bukkitTask);
    }
}