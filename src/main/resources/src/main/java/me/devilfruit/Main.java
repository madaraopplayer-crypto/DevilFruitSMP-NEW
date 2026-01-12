package me.devilfruit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    private NamespacedKey key;

    @Override
    public void onEnable() {
        key = new NamespacedKey(this, "hasfruit");
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("DevilFruitSMP Enabled");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!p.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            p.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            ItemStack fruit = getRandomFruit();
            p.getInventory().addItem(fruit);
            p.sendMessage("§cYou received a Devil Fruit!");
        }
    }

    private ItemStack getRandomFruit() {
        Material[] fruits = {
                Material.APPLE,
                Material.GOLDEN_APPLE,
                Material.CHORUS_FRUIT
        };

        Material m = fruits[new Random().nextInt(fruits.length)];
        ItemStack item = new ItemStack(m);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§dDevil Fruit");
        item.setItemMeta(meta);
        return item;
    }
  }
