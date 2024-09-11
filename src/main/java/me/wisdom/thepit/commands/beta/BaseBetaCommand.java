package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepitapi.commands.AMultiCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BaseBetaCommand extends AMultiCommand {
    public BaseBetaCommand(String executor) {
        super(executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        if(!Thepit.isDev() && !player.isOp()) return;

        super.execute(sender, command, alias, args);
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        if(!player.isOp()) return null;
        return null;
    }
}
