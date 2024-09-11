package me.wisdom.thepit.commands.beta;

import me.wisdom.thepit.storage.StorageManager;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class FreezeCommand extends ACommand {


    public FreezeCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        boolean remove = false;
        boolean effectingOther = false;
        for (String arg : args) {
            if (!arg.contains("-")) effectingOther = true;
            if (arg.equalsIgnoreCase("-r")) remove = true;
        }

        Player target = player;

        if (effectingOther) {
            String playerName = args.get(0);
            target = Bukkit.getPlayer(playerName);
            if (target == null) {
                AOutput.error(player, "&c玩家未找到!");
            }
        }

        if (target == null) return;

        if (remove) {
            AOutput.send(player, "&a&l成功!&7 已从 &e" + target.getName() + "&7 移除数据冻结!");
            StorageManager.frozenPlayers.remove(target.getUniqueId());
        } else if (!StorageManager.frozenPlayers.contains(target.getUniqueId())) {
            AOutput.send(player, "&a&l成功!&7 已为 &e" + target.getName() + "&7 冻结数据!");
            StorageManager.frozenPlayers.add(target.getUniqueId());
        } else {
            AOutput.error(player, "&c数据已经被冻结于 &e" + target.getName() + "&7!");
        }
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
