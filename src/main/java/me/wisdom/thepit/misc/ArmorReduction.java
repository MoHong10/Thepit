package me.wisdom.thepit.misc;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.items.PitItem;
import me.wisdom.thepit.items.mystics.MysticPants;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class ArmorReduction {

    public static double getMissingReductionMultiplier(LivingEntity entity) {
        int actualPoints = getArmorPoints(entity);
        int missingPoints = 0;

        ItemStack leggings = entity.getEquipment().getLeggings();
        if(!Misc.isAirOrNull(leggings) && leggings.getType() == Material.LEATHER_LEGGINGS) {
            PitItem pitItem = ItemFactory.getItem(leggings);
            if(pitItem instanceof MysticPants) missingPoints += 3;
        }

        ItemStack helmet = entity.getEquipment().getHelmet();
        if(!Misc.isAirOrNull(helmet) && helmet.getType() == Material.GOLD_HELMET) {
            missingPoints += 1;
        }

        ItemStack chestplate = entity.getEquipment().getChestplate();
        if(!Misc.isAirOrNull(chestplate) && chestplate.getType() == Material.LEATHER_CHESTPLATE) {
            missingPoints += 5;
        }

        if(actualPoints + missingPoints >= 20) missingPoints = 20 - actualPoints;

        double actualReduction = actualPoints * 4;
        double missingReduction = missingPoints * 4;

        return (100 - actualReduction - missingReduction) / (100 - actualReduction);
    }

    public static int getArmorPoints(LivingEntity entity) {
        int points = 0;
        points += getArmorPoints(entity.getEquipment().getHelmet());
        points += getArmorPoints(entity.getEquipment().getChestplate());
        points += getArmorPoints(entity.getEquipment().getLeggings());
        points += getArmorPoints(entity.getEquipment().getBoots());
        return Math.min(points, 20);
    }

    public static int getArmorPoints(ItemStack itemStack) {
        if(Misc.isAirOrNull(itemStack)) return 0;
        Material material = itemStack.getType();
        switch(material) {
            case LEATHER_HELMET:
            case LEATHER_BOOTS:
            case GOLD_BOOTS:
                return 1;
            case LEATHER_LEGGINGS:
            case GOLD_HELMET:
            case IRON_HELMET:
            case IRON_BOOTS:
                return 2;
            case LEATHER_CHESTPLATE:
            case DIAMOND_HELMET:
            case DIAMOND_BOOTS:
                return 3;
            case IRON_LEGGINGS:
                return 5;
            case IRON_CHESTPLATE:
            case DIAMOND_LEGGINGS:
                return 6;
            case DIAMOND_CHESTPLATE:
                return 8;
        }
        return 0;
    }
}