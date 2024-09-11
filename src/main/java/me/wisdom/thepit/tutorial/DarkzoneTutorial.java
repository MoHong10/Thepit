package me.wisdom.thepit.tutorial;

import me.wisdom.thepit.Thepit;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.darkzone.altar.AltarPedestal;
import me.wisdom.thepit.misc.Misc;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

public class DarkzoneTutorial extends Tutorial {

    public TutorialNPC tutorialNPC;

    public DarkzoneTutorial(PitPlayer pitPlayer) {
        super(pitPlayer.darkzoneTutorialData, pitPlayer);

        tutorialNPC = null;
    }

    @Override
    public Class<? extends Tutorial> getTutorialClass() {
        return DarkzoneTutorial.class;
    }

    @Override
    public void sendStartMessages() {
        if (data.completedObjectives.size() == 0) {
            sendMessage("&e你好！欢迎来到 &5黑暗区&e！", 60);
            sendMessage("&e在你离开 &f出生点&e 之前，我会告诉你所有需要知道的内容！", 110);
        } else if (TutorialManager.isOnLastObjective(pitPlayer.player)) {
            delayTask(() -> {
                sendNPCToLastCheckpoint();
                sendMessage(getProceedMessage(), 0);
            }, 60);

        } else {
            sendMessage("&e欢迎回来到 &5黑暗区&e！", 60);
            sendMessage("&e我会带你去上次遗漏的区域。", 110);
        }
        sendMessage("&e在 &a绿色高亮区域 &e走动一下，我会解释它们的用途！", 160);
    }

    @Override
    public int getStartTicks() {
        return 160;
    }

    @Override
    public void sendCompletionMessages() {
        sendMessage("&e这就是我能展示的全部内容！你已经准备好探索 &5黑暗区&e 了！", 40);
        sendMessage("&e如果你需要帮助，可以随时在聊天中询问或加入 &f&ndiscord.pitsim.net&e。", 100);
        sendMessage("&e现在，前往 &c怪物洞穴 &e开始获得 &f灵魂&e。", 160);
        sendMessage("&e祝你旅途愉快！", 220);
    }

    @Override
    public int getCompletionTicks() {
        return 220;
    }

    @Override
    public void onObjectiveComplete(TutorialObjective objective) {
        if (TutorialManager.isOnLastObjective(pitPlayer.player)) {

            NPCCheckpoint checkpoint = TutorialManager.getCheckpoint(TutorialObjective.MONSTER_CAVES);
            assert checkpoint != null;
            tutorialNPC.walkToCheckPoint(checkpoint);

            Misc.applyPotionEffect((LivingEntity) tutorialNPC.npc.getEntity(), PotionEffectType.SPEED, 999999, 1, false, false);
        }
    }

    @Override
    public boolean isActive() {
        return pitPlayer.prestige >= 5 && data.completedObjectives.size() < getObjectiveSize() && Thepit.status.isDarkzone();
    }

    @Override
    public void onStart() {
        tutorialNPC = new TutorialNPC(this);
    }

    @Override
    public void onTutorialEnd() {
        for (AltarPedestal.AltarReward value : AltarPedestal.AltarReward.values()) {
            value.restorePlayer(pitPlayer.player);
        }

        if (tutorialNPC != null) {
            tutorialNPC.remove();
            tutorialNPC = null;
        }
    }

    @Override
    public String getProceedMessage() {
        if (TutorialManager.isOnLastObjective(pitPlayer.player)) {
            return "&6&n来找我吧，在出生点外的 &c&n怪物洞穴 &6&n完成你的最终目标！";
        } else return "&6&n继续探索其他区域吧。我会在那儿等你！";
    }

    public void sendNPCToLastCheckpoint() {
        NPCCheckpoint checkpoint = TutorialManager.getCheckpoint(TutorialObjective.MONSTER_CAVES);
        assert checkpoint != null;
        tutorialNPC.walkToCheckPoint(checkpoint);

        Misc.applyPotionEffect((LivingEntity) tutorialNPC.npc.getEntity(), PotionEffectType.SPEED, 999999, 1, false, false);
    }
}