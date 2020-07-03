package io.github.rookietec9.enderplugin.utils.datamanagers;

import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * @author Jeremi
 * @version 20.6.7
 */
public class Scheduler {

    private final HashMap<String, Integer> schedulerList;

    public Scheduler() {
        schedulerList = new HashMap<>();
    }

    public void runSingleTask(Runnable runnable, String id, double secDelay) {
        if (schedulerList.get(id) != null) schedulerList.remove(id);
        schedulerList.put(id.toUpperCase(), Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), runnable, (int) (secDelay * 20)));
    }

    public void runRepeatingTask(Runnable runnable, String id, double secDelay, double secLoopFrequency) {
        if (schedulerList.get(id) != null) schedulerList.remove(id);
        schedulerList.put(id.toUpperCase(), Bukkit.getScheduler().scheduleSyncRepeatingTask(EnderPlugin.getInstance(), runnable, (int) (secDelay * 20), (int) (secLoopFrequency * 20)));
    }

    public void runRepeatingTask(Runnable runnable, String id, double secDelay, double secLoopFrequency, double totalDuration) {
        runRepeatingTask(runnable, id, secDelay, secLoopFrequency);
        Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> EnderPlugin.scheduler().cancel(id), (int) ((totalDuration + secDelay + 0.05) * 20));
    }

    public boolean isRunning(String id) {
        return schedulerList.get(id.toUpperCase()) != null && Bukkit.getScheduler().isQueued(schedulerList.get(id.toUpperCase()));
    }

    public void cancel(String id) {
        if (schedulerList.get(id) == null) {
            System.out.println("ERROR : " + id + " ID NOT FOUND!");
            return;
        }
        Bukkit.getScheduler().cancelTask(schedulerList.get(id.toUpperCase()));
    }

    public int bukkitID(String id) {
        return schedulerList.get(id);
    }

    public void runMarker(String id, double totalDuration) {
        if (schedulerList.get(id.toUpperCase()) != null) schedulerList.remove(id.toUpperCase());
        schedulerList.put(id.toUpperCase(), Bukkit.getScheduler().scheduleSyncDelayedTask(EnderPlugin.getInstance(), () -> {}, (int) (totalDuration * 20)));
    }

    public List<String> currentRunningIDs() {
        List<String> currentTasks = new ArrayList<>();
        for (BukkitTask task : Bukkit.getScheduler().getPendingTasks()) {

            if (schedulerList.containsValue(task.getTaskId())) for (String s : schedulerList.keySet()) if (schedulerList.get(s) == task.getTaskId()) currentTasks.add(s);
        }
        return currentTasks;
    }
}