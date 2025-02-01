package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Listeners.HungerListener;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;

import java.util.List;

public class StartChallengeCommand implements CommandExecutor, TabCompleter {

    private final ItemHuntPlugin plugin;

    public StartChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!TeamManager.getInstance().isEveryoneInATeam()) {
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendMessage(ChatColor.RED + "Not everyone is in a team!")
            );
            return false;
        }

        if (args.length != 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /startchallenge <seconds> <skipItems> <elytra(yes/no)> <hunger(yes/no)> <updraftitems>");
            return false;
        }

        try {
            Integer.parseInt(args[0]);
            Integer.parseInt(args[1]);
            Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Usage: /startchallenge <seconds> <skipItems>");
            return false;
        }

        int seconds = Integer.parseInt(args[0]);
        String skipItems = args[1];
        boolean withElytra = args[2].equals("yes");
        boolean withHunger = args[3].equals("yes");
        int updraftItems = Integer.parseInt(args[4]);

        if(updraftItems > 0){
            plugin.setWithUpdraftItem(true);
        }

        if (plugin.isChallengeStarted()) {
            sender.sendMessage(ChatColor.RED + "The challenge has already started!");
            return false;
        }
        plugin.resetChallenge();
        plugin.setChallengeStarted(true);

        if (!withHunger) {
            plugin.getServer().getPluginManager().registerEvents(new HungerListener(), plugin);
        }

        Bukkit.getLogger().info("Generating new random materials for all teams");

        for (Team teamWithPlayer : TeamManager.getInstance().getTeamsWithPlayers()) {
            Bukkit.getLogger().info("Generating new random material for team " + teamWithPlayer.getTeamName());
            plugin.generateNewRandomMaterialToCollect(teamWithPlayer);
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();

            player.sendMessage(ChatColor.GREEN + "The challenge has started!");

            plugin.showItemToCollect(player.getUniqueId());

            player.getInventory().addItem(plugin.getSkipItem(Integer.parseInt(skipItems)));
            player.getInventory().setItem(8, plugin.createBackpack(TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId())));
            player.getInventory().addItem(plugin.getUpdraftItem(updraftItems));

            if (withElytra) {
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta elytraMeta = elytra.getItemMeta();
                assert elytraMeta != null;
                elytraMeta.setUnbreakable(true);
                elytra.setItemMeta(elytraMeta);
                player.getInventory().setChestplate(elytra);
            }

            if (!withHunger) {
                player.setFoodLevel(20);
            }
            player.setHealth(20);

        });

        plugin.startTimer(seconds);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        int length = args.length;

        if (length == 1) {
            return List.of("7200", "120");
        } else if (length == 2) {
            return List.of("1", "2", "3", "4", "5", "6", "7");
        } else if (length == 3) {
            return List.of("yes", "no");
        } else if (length == 4) {
            return List.of("yes", "no");
        }
        return null;
    }
}
