package me.wisdom.thepit.perks;

import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class JewelHunter extends PitPerk {
    public static JewelHunter INSTANCE;

    public JewelHunter() {
        super("Jewel Hunter", "jewelhunter");
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!hasPerk(attackEvent.getAttacker()) || !attackEvent.isDefenderPlayer() ||
                attackEvent.getAttacker() == attackEvent.getDefender()) return;

        if(MapManager.currentMap.world == attackEvent.getDefenderPlayer().getWorld() &&
                MapManager.currentMap.getMid().distance(attackEvent.getDefenderPlayer().getLocation()) < getRange()) {
            return;
        }

        if(EnchantManager.isJewel(attackEvent.getAttackerPlayer().getEquipment().getItemInHand()) ||
                EnchantManager.isJewel(attackEvent.getAttackerPlayer().getEquipment().getLeggings())) {
            AOutput.error(attackEvent.getAttackerPlayer(), "&3&l宝石猎人!&7 使用宝石时无法使用");
            return;
        }

        double damageIncrease = 0;

        ItemStack heldItem = attackEvent.getDefender().getEquipment().getItemInHand();
        if(EnchantManager.isJewel(heldItem)) damageIncrease += getDamageIncrease();

        ItemStack pantsItem = attackEvent.getDefender().getEquipment().getLeggings();
        if(EnchantManager.isJewel(pantsItem)) damageIncrease += getDamageIncrease();

        attackEvent.increasePercent += damageIncrease;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.GOLD_SWORD)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7在中间区域之外，你对每个&3宝石&7（持有或穿戴的）造成&c+" + getDamageIncrease() + "% &7伤害增加"
        );
    }

    @Override
    public String getSummary() {
        return "&a宝石猎人 &7是一个能让你对持有或穿戴宝石物品的玩家造成更多&c伤害&7的技能";
    }

    public int getDamageIncrease() {
        return 5;
    }

    public static int getRange() {
        return 12;
    }
}
