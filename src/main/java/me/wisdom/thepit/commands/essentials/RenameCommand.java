package me.wisdom.thepit.commands.essentials;

import me.wisdom.thepit.misc.Lang;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if(!player.hasPermission("pitsim.rename")) {
            Lang.NO_PERMISSION.send(player);
            return false;
        }

        if (args.length < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /" + label + " <名字>");
            return false;
        }

        ItemStack itemStack = player.getItemInHand();
        if (Misc.isAirOrNull(itemStack)) {
            AOutput.error(player, "&c&l错误!&7 你没有持有任何物品");
            return false;
        }

        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));
        itemStack.setItemMeta(itemMeta);
        player.setItemInHand(itemStack);
        player.updateInventory();
        return false;
    }
}
