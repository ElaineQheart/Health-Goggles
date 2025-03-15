package me.elaineqheart.healthGoggles;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import me.tofaa.entitylib.meta.EntityMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static org.bukkit.attribute.Attribute.*;

public class UpdateTask extends BukkitRunnable implements Listener {

    private final HealthGoggles plugin;
    public static HashMap<UUID, List<Integer>> mobs = new HashMap<>();

    public UpdateTask(HealthGoggles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.getPersistentDataContainer().has(new NamespacedKey(plugin, "hasHealthGogglesOn"))){
            p.getPersistentDataContainer().set(new NamespacedKey(plugin, "hasHealthGogglesOn"), PersistentDataType.BOOLEAN, false);
        }
    }



    public void run() {

        //check if a new player has the helmet on or if a player took it off
        for (Player p : Bukkit.getOnlinePlayers()){
            PersistentDataContainer data = null;
            if(p.getInventory().getHelmet() != null){
                ItemStack item = p.getInventory().getHelmet();
                ItemMeta meta = item.getItemMeta();
                if(meta != null){
                    data = meta.getPersistentDataContainer();
                }
            }

            //if the helmet is not equipped
            if(data == null || !data.has(new NamespacedKey(plugin, "isHealthGoggle"))){
                if(Objects.equals(p.getPersistentDataContainer().get(new NamespacedKey(plugin, "hasHealthGogglesOn"), PersistentDataType.BOOLEAN), false)) {
                    continue;
                }
                p.getPersistentDataContainer().set(new NamespacedKey(plugin, "hasHealthGogglesOn"), PersistentDataType.BOOLEAN, false);
                //REMOVE THE DISPLAYS
                for(Entity e : p.getNearbyEntities(20,20,20)) {
                    hideHealth(p,e);
                }
                mobs.remove(p.getUniqueId());
                continue;
            }
            if(Objects.equals(p.getPersistentDataContainer().get(new NamespacedKey(plugin, "hasHealthGogglesOn"), PersistentDataType.BOOLEAN), false)){
                p.getPersistentDataContainer().set(new NamespacedKey(plugin, "hasHealthGogglesOn"), PersistentDataType.BOOLEAN, true);
            }
            //SET THE DISPLAYS
            for(Entity e : p.getNearbyEntities(20,20,20)) {
                UUID id = p.getUniqueId();
                if(!mobs.containsKey(id) ||!mobs.get(id).contains(e.getEntityId())){
                    List<Integer> list = new ArrayList<>();
                    if(mobs.containsKey(id)){
                       list = mobs.get(id);
                    }
                    list.add(e.getEntityId());
                    mobs.put(id,list);
                    showHealth(p,e);
                }
            }



        }

    }


    public static void showHealth(Player p, Entity e){
        EntityMeta metadata = EntityMeta.createMeta(e.getEntityId(), EntityTypes.ENTITY);
        if(!(e instanceof LivingEntity le)){
            //an item doesn't have a custom name
            return;
        }
        double health = Math.round(le.getHealth()*10) / 10D;
        double maxHealth = Math.round(Objects.requireNonNull(le.getAttribute(GENERIC_MAX_HEALTH)).getBaseValue()*10) / 10D;
        String healthString = String.valueOf(health);
        String customName = e.getCustomName();
        if(customName == null) {
            metadata.setCustomName(Component.text(healthString + "/" + maxHealth +
                    " HP"));
        }else{
            metadata.setCustomName(Component.text(customName + " (" + healthString + "/" + maxHealth +
                    " HP)"));
        }
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(e.getEntityId(), metadata);
        PacketEvents.getAPI().getPlayerManager().sendPacket(p, packet);
    }
    public static void hideHealth(Player p, Entity e){
        EntityMeta metadata = EntityMeta.createMeta(e.getEntityId(), EntityTypes.ENTITY);
        String customName = e.getCustomName();
        if (customName == null) {
            metadata.setCustomName(null);
        } else {
            metadata.setCustomName(Component.text(customName));
        }
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(e.getEntityId(), metadata);
        PacketEvents.getAPI().getPlayerManager().sendPacket(p, packet);
    }


}
