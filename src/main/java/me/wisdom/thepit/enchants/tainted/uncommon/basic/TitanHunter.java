package me.wisdom.thepit.enchants.tainted.uncommon.basic;

import me.wisdom.thepit.controllers.objects.BasicDarkzoneEnchant;
import me.wisdom.thepit.darkzone.PitMob;
import me.wisdom.thepit.darkzone.mobs.PitCreeper;
import me.wisdom.thepit.darkzone.mobs.PitEnderman;
import me.wisdom.thepit.darkzone.mobs.PitIronGolem;
import me.wisdom.thepit.enums.ApplyType;

import java.util.Arrays;
import java.util.List;

public class TitanHunter extends BasicDarkzoneEnchant {
    public static TitanHunter INSTANCE;

    public TitanHunter() {
        super("Titan Hunter", false, ApplyType.SCYTHES,
                "titanhunter", "titan", "hunter3", "hunt3");
        isUncommonEnchant = true;
        isTainted = true;
        INSTANCE = this;
    }

    @Override
    public int getBaseStatPercent(int enchantLvl) {
        return enchantLvl * 11 + 15
                ;
    }

    @Override
    public boolean isOffensive() {
        return true;
    }

    @Override
    public List<Class<? extends PitMob>> getApplicableMobs() {
        return Arrays.asList(PitCreeper.class, PitIronGolem.class, PitEnderman.class);
    }
}
