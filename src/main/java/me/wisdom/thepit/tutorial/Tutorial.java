package me.wisdom.thepit.tutorial;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.DiscordLogChannel;
import me.wisdom.thepit.misc.Misc;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.misc.AOutput;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Tutorial {

    public final TutorialData data;

    protected UUID uuid;
    protected PitPlayer pitPlayer;
    protected BossBar bossBar;
    protected BukkitTask particleRunnable;
    public boolean isInObjective = false;

    public List<BukkitTask> delayedTasks = new ArrayList<>();

    public abstract Class<? extends Tutorial> getTutorialClass();
    public abstract void sendStartMessages();
    public abstract int getStartTicks();
    public abstract void sendCompletionMessages();
    public abstract int getCompletionTicks();
    public abstract void onObjectiveComplete(TutorialObjective objective);
    public abstract boolean isActive();
    public abstract void onStart();
    public abstract void onTutorialEnd();

    public abstract String getProceedMessage();

    @Deprecated
    public Tutorial(FileConfiguration playerData) {
        this.data = new TutorialData();

        this.data.hasStartedTutorial = playerData.getBoolean("tutorial.has-started");
        for(String string : playerData.getStringList("tutorial.completed-objectives")) {
            TutorialObjective objective = TutorialObjective.getByRefName(string);
            if(objective == null) continue;
            this.data.completedObjectives.add(objective);
        }
    }

    public Tutorial(TutorialData data, PitPlayer pitPlayer) {
        this.data = data;
        this.pitPlayer = pitPlayer;
    }

    public void attemptStart() {
        this.uuid = pitPlayer.uuid;

        if(!isActive()) return;
        System.out.println(3);
        onStart();

        sendStartMessages();
        if(!data.hasStartedTutorial) {
            isInObjective = true;
            data.hasStartedTutorial = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    isInObjective = false;
                    updateBossBar();
                    startRunnable();
                    Sounds.TUTORIAL_MESSAGE.play(pitPlayer.player);
                }
            }.runTaskLater(Thepit.INSTANCE, getStartTicks());
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateBossBar();
                    startRunnable();
                }
            }.runTaskLater(Thepit.INSTANCE, 20);
        }
    }

    public void completeObjective(TutorialObjective objective, long delay) {
        if(isCompleted(objective)) return;
        isInObjective = true;

        if(objective == TutorialObjective.TAINTED_WELL) Misc.logToDiscord(DiscordLogChannel.TUTORIAL_LOG_CHANNEL, pitPlayer.player.getName() + " 已完成了被污染的井的工作!");

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                data.completedObjectives.add(objective);
                updateBossBar();
                AOutput.send(pitPlayer.player, "&a&l教程!&7 已完成目标: " + objective.display);
                onObjectiveComplete(objective);
                Sounds.LEVEL_UP.play(pitPlayer.player);

                if(data.completedObjectives.size() == getObjectiveSize()) {
                    if(particleRunnable != null) particleRunnable.cancel();
                    Misc.logToDiscord(DiscordLogChannel.TUTORIAL_LOG_CHANNEL, pitPlayer.player.getName() + " 已完成教程!");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            AOutput.send(pitPlayer.player, "&a&l教程完成!");
                            Sounds.LEVEL_UP.play(pitPlayer.player);
                            onTutorialEnd();
                        }
                    }.runTaskLater(Thepit.INSTANCE, getCompletionTicks() + 20);

                    sendCompletionMessages();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Audience audience = Thepit.adventure.player(uuid);
                            audience.hideBossBar(bossBar);
                        }
                    }.runTaskLater(Thepit.INSTANCE, getCompletionTicks() + 60);
                }
                isInObjective = false;
            }
        };

        if(delay == 0) runnable.run();
        else runnable.runTaskLater(Thepit.INSTANCE, delay);
    }

    public boolean isCompleted(TutorialObjective objective) {
        return data.completedObjectives.contains(objective);
    }

    public void updateBossBar() {
        Audience audience = Thepit.adventure.player(uuid);
        audience.hideBossBar(bossBar);

        Component name = Component.text(ChatColor.translateAlternateColorCodes('&', "&a&l目标：&7与高亮区域交互 &7("
                + data.completedObjectives.size() + "/" + getObjectiveSize() + ")"));
        float progress = ((float) data.completedObjectives.size()) / (float) getObjectiveSize();

        bossBar = BossBar.bossBar(name, progress, BossBar.Color.PINK, BossBar.Overlay.PROGRESS);

        audience.showBossBar(bossBar);
    }

    public int getObjectiveSize() {
        return TutorialObjective.getObjectives(getTutorialClass()).size();
    }

    public void sendMessage(String text, long ticks) {
        delayedTasks.add(new BukkitRunnable() {
            @Override
            public void run() {
                Sounds.TUTORIAL_MESSAGE.play(pitPlayer.player);
                AOutput.send(pitPlayer.player, text);
            }
        }.runTaskLater(Thepit.INSTANCE, ticks));
    }

    private void startRunnable() {

        particleRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(!pitPlayer.player.isOnline()) {
                    cancel();
                    return;
                }
                List<TutorialObjective> tutorialObjectives = new ArrayList<>(TutorialObjective.getObjectives(getTutorialClass()));
                tutorialObjectives.removeAll(data.completedObjectives);
                for(TutorialObjective objective : tutorialObjectives) {
                    if(Math.random() < 0.4) continue;
                    TutorialObjective.ParticleBox particleBox = objective.getParticleBox();

                    Location location = particleBox.location.clone();
                    int repetitions = (int) (particleBox.height + particleBox.length + particleBox.width) / 2;

                    for(int i = 0; i < repetitions; i++) {
                        Location finalLocation = location.clone().add(Misc.randomOffset(particleBox.length), Math.random() * particleBox.height,
                                Misc.randomOffset(particleBox.width));
                        pitPlayer.player.playEffect(finalLocation, Effect.HAPPY_VILLAGER, 1);
                    }
                }
            }
        }.runTaskTimer(Thepit.INSTANCE, 0L, 2L);
    }

    public void endTutorial() {
        if(!isActive()) return;

        Audience audience = Thepit.adventure.player(uuid);
        audience.hideBossBar(bossBar);
        if(particleRunnable != null) particleRunnable.cancel();
        for(BukkitTask runnable : delayedTasks) runnable.cancel();
        bossBar = null;
        onTutorialEnd();
    }

    public Player getPlayer() {
        return pitPlayer.player;
    }

    public void delayTask(Runnable task, long ticks) {
        delayedTasks.add(new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLater(Thepit.INSTANCE, ticks));
    }

}
