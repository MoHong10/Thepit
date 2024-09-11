package me.wisdom.thepit.misc;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.Method;

public class ReloadManager {

    public static void init() {
        new BukkitRunnable() {
            long lastModified = 0;
            boolean startedUpload = false;

            @Override
            public void run() {
                try {
                    Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
                    getFileMethod.setAccessible(true);
                    File file = (File) getFileMethod.invoke(Thepit.INSTANCE);

                    if(lastModified == 0) {
                        lastModified = file.lastModified();
                        return;
                    }

                    if(file.lastModified() == lastModified && startedUpload) {
                        cancel();
                        AOutput.log("JAR 上传完成。正在重启插件");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "plugman reload pitsim");
                        return;
                    }

                    if(file.lastModified() != lastModified && !startedUpload) {
                        startedUpload = true;
                        AOutput.log("检测到服务器 JAR 文件上传。等待完成");
                    }
                    lastModified = file.lastModified();
                } catch(Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 10L);
    }
}
