package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;

import java.util.*;

public class ShowCollectedItemsCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    private static ArrayList<Inventory> teamInventories = new ArrayList<>();;
    Set<Integer> skipSpaces = new HashSet<>();

    public ShowCollectedItemsCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
        skipSpaces.add(0);
        skipSpaces.add(8);
        skipSpaces.add(9);
        skipSpaces.add(17);
        skipSpaces.add(18);
        skipSpaces.add(26);
        skipSpaces.add(27);
        skipSpaces.add(35);
        skipSpaces.add(36);
        skipSpaces.add(44);
        skipSpaces.add(45);
        skipSpaces.add(53);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        UUID playerID = UUID.fromString(args[1]);
        int teamIndex = Integer.parseInt(args[0]);

        Player player = Bukkit.getPlayer(playerID);
        assert player != null;

        Team team = TeamManager.getInstance().getTeam(teamIndex);

        ArrayList<ItemStack> itemsCollectedByTeam = plugin.getItemsCollectedByTeam(team);

        Iterator<ItemStack> it = itemsCollectedByTeam.iterator();

        Bukkit.getLogger().info(it.hasNext() + " ");

        int loopCount = 0;
        while (it.hasNext()) {
            Inventory inventory = createEmptyInventory(team.getTeamName());
            teamInventories.add(inventory);
            for (int i = 10; i < 53; i++) {
                if (i == 27 && loopCount != 0) {
                    inventory.setItem(i, new ItemStack(Material.STONE_BUTTON));
                    continue;
                }
                if (isSkipSpace(i)) {
                    continue;
                }
                if (it.hasNext()) {
                    inventory.setItem(i, it.next());
                }
            }
            if(it.hasNext()){
                inventory.setItem(35, new ItemStack(Material.OAK_BUTTON));
            }
            loopCount++;
        }

        player.openInventory(teamInventories.getFirst());

        return true;
    }

    private boolean isSkipSpace(int i) {
        return skipSpaces.contains(i);
    }

    private Inventory createEmptyInventory(String teamName) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Items collected " + teamName);
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
        return inventory;
    }

    public static void openInventory(Player player, int index){
        if(teamInventories.get(index) != null) {
            player.openInventory(teamInventories.get(index));
        }
    }

}
