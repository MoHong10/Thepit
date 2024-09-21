package me.wisdom.thepit.market;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.events.MessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class MarketMessaging implements Listener {

    @EventHandler
    public void onMessage(MessageEvent event) {
        PluginMessage message = event.getMessage();
        List<String> strings = message.getStrings();
        List<Boolean> booleans = message.getBooleans();

        if(strings.size() >= 3 && strings.get(0).equals("市场更新")) {
            UUID listingUUID = UUID.fromString(strings.get(1));

            MarketListing listing = MarketManager.getListing(listingUUID);
            if(listing != null) {
                listing.updateListing(message);

                for(YourListingsPanel panel : YourListingsPanel.panels) {
                    panel.placeClaimables();
                }
                return;
            }

            listing = new MarketListing(message);
            MarketManager.listings.add(listing);

            new BukkitRunnable() {
                @Override
                public void run() {
                    MarketGUI.updateGUIS();
                }
            }.runTask(Thepit.INSTANCE);
        }

        if(strings.size() >= 2 && strings.get(0).equals("MARKET REMOVAL")) {
            UUID listingUUID = UUID.fromString(strings.get(1));

            MarketListing listing = MarketManager.getListing(listingUUID);
            if(listing != null) {
                MarketManager.listings.remove(listing);

                for(YourListingsPanel panel : YourListingsPanel.panels) {
                    panel.placeClaimables();
                }
            }
        }

        if(strings.size() >= 3 && strings.get(0).equals("MARKET ASYNC")) {
            UUID playerUUID = UUID.fromString(strings.get(1));

            MarketAsyncTask task = MarketAsyncTask.getTask(playerUUID);
            task.respond(booleans.get(0));
        }
    }
}
