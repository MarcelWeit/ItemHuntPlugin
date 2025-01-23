package weitma.itemHuntPlugin;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import weitma.itemHuntPlugin.Commands.ShowResultsCommand;
import weitma.itemHuntPlugin.Commands.StartChallengeCommand;
import weitma.itemHuntPlugin.Commands.StopChallengeCommand;
import weitma.itemHuntPlugin.Listeners.ItemCollectListener;
import weitma.itemHuntPlugin.Listeners.SkipItemUseListener;

import java.util.*;
import java.util.stream.Collectors;

public final class ItemHuntPlugin extends JavaPlugin {

    private static HashMap<UUID, Material> itemsToCollectByPlayers;
    private boolean challengeStarted;
    private HashMap<UUID, ArrayList<Material>> itemsCollectedByPlayers;
    private HashMap<UUID, BossBar> bossBars;
    private int taskID;

    public ItemHuntPlugin() {
        this.challengeStarted = false;
        itemsToCollectByPlayers = new HashMap<>();
        itemsCollectedByPlayers = new HashMap<>();
        bossBars = new HashMap<>();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        getCommand("startchallenge").setExecutor(new StartChallengeCommand(this));
        getCommand("stopchallenge").setExecutor(new StopChallengeCommand(this));
        getCommand("showresults").setExecutor(new ShowResultsCommand(this));

        getServer().getPluginManager().registerEvents(new ItemCollectListener(this), this);
        getServer().getPluginManager().registerEvents(new SkipItemUseListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        stopTimer();
    }

    public void putItemToCollect(UUID player, Material item) {
        itemsToCollectByPlayers.put(player, item);
    }

    public void addItemToCollectedByPlayer(UUID id, Material material) {
        if (itemsCollectedByPlayers.containsKey(id)) {
            itemsCollectedByPlayers.get(id).add(material);
        } else {
            ArrayList<Material> materials = new ArrayList<>();
            materials.add(material);
            itemsCollectedByPlayers.put(id, materials);
        }
    }

    public void generateNewRandomMaterialToCollect(UUID id) {
        List<Material> pickableItems = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .toList();
        Material material = pickableItems.get((int) (Math.random() * pickableItems.size()));
        putItemToCollect(id, material);
    }

    public void showItemToCollect(UUID playerID) {
        Player player = Bukkit.getPlayer(playerID);

        BossBar oldBossBar = bossBars.remove(playerID);
        if (oldBossBar != null)
            oldBossBar.removeAll();

        Material itemToCollect = getItemToCollect(playerID);
        String itemName = Arrays.stream(itemToCollect.name().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        BossBar bossbar = Bukkit.createBossBar(itemName,
                BarColor.WHITE,
                BarStyle.SOLID);
        bossbar.setProgress(0.0);
        bossbar.addPlayer(player);
        bossBars.put(playerID, bossbar);


    }

    public boolean isChallengeStarted() {
        return challengeStarted;
    }

    public void setChallengeStarted(boolean challengeStarted) {
        this.challengeStarted = challengeStarted;
    }

    public void startCountdown(int seconds) {
        // Broadcast the time left to all players
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            int timeLeft = seconds;

            boolean playedSound = false;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    StringBuilder message = new StringBuilder();
                    message.append(ChatColor.GREEN).append(ChatColor.BOLD);

                    int hoursRemaining = timeLeft / 3600;
                    int minutesRemaining = (timeLeft % 3600) / 60;
                    int secondsRemaining = timeLeft % 60;

                    // Broadcast the time left to all players
                    if (hoursRemaining > 0) {
                        if (hoursRemaining < 10) {
                            message.append("0");
                        }
                        message.append(hoursRemaining);
                    } else {
                        message.append("00");
                    }
                    message.append(":");

                    if (minutesRemaining > 0) {
                        if (minutesRemaining < 10) {
                            message.append("0");
                        }
                        message.append(minutesRemaining);
                    } else {
                        message.append("00");
                    }
                    message.append(":");

                    if (secondsRemaining > 0) {
                        if (secondsRemaining < 10) {
                            message.append("0");
                        }
                        message.append(secondsRemaining);
                    } else {
                        message.append("00");
                    }

                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player ->
                            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(message.toString()))
                    ));

                    timeLeft--;

                } else {
                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player -> {
                        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "00:00" +
                                ":00"));
                        if(!playedSound){
                            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
                        }
                    }));
                    playedSound = true;

                }
            }
        }, 0L, 20L); // 20 ticks = 1 second
    }

    public void stopTimer(){
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void stopChallenge(){
        stopTimer();
        challengeStarted = false;
        itemsToCollectByPlayers.clear();
        itemsCollectedByPlayers.clear();
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        for (BossBar bossBar : bossBars.values()) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
        bossBars.clear();
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(ChatColor.RED + "The challenge has been stopped!")
        );
    }

    public static Material getItemToCollect(UUID player) {
        return itemsToCollectByPlayers.get(player);
    }

    public HashMap<UUID, ArrayList<Material>> getItemsCollectedByPlayers() {
        return itemsCollectedByPlayers;
    }

    public ItemStack getSkipItem(int amount){
        ItemStack skipItem = new ItemStack(Material.NETHER_STAR, amount); // Choose your item type
        ItemMeta meta = skipItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Skip Item"); // Set a custom name
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to skip")); // Set lore
            skipItem.setItemMeta(meta);
        }

        return skipItem;
    }

    public void skipItemToCollect(UUID uuid) {
        Bukkit.getPlayer(uuid).getInventory().addItem(new ItemStack(getItemToCollect(uuid)));
    }
}
