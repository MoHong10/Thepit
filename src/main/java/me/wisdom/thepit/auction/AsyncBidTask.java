package me.wisdom.thepit.auction;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AsyncBidTask {

    public static Map<UUID, AsyncBidTask> taskMap = new HashMap<>();

    private final Runnable success;
    private final Runnable failure;
    private final int auctionSlot;
    private final Player player;
    private final int bidAmount;
    private BukkitTask timeout;

    public AsyncBidTask(int auctionSlot, Player executor, int bidAmount, Runnable success, Runnable failure) {
        this.success = success;
        this.failure = failure;
        this.auctionSlot = auctionSlot;
        this.player = executor;
        this.bidAmount = bidAmount;
        this.timeout = null;

        timeout = new BukkitRunnable() {
            @Override
            public void run() {
                failure.run();
                taskMap.remove(player.getUniqueId());
            }
        }.runTaskLater(Thepit.INSTANCE, 20);


        if(taskMap.containsKey(executor.getUniqueId())) {
            respond(false);
        }

        taskMap.put(executor.getUniqueId(), this);

        PluginMessage message = new PluginMessage().writeString("AUCTION BID");
        message.writeString(Thepit.serverName).writeString(executor.getUniqueId().toString()).writeInt(auctionSlot).writeInt(bidAmount);
        message.send();
    }

    public Runnable getSuccess() {
        return success;
    }

    public Runnable getFailure() {
        return failure;
    }

    public BukkitTask getTimeout() {
        return timeout;
    }

    public int getAuctionSlot() {
        return auctionSlot;
    }

    public Player getPlayer() {
        return player;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void respond(boolean success) {
        timeout.cancel();
        taskMap.remove(player.getUniqueId());
        if(success) {
            this.success.run();
        } else {
            this.failure.run();
        }
    }

    public static AsyncBidTask getTask(UUID playerUUID) {
        return taskMap.get(playerUUID);
    }

    public static Runnable getDefaultFail(Player player) {
        return getDefaultFail(player, "&c有一个问题。请稍后再试.");
    }

    public static Runnable getDefaultFail(Player player, String message) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                AOutput.error(player, message);
                Sounds.NO.play(player);
                player.closeInventory();
            }
        };
    }
}