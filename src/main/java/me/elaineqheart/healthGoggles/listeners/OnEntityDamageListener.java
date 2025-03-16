package me.elaineqheart.healthGoggles.listeners;

import me.elaineqheart.healthGoggles.HealthGoggles;
import me.elaineqheart.healthGoggles.UpdateTask;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class OnEntityDamageListener implements Listener {

    private final HealthGoggles plugin;

    public OnEntityDamageListener(HealthGoggles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        update(entity);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event){
        Entity entity = event.getEntity();
        update(entity);
    }

    @EventHandler
    public void onEntityHeal(EntityRegainHealthEvent event){
        Entity entity = event.getEntity();
        update(entity);
    }

    public void update(Entity entity){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(Objects.equals(p.getPersistentDataContainer().get(new NamespacedKey(plugin, "hasHealthGogglesOn"), PersistentDataType.BOOLEAN), false)){
                continue;
            }
            p.getNearbyEntities(20,20,20).forEach(e -> {
                if(e.equals(entity)){
                    Bukkit.getScheduler().runTaskLater(plugin, () ->{
                        UpdateTask.showHealth(p,e);
                    }, 1);

                }
            });
        }
    }

}
