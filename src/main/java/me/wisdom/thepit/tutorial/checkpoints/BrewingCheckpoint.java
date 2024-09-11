package me.wisdom.thepit.tutorial.checkpoints;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.tutorial.Midpoint;
import me.wisdom.thepit.tutorial.NPCCheckpoint;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import org.bukkit.Location;

public class BrewingCheckpoint extends NPCCheckpoint {
    public BrewingCheckpoint() {
        super(TutorialObjective.BREWING, new Location(MapManager.getDarkzone(),
                218, 91, -99.5, 13, 0), Midpoint.SPAWN1);
    }

    @Override
    public void onCheckpointEngage(Tutorial tutorial) {
        tutorial.sendMessage("&e这里是 &d&l酿造区域&e！", 0);
        tutorial.sendMessage("&e在这里你可以使用 &c怪物掉落物 &e来酿造 &d药水&e。", 60);
        tutorial.sendMessage("&e目前药水有 &f10 种独特效果 &e可用于 &a主世界 &e和 &5黑暗区域&e。", 120);
        tutorial.sendMessage("&e药水系统将很快进行重做，请耐心等待。", 180);
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
