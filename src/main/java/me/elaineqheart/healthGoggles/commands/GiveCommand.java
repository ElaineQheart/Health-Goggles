package me.elaineqheart.healthGoggles.commands;

import me.elaineqheart.healthGoggles.HealthGoggles;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class GiveCommand implements CommandExecutor {

    HealthGoggles plugin;

    public GiveCommand(HealthGoggles plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player p){

            ItemStack item = new ItemStack(Material.LEATHER_HELMET);
            ItemMeta meta = item.getItemMeta();
            meta.setItemName(ChatColor.RED + "Health Goggles");
            meta.setLore(List.of(new String[]{"Allows the wearer to see", "the health of their opponents!"}));
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_DYE);

            LeatherArmorMeta leatherMeta = (LeatherArmorMeta)meta;
            leatherMeta.setColor(Color.fromRGB(215,152,148));

            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, "isHealthGoggle"), PersistentDataType.BOOLEAN, true);


            item.setItemMeta(meta);

            p.getInventory().addItem(item);

        }

        return true;
    }
}
