package me.wisdom.thepit.darkzone.abilities.minion;

import me.wisdom.thepit.darkzone.SubLevelType;

public class IronGolemMinionAbility extends MinionAbility {

    public IronGolemMinionAbility() {
        super(0, SubLevelType.IRON_GOLEM, 1);
    }

    @Override
    public void onEnable() {
        spawnMobs(null, 1);
    }
}
