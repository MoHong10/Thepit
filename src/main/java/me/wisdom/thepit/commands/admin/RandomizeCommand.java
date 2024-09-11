package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RandomizeCommand extends ACommand {
    public RandomizeCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(Misc.isAirOrNull(player.getItemInHand())) {
            AOutput.error(player, "&c无效的物品!");
            return;
        }

        PitItem pitItem = ItemFactory.getItem(player.getItemInHand());
        if(pitItem == null || !pitItem.hasUUID) {
            AOutput.error(player, "&c错误!&7 该物品不应该有 UUID");
            return;
        }

        ItemStack heldStack = player.getItemInHand();
        heldStack = pitItem.randomizeUUID(heldStack);
        player.getInventory().setItemInHand(heldStack);
        player.updateInventory();
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
