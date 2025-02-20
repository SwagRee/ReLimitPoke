package io.github.swagree.relimitpoke;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandLimit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return false;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§e/rpc reload  §f- 重载插件");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"plugin reload relitmitpoke");
            sender.sendMessage("重载成功");
        }

        if (args[1].equalsIgnoreCase("bindMasterBall")) {
            String arg = args[0];
            int num = 0;
            try {
                num = Integer.valueOf(args[2]);
            }catch (Exception e){
                sender.sendMessage("您输入的不是数字");
            }

            Player getBindPokeBallPlayer = Bukkit.getPlayer(arg);

            Material material = Material.valueOf("PIXELMON_MASTER_BALL");
            ItemStack itemStack = new ItemStack(material);
            List<String> lores = new ArrayList<>();
            lores.add(0,"§7已绑定");
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(lores);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(num);

            getBindPokeBallPlayer.getInventory().addItem(itemStack);
            sender.sendMessage("§e成功发放绑定大师球");
            getBindPokeBallPlayer.sendMessage("§e成功获得绑定大师球");
        }
        return false;
    }
}
