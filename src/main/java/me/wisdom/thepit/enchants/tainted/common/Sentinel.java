package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitCreeper;
import me.wisdom.thepit.darkzone.mobs.PitEnderman;
import me.wisdom.thepit.darkzone.mobs.PitSpider;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Sentinel extends BasicDarkzoneEnchant {
    public static Sentinel INSTANCE;

    public Sentinel() {
        super("Sentinel", false, ApplyType.SCYTHES,
                "sentinel", "sentinal");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 9 + 5;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitSpider.class, PitCreeper.class, PitEnderman.class);
    }
}
