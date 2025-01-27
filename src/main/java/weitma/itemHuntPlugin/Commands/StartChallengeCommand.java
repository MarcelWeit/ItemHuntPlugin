package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Listeners.HungerListener;

public class StartChallengeCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    private int taskID;

    public StartChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length != 4){
            sender.sendMessage(ChatColor.RED + "Usage: /startchallenge <seconds> <skipItems> <elytra(yes/no)> <hunger(yes/no)>");
            return false;
        }

        try{
            Integer.parseInt(args[0]);
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "Usage: /startchallenge <seconds> <skipItems>");
            return false;
        }

        int seconds = Integer.parseInt(args[0]);
        String skipItems = args[1];
        boolean withElytra = args[2].equals("yes");
        boolean withHunger = args[3].equals("yes");

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
            player.sendMessage(ChatColor.GREEN + "The challenge has started! You have "+ seconds/60 + " minutes to collect items!");
            player.sendMessage(ChatColor.GREEN + "Everyone got " + skipItems + " skip items! Use them wisely!");
            plugin.showItemToCollect(player.getUniqueId());
            player.getInventory().addItem(plugin.getSkipItem(Integer.parseInt(skipItems)));
            player.getInventory().setItem(8, plugin.createBackpack(player.getUniqueId()));
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
        });

        plugin.startTimer(seconds);

        return true;
    }

}
