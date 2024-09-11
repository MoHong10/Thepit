package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FreshCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!Thepit.isDev()) {
            if (!player.hasPermission("group.nitro")) {
                AOutput.send(player, "&c您必须提升我们的 Discord 服务器才能访问此功能！&7 加入链接：&f&ndiscord.pitsim.net");
                return false;
            }
        }

        if (args.length < 1) {
            AOutput.error(player, "用法：/fresh <sword|bow|fresh>");
            return false;
        }

        String type = args[0].toLowerCase();
        ItemStack mystic = MysticFactory.getFreshItem(player, type);
        if (mystic == null) {
            AOutput.error(player, "用法：/fresh <sword|bow|fresh>");
            return false;
        }

        if (!Thepit.isDev() && !player.isOp()) {
            if (MysticType.getMysticType(mystic) == MysticType.TAINTED_CHESTPLATE || MysticType.getMysticType(mystic) == MysticType.TAINTED_SCYTHE) {
                AOutput.error(player, "&c试试别的办法。");
                return false;
            }
        }

        AUtil.giveItemSafely(player, mystic);
        return false;
    }
}
