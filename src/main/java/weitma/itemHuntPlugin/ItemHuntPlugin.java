package weitma.itemHuntPlugin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import weitma.itemHuntPlugin.Commands.*;
import weitma.itemHuntPlugin.Listeners.*;

import java.util.*;
import java.util.stream.Collectors;

public final class ItemHuntPlugin extends JavaPlugin {

    private static HashMap<UUID, Material> itemsToCollectByPlayers;
    public static final int BACKPACK_ID = 100000;
    public static final int SKIPITEM_ID = 100001;
    public static final int SPECIAL_ROCKET_ID = 1000002;
    private final HashMap<UUID, ArrayList<ItemStack>> itemsCollectedByPlayers;
    private final HashMap<UUID, BossBar> bossBars;
    private final HashMap<UUID, Inventory> backpackInventories;
    private boolean challengeStarted;
    private boolean challengeFinished;
    private int taskID;

    private String currentTimer = "00:00:00";

    private ShowResultsCommand showResultsCommand;

    public ItemHuntPlugin() {
        this.backpackInventories = new HashMap<>();
        this.challengeStarted = false;
        this.challengeFinished = false;
        itemsToCollectByPlayers = new HashMap<>();
        itemsCollectedByPlayers = new HashMap<>();
        bossBars = new HashMap<>();
    }

