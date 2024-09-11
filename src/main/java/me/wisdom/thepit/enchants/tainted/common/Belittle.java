package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitBlaze;
import me.wisdom.thepit.darkzone.mobs.PitSpider;
import me.wisdom.thepit.darkzone.mobs.PitWolf;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Belittle extends BasicDarkzoneEnchant {
    public static Belittle INSTANCE;

    public Belittle() {
        super("Belittle", false, ApplyType.CHESTPLATES,
                "belittle");
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
        return Arrays.asList(PitSpider.class, PitWolf.class, PitBlaze.class);
    }
}
