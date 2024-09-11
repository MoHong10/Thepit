package me.wisdom.thepit.killstreaks;

import me.wisdom.thepit.controllers.ItemFactory;
import me.wisdom.thepit.controllers.objects.Killstreak;
import me.wisdom.thepit.controllers.objects.PitPlayer;
import me.wisdom.thepit.items.misc.VeryYummyBread;
import me.wisdom.thepit.items.misc.YummyBread;
import me.wisdom.thepit.megastreaks.RNGesus;
import me.wisdom.thepit.misc.Sounds;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import me.wisdom.thepitapi.misc.AOutput;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Baker extends Killstreak {
    public static Baker INSTANCE;

    public Baker() {
        super("Baker", "Baker", 15, 22);
        INSTANCE = this;
    }

    @Override
    public void proc(Player player) {
        PitPlayer pitPlayer = PitPlayer.getPitPlayer(player);
        if(pitPlayer.getMegastreak() instanceof RNGesus && pitPlayer.getKills() >= RNGesus.INSTABILITY_THRESHOLD) {
            AOutput.error(player, "&c&lUNSTABLE!&7 Baker cannot be used in this reality");
            return;
        }

        double random = Math.random();
        if(random > 0.9) ItemFactory.getItem(VeryYummyBread.class).giveItem(player, 1);
        else ItemFactory.getItem(YummyBread.class).giveItem(player, 1);
        player.updateInventory();
        Sounds.BREAD_GIVE.play(player);
    }

    @Override
    public void reset(Player player) {
    }

    @Override
    public ItemStack getDisplayStack(Player player) {
        AItemStackBuilder builder = new AItemStackBuilder(Material.BREAD)
                .setName("&e" + displayName)
                .setLore(new ALoreBuilder(
                        "&7Every: &c" + killInterval + " kills",
                        "",
                        "&7Obtain either a &6Yummy bread &7or",
                        "&6Very yummy bread&7. (Lost on death)"
                ));

        return builder.getItemStack();
    }

    @Override
    public String getSummary() {
        return "&eBaker&7 is a killstreak that gives you one of two types of bread every &c15 kills&7: &eyummy bread, " +
                "which increases your &cdamage &7against bots (and stacks), and &6very yummy bread&7, which &cheals&7 " +
                "you and gives &9absorption&7";
    }
}
