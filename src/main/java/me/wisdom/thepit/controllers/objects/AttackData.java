package me.wisdom.thepit.controllers.objects;

public class AttackData {

    public enum AttackType {
        MELEE_VS_PLAYERS_OUTSIDE_MIDDLE,
        MELEE_VS_PLAYERS_INSIDE_MIDDLE,
        RANGED_VS_PLAYERS_OUTSIDE_MIDDLE,
        OFFENSE_VS_BOTS,
        DEFENCE_VS_BOTS,

        MELEE_VS_PLAYERS_DARKZONE,
        RANGED_VS_PLAYERS_DARKZONE,
        OFFENSE_VS_MOBS,
        DEFENCE_VS_MOBS
    }
}
