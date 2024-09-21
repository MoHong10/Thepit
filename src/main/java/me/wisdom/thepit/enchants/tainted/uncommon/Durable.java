package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.misc.PitLoreBuilder;
import java.util.List;

public class Durable extends PitEnchant {
    public static Durable INSTANCE;

    public Durable() {
        super("Durable", false, ApplyType.TAINTED,
                "durable", "dura");
        isUncommonEnchant = true;
        isTainted = true;

        INSTANCE = this;
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7This item has " + getExtraLives(enchantLvl) + " extra lives"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "increases the max lives on the item it is enchanted onto";
    }

    public static int getExtraLives(int enchantLvl) {
        return enchantLvl > 0 ? enchantLvl * 30 : 0;
    }
}
