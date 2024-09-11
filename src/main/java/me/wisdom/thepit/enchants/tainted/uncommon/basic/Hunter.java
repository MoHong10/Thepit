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

public class Hunter extends BasicDarkzoneEnchant {
    public static Hunter INSTANCE;

    public Hunter() {
        super("Hunter", false, ApplyType.SCYTHES,
                "hunter", "hunt");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 11 + 11;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitZombie.class, PitSkeleton.class, PitSpider.class, PitWolf.class);
    }
}
