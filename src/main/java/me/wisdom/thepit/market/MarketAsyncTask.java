package me.wisdom.thepit.market;

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

public class MarketAsyncTask {

    public static Map<UUID, MarketAsyncTask> taskMap = new HashMap<>();

    private final MarketTask task;
    private final Runnable success;
    private final Runnable failure;
    private final UUID listingUUID;
    private final Player player;
    private final int parameter;
    private BukkitTask timeout;

    public MarketAsyncTask(MarketTask task, UUID listing, Player executor, int parameter, Runnable success, Runnable failure) {
        this.task = task;
        this.success = success;
        this.failure = failure;
        this.listingUUID = listing;
        this.player = executor;
        this.parameter = parameter;
        this.timeout = null;

        if(!Thepit.MARKET_ENABLED) {
            failure.run();
            return;
        }

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

        PluginMessage message = new PluginMessage().writeString(task.proxyName);
        message.writeString(executor.getUniqueId().toString()).writeString(listing.toString());
        if(task == MarketTask.BIN_ITEM || task == MarketTask.PLACE_BID) {
            message.writeInt(parameter);
        }
        message.send();
    }

    public MarketAsyncTask(MarketTask task, Player executor, CreateListingInfo info,  Runnable success, Runnable failure) {
        this.task = task;
        this.player = executor;
        this.success = success;
        this.failure = failure;
        this.listingUUID = UUID.randomUUID();
        this.parameter = 0;
        this.timeout = null;

        if(task != MarketTask.CREATE_LISTING) return;

        if(taskMap.containsKey(executor.getUniqueId())) {
            respond(false);
        }

        taskMap.put(executor.getUniqueId(), this);

        timeout = new BukkitRunnable() {
            @Override
            public void run() {
                failure.run();
                taskMap.remove(player.getUniqueId());
            }
        }.runTaskLater(Thepit.INSTANCE, 20);

        PluginMessage message = new PluginMessage()
                .writeString(task.proxyName)
                .writeString(executor.getUniqueId().toString())
                .writeString(listingUUID.toString())
                .writeString(info.item)
                .writeInt(info.startingBid)
                .writeInt(info.binPrice)
                .writeLong(info.duration)
                .writeBoolean(info.isStackBin);

        message.send();
    }

    public MarketTask getTask() {
        return task;
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
    public UUID getListingUUID() {
        return listingUUID;
    }

    public Player getPlayer() {
        return player;
    }

    public int getParameter() {
        return parameter;
    }

    public void respond(boolean success) {
        timeout.cancel();
        taskMap.remove(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if(success) {
                    MarketAsyncTask.this.success.run();
                } else {
                    MarketAsyncTask.this.failure.run();
                }
            }
        }.runTask(Thepit.INSTANCE);
    }

    public enum MarketTask {
        CREATE_LISTING("创建列表"),
        REMOVE_LISTING("移除列表"),
        PLACE_BID("下市场竞标"),
        BIN_ITEM("列出 BIN"),
        CLAIM_ITEM("领取列表物品"),
        CLAIM_SOULS("领取列表灵魂"),
        STAFF_REMOVE("工作人员移除列表");

        public final String proxyName;

        MarketTask(String proxyName) {
            this.proxyName = proxyName;
        }
    }

    public static MarketAsyncTask getTask(UUID playerUUID) {
        return taskMap.get(playerUUID);
    }

    public static Runnable getDefaultFail(Player player) {
        return getDefaultFail(player, "&c您的请求出现问题。请稍后再试。");
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

    public static class CreateListingInfo {

        public String item;
        public int startingBid;
        public int binPrice;
        public long duration;
        public boolean isStackBin;

        public void setItem(String item) {
            this.item = item;
        }

        public void setStartingBid(int startingBid) {
            this.startingBid = startingBid;
        }

        public void setBinPrice(int binPrice) {
            this.binPrice = binPrice;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public void setStackBin(boolean isStackBin) {
            this.isStackBin = isStackBin;
        }
    }
}
