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
private ItemStack getRandomFruit() {
    Material[] fruits = { Material.APPLE, Material.GOLDEN_APPLE, Material.CHORUS_FRUIT };
    String[] names = { "§cRed Spiral Fruit", "§eYellow Lightning Fruit", "§5Purple Dark Fruit" };
    int[] modelData = { 1001, 1002, 1003 }; // assign custom model IDs

    int index = new Random().nextInt(fruits.length);

    ItemStack item = new ItemStack(fruits[index]);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(names[index]);
    meta.setCustomModelData(modelData[index]); // set custom model
    item.setItemMeta(meta);
    return item;
}
@EventHandler
public void onUse(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    ItemStack item = e.getItem();
    if (item == null || !item.hasItemMeta()) return;
    ItemMeta meta = item.getItemMeta();
    if (!meta.hasDisplayName()) return;

    String name = meta.getDisplayName();
    long current = System.currentTimeMillis();

    // Check cooldown
    if (cooldowns.containsKey(player.getUniqueId())) {
        long last = cooldowns.get(player.getUniqueId());
        if (current - last < 1000) return;
    }

    if (name.equals("§cRed Spiral Fruit")) {
        if (isCooldown(player, 8)) return;
        player.launchProjectile(org.bukkit.entity.Fireball.class);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0,1,0), 20, 0.5,0.5,0.5,0.05);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1f,1f);
        sendCooldown(player, 8);

    } else if (name.equals("§eYellow Lightning Fruit")) {
        if (isCooldown(player, 12)) return;
        Location loc = player.getTargetBlockExact(20) != null ? player.getTargetBlockExact(20).getLocation().add(0,1,0) : player.getLocation();
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 30, 0.5,0.5,0.5);
        player.getWorld().strikeLightning(loc);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f,1f);
        sendCooldown(player, 12);

    } else if (name.equals("§5Purple Dark Fruit")) {
        if (isCooldown(player, 5)) return;
        Location start = player.getLocation();
        Location end = start.add(player.getLocation().getDirection().multiply(5));
        player.getWorld().spawnParticle(Particle.PORTAL, start.add(0,1,0), 30, 0.5,0.5,0.5);
        player.teleport(end);
        player.getWorld().spawnParticle(Particle.PORTAL, end.add(0,1,0), 30,0.5,0.5,0.5);
        player.getWorld().playSound(end, Sound.ENTITY_ENDERMAN_TELEPORT, 1f,1f);
        sendCooldown(player, 5);
    }
private boolean isCooldown(Player p, int seconds) {
    UUID id = p.getUniqueId();
    if (!cooldowns.containsKey(id)) return false;
    long last = cooldowns.get(id);
    long now = System.currentTimeMillis();
    if (now - last < seconds * 1000) {
        long left = (seconds*1000 - (now - last))/1000;
        p.spawnParticle(Particle.SMOKE_NORMAL, p.getLocation().add(0,1,0), 10, 0.3,0.3,0.3);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f,1f);
        p.sendMessage("§cAbility on cooldown! " + left + "s left.");
        return true;
    }
    return false;
}

private void sendCooldown(Player p, int seconds) {
    cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
    new BukkitRunnable() {
        @Override
        public void run() {
            cooldowns.remove(p.getUniqueId());
        }
    }.runTaskLater(this, seconds*20L);
        }
