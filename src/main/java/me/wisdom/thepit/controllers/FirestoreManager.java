package me.wisdom.thepit.controllers;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.Config;
import me.wisdom.thepitapi.misc.AOutput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FirestoreManager {
    public static Firestore FIRESTORE;

    public static final String SERVER_COLLECTION = Collection.getCollection(Thepit.serverName).refName[0];
    public static final String CONFIG_DOCUMENT = "config";

    public static final String PLAYERDATA_COLLECTION = SERVER_COLLECTION + "-playerdata";

    public static Config CONFIG;

    public static void init() {
        try {
            AOutput.log("Loading PitSim database");
            InputStream serviceAccount = Files.newInputStream(new File(Thepit.INSTANCE.getDataFolder() + "/google-key.json").toPath());
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            try {
                FirebaseApp.initializeApp(options);
            } catch(IllegalStateException exception) {
                AOutput.log("Firestore already initialized");
            }

            FIRESTORE = FirestoreClient.getFirestore();
            AOutput.log("PitSim 数据库加载中!");
        } catch(IOException exception) {
            AOutput.log("PitSim 数据库加载失败。正在禁用插件...");
            Thepit.INSTANCE.getServer().getPluginManager().disablePlugin(Thepit.INSTANCE);
            return;
        }

//		new Thread(FirestoreManager::fetchDocuments).start();
        FirestoreManager.fetchDocuments();
    }

    public static void fetchDocuments() {
        AOutput.log("记载基础 PitSim 数据");
        try {
            if(!FIRESTORE.collection(SERVER_COLLECTION).document(CONFIG_DOCUMENT).get().get().exists()) {
                CONFIG = new Config();
                CONFIG.save();
            } else {
                CONFIG = FIRESTORE.collection(SERVER_COLLECTION).document(CONFIG_DOCUMENT).get().get().toObject(Config.class);
            }

        } catch(Exception exception) {
            exception.printStackTrace();
            AOutput.log("Firestore 加载关键数据失败。正在禁用插件...");
            Thepit.INSTANCE.getServer().getPluginManager().disablePlugin(Thepit.INSTANCE);
        }
        AOutput.log("PitSim 数据加载");
    }

    public enum Collection {
        DEV("dev"),
        KYRO("kyro"),
        PITSIM("pitsim", "darkzone");

        public String[] refName;
        Collection(String... refName) {
            this.refName = refName;
        }

        public static Collection getCollection(String serverName) {
            for(Collection value : values()) {
                for(String s : value.refName) {
                    if(serverName.contains(s)) return value;
                }
            }
            throw new RuntimeException();
        }
    }
}