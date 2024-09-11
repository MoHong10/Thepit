package me.wisdom.thepit.enums;

public enum MobStatus {
    STANDARD,
    MINION;

    public boolean isMinion() {
        return this == MobStatus.MINION;
    }
}
