package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitBlaze;
import me.wisdom.thepit.darkzone.mobs.PitSkeleton;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class WhoNeedsBows extends BasicDarkzoneEnchant {
    public static WhoNeedsBows INSTANCE;

    public WhoNeedsBows() {
        super("Who Needs Bows?", false, ApplyType.SCYTHES,
                "whoneedsbows", "whatsabow");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 9 + 11;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitSkeleton.class, PitBlaze.class);
    }
}
