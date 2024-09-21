package me.wisdom.thepit.enchants.overworld;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.ApplyType;
import me.wisdom.thepit.enums.KillModifier;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.misc.Sounds;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.List;

public class Executioner extends PitEnchant {
    public static Executioner INSTANCE;

    public Executioner() {
        super("Executioner", true, ApplyType.MELEE,
                "executioner", "exe");
        INSTANCE = this;
    }

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isKillerPlayer() || !killEvent.hasKillModifier(KillModifier.EXECUTION)) return;

        Sounds.EXE.play(killEvent.getKiller());
        killEvent.getDead().getWorld().playEffect(killEvent.getDead().getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);

        PitPlayer pitPlayer = killEvent.getKillerPitPlayer();
        pitPlayer.stats.executioner++;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!canApply(attackEvent)) return;

        int enchantLvl = attackEvent.getAttackerEnchantLevel(this);
        if(enchantLvl == 0) return;

        attackEvent.executeUnder = getExecuteHealth(enchantLvl);

//		if(attackEvent.attacker.getName().equals("KyroKrypt")) {
//			yeet(attackEvent.defender);
//		}
    }

    public void yeet(Player willBeCrashed) {
        final EntityPlayer nmsPlayer = ((CraftPlayer) willBeCrashed).getHandle();
        final EntityCreeper entity = new EntityCreeper(nmsPlayer.world);
        final DataWatcher dataWatcher = new DataWatcher(entity);
        dataWatcher.a(18, (Object) Integer.MAX_VALUE);
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(entity);
        nmsPlayer.playerConnection.sendPacket(spawnPacket);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Thepit.INSTANCE, () -> {
            PacketPlayOutEntityMetadata crashPacket = new PacketPlayOutEntityMetadata(entity.getId(), dataWatcher, true);
            nmsPlayer.playerConnection.sendPacket(crashPacket);
        }, 5L);
    }

    @Override
    public List<String> getNormalDescription(int enchantLvl) {
        return new PitLoreBuilder(
                "&7Hitting an enemy below &c" + Misc.getHearts(getExecuteHealth(enchantLvl)) + " &7instantly kills them"
        ).getLore();
    }

    @Override
    public String getSummary() {
        return getDisplayName(false, true) + " &7is an enchant that instantly " +
                "kills bots and players that are under a certain health " +
                "threshold. This enchant is particularly good for streaking";
    }

    public double getExecuteHealth(int enchantLvl) {
        if(enchantLvl > 2) return enchantLvl * 2 - 1;
        return enchantLvl + 1;
    }
}
