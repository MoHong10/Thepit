package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitBlaze;
import me.wisdom.thepit.darkzone.mobs.PitSpider;
import me.wisdom.thepit.darkzone.mobs.PitWolf;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Intimidating extends BasicDarkzoneEnchant {
    public static Intimidating INSTANCE;

    public Intimidating() {
        super("Intimidating", false, ApplyType.SCYTHES,
                "intimidating");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 11 + 8;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitSpider.class, PitWolf.class, PitBlaze.class);
    }
}
