package io.github.rookietec9.enderplugin.events.hub;

import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * @author Jeremi
 * @version 11.6.0
 * @since 9.5.0
 */
public class StopWeather implements Listener {

    @EventHandler
    public void runEvent(WeatherChangeEvent event) {
        for (String string: EnderPlugin.getInstance().getConfig().getStringList("noWeather")) if (event.getWorld().getName().equalsIgnoreCase(string)) {
            if (event.getWorld().getWeatherDuration() != 0 || event.getWorld().getThunderDuration() != 0) {
                event.setCancelled(true);
            }
        }
    }
}