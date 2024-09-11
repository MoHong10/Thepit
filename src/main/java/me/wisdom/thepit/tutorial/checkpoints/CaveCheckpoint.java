package me.wisdom.thepit.tutorial.checkpoints;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.tutorial.Midpoint;
import me.wisdom.thepit.tutorial.NPCCheckpoint;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import org.bukkit.Location;

public class CaveCheckpoint extends NPCCheckpoint {
    public CaveCheckpoint() {
        super(TutorialObjective.MONSTER_CAVES, new Location(MapManager.getDarkzone(),
                276.5, 91, -117.5, 0, 0), Midpoint.ENTRANCE, Midpoint.SPAWN1, Midpoint.SPAWN2, Midpoint.EXIT, Midpoint.PATH1, Midpoint.PATH2);
    }

    @Override
    public void onCheckpointEngage(Tutorial tutorial) {
        tutorial.sendMessage("&e欢迎来到 &c怪物洞窟&e！", 0);
        tutorial.sendMessage("&e在这里你可以击败怪物来获得 &f灵魂&e。", 60);
        tutorial.sendMessage("&e洞窟分为 &410 级&e，每一层都有独特的 &4BOSS 战斗&e。", 120);
        tutorial.sendMessage("&e使用在这里获得的 &f灵魂 &e回到 &5出生点 &e以通过不同的 &4等级&e。", 180);
        tutorial.sendMessage("&e与我互动以继续。", 240);
    }

    @Override
    public void onCheckpointSatisfy(Tutorial tutorial) {

    }

    @Override
    public int getEngageDelay() {
        return 240;
    }

    @Override
    public int getSatisfyDelay() {
        return 0;
    }

    @Override
    public boolean canEngage(Tutorial tutorial) {
        return true;
    }

    @Override
    public boolean canSatisfy(Tutorial tutorial) {
        return true;
    }

    @Override
    public void onCheckPointDisengage(Tutorial tutorial) {

    }
}
