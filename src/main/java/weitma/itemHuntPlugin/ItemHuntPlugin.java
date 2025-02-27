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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import weitma.itemHuntPlugin.Commands.*;
import weitma.itemHuntPlugin.Listeners.*;
import weitma.itemHuntPlugin.Utils.InGameTimer;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;

import java.util.*;
import java.util.stream.Collectors;

public final class ItemHuntPlugin extends JavaPlugin {

    public static final int BACKPACK_ID = 100000;
    public static final int SKIPITEM_ID = 100001;
    public static final int UPDRAFT_ITEM = 1000003;
    public static final int TEAM_ITEM = 1000004;

    public static final int RANDOM_ORDER_GAMEMODE = 0;
    public static final int SAME_ORDER_GAMEMODE = 1;

    private static final ArrayList<Material> itemCollectOrder = new ArrayList<>();
    private static final HashMap<Team, Material> itemToCollectByTeam = new HashMap<>();
    private final HashMap<Team, ArrayList<ItemStack>> itemsCollectedByTeam;
    private final HashMap<UUID, BossBar> bossBars;
    private final HashMap<Team, Inventory> backpackInventoryForTeam;
    private final HashMap<UUID, String> displayNames;
    private final InGameTimer inGameTimer;
    private boolean challengeStarted;
    private boolean challengeFinished;
    private ShowResultsCommand showResultsCommand;
    private ShowCollectedItemsCommand showCollectedItemsCommand;
    private boolean withUpdraftItem = false;
    private NightSkipVoteCommand nightSkipVoteCommand;
    private int gamemode;

