package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitCreeper;
import me.wisdom.thepit.darkzone.mobs.PitSpider;
import me.wisdom.thepit.darkzone.mobs.PitWolf;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Huggable extends BasicDarkzoneEnchant {
    public static Huggable INSTANCE;

    public Huggable() {
        super("Huggable", false, ApplyType.CHESTPLATES,
                "huggable", "hug");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 5 + 5;
    }

    @Override
    public boolean isOffensive() {
        return false;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitSpider.class, PitWolf.class, PitCreeper.class);
    }
}
