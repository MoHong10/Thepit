package me.wisdom.thepit.perks;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitEnchant;
import me.wisdom.thepit.controllers.objects.PitPerk;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.misc.PitLoreBuilder;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dispersion extends PitPerk {
    public static Dispersion INSTANCE;

    public Dispersion() {
        super("Dispersion", "dispersion");
        INSTANCE = this;
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!attackEvent.isAttackerPlayer() || !hasPerk(attackEvent.getDefenderPlayer())) return;

        if(MapManager.currentMap.world != attackEvent.getDefenderPlayer().getWorld()) return;
        if(MapManager.currentMap.getMid().distance(attackEvent.getDefenderPlayer().getLocation()) > getRange()) return;

        List<PitEnchant> toRemove = new ArrayList<>();
        for(Map.Entry<PitEnchant, Integer> entry : attackEvent.getAttackerEnchantMap().entrySet()) {
            if(Math.random() > getChance() / 100.0) continue;
            toRemove.add(entry.getKey());
        }
        for(PitEnchant pitEnchant : toRemove) attackEvent.getAttackerEnchantMap().remove(pitEnchant);
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.WEB)
                .getItemStack();
    }

    @Override
    public void addBaseDescription(PitLoreBuilder loreBuilder, Player player) {
        loreBuilder.addLongLine(
                "&d在中路时，" + getChance() + "% &7的对手攻击附魔会被分散"
        );
    }

    @Override
    public String getSummary() {
        return "&aDispersion &7是一个技能，能够在中路时分散你对手攻击的附魔效果。";
    }

    public static int getRange() {
        return 10;
    }

    public static int getChance() {
        return 60;
    }
}
