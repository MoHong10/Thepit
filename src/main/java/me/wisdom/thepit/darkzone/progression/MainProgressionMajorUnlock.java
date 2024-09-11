package me.wisdom.thepit.darkzone.progression;

import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.enums.UnlockState;
import org.bukkit.inventory.ItemStack;

public class MainProgressionMajorUnlock extends MainProgressionUnlock {
    public SkillBranch skillBranch;

    public MainProgressionMajorUnlock(Class<? extends SkillBranch> clazz, int index) {
        super(ProgressionManager.getSkillBranch(clazz).getRefName(), ProgressionManager.branchSlots.get(index).guiXPos,
                ProgressionManager.branchSlots.get(index).guiYPos);
        this.skillBranch = ProgressionManager.getSkillBranch(clazz);
    }

    @Override
    public ItemStack getDisplayStack(PitPlayer pitPlayer, UnlockState unlockState) {
        return skillBranch.getMainDisplayStack(pitPlayer, this, unlockState);
    }

    @Override
    public String getDisplayName() {
        return skillBranch.getDisplayName();
    }
}
