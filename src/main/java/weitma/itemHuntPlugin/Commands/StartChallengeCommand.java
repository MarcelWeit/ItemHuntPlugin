package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Text;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Listeners.HungerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StartChallengeCommand implements CommandExecutor, TabCompleter {

    private final ItemHuntPlugin plugin;

    private int taskID;

    public StartChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!plugin.isEveryoneInATeam()){
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendMessage(ChatColor.RED + "Not everyone is in a team!")
            );
            return false;
        }

        if(args.length != 5){
            sender.sendMessage(ChatColor.RED + "Usage: /startchallenge <seconds> <skipItems> <elytra(yes/no)> <hunger(yes/no)> <updraftitems>");
            return false;
        }

        try{
            Integer.parseInt(args[0]);
            Integer.parseInt(args[1]);
            Integer.parseInt(args[4]);
        } catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "Usage: /startchallenge <seconds> <skipItems>");
            return false;
        }

        int seconds = Integer.parseInt(args[0]);
        String skipItems = args[1];
        boolean withElytra = args[2].equals("yes");
        boolean withHunger = args[3].equals("yes");
        int updraftItems = Integer.parseInt(args[4]);

        if(plugin.isChallengeStarted()){
            sender.sendMessage(ChatColor.RED + "The challenge has already started!");
            return false;
        }
        plugin.resetChallenge();
        plugin.setChallengeStarted(true);

        if(!withHunger) {
            plugin.getServer().getPluginManager().registerEvents(new HungerListener(), plugin);
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();

            plugin.generateNewRandomMaterialToCollect(player.getUniqueId());

            player.sendMessage(ChatColor.GREEN + "The challenge has started!");

            plugin.showItemToCollect(player.getUniqueId());

            player.getInventory().addItem(plugin.getSkipItem(Integer.parseInt(skipItems)));
            player.getInventory().setItem(8, plugin.createBackpack(player.getUniqueId()));
            player.getInventory().addItem(plugin.getUpdraftItem(updraftItems));

            if(withElytra) {
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta elytraMeta = elytra.getItemMeta();
                elytraMeta.setUnbreakable(true);
                elytra.setItemMeta(elytraMeta);
                player.getInventory().setChestplate(elytra);
            }

            if(!withHunger){
                player.setFoodLevel(20);
            }

            sendTeamsMessage(player);
        });

        plugin.startTimer(seconds);

        return true;
    }

    private void sendTeamsMessage(Player player) {
        for(int i=0; i<9; i++){
            ArrayList<UUID> playersInTeam = plugin.getPlayersInTeam(i);
            if(playersInTeam.isEmpty()) continue;
            StringBuilder message = new StringBuilder();
            message.append(ItemHuntPlugin.teamNames[i-1]);
            message.append(": ");
            for (int j = 0; j < playersInTeam.size(); j++) {
                UUID playerId = playersInTeam.get(j);
                message.append(Bukkit.getPlayer(playerId).getName());
                if (j < playersInTeam.size() - 1) {
                    message.append(", ");
                }
            }
            player.sendMessage(message.toString());
        }
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
