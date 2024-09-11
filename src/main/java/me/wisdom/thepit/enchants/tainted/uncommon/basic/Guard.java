package me.wisdom.thepit.enchants.tainted.uncommon.basic;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitSkeleton;
import me.wisdom.thepit.darkzone.mobs.PitSpider;
import me.wisdom.thepit.darkzone.mobs.PitWolf;
import me.wisdom.thepit.darkzone.mobs.PitZombie;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Guard extends BasicDarkzoneEnchant {
    public static Guard INSTANCE;

    public Guard() {
        super("Guard", false, ApplyType.CHESTPLATES,
                "guard", "shield1");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 5 + 4;
    }

    @Override
    public boolean isOffensive() {
        return false;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitZombie.class, PitSkeleton.class, PitSpider.class, PitWolf.class);
    }
}
