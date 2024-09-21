package me.wisdom.thepit.commands.admin;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.enums.PantColor;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.TemporaryItem;
import me.wisdom.thepit.misc.Constant;
import me.wisdom.thepitapi.commands.ACommand;
import me.wisdom.thepitapi.commands.AMultiCommand;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JewelCommand extends ACommand {
    public JewelCommand(AMultiCommand base, String executor) {
        super(base, executor);
    }

    @Override
    public void execute(CommandSender sender, Command command, String alias, List<String> args) {
        if(!(sender instanceof Player)) return;
        if(args.size() < 1) {
            AOutput.error(sender, "Usage: /jewel <sword|bow|pants> [enchant] [max-lives]");
            return;
        }

        MysticType mysticType = null;
        String type = args.get(0).toLowerCase();
        switch(type) {
            case "sword":
                mysticType = MysticType.SWORD;
                break;
            case "bow":
                mysticType = MysticType.BOW;
                break;
            case "pants":
            case "pant":
            case "fresh":
                mysticType = MysticType.PANTS;
                break;
        }
        if(mysticType == null) {

            AOutput.error(sender, "Usage: /fresh <sword|bow|pants>");
            return;
        }

        AUtil.giveItemSafely((Player) sender, getJewel(mysticType, args.get(1), Integer.parseInt(args.get(2))));
    }

    public static ItemStack getJewel(MysticType mysticType, String pitEnchant, int maxLives) {
        ItemStack jewel = MysticFactory.getJewelItem(mysticType);
        NBTItem nbtItem = new NBTItem(jewel);

        PitItem pitItem = ItemFactory.getItem(jewel);
        assert pitItem != null;
        TemporaryItem temporaryItem = pitItem.getAsTemporaryItem();

        PitEnchant jewelEnchant = EnchantManager.getEnchant(pitEnchant);
        if(jewelEnchant != null) {
            nbtItem.setInteger(NBTTag.ITEM_JEWEL_KILLS.getRef(), Constant.JEWEL_KILLS);

            if(maxLives <= 0) maxLives = EnchantManager.getRandomMaxLives();

            nbtItem = new NBTItem(PantColor.setPantColor(nbtItem.getItem(), PantColor.getNormalRandom()));
            try {
                jewel = EnchantManager.addEnchant(nbtItem.getItem(), jewelEnchant, 3, false, true, -1);
            } catch(Exception ignored) {}
            temporaryItem.addMaxLives(jewel, maxLives);
        }

        pitItem.updateItem(jewel);
        return jewel;
    }

    @Override
    public List<String> getTabComplete(Player player, String current, List<String> args) {
        return null;
    }
}
