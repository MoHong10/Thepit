package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.progression.DarkzoneData;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ResetCommand extends ACommand {
    public ResetCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.darkzoneData = new DarkzoneData();
        Sounds.SUCCESS.play(player);
        AOutput.send(player, "&a&l成功!&7 已重置你的黑暗区域进度数据!");
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