    public ItemHuntPlugin() {
        this.backpackInventoryForTeam = new HashMap<>();
        this.challengeStarted = false;
        this.challengeFinished = false;
        itemsCollectedByTeam = new HashMap<>();
        bossBars = new HashMap<>();
        displayNames = new HashMap<>();
        this.inGameTimer = new InGameTimer(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        showResultsCommand = new ShowResultsCommand(this);
        showCollectedItemsCommand = new ShowCollectedItemsCommand(this);
        nightSkipVoteCommand = new NightSkipVoteCommand();

        itemCollectOrder.add(Material.OAK_PLANKS);

        getCommand("startchallenge").setExecutor(new StartChallengeCommand(this));
        getCommand("results").setExecutor(showResultsCommand);
        getCommand("teams").setExecutor(new ShowTeamsCommand(this));
        getCommand("skipitem").setExecutor(new AdminSkipItemCommand(this));
        getCommand("showcollecteditems").setExecutor(showCollectedItemsCommand);
        getCommand("resetchallenge").setExecutor(new resetChallengeCommand(this));
        getCommand("timer").setExecutor(new TimerCommands(inGameTimer));
        getCommand("startvoteskipnight").setExecutor(nightSkipVoteCommand);
        getCommand("voteskipnighttrue").setExecutor(new PlayerVoteSkipNightTrueCommand(nightSkipVoteCommand));
        getCommand("voteskipnightfalse").setExecutor(new PlayerVoteSkipNightFalseCommand(nightSkipVoteCommand));
        getCommand("giveupdraftitem").setExecutor(new GiveUpdraftItemCommand(this));

        getServer().getPluginManager().registerEvents(new ItemCollectListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, showCollectedItemsCommand), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        checkNight();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        resetChallenge();
    }

    public Material getItemToCollect(Team team) {
        return itemToCollectByTeam.get(team);
    }

    private void addItemToCollectedByTeam(Team team, ItemStack item) {
        if (itemsCollectedByTeam.containsKey(team)) {
            itemsCollectedByTeam.get(team).add(item);
        } else {
            ArrayList<ItemStack> materials = new ArrayList<>();
            materials.add(item);
            itemsCollectedByTeam.put(team, materials);
        }
    }

    // New 1.21 items are included
    // Excluded: Silk Touch obtainables, Eggs, Music, Disc, End Items, Command, Unobtainable
    public void generateNewRandomMaterialToCollect(Team team) {

        if (gamemode == SAME_ORDER_GAMEMODE) {
            if (getItemsCollectedByTeam(team).size() < itemCollectOrder.size()) {
                itemToCollectByTeam.put(team, itemCollectOrder.get(getItemsCollectedByTeam(team).size()));
                Bukkit.getLogger().info("Same Order: " + getItemToCollect(team).toString());
                return;
            }
        }

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
                .filter(material -> !material.name().contains("BEEHIVE"))
                .filter(material -> !material.name().contains("BEE_NEST"))
                .filter(material -> !material.name().contains("ANCIENT_DEBRIS"))
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
                .filter(material -> !material.name().contains("BEDROCK"))
                .filter(material -> !material.name().contains("ICE"))
                .filter(material -> !material.name().contains("MYCELIA"))
                .filter(material -> !material.name().contains("PURPUR"))
                .filter(material -> !material.name().contains("GHAST"))
                .filter(material -> !material.name().contains("KNOWLEDGE"))
                .filter(material -> !material.name().contains("SADDLE"))
                .filter(material -> !material.name().contains("HORSE"))
                .filter(material -> !material.name().contains("CREAKING"))
                .filter(material -> !material.name().contains("RESIN"))
                .toList();

        // pickable 961 items
        // material.values 1537 items
        Material material = pickableItems.get((int) (Math.random() * pickableItems.size()));

        itemCollectOrder.add(material);
        itemToCollectByTeam.put(team, material);
    }

    public void showItemToCollect(UUID playerID) {
        Player player = Bukkit.getPlayer(playerID);

        BossBar oldBossBar = bossBars.remove(playerID);
        if (oldBossBar != null)
            oldBossBar.removeAll();

        Material itemToCollect = getItemToCollect(TeamManager.getInstance().getTeamOfPlayer(playerID));
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

        // Passenger
        //        ItemStack itemStack = new ItemStack(itemToCollect);
        //        Item itemEntity = player.getWorld().dropItem(player.getLocation(), itemStack);
        //        itemEntity.setPickupDelay(Integer.MAX_VALUE); // Prevent the item from being picked up
        //        player.addPassenger(itemEntity);

        TextComponent message = new TextComponent(ChatColor.DARK_AQUA + "Collect: ");
        TextComponent link = new TextComponent(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "[" + itemName + "]");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.wiki/w/" + itemNameForWiki));
        message.addExtra(link);
        player.spigot().sendMessage(message);

        player.setPlayerListName(displayNames.get(playerID) + ChatColor.GOLD + " [" + itemName + "]");
    }

    public boolean isChallengeStarted() {
        return challengeStarted;
    }

    public void setChallengeStarted(boolean challengeStarted) {
        this.challengeStarted = challengeStarted;
    }

    public void startTimer(int seconds) {
        inGameTimer.startTimer(seconds);
    }

    public void onTimeExpired() {
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
        challengeFinished = true;
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        for (BossBar bossBar : bossBars.values()) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
    }

    public void resetChallenge() {
        inGameTimer.stopTimer();
        showResultsCommand.resetScores();
        showCollectedItemsCommand.clearTeamInventories();
        challengeFinished = false;
        challengeStarted = false;
        withUpdraftItem = false;
        itemToCollectByTeam.clear();
        itemsCollectedByTeam.clear();
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());
        for (BossBar bossBar : bossBars.values()) {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }
        bossBars.clear();
        Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(ChatColor.RED + "The challenge has been reset!");
                    if (!player.getPassengers().isEmpty()) {
                        player.getPassengers().forEach(passenger -> player.removePassenger(passenger));
                    }
                    player.setDisplayName(player.getName());
                }
        );
    }

    public boolean isChallengeFinished() {
        return challengeFinished;
    }

    public HashMap<Team, ArrayList<ItemStack>> getItemsCollectedByTeam() {
        return itemsCollectedByTeam;
    }

    public ArrayList<ItemStack> getItemsCollectedByTeam(Team team) {
        if (itemToCollectByTeam.get(team) == null) {
            return new ArrayList<>();
        } else {
            return itemsCollectedByTeam.get(team);
        }
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
        ItemStack itemToCollect = new ItemStack(getItemToCollect(TeamManager.getInstance().getTeamOfPlayer(uuid)));
        Player p = Bukkit.getPlayer(uuid);
        p.getInventory().addItem(itemToCollect);
        successfullPickup(p, itemToCollect.getType(), true, false);
    }

    public void successfullPickup(Player player, Material itemPickedUp, boolean wasSkipItem, boolean skippedByAdmin) {

        String itemReadable = Arrays.stream(itemPickedUp.name().split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        if (!skippedByAdmin) {
            for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                Team teamOfPlayer = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId());
                playerOnline.sendMessage(teamOfPlayer.getChatColor() + teamOfPlayer.getTeamName() + " (" + player.getName() + ") " + ChatColor.WHITE + "collected " + itemReadable);
            }
        } else {
            for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                playerOnline.sendMessage(ChatColor.RED + itemReadable + " was skipped by Admin for " + TeamManager.getInstance().getTeamOfPlayer(playerOnline.getUniqueId()).getTeamName() + "!");
            }
        }

        ItemStack itemPickedUpStack = new ItemStack(itemPickedUp);
        ItemMeta itemPickedUpStackMeta = itemPickedUpStack.getItemMeta();
        assert itemPickedUpStackMeta != null;

        if (wasSkipItem) {
            itemPickedUpStackMeta.setLore(Arrays.asList("Collected by " + player.getName() + " at", inGameTimer.getCurrentTimer(), " (Skipped)"));
        } else {
            itemPickedUpStackMeta.setLore(Arrays.asList("Collected by " + player.getName() + " at", inGameTimer.getCurrentTimer()));
        }
        itemPickedUpStack.setItemMeta(itemPickedUpStackMeta);
        Team teamOfPlayer = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId());
        if (!skippedByAdmin) {
            addItemToCollectedByTeam(teamOfPlayer, itemPickedUpStack);
        }
        generateNewRandomMaterialToCollect(teamOfPlayer);

        for (UUID uuidInTeam : TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId()).getTeamMembers()) {
            Player playerInTeam = Bukkit.getPlayer(uuidInTeam);
            assert playerInTeam != null;
            playerInTeam.getPassengers().forEach(passenger -> {
                passenger.setVisibleByDefault(false);
                player.removePassenger(passenger);
            });

            showItemToCollect(uuidInTeam);
            playerInTeam.playSound(player.getLocation(), "entity.experience_orb.pickup", 1, 1);

            if (isWithUpdraftItem() && !wasSkipItem) {
                playerInTeam.getInventory().addItem(getUpdraftItem(1));
            }
        }
    }

    public ItemStack createBackpack(Team team, int size, int skipItemsAmount) throws IllegalArgumentException {
        if (size % 9 != 0) {
            throw new IllegalArgumentException("Backpack-Size must be a multiple of 9");
        }
        ItemStack backpack = new ItemStack(Material.BUNDLE);
        ItemMeta meta = backpack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Backpack " + team.getTeamName());
        meta.setUnbreakable(true);
        meta.setCustomModelData(BACKPACK_ID);
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to open"));
        backpack.setItemMeta(meta);
        Inventory backpackInventory = Bukkit.createInventory(null, size, "Backpack of " + team.getTeamName());
        backpackInventory.addItem(getSkipItem(skipItemsAmount));
        backpackInventoryForTeam.put(team, backpackInventory);
        return backpack;
    }

    public Inventory getBackpackInventory(Team team) {
        return backpackInventoryForTeam.get(team);
    }

    public ItemStack getUpdraftItem(int amount) {
        ItemStack updraft = new ItemStack(Material.FEATHER, amount);
        ItemMeta updraftMeta = updraft.getItemMeta();
        updraftMeta.setDisplayName(ChatColor.GOLD + "Updraft");
        updraftMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right-click to teleport in the air"));
        updraftMeta.setCustomModelData(UPDRAFT_ITEM);
        updraft.setItemMeta(updraftMeta);
        return updraft;
    }

    public void setPlayerListName(String listName, UUID playerID) {
        displayNames.put(playerID, listName);
        Bukkit.getPlayer(playerID).setPlayerListName(listName);
    }

    public boolean isWithUpdraftItem() {
        return withUpdraftItem;

    }

    public void setWithUpdraftItem(boolean withUpdraftItem) {
        this.withUpdraftItem = withUpdraftItem;
    }

    public void checkNight() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getWorlds().forEach(world -> {
                    if (world.getTime() > 13000 && world.getTime() < 23000) {
                        if (!nightSkipVoteCommand.isVoteRunning() && !nightSkipVoteCommand.didPlayersVoteNo()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "startvoteskipnight");
                            nightSkipVoteCommand.setVoteRunning(true);
                        }
                    }
                    if (world.getTime() < 500) {
                        Bukkit.getLogger().info("Day started");
                        if (nightSkipVoteCommand.isVoteRunning()) {
                            nightSkipVoteCommand.setVoteRunning(false);
                            nightSkipVoteCommand.setPlayersVotedNo(false);
                            Bukkit.getOnlinePlayers().forEach(player ->
                                    player.sendMessage(ChatColor.RED + "Vote has been canceled since its already day"));
                        }
                        nightSkipVoteCommand.setVoteRunning(false);
                        nightSkipVoteCommand.setPlayersVotedNo(false);
                    }
                });
            }
        }.runTaskTimer(this, 0, 200);
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }
}
