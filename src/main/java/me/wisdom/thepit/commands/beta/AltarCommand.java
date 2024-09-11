package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.DarkzoneLeveling;
import me.wisdom.thepit.darkzone.altar.AltarManager;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AltarCommand extends ACommand {
    public AltarCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if(args.size() < 1) {
            AOutput.error(player, "&c&l错误!&7 用法: /beta altar <等级>");
            return;
        }

        int level;
        try {
            level = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            AOutput.error(player, "&c&l错误!&7 无效的数字!");
            return;
        }

        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        pitPlayer.darkzoneData.altarXP = DarkzoneLeveling.getXPToLevel(level);
        if(Thepit.status.isDarkzone()) AltarManager.hologram.updateHologram(player);
        Sounds.SUCCESS.play(player);
        AOutput.send(player, "&a&l成功!&7 将你的 &4祭坛 &7设置为 &c等级 " + level + "&7!");
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
