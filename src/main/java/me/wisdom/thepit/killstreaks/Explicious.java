package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Explicious extends Killstreak {

    public static Explicious INSTANCE;

    public Explicious() {
        super("Explicious", "Explicious", 3, 2);
        INSTANCE = this;
    }

    List<LivingEntity> rewardPlayers = new ArrayList<>();

    @EventHandler
    public void onKill(KillEvent killEvent) {
        if(rewardPlayers.contains(killEvent.getKiller())) {
            killEvent.xpReward += 50;
            killEvent.xpCap += 50;
            rewardPlayers.remove(killEvent.getKiller());
        }
    }

    @Override
    public void proc(Player player) {
        if(!rewardPlayers.contains(player)) rewardPlayers.add(player);
    }

    @Override
    public void reset(Player player) {
        rewardPlayers.remove(player);
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.INK_SACK, 1, 12)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7Gain &b+50 XP&7 and &b+50 max XP",
                        "&7on your next kill."
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&bExplicious&7 is a killstreak that increases &bXP&7 and &bmax XP&7 on your next kill every &c3 kills";
    }
}
