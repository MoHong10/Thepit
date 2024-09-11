package me.wisdom.thepit.tutorial.checkpoints;

import de.tr7zw.nbtapi.NBTItem;
import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.TaintedWell;
import me.wisdom.thepit.enums.MysticType;
import me.wisdom.thepit.enums.NBTTag;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.mystics.TaintedChestplate;
import me.wisdom.thepit.items.mystics.TaintedScythe;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.tutorial.Midpoint;
import me.wisdom.thepit.tutorial.NPCCheckpoint;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import me.wisdom.thepitapi.misc.AUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaintedWellCheckpoint extends NPCCheckpoint {
    public TaintedWellCheckpoint() {
        super(TutorialObjective.TAINTED_WELL, new Location(MapManager.getDarkzone(),
                188.5, 92, -101.5, 13, 0), Midpoint.SPAWN1);
    }

    @Override
    public void onCheckpointEngage(Tutorial tutorial) {
        tutorial.delayTask(() -> giveFreshItems(tutorial), 120);

        tutorial.sendMessage("&e这里是 &5&lTainted Well&e。", 0);
        tutorial.sendMessage("&e在这里，你可以为你在暗区找到的各种 &a新鲜物品 &e进行附魔。", 60);
        tutorial.sendMessage("&e试试吧？", 120);
        tutorial.sendMessage("&d为你提供的 &5镰刀 &e和 &5胸甲 &e进行附魔至 &f等级 II&e。", 180);
        tutorial.sendMessage("&e你可以通过右键点击 &5&lTainted Well &e和右键点击 &a附魔 &e按钮 来完成。", 240);
        tutorial.sendMessage("&e确保每件物品都附魔两次，以达到 &f等级 II&e。", 300);
        tutorial.sendMessage("&e接下来，使用 &c移除物品 &e按钮 来取回你的物品。", 360);
        tutorial.sendMessage("&e一旦你完成了 &a两个物品&e，回来找我，我会给你下一个任务。", 420);
    }

    @Override
    public void onCheckpointSatisfy(Tutorial tutorial) {
        tutorial.delayTask(() -> removeTutorialTag(tutorial.getPlayer()), getSatisfyDelay());
        tutorial.sendMessage("&e做得好！", 0);
        tutorial.sendMessage("&e你可以保留这些物品，因为你在 &5暗区 &e冒险中会需要它们。", 60);
    }

    @Override
    public int getEngageDelay() {
        return 420;
    }

    @Override
    public int getSatisfyDelay() {
        return 60;
    }

    @Override
    public boolean canEngage(Tutorial tutorial) {
        return Misc.getEmptyInventorySlots(tutorial.getPlayer()) >= 2;
    }

    @Override
    public boolean canSatisfy(Tutorial tutorial) {
        Player player = tutorial.getPlayer();

        boolean hasChestplate = false;
        boolean hasScythe = false;

        List<ItemStack> items = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        items.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        for(ItemStack itemStack : items) {
            if(!ItemFactory.isTutorialItem(itemStack)) continue;
            PitItem pitItem = ItemFactory.getItem(itemStack);
            if(pitItem == null) continue;
            NBTItem nbtItem = new NBTItem(itemStack);

            if(pitItem instanceof TaintedChestplate) {
                if(nbtItem.getInteger(NBTTag.TAINTED_TIER.getRef()) >= 2) hasChestplate = true;
            } else if(pitItem instanceof TaintedScythe) {
                if(nbtItem.getInteger(NBTTag.TAINTED_TIER.getRef()) >= 2) hasScythe = true;
            }
        }

        return hasChestplate && hasScythe;
    }

    @Override
    public void onCheckPointDisengage(Tutorial tutorial) {
        TaintedWell.tutorialReset(tutorial.getPlayer());
    }

    public void removeTutorialTag(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        PitItem pitItem = ItemFactory.getItem(chestplate);
        if(pitItem != null) {
            ItemFactory.setTutorialItem(chestplate, false);
            player.getInventory().setChestplate(chestplate);
        }

        for(int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack itemStack = player.getInventory().getContents()[i];
            PitItem item = ItemFactory.getItem(itemStack);
            if(!(item instanceof TaintedChestplate) && !(item instanceof TaintedScythe)) continue;
            ItemFactory.setTutorialItem(itemStack, false);
            player.getInventory().setItem(i, itemStack);
        }

        player.updateInventory();
    }

    public void giveFreshItems(Tutorial tutorial) {
        Player player = tutorial.getPlayer();
        if(Misc.getEmptyInventorySlots(player) < 2) {
            tutorial.sendMessage("&c你没有足够的背包空间来继续教程！请在再次与我交谈之前腾出更多空间。", 5);
            return;
        }

        ItemStack scythe = MysticFactory.getFreshItem(MysticType.TAINTED_SCYTHE, null);
        ItemFactory.setTutorialItem(scythe, true);
        EnchantManager.setItemLore(scythe, player);
        AUtil.giveItemSafely(player, scythe);

        ItemStack chestplate = MysticFactory.getFreshItem(MysticType.TAINTED_CHESTPLATE, null);
        ItemFactory.setTutorialItem(chestplate, true);
        EnchantManager.setItemLore(chestplate, player);
        AUtil.giveItemSafely(player, chestplate);
    }
}
