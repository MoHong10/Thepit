package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitEnderman;
import me.wisdom.thepit.darkzone.mobs.PitWitherSkeleton;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class AnkleBiter extends BasicDarkzoneEnchant {
    public static AnkleBiter INSTANCE;

    public AnkleBiter() {
        super("Ankle-Biter", false, ApplyType.SCYTHES,
                "anklebiter", "ankle");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 10 + 6;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitWitherSkeleton.class, PitEnderman.class);
    }
}
