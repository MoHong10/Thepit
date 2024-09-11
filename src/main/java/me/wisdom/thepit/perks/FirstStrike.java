package me.wisdom.thepit.perks;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepit.upgrades.UnlockFirstStrike;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstStrike extends PitPerk {
    public static FirstStrike INSTANCE;
    public static Map<Player, List<LivingEntity>> hitPlayers = new HashMap<>();

    public FirstStrike() {
        super("First Strike", "firststrike");
        renownUpgradeClass = UnlockFirstStrike.class;
        INSTANCE = this;
    }

    @EventHandler
    public void onHit(AttackEvent.Apply attackEvent) {
        if(!hasPerk(attackEvent.getAttacker())) return;

        hitPlayers.putIfAbsent(attackEvent.getAttackerPlayer(), new ArrayList<>());
        List<LivingEntity> hitList = hitPlayers.get(attackEvent.getAttackerPlayer());

        if(hitList.contains(attackEvent.getDefender())) return;
        attackEvent.increasePercent += 30;

        hitPlayers.get(attackEvent.getAttackerPlayer()).add(attackEvent.getDefender());
        new BukkitRunnable() {
            @Override
            public void run() {
                hitPlayers.get(attackEvent.getAttackerPlayer()).remove(attackEvent.getDefender());
            }
        }.runTaskLater(Thepit.INSTANCE, 120L);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.COOKED_CHICKEN)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&7首次击中玩家或机器人时造成 &c+30% 伤害"
        );
    }

    @Override
    public String getSummary() {
        return "&eFirst Strike &7是一个在 &erenown 商店解锁的技能，可以在你首次击中机器人或玩家时" +
                "增加 &c攻击力&7 并赋予你 &e速度&7。";
    }
}
