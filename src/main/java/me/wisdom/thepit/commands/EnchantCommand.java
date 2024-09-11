package me.wisdom.thepit.commands;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enchants.overworld.SelfCheckout;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.exceptions.*;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!Thepit.isDev()) {
            if (!player.hasPermission("group.nitro")) {
                AOutput.send(player, "&c&l错误！&7 你必须提升我们的Discord服务器才能使用此功能！&7 加入: &f&ndiscord.pitsim.net");
                return false;
            }

            if (MysticType.getMysticType(player.getItemInHand()) == MysticType.TAINTED_CHESTPLATE || MysticType.getMysticType(player.getItemInHand()) == MysticType.TAINTED_SCYTHE) {
                if (!player.isOp()) {
                    AOutput.error(player, "&c你很聪明。");
                    return false;
                }
            }
        }

        PitItem pitItem = ItemFactory.getItem(player.getItemInHand());
        if (pitItem == null || !pitItem.isMystic) {
            AOutput.error(player, "&c&l错误！&7 你没有持有神秘物品");
            return false;
        }

        if (MysticFactory.isGemmed(player.getItemInHand())) {
            AOutput.error(player, "&c&l错误！&7 你不能修改镶嵌宝石的物品");
            return false;
        }

        if (args.length < 2) {
            AOutput.error(player, "用法: /enchant <名字> <等级>");
            return false;
        }

        String refName = args[0].toLowerCase();
        PitEnchant pitEnchant = EnchantManager.getEnchant(refName);
        if (pitEnchant == null) {
            AOutput.error(player, "&c&l错误！&7 那个附魔不存在");
            return false;
        }

        if (pitEnchant == SelfCheckout.INSTANCE && !player.isOp()) {
            AOutput.error(player, "&c你很聪明。");
            return false;
        }

        if (!EnchantManager.canTypeApply(player.getItemInHand(), pitEnchant) && !player.isOp()) {
            AOutput.error(player, "&c你很聪明。");
            return false;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (Exception ignored) {
            AOutput.error(player, "用法: /enchant <名字> <等级>");
            return false;
        }

        ItemStack updatedItem;
        try {
            if (player.isOp() || Thepit.isDev()) {
                updatedItem = EnchantManager.addEnchant(player.getItemInHand(), pitEnchant, level, false);
            } else {
                updatedItem = EnchantManager.addEnchant(player.getItemInHand(), pitEnchant, level, true);
            }
        } catch (Exception exception) {
            if (exception instanceof MismatchedEnchantException) {
                AOutput.error(player, "&c&l错误！&7 该附魔不能在该物品上使用");
            } else if (exception instanceof InvalidEnchantLevelException) {
                if (!((InvalidEnchantLevelException) exception).levelTooHigh) {
                    AOutput.error(player, "&c&l错误！&7 等级太低");
                } else {
                    AOutput.error(player, "&c&l错误！&7 等级太高");
                }
            } else if (exception instanceof MaxTokensExceededException) {
                if (((MaxTokensExceededException) exception).isRare) {
                    AOutput.error(player, "&c&l错误！&7 物品上的稀有标记不能超过4个");
                } else {
                    AOutput.error(player, "&c&l错误！&7 物品上的标记不能超过8个");
                }
            } else if (exception instanceof MaxEnchantsExceededException) {
                AOutput.error(player, "&c&l错误！&7 物品上的附魔不能超过3个");
            } else if (exception instanceof IsJewelException) {
                AOutput.error(player, "&c&l错误！&7 你不能修改珠宝附魔");
            } else if (exception instanceof NoCommonEnchantException) {
                AOutput.error(player, "&c&l错误！&7 物品上必须至少有一个普通附魔");
            } else {
                exception.printStackTrace();
            }
            return false;
        }

        player.setItemInHand(updatedItem);
        if (level == 0) {
            AOutput.send(player, "&a&l成功！&7 移除了附魔: " + pitEnchant.getDisplayName() +
                    EnchantManager.enchantLevelToRoman(level));
        } else {
            AOutput.send(player, "&a&l成功！&7 添加了附魔: " + pitEnchant.getDisplayName() +
                    EnchantManager.enchantLevelToRoman(level));
        }
        return false;
    }
}
