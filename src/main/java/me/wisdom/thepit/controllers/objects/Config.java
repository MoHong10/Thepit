package me.wisdom.thepit.controllers.objects;

import com.google.cloud.firestore.annotation.Exclude;
import me.wisdom.thepitapi.misc.AOutput;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.FirestoreManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Config {
    @Exclude
    public boolean onSaveCooldown = false;
    @Exclude
    public boolean saveQueued = false;

    public String prefix = "";
    public String errorPrefix = "&c";
    public boolean nons = true;
    public String mapData;

    public Map<String, String> boosterActivatorMap = new HashMap<>();
    public Map<String, Integer> boosters = new HashMap<>();

    //	PitSim pass stuff
    public Date currentPassStart;
    public CurrentPassData currentPassData;

    public static class CurrentPassData {
        public Map<String, Integer> activeWeeklyQuests = new HashMap<>();
    }

    public Security security = new Security();

    public static class Security {
        public boolean requireVerification = false;
        public boolean requireCaptcha = false;
    }

    public String pteroURL = null;
    public String pteroClientKey = null;

    public String sqlDataURL = null;
    public String sqlDataUser = null;
    public String sqlDataPass = null;

    public String alertsWebhook = null;
    public String bansWebhook = null;

    public Config() {}

    @Exclude
    public void save() {
        if(!Thepit.serverName.equals("pitsim-1") && !Thepit.serverName.equals("pitsimdev-1")) return;
        if(onSaveCooldown && !saveQueued) {
            saveQueued = true;
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                saveQueued = false;
                save();
            }).start();
        }
        if(!saveQueued && !onSaveCooldown) {
            FirestoreManager.FIRESTORE.collection(FirestoreManager.SERVER_COLLECTION).document(FirestoreManager.CONFIG_DOCUMENT).set(this);
            AOutput.log("Saving PitSim Config");
            onSaveCooldown = true;
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                onSaveCooldown = false;
            }).start();
        }
    }

    @Exclude
    public void load() {
        try {
            FirestoreManager.CONFIG = FirestoreManager.FIRESTORE.collection(FirestoreManager.SERVER_COLLECTION)
                    .document(FirestoreManager.CONFIG_DOCUMENT).get().get().toObject(Config.class);
        } catch(InterruptedException | ExecutionException exception) {
            throw new RuntimeException(exception);
        }
    }
}