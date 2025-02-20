package io.github.swagree.relimitpoke.PokeListener;


import catserver.api.bukkit.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.ThrowPokeballEvent;
import net.minecraft.item.ItemStack;
import org.bukkit.Bukkit;

import org.bukkit.block.DoubleChest;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


public class ListenerBind implements Listener {
    @EventHandler
    public void onBind(ForgeEvent event) {
        if (event.getForgeEvent() instanceof ThrowPokeballEvent) {
            ThrowPokeballEvent forgeEvent = (ThrowPokeballEvent) event.getForgeEvent();
            ItemStack itemStack = forgeEvent.itemStack;
            if (itemStack == null) {
                return;
            }
            org.bukkit.inventory.ItemStack bukkitCopy = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) (Object) itemStack);
            if (!(bukkitCopy.hasItemMeta() && bukkitCopy.getItemMeta().hasLore())) {
                return;
            }
            String lore = bukkitCopy.getItemMeta().getLore().get(0);
            if (lore.equalsIgnoreCase("§7已绑定")) {
                Player player = Bukkit.getPlayer(forgeEvent.player.getUniqueID());
                player.sendMessage("§c对不起，这个大师球绑定了 只能战斗中投掷");
                forgeEvent.setCanceled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        org.bukkit.inventory.ItemStack itemStack = event.getItemDrop().getItemStack();

        if (itemStack == null) {
            return;
        }
        if (!itemStack.hasItemMeta()) {
            return;
        }

        if (!itemStack.getItemMeta().hasLore()) {
            return;
        }
        if (itemStack.getItemMeta().getLore().get(0).equalsIgnoreCase("§7已绑定")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c对不起，不可以丢弃绑定的球");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // 检查是否为数字键点击
        if (event.getClick() == ClickType.NUMBER_KEY) {
            int hotbarButton = event.getHotbarButton(); // 获取数字键对应的热键栏位
            org.bukkit.inventory.ItemStack hotbarItem = event.getWhoClicked().getInventory().getItem(hotbarButton);// 获取热键栏位的物品
            if (hotbarItem != null && hotbarItem.hasItemMeta() && hotbarItem.getItemMeta().hasLore()) {
                if (hotbarItem.getItemMeta().getLore().get(0).equalsIgnoreCase("§7已绑定")) {
                    event.setCancelled(true); // 取消数字键操作
                    event.getWhoClicked().sendMessage("数字快捷键在这个容器中被禁用了！");
                    return;
                }
            }
        }

        org.bukkit.inventory.ItemStack cursorItem = event.getCursor();// 获取光标上的物品
        if (cursorItem != null && cursorItem.hasItemMeta() && cursorItem.getItemMeta().hasLore()) {
            if (cursorItem.getItemMeta().getLore().get(0).equalsIgnoreCase("§7已绑定")) {
                Inventory inventory = event.getInventory();
                InventoryHolder holder = inventory.getHolder();
                if (holder instanceof org.bukkit.block.BlockState || holder instanceof DoubleChest) {
                    event.setCancelled(true); // 取消放置物品的操作
                    event.getWhoClicked().sendMessage("你不能在这个容器放置绑定物品！");
                    return;
                }
            }
        }

        org.bukkit.inventory.ItemStack currentItem = event.getCurrentItem();// 获取当前点击的物品
        if (currentItem == null || !currentItem.hasItemMeta() || !currentItem.getItemMeta().hasLore()) {
            return;
        }

        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof org.bukkit.block.BlockState || holder instanceof DoubleChest) {
            if (currentItem.getItemMeta().getLore().get(0).equalsIgnoreCase("§7已绑定")) {
                event.setCancelled(true); // 取消放置物品的操作
                event.getWhoClicked().sendMessage("你不能在这个容器放置绑定物品！");
            }
        }
    }


    @EventHandler
    public void onPick(PlayerPickupItemEvent event) {
        org.bukkit.inventory.ItemStack itemStack = event.getItem().getItemStack();
        if (itemStack == null) {
            return;
        }
        if (!itemStack.hasItemMeta()) {
            return;
        }

        if (!itemStack.getItemMeta().hasLore()) {
            return;
        }
        if (itemStack.getItemMeta().getLore().get(0).equalsIgnoreCase("§7已绑定")) {
            event.setCancelled(true); // 取消放置物品的操作
            event.getPlayer().sendMessage("您不能拾取绑定物品，已自动删除绑定物品！");
            itemStack.setAmount(0);
        }
    }


}
