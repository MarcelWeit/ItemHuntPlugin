package weitma.itemHuntPlugin.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;

import java.util.*;

public class ShowResultsCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;
    private int currentIndex = 0;
    private List<Map.Entry<Team, Integer>> sortedScores;

    public ShowResultsCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (plugin.isChallengeFinished()) {
            if (sortedScores == null) {
                initializeScores();
            }
            if (currentIndex < sortedScores.size()) {
                displayNextTeamResults();
            } else {
                currentIndex = 0; // Reset index if all players have been displayed
            }
        } else {
            sender.sendMessage(ChatColor.RED + "The challenge has not finished yet!");
        }
        return true;
    }

    private void initializeScores() {
        HashMap<Team, ArrayList<ItemStack>> itemsCollectedByTeam = plugin.getItemsCollectedByTeam();
        HashMap<Team, Integer> scores = new HashMap<>();
        itemsCollectedByTeam.forEach((team, materials) -> {
            scores.put(team, materials.size());
        });
        sortedScores = scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();
    }

    private void displayNextTeamResults() {
        Map.Entry<Team, Integer> entry = sortedScores.get(currentIndex);
        currentIndex++;
        showResultsAsInventory(entry, plugin.getItemsCollectedByTeam());
    }

    private void showResultsAsInventory(Map.Entry<Team, Integer> entry, HashMap<Team, ArrayList<ItemStack>> itemsCollectedByTeam) {
        Team team = entry.getKey();
        int score = entry.getValue();

        // Create inventory
        Inventory inventory = Bukkit.createInventory(null, 54, "Items collected");
        ItemStack whitePane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta whitePaneMeta = whitePane.getItemMeta();
        assert whitePaneMeta != null;
        whitePaneMeta.setDisplayName(" ");
        whitePane.setItemMeta(whitePaneMeta);

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackPaneMeta = blackPane.getItemMeta();
        assert blackPaneMeta != null;
        blackPaneMeta.setDisplayName(" ");
        blackPane.setItemMeta(blackPaneMeta);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, whitePane);
        }
        for (int i = 9; i < 54; i++) {
            inventory.setItem(i, blackPane);
        }

        // open inventory for all Players
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.openInventory(inventory);
        }

        Set<Integer> skipSpaces = new HashSet<>();
        for (int i = 1; i < 100; i++) {
            skipSpaces.add((i * 8) + i - 1);
            skipSpaces.add((i * 9));
        }

        new BukkitRunnable() {
            final int maxCounter = itemsCollectedByTeam.get(team).size() + 9;
            int listcounter = 0;
            int placement = 10;

            @Override
            public void run() {
                if (listcounter >= maxCounter - 9) {

                    int playerPlacement = sortedScores.size() - currentIndex + 1;
                    cancel(); // Stop the task when all items are added

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.sendTitle(playerPlacement + ". " + team.getTeamName(), ChatColor.GOLD + Integer.toString(score) + " Tasks completed", 10, 100,
                                20);
                        onlinePlayer.playSound(onlinePlayer.getLocation(), "minecraft:entity.player.levelup", 1, 1);

                        TextComponent messsagePlacement = new TextComponent(playerPlacement + ". " + team.getTeamName() + " - " + score);
                        TextComponent messageInventory = new TextComponent(ChatColor.GREEN + " [Show Items]");
                        messageInventory.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/showcollecteditems " + TeamManager.getInstance().getTeamIndex(team.getTeamName()) + " " + onlinePlayer.getUniqueId() + " " + "0"));

                        messsagePlacement.addExtra(messageInventory);
                        onlinePlayer.spigot().sendMessage(messsagePlacement);
                    }

                    return;
                }

                // reset the inventory on second page
                if (placement % 53 == 0) { //second page
                    for (int c = 0; c < 9; c++) {
                        inventory.setItem(c, whitePane);
                    }
                    for (int d = 9; d < 54; d++) {
                        inventory.setItem(d, blackPane);
                    }
                    placement = 10;
                }

                // skip two blank spaces in inventory
                if (skipSpaces.contains(placement)) {
                    placement++;
                    placement++;
                }

                // add the item to the current place in inventory and increase the counters
                inventory.setItem(placement, new ItemStack(itemsCollectedByTeam.get(team).get(listcounter)));
                listcounter++;
                placement++;
            }
        }.runTaskTimer(plugin, 0L, 5L); // 0L initial delay, 20L (1 second) between iterations
    }

    public void resetScores() {
        this.sortedScores = null;
        this.currentIndex = 0;
    }

}
