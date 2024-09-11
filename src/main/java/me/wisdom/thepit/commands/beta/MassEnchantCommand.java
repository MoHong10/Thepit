package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.inventories.MassEnchantGUI;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MassEnchantCommand extends ACommand {
    public MassEnchantCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(args.size() < 1) {
            AOutput.error(player, "&c用法: /massenchant <mystic-type>");
            return;
        }

        String mysticType = args.get(0);
        ItemStack mystic = MysticFactory.getFreshItem(player, mysticType);
        if(mystic == null) {
            AOutput.error(player, "&c无效的神秘类型!");
            return;
        }

        MassEnchantGUI gui = new MassEnchantGUI(player, mysticType);
        gui.open();
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
