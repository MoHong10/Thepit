package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RenownCommand extends ACommand {
    public RenownCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(args.size() < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /beta renown <amount>");
            return;
        }

        int renown;
        try {
            renown = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            AOutput.error(player, "&c&l错误!&7 无效的数字!");
            return;
        }

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.renown += renown;
        Sounds.SUCCESS.play(player);
        AOutput.send(player, "&a&l成功!&7 已添加 &e" + renown + " 点声望 &7到你的账户!");
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}

