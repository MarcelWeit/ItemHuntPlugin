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

    private static final HashMap<Team, ArrayList<Inventory>> teamInventories = new HashMap<>();
    Set<Integer> skipCellsInInv = new HashSet<>();
    private static final HashMap<Player, Integer> currentOpenInv = new HashMap<>();

    public ShowCollectedItemsCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
        skipCellsInInv.add(0);
        skipCellsInInv.add(8);
        skipCellsInInv.add(9);
        skipCellsInInv.add(17);
        skipCellsInInv.add(18);
        skipCellsInInv.add(26);
        skipCellsInInv.add(27);
        skipCellsInInv.add(35);
        skipCellsInInv.add(36);
        skipCellsInInv.add(44);
        skipCellsInInv.add(45);
        skipCellsInInv.add(53);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int teamIndex = Integer.parseInt(args[0]);
        UUID playerID = UUID.fromString(args[1]);
        int index = Integer.parseInt(args[2]);

        Player player = Bukkit.getPlayer(playerID);
        assert player != null;

        Team team = TeamManager.getInstance().getTeam(teamIndex);
        if(!teamInventories.containsKey(team)) {
            teamInventories.put(team, new ArrayList<>());
        }

        ArrayList<ItemStack> itemsCollectedByTeam = plugin.getItemsCollectedByTeam(team);

        Iterator<ItemStack> it = itemsCollectedByTeam.iterator();

        int loopCount = 0;
        while (it.hasNext()) {
            Inventory inventory = createEmptyInventory(team.getTeamName());
            teamInventories.get(team).add(inventory);
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

        player.openInventory(teamInventories.get(team).get(index));

        currentOpenInv.put(player, index);

        return true;
    }

    private boolean isSkipSpace(int i) {
        return skipCellsInInv.contains(i);
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

    public void clearTeamInventories(){
        teamInventories.clear();
    }

    public int getInvIndexOfPlayer(Player player) {
        return currentOpenInv.get(player);
    }

}
