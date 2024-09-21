package me.wisdom.thepit.helmetabilities;

import me.wisdom.thepit.controllers.LevelManager;
import me.wisdom.thepit.controllers.objects.HelmetAbility;
import me.wisdom.thepit.controllers.objects.HelmetManager;
import me.wisdom.thepit.events.AttackEvent;
import me.wisdom.thepit.events.KillEvent;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class GoldRushAbility extends HelmetAbility {
    public static final int GOLD_RUSH_COST = 1_000;
    public static final double GOLD_REQ_MULTIPLIER = 2.0;
    public static final DecimalFormat formatter = new DecimalFormat("#,###");

    public GoldRushAbility(Player player) {
        super(player, "Gold Rush", "goldrush", true, 12);
    }

    @EventHandler
    public void onAttack(AttackEvent.Apply attackEvent) {
        if(!isActive || player != attackEvent.getAttacker()) return;
//		GoldenHelmet goldenHelmet = HelmetListeners.getHelmetInstance(attackEvent.attacker);
//		assert goldenHelmet != null;
//		if(!goldenHelmet.withdrawGold(1200)) {
//			AOutput.error(attackEvent.attacker,"&cNot enough gold!");
//			goldenHelmet.deactivate();
//			Sounds.NO.play(player);
//			return;
//		}
        Sounds.GOLD_RUSH.play(attackEvent.getAttacker());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(KillEvent killEvent) {
        if(!isActive || player != killEvent.getKiller()) return;

        ItemStack goldenHelmet = HelmetManager.getHelmet(killEvent.getKiller());
        assert goldenHelmet != null;
        if(!HelmetManager.withdrawGold(player, goldenHelmet, GOLD_RUSH_COST)) {
            AOutput.error(player, "&cNot enough gold!");
            HelmetManager.deactivate(player);
            Sounds.NO.play(player);
        }

        LevelManager.addGoldReq(player, (int) killEvent.getFinalGold() * (GOLD_REQ_MULTIPLIER - 1));
    }

    @Override
    public void onActivate() {
        ItemStack goldenHelmet = HelmetManager.getHelmet(player);
        assert goldenHelmet != null;

        Sounds.HELMET_ACTIVATE.play(player);
        AOutput.send(player, "&6&lGOLDEN HELMET! &aActivated &9Gold Rush&7. (&6-" + GOLD_RUSH_COST + "g&7 per kill)");
    }

    @Override
    public boolean shouldActivate() {
        ItemStack goldenHelmet = HelmetManager.getHelmet(player);

        assert goldenHelmet != null;
        if(!HelmetManager.withdrawGold(player, goldenHelmet, GOLD_RUSH_COST)) {
            AOutput.error(player, "&cNot enough gold!");
            Sounds.NO.play(player);
            return false;
        }
        return true;
    }

    @Override
    public void onDeactivate() {
        AOutput.send(player, "&6&lGOLDEN HELMET! &cDeactivated &9Gold Rush&c.");
    }

    @Override
    public void onProc() {
    }

    @Override
    public List<String> getDescription() {
        DecimalFormat multiplierFormat = new DecimalFormat("#.#");
        return Arrays.asList("&7Double-Sneak to toggle Gold",
                "&7Rush. Gold requirement multiplied",
                "&7on kill by &6x" + multiplierFormat.format(GOLD_REQ_MULTIPLIER),
                "&7Cost: &6" + formatter.format(GOLD_RUSH_COST) + "g &7per kill");
    }

    @Override
    public ItemStack getDisplayStack() {
        AItemStackBuilder builder = new AItemStackBuilder(Material.GOLD_INGOT);
        builder.setName("&e" + name);
        ALoreBuilder loreBuilder = new ALoreBuilder();
        loreBuilder.addLore(getDescription());
        builder.setLore(loreBuilder);

        return builder.getItemStack();
    }
}
