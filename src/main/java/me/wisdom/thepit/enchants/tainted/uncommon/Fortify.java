package me.wisdom.thepit.enchants.tainted.uncommon;

import me.wisdom.thepit.controllers.EnchantManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.misc.PitLoreBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public class Fortify extends PitEnchant {
    public static Fortify INSTANCE;

    public Fortify() {
        super("Fortify", false, ApplyType.CHESTPLATES,
                "fortify");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    public static int getShieldIncrease(Player player) {
        if(!INSTANCE.isEnabled()) return 0;

        int enchantLvl = EnchantManager.getEnchantLevel(player, INSTANCE);
        if(enchantLvl == 0) return 0;

        return getShieldIncrease(enchantLvl);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Increases your max shield by &9+" + getShieldIncrease(enchantLvl) + " &7hitpoints"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is a &5Darkzone &7enchant that " +
                "increases the max hp of your shield";
    }

    public static int getShieldIncrease(int enchantLvl) {
        return enchantLvl * 30 + 10;
    }
}
