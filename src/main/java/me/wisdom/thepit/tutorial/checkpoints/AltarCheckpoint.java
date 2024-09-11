package me.wisdom.thepit.tutorial.checkpoints;

import me.wisdom.thepit.controllers.MapManager;
import me.wisdom.thepit.darkzone.altar.AltarPedestal;
import me.wisdom.thepit.tutorial.Midpoint;
import me.wisdom.thepit.tutorial.NPCCheckpoint;
import me.wisdom.thepit.tutorial.Tutorial;
import me.wisdom.thepit.tutorial.TutorialObjective;
import org.bukkit.Location;

public class AltarCheckpoint extends NPCCheckpoint {
    public AltarCheckpoint() {
        super(TutorialObjective.ALTAR, new Location(MapManager.getDarkzone(),
                218.5, 91, -88.5, 13, 0), Midpoint.SPAWN1);
    }

    @Override
    public void onCheckpointEngage(Tutorial tutorial) {
        tutorial.sendMessage("&e这里是 &4&l污染祭坛&e！", 0);
        tutorial.sendMessage("&e正如你所见，&4&l祭坛 &e会缓慢地窃取你的 &a主世界 &6金币 &e和 &b经验值&e。", 60);
        tutorial.sendMessage("&e为了恢复你的资源，向它献祭 &f灵魂 &e吧。", 120);
        tutorial.sendMessage("&e这样做，你还可能获得奖励，如 &4恶魔凭证 &e和声望。", 180);
        tutorial.sendMessage("&e右击 &3传送门框架 &e来打开 &4&l祭坛 &e并献祭一些 &f灵魂&e。", 240);
        tutorial.sendMessage("&e献祭完成后来找我。", 300);
    }

    @Override
    public void onCheckpointSatisfy(Tutorial tutorial) {
        tutorial.delayTask(() -> {
            for(AltarPedestal.AltarReward value : AltarPedestal.AltarReward.values()) {
                value.storedTemporaryReward.remove(tutorial.getPlayer().getUniqueId());
            }
        }, getSatisfyDelay());

        tutorial.sendMessage("&e干得好！", 0);
        tutorial.sendMessage("&e你可以稍后解锁 &3祭坛台座 &e以获得更多 &4祭坛经验值&e。", 60);
        tutorial.sendMessage("&e你可能需要每次声望时重新访问 &4&l祭坛&e。", 120);
    }

    @Override
    public int getEngageDelay() {
        return 300;
    }

    @Override
    public int getSatisfyDelay() {
        return 120;
    }

    @Override
    public boolean canEngage(Tutorial tutorial) {
        return true;
    }

    @Override
    public boolean canSatisfy(Tutorial tutorial) {
        return AltarPedestal.AltarReward.ALTAR_XP.storedTemporaryReward.containsKey(tutorial.getPlayer().getUniqueId());
    }

    @Override
    public void onCheckPointDisengage(Tutorial tutorial) {
        for(AltarPedestal.AltarReward value : AltarPedestal.AltarReward.values()) {
            value.restorePlayer(tutorial.getPlayer());
        }
    }
}
