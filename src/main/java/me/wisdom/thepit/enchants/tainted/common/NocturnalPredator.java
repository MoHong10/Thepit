package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.*;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class NocturnalPredator extends BasicDarkzoneEnchant {
    public static NocturnalPredator INSTANCE;

    public NocturnalPredator() {
        super("Nocturnal Predator", false, ApplyType.SCYTHES,
                "nocturnalpredator", "nocturnal", "predator");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 7 + 6;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitZombie.class, PitSkeleton.class, PitSpider.class, PitCreeper.class, PitEnderman.class);
    }
}
