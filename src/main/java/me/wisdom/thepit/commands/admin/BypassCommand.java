package me.wisdom.thepit.commands.admin;

import me.wisdom.thepit.controllers.PlayerManager;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BypassCommand extends ACommand {
    public BypassCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!sender.isOp()) return;
        Player player = (Player) sender;
        if(PlayerManager.toggledPlayers.contains(player)) PlayerManager.toggledPlayers.remove(player);
        else PlayerManager.toggledPlayers.add(player);
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
