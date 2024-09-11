package me.wisdom.thepit.upgrades;

import me.wisdom.thepit.controllers.UpgradeManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.controllers.objects.TieredRenownUpgrade;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.megastreaks.Uberstreak;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UberInsurance extends TieredRenownUpgrade {
    public static UberInsurance INSTANCE;

    public UberInsurance() {
        super("Uber Insurance", "LIFE_INSURANCE", 20);
        INSTANCE = this;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onKill(KillEvent killEvent) {
        if(!killEvent.isDeadPlayer() || !isApplicable(killEvent.getDeadPlayer())) return;
        for(Map.Entry<PlayerItemLocation, KillEvent.ItemInfo> entry : new ArrayList<>(killEvent.getVulnerableItems().entrySet())) {
            KillEvent.ItemInfo itemInfo = entry.getValue();
            if(!itemInfo.pitItem.isMystic) continue;
            killEvent.removeVulnerableItem(entry.getKey());
        }
    }

    public static boolean isApplicable(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        int tier = UpgradeManager.getTier(player, INSTANCE);

        if(!(pitPlayer.getMegastreak() instanceof Uberstreak)) return false;
        if(pitPlayer.getKills() >= 400 && tier >= 3) return true;
        if(pitPlayer.getKills() >= 450 && tier >= 2) return true;
        if(pitPlayer.getKills() >= 500 && tier >= 1) return true;
        return false;
    }

    @Override
    public ItemStack getBaseDisplayStack() {
        return new AItemStackBuilder(Material.BOOK_AND_QUILL)
                .getItemStack();
    }

    @Override
    public String getCurrentEffect(int tier) {
        return "&7在 &dUber " + Math.max(550 - tier * 50, 100) + " 时保留生命";
    }

    @Override
    public String getEffectPerTier() {
        return "&7在完成 &dUberstreaks &7时更早地保留生命，每 &dMystics &750 击杀";
    }

    @Override
    public String getSummary() {
        return "&dUber 保险&7 是一个 &e声望&7 特权，在 &dUberstreak&7 中根据你获得的 &c击杀&7 数量保存你的生命";
    }

    @Override
    public List<Integer> getTierCosts() {
        return Arrays.asList(40, 75, 150);
    }
}
