package me.wisdom.thepit.tutorial.checkpoints;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.diamond.DiamondLeggings;
import me.wisdom.thepit.tutorial.Midpoint;
import me.wisdom.thepit.tutorial.NPCCheckpoint;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarketShopCheckpoint extends NPCCheckpoint {
    public MarketShopCheckpoint() {
        super(TutorialObjective.MARKET_SHOP, new Location(MapManager.getDarkzone(),
                205.5, 91, -86.5, 13, 0), Midpoint.SPAWN1);
    }

    @Override
    public void onCheckpointEngage(Tutorial tutorial) {
        tutorial.sendMessage("&e这里是 &b&l污浊商店 &e和 &3&l玩家市场&e！", 0);
        tutorial.sendMessage("&e这些是购买 &5暗区物品 &e的主要地点，使用 &f灵魂&e。", 60);
        tutorial.sendMessage("&e你可以在 &3&l玩家市场&e 与其他 &a玩家 &e买卖物品。", 120);
        tutorial.sendMessage("&e而在 &b&l污浊商店 &e你可以购买特定的物品！", 180);
        tutorial.sendMessage("&e去 &6购买 &e&b钻石护腿 &e从 &b&l污浊商店&e。", 240);
        tutorial.sendMessage("&e完成后再来找我。", 300);
    }

    @Override
    public void onCheckpointSatisfy(Tutorial tutorial) {
        tutorial.delayTask(() -> removeTutorialTag(tutorial.getPlayer()), getSatisfyDelay());
        tutorial.sendMessage("&e做得好！", 0);
        tutorial.sendMessage("&e现在用那些护腿保护自己，因为你的 &d神秘裤子 &e在这里不会有效！", 60);
        tutorial.sendMessage("&e别忘了回来看看 &3&l玩家市场 &e，一旦你有更多的 &f灵魂&e。", 120);
    }

    @Override
    public int getEngageDelay() {
        return 300;
    }

    @Override
    public int getSatisfyDelay() {
        return 120;
    }

    @Override
    public boolean canEngage(Tutorial tutorial) {
        return true;
    }

    @Override
    public boolean canSatisfy(Tutorial tutorial) {
        Player player = tutorial.getPlayer();
        List<ItemStack> items = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        items.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        for(ItemStack item : items) {
            PitItem pitItem = ItemFactory.getItem(item);
            if(!(pitItem instanceof DiamondLeggings)) continue;
            if(ItemFactory.isTutorialItem(item)) return true;
        }

        return false;
    }

    public void removeTutorialTag(Player player) {
        for(int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack item = player.getInventory().getContents()[i];
            PitItem pitItem = ItemFactory.getItem(item);
            if(!(pitItem instanceof DiamondLeggings)) continue;
            if(ItemFactory.isTutorialItem(item)) {
                ItemFactory.setTutorialItem(item, false);
                player.getInventory().setItem(i, item);
                return;
            }
        }

        ItemStack[] armor = player.getInventory().getArmorContents();
        for(int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            PitItem pitItem = ItemFactory.getItem(item);
            if(!(pitItem instanceof DiamondLeggings)) continue;
            if(ItemFactory.isTutorialItem(item)) {
                ItemFactory.setTutorialItem(item, false);
                armor[i] = item;
                player.getInventory().setArmorContents(armor);
                return;
            }
        }
    }

    @Override
    public void onCheckPointDisengage(Tutorial tutorial) {

    }


}
