package me.wisdom.thepit.darkzone;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.ActionBarManager;
import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.SpawnManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.items.MysticFactory;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.PlayerItemLocation;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;

public class VoucherManager implements Listener {

    static {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    if(!SpawnManager.isInSpawn(player)) continue;
                    if(player.getWorld() == MapManager.getDarkzone()) continue;

                    PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
                    int vouchers = pitPlayer.darkzoneData.demonicVouchers;
                    if(vouchers <= 0) continue;

                    int mystics = Misc.getItemCount(player, true, (pitItem, itemStack) -> MysticFactory.isJewel(itemStack, true));

                    String voucherText = vouchers != 1 ? "Vouchers" : "Voucher";
                    String mysticText = mystics != 1 ? "Jewels" : "Jewel";
                    String message = "&4" + vouchers + " " + voucherText + "&7, &d" + mystics + " " + mysticText;

                    ActionBarManager.sendActionBar(player, message);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0, 20);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(KillEvent event) {
        if(!Thepit.getStatus().isOverworld()) return;
        Player deadPlayer = event.getDeadPlayer();
        PitPlayer deadPitPlayer = event.getDeadPitPlayer();
        if(!deadPitPlayer.isOnMega()) return;
        int totalVouchersUsed = 0;

        for(Map.Entry<PlayerItemLocation, KillEvent.ItemInfo> entry : new ArrayList<>(event.getVulnerableItems().entrySet())) {
            KillEvent.ItemInfo info = entry.getValue();
            ItemStack itemStack = info.itemStack;

            boolean isJewel = MysticFactory.isJewel(itemStack, true);
            if(deadPitPlayer.darkzoneData.demonicVouchers > 0 && isJewel) {
                totalVouchersUsed++;
                deadPitPlayer.darkzoneData.demonicVouchers--;
                event.removeVulnerableItem(entry.getKey());
            }
        }

        if(totalVouchersUsed > 0) {
            int finalTotalVouchersUsed = totalVouchersUsed;
            new BukkitRunnable() {
                @Override
                public void run() {
                    AOutput.send(deadPlayer, "&4&lHERESY!&7 Saved &f" + finalTotalVouchersUsed + " &7lives with &4Demonic Vouchers&7!");
                    Sounds.VOUCHER_USE.play(deadPlayer);
                }
            }.runTaskLater(Thepit.INSTANCE, 10);
        }
    }
}
