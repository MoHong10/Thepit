package me.wisdom.thepit.tutorial;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import org.bukkit.configuration.file.FileConfiguration;

public class OverworldTutorial extends Tutorial {

    public OverworldTutorial(PitPlayer pitPlayer) {
        super(pitPlayer.overworldTutorialData, pitPlayer);
    }

    @Deprecated
    public OverworldTutorial(FileConfiguration playerData) {
        super(playerData);
    }

    @Override
    public Class<? extends Tutorial> getTutorialClass() {
        return OverworldTutorial.class;
    }

    @Override
    public void sendStartMessages() {
        sendMessage("&e你好！欢迎来到 &6&lPit&e&lSim&e！", 0);
        sendMessage("&e在你开始之前，我们需要讲解一些基础知识。", 20 * 2);
        sendMessage("&e与出生点周围的各种NPC互动，以了解如何玩这个游戏", 20 * 6);
    }

    @Override
    public int getStartTicks() {
        return 20 * 4;
    }

    @Override
    public void sendCompletionMessages() {
        sendMessage("&e如果你忘记了任何信息，每个NPC的右下角都有帮助菜单。", 90);
        sendMessage("&e你也可以加入我们的Discord服务器 &f&ndiscord.pitsim.net &efor 以获取更多帮助。", 150);
        sendMessage("&e以上就是全部内容，祝你在服务器上玩得愉快！", 210);
    }

    @Override
    public int getCompletionTicks() {
        return 210;
    }

    @Override
    public void onObjectiveComplete(TutorialObjective objective) {

    }

    @Override
    public boolean isActive() {
        return pitPlayer.prestige <= 1 && data.completedObjectives.size() < getObjectiveSize() && Thepit.status.isOverworld();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onTutorialEnd() {

    }

    @Override
    public String getProceedMessage() {
        return null;
    }
}
