package fr.ethan.cachecache.Listeners;

import fr.ethan.cachecache.Mains.CacheCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Listeners implements Listener {
    private static CacheCache plugin = CacheCache.plugin;

    Listeners(){
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public boolean foodLevel(FoodLevelChangeEvent event){
        event.setCancelled(true);
        return true;
    }
}