    public Material getItemToCollect(UUID player) {
        return itemsToCollectByPlayers.get(player);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        showResultsCommand = new ShowResultsCommand(this);

        getCommand("startchallenge").setExecutor(new StartChallengeCommand(this));
        getCommand("stopchallenge").setExecutor(new StopChallengeCommand(this));
        getCommand("results").setExecutor(showResultsCommand);
        getCommand("teams").setExecutor(new ShowTeamsCommand());
        getCommand("skipitem").setExecutor(new AdminSkipItemCommand(this));
        getCommand("reset").setExecutor(new ResetWorldCommand(this));

        getServer().getPluginManager().registerEvents(new ItemCollectListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        //        stopTimer();
    }

    private void putItemToCollect(UUID player, Material item) {
        itemsToCollectByPlayers.put(player, item);
    }

    private void addItemToCollectedByPlayer(UUID id, ItemStack item) {
        if (itemsCollectedByPlayers.containsKey(id)) {
            itemsCollectedByPlayers.get(id).add(item);
        } else {
            ArrayList<ItemStack> materials = new ArrayList<>();
            materials.add(item);
            itemsCollectedByPlayers.put(id, materials);
        }
    }

    // New 1.21 items are included
    // Excluded: Silk Touch obtainables, Eggs, Music, Disc, End Items, Command, Unobtainable
    public void generateNewRandomMaterialToCollect(UUID id) {

        List<Material> pickableItems = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .filter(material -> !material.name().contains("SPAWN_EGG"))
                .filter(material -> !material.name().contains("MUSIC_DISC"))
                .filter(material -> !material.isAir())
                .filter(material -> !material.name().contains("COMMAND"))
                .filter(material -> !material.name().contains("BARRIER"))
                .filter(material -> !material.name().contains("STRUCTURE"))
                .filter(material -> !material.name().equals("LIGHT"))
                .filter(material -> !material.name().contains("JIGSAW"))
                .filter(material -> !material.name().contains("PETRIFIED_OAK_SLAB"))
                .filter(material -> !material.name().contains("INFESTED"))
                .filter(material -> !material.name().contains("SPAWNER"))
                .filter(material -> !material.name().contains("FARMLAND"))
                .filter(material -> !material.name().contains("DIRT_PATH"))
                .filter(material -> !material.name().contains("CHORUS_PLANT"))
                .filter(material -> !material.name().contains("BUDDING_AMETHYST"))
                .filter(material -> !material.name().contains("REINFORED_DEEPSLATE"))
                .filter(material -> !material.name().contains("ARMOR_TRIM"))
                .filter(material -> !material.name().endsWith("ORE"))
                .filter(material -> !material.name().contains("EXPOSED"))
                .filter(material -> !material.name().contains("WEATHERED"))
                .filter(material -> !material.name().contains("OXIDIZED"))
                .filter(material -> !material.name().contains("HEAD"))
                .filter(material -> !material.name().contains("SKULL"))
                .filter(material -> !material.name().contains("BANNER_PATTERN"))
                .filter(material -> !material.name().equals("POTION"))
                .filter(material -> !material.name().contains("POTTERY_SHERD"))
                .filter(material -> !material.name().contains("LARGE_FERN"))
                .filter(material -> !material.name().contains("SNIFFER_EGG"))
                .filter(material -> !material.name().contains("FROGLIGHT"))
                .filter(material -> !material.name().contains("NETHERITE"))
                .filter(material -> !material.name().contains("TOTEM"))
                .filter(material -> !material.name().equals("ENCHANTED_BOOK"))
                .filter(material -> !material.name().contains("ELYTRA"))
                .filter(material -> !material.name().contains("SHULKER"))
                .filter(material -> !material.name().contains("TORCHFLOWER"))
                .filter(material -> !material.name().equals("MACE"))
                .filter(material -> !material.name().contains("ANVIL"))
                .filter(material -> !material.name().contains("PHANTOM"))
                .filter(material -> !material.name().contains("BELL"))
                .filter(material -> !material.name().contains("BEEHIVE"))
                .filter(material -> !material.name().contains("BEE_NEST"))
                .filter(material -> !material.name().contains("ANCIENT_DEBRIS"))
                .filter(material -> !material.name().contains("_TERRACOTTA"))
                .filter(material -> !material.name().contains("AMETHYST_BUD"))
                .filter(material -> !material.name().contains("AMETHYST_CLUSTER"))
                .filter(material -> !material.name().contains("BUDDING_AMETHYST"))
                .filter(material -> !material.name().contains("CORAL"))
                .filter(material -> !material.name().contains("CREAKING"))
                .filter(material -> !material.name().contains("DECORATED"))
                .filter(material -> !material.name().contains("DOUBLE_SLAB"))
                .filter(material -> !material.name().contains("GILDED_BLACKSTONE"))
                .filter(material -> !material.name().contains("GRASS_BLOCK"))
                .filter(material -> !material.name().contains("NYLIUM"))
                .filter(material -> !material.name().contains("PODZOL"))
                .filter(material -> !material.name().contains("SCULK"))
                .filter(material -> !material.name().contains("SEA_LANTERN"))
                .filter(material -> !material.name().contains("SOUL_CAMPFIRE"))
                .filter(material -> !material.name().contains("SUSPICIOUS"))
                .filter(material -> !material.name().contains("TURTLE"))
                .filter(material -> !material.name().startsWith("END_"))
                .filter(material -> !material.name().contains("HEART"))
                .filter(material -> !material.name().contains("PRISMARINE"))
                .filter(material -> !material.name().contains("VAULT"))
                .filter(material -> !material.name().contains("GOAT"))
                .filter(material -> !material.name().contains("CHORUS"))
                .filter(material -> !material.name().contains("FROGSPAWN"))
                .filter(material -> !material.name().contains("NETHER_STAR"))
                .filter(material -> !material.name().contains("DEBUG"))
                .filter(material -> !material.name().contains("LINGERING"))
                .filter(material -> !material.name().contains("CONDUIT"))
                .filter(material -> !material.name().contains("PITCHER"))
                .filter(material -> !material.name().contains("ENCHANTED_GOLDEN_APPLE"))
                .filter(material -> !material.name().contains("ECHO_SHARD"))
                .filter(material -> !material.name().contains("DRAGON"))
                .filter(material -> !material.name().contains("NAUTILUS"))
                .filter(material -> !material.name().contains("SPONGE"))
                .filter(material -> !material.name().contains("SMITHING_TEMPLATE"))
                .filter(material -> !material.name().contains("SPAWN_EGG"))
                .filter(material -> !material.name().contains("BEACON"))
                .filter(material -> !material.name().contains("TRIDENT"))
                .filter(material -> !material.name().contains("SPECTRAL"))
                .filter(material -> !material.name().contains("NAME_TAG"))
                .filter(material -> !material.name().contains("WAXED_"))
                .filter(material -> !material.name().contains("TRIPWIRE"))
                .filter(material -> !material.name().contains("BEDROCK"))
                .filter(material -> !material.name().contains("ICE"))
                .filter(material -> !material.name().contains("MYCELIA"))
                .filter(material -> !material.name().contains("PURPUR"))

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

        String itemNameForWiki = Arrays.stream(itemToCollect.name().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining("_"));

        BossBar bossbar = Bukkit.createBossBar(itemName,
                BarColor.WHITE,
                BarStyle.SOLID);
        bossbar.setProgress(0.0);
        bossbar.addPlayer(player);
        bossBars.put(playerID, bossbar);

        ItemStack itemStack = new ItemStack(itemToCollect);
        Item itemEntity = player.getWorld().dropItem(player.getLocation(), itemStack);
        itemEntity.setPickupDelay(Integer.MAX_VALUE); // Prevent the item from being picked up
        player.addPassenger(itemEntity);

        TextComponent message = new TextComponent(ChatColor.DARK_AQUA + "Collect: ");
        TextComponent link = new TextComponent(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "[" + itemName + "]");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.wiki/w/" + itemNameForWiki));
        message.addExtra(link);
        player.spigot().sendMessage(message);
    }

    public boolean isChallengeStarted() {
        return challengeStarted;
    }

    public void setChallengeStarted(boolean challengeStarted) {
        this.challengeStarted = challengeStarted;
    }

    public void startTimer(int seconds) {
        // Broadcast the time left to all players
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            int timeLeft = seconds;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    StringBuilder message = new StringBuilder();
                    message.append(ChatColor.GOLD).append(ChatColor.BOLD);

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

                    currentTimer = message.toString();

                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player ->
                            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(message.toString()))
                    ));

                    timeLeft--;

                } else {
                    challengeFinished();
                }
            }
        }, 0L, 20L); // 20 ticks = 1 second
    }

    private void challengeFinished() {
        Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player -> {
            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "00:00" +
                    ":00"));
            player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1, 1);
            player.sendMessage(ChatColor.GREEN + "Time's up! The challenge has ended!");
            if (!player.getPassengers().isEmpty()) {
                player.getPassengers().forEach(passenger -> {
                    passenger.setVisibleByDefault(false);
                    player.removePassenger(passenger);
                });
            }
        }));
        challengeStarted = false;
        challengeFinished = true;
        stopTimer();
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        for (BossBar bossBar : bossBars.values()) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public boolean isChallengeFinished() {
        return challengeFinished;
    }

    public void resetChallenge() {
        stopTimer();
        showResultsCommand.resetScores();
        showResultsCommand.setCurrentIndex(0);
        challengeFinished = false;
        itemsToCollectByPlayers.clear();
        itemsCollectedByPlayers.clear();
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        for (BossBar bossBar : bossBars.values()) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
        bossBars.clear();
        Bukkit.getOnlinePlayers().forEach(player -> {
                    if (challengeStarted) {
                        player.sendMessage(ChatColor.RED + "The challenge has been stopped!");
                    }
                    if (!player.getPassengers().isEmpty()) {
                        player.getPassengers().forEach(passenger -> player.removePassenger(passenger));
                    }
                }
        );
        challengeStarted = false;
    }

    public HashMap<UUID, ArrayList<ItemStack>> getItemsCollectedByPlayers() {
        return itemsCollectedByPlayers;
    }

    public ItemStack getSkipItem(int amount) {
        ItemStack skipItem = new ItemStack(Material.BARRIER, amount); // Choose your item type
        ItemMeta meta = skipItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Skip Item"); // Set a custom name
            meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to skip")); // Set lore
            meta.setCustomModelData(SKIPITEM_ID);
            skipItem.setItemMeta(meta);
        }

        return skipItem;
    }

    public void skipItemToCollect(UUID uuid) {
        ItemStack itemToCollect = new ItemStack(getItemToCollect(uuid));
        Player p = Bukkit.getPlayer(uuid);
        p.getInventory().addItem(itemToCollect);
        successfullPickup(p, itemToCollect.getType(), true, false);
    }

    public void successfullPickup(Player player, Material itemPickedUp, boolean wasSkipItem, boolean skippedByAdmin) {

        String itemReadable = Arrays.stream(itemPickedUp.name().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        if (!skippedByAdmin) {
            player.sendMessage(ChatColor.GREEN + itemReadable + ChatColor.WHITE + " collected!");
        } else {
            player.sendMessage(ChatColor.RED + itemReadable + " was skipped by Admin!");
        }

        player.getPassengers().forEach(passenger -> {
            passenger.setVisibleByDefault(false);
            player.removePassenger(passenger);
        });

        ItemStack itemPickedUpStack = new ItemStack(itemPickedUp);
        ItemMeta itemPickedUpStackMeta = itemPickedUpStack.getItemMeta();
        assert itemPickedUpStackMeta != null;

        if (wasSkipItem) {
            itemPickedUpStackMeta.setLore(Arrays.asList("Collected at ", currentTimer, " (Skipped)"));
        } else {
            itemPickedUpStackMeta.setLore(Arrays.asList("Collected at ", currentTimer));
        }
        itemPickedUpStack.setItemMeta(itemPickedUpStackMeta);

        if (!skippedByAdmin) {
            addItemToCollectedByPlayer(player.getUniqueId(), itemPickedUpStack);
        }
        generateNewRandomMaterialToCollect(player.getUniqueId());
        showItemToCollect(player.getUniqueId());
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1, 1);
    }

    public ItemStack createBackpack(UUID playerID) {
        ItemStack backpack = new ItemStack(Material.BUNDLE);
        ItemMeta meta = backpack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Backpack");
        meta.setUnbreakable(true);
        meta.setCustomModelData(BACKPACK_ID);
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to open"));
        backpack.setItemMeta(meta);
        backpackInventories.put(playerID, Bukkit.createInventory(Bukkit.getPlayer(playerID), 27, Bukkit.getPlayer(playerID).getName() + "'s Backpack"));
        return backpack;
    }

    public Inventory getBackpackInventory(UUID playerID) {
        return backpackInventories.get(playerID);
    }

    public ItemStack getSpecialRocket(){
        ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET, 2);
        ItemMeta rocketMeta = rocket.getItemMeta();
        rocketMeta.setCustomModelData(ItemHuntPlugin.SPECIAL_ROCKET_ID);
        rocket.setItemMeta(rocketMeta);
        return rocket;
    }

}
