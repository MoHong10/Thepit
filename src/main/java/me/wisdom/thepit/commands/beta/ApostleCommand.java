package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.megastreaks.Apostle;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ApostleCommand extends ACommand {
    public ApostleCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        if(args.size() < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /beta apostle <奖励>");
            return;
        }

        int bonus;
        try {
            bonus = Integer.parseInt(args.get(0));
            if(bonus < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            AOutput.error(player, "&c&l错误!&7 无效的数字!");
            return;
        }

        if(bonus > Apostle.getMaxMaxXPIncrease()) {
            AOutput.error(player, "&c&l错误!&7 最大 " + Apostle.INSTANCE.getCapsDisplayName() + "&7 奖励是 &b+" +
                    Apostle.getMaxMaxXPIncrease() + " 最大经验&7!");
            return;
        }

        pitPlayer.apostleBonus = bonus;
        Sounds.SUCCESS.play(player);
        AOutput.send(player, "&a&l成功!&7 将你的 " + Apostle.INSTANCE.getCapsDisplayName() + "&7 奖励设置为 &b+" + bonus + " 最大经验&7!");
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}