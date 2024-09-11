package me.wisdom.thepit.commands.admin;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.DarkzoneLeveling;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.help.HelpManager;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepit.serverstatistics.StatisticDataChunk;
import me.wisdom.thepit.serverstatistics.StatisticsManager;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class KyroCommand extends ACommand {
    public KyroCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(!Misc.isKyro(player.getUniqueId())) {
            AOutput.error(player, "&c&l错误!&7 你必须是 &9Kyro &7才能执行此操作");
            return;
        }

        if(args.isEmpty()) {
            sendHelpMessage(player);
            return;
        }

        String subCommand = args.get(0).toLowerCase();
        if(subCommand.equals("sync")) {
            AOutput.send(player, "&9&lAI!&7 正在更新 Dialogflow 模型");
            new Thread(HelpManager::updateIntentsAndPages).start();
        } else if(subCommand.equals("clear")) {
            AOutput.send(player, "&9&lAI!&7 正在清除保存的 Dialogflow 意图请求");
            HelpManager.clearStoredData();
        } else if(subCommand.equals("stats")) {
            StatisticDataChunk dataChunk = StatisticsManager.getDataChunk();
            dataChunk.send();
            StatisticsManager.resetDataChunk();
            AOutput.send(player, "&9&l开发者!&7 正在将统计数据发送到代理");
        } else if(subCommand.equals("altarxp")) {
            pitPlayer.darkzoneData.altarXP = 0;
            new BukkitRunnable() {
                @Override
                public void run() {
                    DarkzoneLeveling.giveXP(pitPlayer, 100);
                }
            }.runTaskTimer(Thepit.INSTANCE, 0L, 10L);
        } else if(subCommand.equals("lockitem")) {
            ItemStack itemStack = player.getItemInHand();
            NBTItem nbtItem = new NBTItem(itemStack, true);
            nbtItem.setBoolean(NBTTag.IS_LOCKED.getRef(), true);
            player.setItemInHand(itemStack);
            player.updateInventory();
            AOutput.send(player, "&c&l锁定!&7 该物品已被锁定!");
            Sounds.SUCCESS.play(player);
        } else {
            sendHelpMessage(player);
        }
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }

    public void sendHelpMessage(Player player) {
        AOutput.error(player, "&c&l错误!&7 使用方法: <sync|clear|stats|altarxp|lockitem>");
    }
}
