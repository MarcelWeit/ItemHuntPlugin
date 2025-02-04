package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
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

        Player player = (Player) sender;

        String usage = "Usage: /startchallenge <seconds> <skipItems> <elytra(yes/no)> <hunger(yes/no)> <updraftitems> <backpacksize(n*9<=54)";

        if (args.length != 6) {
            sender.sendMessage(ChatColor.RED + usage);
            return false;
        }

        try {
            Integer.parseInt(args[0]);
            Integer.parseInt(args[1]);
            Integer.parseInt(args[4]);
            Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + usage);
            return false;
        }

        int seconds = Integer.parseInt(args[0]);
        int skipItems = Integer.parseInt(args[1]);
        boolean withElytra = args[2].equals("yes");
        boolean withHunger = args[3].equals("yes");
        int updraftItems = Integer.parseInt(args[4]);
        int backpackSize = Integer.parseInt(args[5]);

        if(skipItems < 0 || updraftItems < 0 || backpackSize < 0){
            player.sendMessage("Online positive integers are allowed for skipitems, updraftitems and backpacksize");
        }

        if((!args[2].equals("yes") && !args[2].equals("no")) || (!args[3].equals("yes") && !args[3].equals("no"))){
            sender.sendMessage(ChatColor.RED + usage);
        }

        if(backpackSize % 9 != 0 || backpackSize > 54){
            player.sendMessage(ChatColor.RED + "Backpack-Size must be a multiple of 9 up to 54");
            return false;
        }

        if(updraftItems > 0){
            plugin.setWithUpdraftItem(true);
        }

        if (plugin.isChallengeFinished()) {
            sender.sendMessage(ChatColor.RED + "The challenge has finished already!");
            return false;
        }

        if (plugin.isChallengeStarted()) {
            sender.sendMessage(ChatColor.RED + "The challenge has already started!");
            return false;
        }
        plugin.setChallengeStarted(true);

        if (!withHunger) {
            plugin.getServer().getPluginManager().registerEvents(new HungerListener(), plugin);
        }

        Bukkit.getLogger().info("Generating new random materials for all teams");

        for (Team teamWithPlayer : TeamManager.getInstance().getTeamsWithPlayers()) {
            Bukkit.getLogger().info("Generating new random material for team " + teamWithPlayer.getTeamName());
            plugin.generateNewRandomMaterialToCollect(teamWithPlayer);
        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            onlinePlayer.getInventory().clear();

            onlinePlayer.sendMessage(ChatColor.GREEN + "The challenge has started!");

            plugin.showItemToCollect(onlinePlayer.getUniqueId());

            onlinePlayer.getInventory().addItem(plugin.getSkipItem(skipItems));
            if(backpackSize != 0) {
                ItemStack backpack = plugin.createBackpack(TeamManager.getInstance().getTeamOfPlayer(onlinePlayer.getUniqueId()), backpackSize);
                onlinePlayer.getInventory().setItem(8, backpack);
            }
            onlinePlayer.getInventory().addItem(plugin.getUpdraftItem(updraftItems));

            if (withElytra) {
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta elytraMeta = elytra.getItemMeta();
                assert elytraMeta != null;
                elytraMeta.setUnbreakable(true);
                elytra.setItemMeta(elytraMeta);
                onlinePlayer.getInventory().setChestplate(elytra);
            }

            if (!withHunger) {
                onlinePlayer.setFoodLevel(20);
            }
            onlinePlayer.setHealth(20);

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
        } else if (length == 5) {
        return List.of("1", "2", "3", "4", "5", "6", "7");
        }
        return null;
    }
}
