package me.wisdom.thepit.tutorial.checkpoints;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.progression.MainProgressionPanel;
import me.wisdom.thepit.darkzone.progression.MainProgressionUnlock;
import me.wisdom.thepit.darkzone.progression.ProgressionManager;
import me.wisdom.thepit.tutorial.Midpoint;
import me.wisdom.thepit.tutorial.NPCCheckpoint;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProgressionCheckpoint extends NPCCheckpoint {
    public ProgressionCheckpoint() {
        super(TutorialObjective.PROGRESSION, new Location(MapManager.getDarkzone(),
                190.5, 91, -86.5, 13, 0), Midpoint.SPAWN1);
    }

    @Override
    public void onCheckpointEngage(Tutorial tutorial) {
        tutorial.sendMessage("&e这里是 &5&l暗区进度&e 商店！", 0);
        tutorial.sendMessage("&e这将是你在 &5暗区&e 解锁内容的主要方式。", 60);
        tutorial.sendMessage("&e你可以使用 &6升级 &e来购买 &f污浊灵魂&e，这些灵魂来自击杀 &c怪物 &e和 &4首领&e。", 120);
        tutorial.sendMessage("&e去解锁前两个标记为 &a&l免费！&e 的 &6升级。", 180);
        tutorial.sendMessage("&e完成后再来找我。", 240);
        tutorial.sendMessage("&6&n如果你已经解锁了这些，请再来找我。", 300);
    }

    @Override
    public void onCheckpointSatisfy(Tutorial tutorial) {
        tutorial.sendMessage("&e做得好！", 0);
        tutorial.sendMessage("&e这是你将花费 &f灵魂&e 的主要地方，所以你很快会回到这里！", 60);
    }

    @Override
    public int getEngageDelay() {
        return 300;
    }

    @Override
    public int getSatisfyDelay() {
        return 60;
    }

    @Override
    public boolean canEngage(Tutorial tutorial) {
        return true;
    }

    @Override
    public boolean canSatisfy(Tutorial tutorial) {
        Player player = tutorial.getPlayer();
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);

        for(MainProgressionUnlock tutorialUnlock : MainProgressionPanel.tutorialUnlocks) {
            if(!ProgressionManager.isUnlocked(pitPlayer, tutorialUnlock)) return false;
        }

        return true;
    }

    @Override
    public void onCheckPointDisengage(Tutorial tutorial) {

    }
}
