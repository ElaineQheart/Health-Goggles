package me.elaineqheart.healthGoggles;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.elaineqheart.healthGoggles.commands.GiveCommand;
import me.elaineqheart.healthGoggles.listeners.OnEntityDamageListener;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class HealthGoggles extends JavaPlugin {

    @Override
    public void onLoad(){
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings()
                .checkForUpdates(false)
                .reEncodeByDefault(false)
                .bStats(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {

        getCommand("goggles").setExecutor(new GiveCommand(this));
        getServer().getPluginManager().registerEvents(new UpdateTask(this),this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageListener(this),this);
        BukkitTask task = new UpdateTask(this).runTaskTimer(this,0,5);

        PacketEvents.getAPI().init();
        APIConfig settings = new APIConfig(PacketEvents.getAPI())
                .debugMode()
                .checkForUpdates()
                .tickTickables()
                .useBstats()
                .usePlatformLogger();
        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
        EntityLib.init(platform, settings);
    }

    @Override
    public void onDisable(){

        PacketEvents.getAPI().terminate();

    }

}
