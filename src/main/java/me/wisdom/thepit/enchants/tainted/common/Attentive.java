package me.wisdom.thepit.enchants.tainted.common;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitCreeper;
import me.wisdom.thepit.darkzone.mobs.PitEnderman;
import me.wisdom.thepit.darkzone.mobs.PitSpider;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class Attentive extends BasicDarkzoneEnchant {
    public static Attentive INSTANCE;

    public Attentive() {
        super("Attentive", false, ApplyType.CHESTPLATES,
                "attentive");
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 5 + 3;
    }

    @Override
    public boolean isOffensive() {
        return false;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitSpider.class, PitCreeper.class, PitEnderman.class);
    }
}
