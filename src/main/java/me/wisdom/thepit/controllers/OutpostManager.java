package me.wisdom.thepit.controllers;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.wisdom.pitguilds.Guild;
import me.wisdom.pitguilds.GuildManager;
import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.OutpostBanner;
import me.wisdom.thepit.controllers.objects.PluginMessage;
import me.wisdom.thepit.holograms.Hologram;
import me.wisdom.thepit.holograms.RefreshMode;
import me.wisdom.thepit.holograms.ViewMode;
import me.wisdom.thepit.misc.Formatter;
import me.wisdom.thepit.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OutpostManager implements Listener {
    public static final Location CENTER_LOCATION = new Location(MapManager.getDarkzone(), 227.5, 93, -189.5);

    public static double CONTROL_INCREMENT = 1;

    public static Guild controllingGuild;
    public static boolean isActive;
    public static double percentControlled = 0;

    public static long lastContestingNotification = 0;

    public static OutpostBanner banner;
    public static Hologram hologram;

    public OutpostManager() {
        banner = new OutpostBanner();
        hologram = new Hologram(CENTER_LOCATION.clone().add(0, 5, 0), ViewMode.ALL, RefreshMode.MANUAL) {
            @Override
            public List<String> getStrings(Player player) {
                List<String> strings = new ArrayList<>();
                strings.add("&3&l前哨站");
                strings.add("&8&m------------------");
                strings.add("&7控制者: &b" + (controllingGuild == null ? "&c无" : controllingGuild.color + controllingGuild.name));
                strings.add("&7控制率: &f" + (int) percentControlled + "%");
                strings.add("&7状态: " + getStatus());
                strings.add("&8&m------------------");
                strings.add("&7奖励 &6+" + Formatter.formatGoldFull(getGoldAmount()) + "&7/10m, &d+" + getOutpostFreshIncrease() + "% 神秘发现");
                return strings;
            }
        };

        if(Thepit.status.isDarkzone()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<Guild> guildsInOutpost = getGuildsInOutpost();
                    List<Guild> enemyGuilds = new ArrayList<>(guildsInOutpost);
                    boolean activeGuildPresent = enemyGuilds.remove(controllingGuild);

//			    	A guild controls the outpost and no one is capturing it
                    if(enemyGuilds.isEmpty() && controllingGuild != null) {
                        if(isActive || activeGuildPresent) {
                            increaseControl();
                        } else {
                            decreaseControl(null);
                        }
                    } else if(controllingGuild != null && !activeGuildPresent && enemyGuilds.size() == 1) {
                        decreaseControl(enemyGuilds.remove(0));
                    } else if(controllingGuild == null && guildsInOutpost.size() == 1) {
                        setControllingGuild(guildsInOutpost.remove(0));
                    }
                    hologram.updateHologram();
                }
            }.runTaskTimer(Thepit.INSTANCE, 0L, 20L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(isActive) {
                        String notification = "&7已添加 &6+" + Formatter.formatGoldFull(getGoldAmount())
                                + " &7到公会银行，以奖励控制前哨站!";

                        new PluginMessage()
                                .writeString("OUTPOST GOLD")
                                .writeString(controllingGuild.uuid.toString())
                                .writeString(notification)
                                .writeInt(getGoldAmount())
                                .send();
                    }
                }
            }.runTaskTimer(Thepit.INSTANCE, 20 * 60 * 10, 20 * 60 * 10);
        }
    }

    public static String getStatus() {
        if(controllingGuild == null) return "&6&l未控制";
        List<Guild> guildsInOutpost = getGuildsInOutpost();
        List<Guild> enemyGuilds = new ArrayList<>(guildsInOutpost);
        boolean activeGuildPresent = enemyGuilds.remove(controllingGuild);

        if (enemyGuilds.size() > 0 || !isActive) return "&c&l争夺中";
        return "&a&l已控制";
    }

    public static void increaseControl() {
        if(percentControlled == 100) return;
        if(percentControlled + CONTROL_INCREMENT >= 100) {
            setActive(true);
            setPercentControlled(100);
            lastContestingNotification = 0L;
            String message = "&3&l前哨站! " + controllingGuild.color + controllingGuild.name + " &7已占领前哨站!";

            List<Guild> guildsInOutpost = getGuildsInOutpost();
            List<Guild> enemyGuilds = new ArrayList<>(guildsInOutpost);
            boolean activeGuildPresent = enemyGuilds.remove(controllingGuild);

            if(activeGuildPresent) Misc.broadcast(message, false);
        } else {
            setPercentControlled(percentControlled + CONTROL_INCREMENT);
        }
    }

    public static void decreaseControl(Guild capturingGuild) {
        if(percentControlled == 0) return;
        if(percentControlled - CONTROL_INCREMENT <= 0) {
            if(isActive) {
                String message = "&3&l前哨站! " + controllingGuild.color + controllingGuild.name + " &7已失去对前哨站的控制!";
                Misc.broadcast(message, false);
            }
            setControllingGuild(null);
            setActive(false);
            setPercentControlled(0);
        } else {
            setPercentControlled(percentControlled - CONTROL_INCREMENT);
            if(isActive && lastContestingNotification + 20 * 10 < Thepit.currentTick) {
                lastContestingNotification = Thepit.currentTick;
                sendGuildMessage(controllingGuild.uuid, "&e前哨站正在被 " + capturingGuild.color + capturingGuild.name + "&e 占领!");
            }
        }
    }

    public static void setControllingGuild(Guild guild) {
        controllingGuild = guild;
        sendOutpostData();
    }

    public static void setActive(boolean active) {
        isActive = active;
        sendOutpostData();
    }

    public static void setPercentControlled(double percent) {
        percentControlled = percent;
        banner.setBanner(controllingGuild);
        banner.setPercent((int) percent);
    }

    public static void sendOutpostData() {
        PluginMessage message = new PluginMessage().writeString("OUTPOST DATA");
        message.writeString(controllingGuild == null ? "null" : controllingGuild.uuid.toString());
        message.writeBoolean(isActive);
        message.send();
    }

    public static List<Guild> getGuildsInOutpost() {
        List<Guild> guilds = new ArrayList<>();

        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionManager regions = container.get(MapManager.getDarkzone());
        assert regions != null;

        for(Player player : Bukkit.getOnlinePlayers()) {
            ApplicableRegionSet set = regions.getApplicableRegions((BukkitUtil.toVector(player.getLocation())));
            for(ProtectedRegion region : set) {
                if(!region.getId().equals("outpost")) continue;
                Guild guild = GuildManager.getGuild(player);
                if(guild != null && !guilds.contains(guild)) guilds.add(guild);
            }
        }

        return guilds;
    }

    public static void sendGuildMessage(UUID guildUUID, String message) {
        PluginMessage pluginMessage = new PluginMessage().writeString("工会信息");
        pluginMessage.writeString(guildUUID.toString());
        pluginMessage.writeString(message);
        pluginMessage.send();
    }

    public static boolean shouldReceiveRewards(Player player) {
        Guild guild = GuildManager.getGuild(player);
        return shouldReceiveRewards(guild);
    }

    public static boolean shouldReceiveRewards(Guild guild) {
        return guild != null && OutpostManager.controllingGuild == guild && OutpostManager.isActive;
    }

    public static int getOutpostFreshIncrease() {
        return 50;
    }

    public static int getGoldAmount() {
        return 50_000;
    }
}
