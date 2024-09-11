package me.wisdom.thepit.tutorial;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.wisdom.thepitapi.builders.AItemStackBuilder;
import me.wisdom.thepitapi.builders.ALoreBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class HelpItemStacks {

    public static ItemStack BASE_ITEMSTACK;

    static {
        BASE_ITEMSTACK = getCustomHead("badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a");
    }

    public static ItemStack getPerksItemStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&e特权、连击与大连击")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&a特权 &7是被动帮助你战斗的能力",
                        "&e连击 &7是在连击时激活的能力",
                        "&c大连击 &7塑造你的连击风格",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getKitsItemStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&d神秘物品套件")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&d神秘物品 &7是",
                        "&6&lPit&e&lSim&7 战斗的核心。你可以在这里",
                        "&7获取专为 &cPvP&7 和",
                        "&b连击&7 设计的套装。你可以尝试",
                        "&7其他 &e附魔组合 &7在",
                        "&d神秘井中",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getPrestigeItemStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&e声望与荣誉")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&7达到等级 &f[&b&l120&f]&7 后，",
                        "&7你可以进行 &e声望提升&7。这将重置",
                        "&7你的等级到 &f[&71&f]&7，并且清除",
                        "&7你的 &6金币。&7作为交换，你将获得 &e荣誉&7，",
                        "&7这可以用来在 &e荣誉商店&7 购买强大的",
                        "&7升级",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getKeeperItemStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&e守护者")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&7觉得中间的玩家太多了吗？&7在这里你可以前往其他",
                        "&7PitSim &2大厅 &7（如果它们开放的话）",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getAdminEditItemStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&e背包编辑")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&7右侧的区域是 &b护甲",
                        "&7下方的行是 &e快捷栏",
                        "&7右侧可以访问 &5末影箱",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getMainProgressionStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&e主要进展")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&7通过解锁通往不同技能树的路径来解锁",
                        "&7对其的访问权限，从信标开始。每棵技能树",
                        "&7提供各种升级和解锁，你可以在解锁后点击",
                        "&7查看它们",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getProgressionStack() {
        return new AItemStackBuilder(BASE_ITEMSTACK.clone())
                .setName("&e进展")
                .setLore(new ALoreBuilder(
                        "&8&m----------------------------",
                        "&7从最左侧的解锁开始升级此技能分支，",
                        "&7并向右移动到最后的升级。要购买",
                        "&7最后的升级，你必须解锁两条路径",
                        "&8&m----------------------------"
                ))
                .getItemStack();
    }

    public static ItemStack getCustomHead(String url) {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        Field field = null;

        assert skullMeta != null;

        if(url.length() < 16) {

            skullMeta.setOwner(url);

            skull.setItemMeta(skullMeta);
            return skull;
        }

        StringBuilder s_url = new StringBuilder();
        s_url.append("https://textures.minecraft.net/texture/").append(url); // We get the texture link.

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null); // We create a GameProfile

        // We get the bytes from the texture in Base64 encoded that comes from the Minecraft-URL.
        byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", s_url.toString()).getBytes());

        // We set the texture property in the GameProfile.
        gameProfile.getProperties().put("textures", new Property("textures", new String(data)));

        try {

            field = skullMeta.getClass().getDeclaredField("profile"); // We get the field profile.

            field.setAccessible(true); // We set as accessible to modify.
            field.set(skullMeta, gameProfile); // We set in the skullMeta the modified GameProfile that we created.

        } catch(Exception e) {
            e.printStackTrace();
        }

        skull.setItemMeta(skullMeta);

        return skull; //Finally, you have the custom head!

    }

}
