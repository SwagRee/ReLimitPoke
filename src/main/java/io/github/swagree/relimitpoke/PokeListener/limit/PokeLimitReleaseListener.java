package io.github.swagree.relimitpoke.PokeListener.limit;

import catserver.api.bukkit.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.RidePokemonEvent;
import io.github.swagree.relimitpoke.YmlUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class PokeLimitReleaseListener implements Listener {


    @EventHandler
    public void onPokeRelease(ForgeEvent e) {
        if (e.getForgeEvent() instanceof RidePokemonEvent) {
            UUID uuid = ((RidePokemonEvent) e.getForgeEvent()).player.getUniqueID();
            Player player = Bukkit.getPlayer(uuid);
            boolean enable = YmlUtil.limitPokeEventWorld.getBoolean("limitSendOutWorld.enable");
            if(!enable){
                return;
            }
            List<String> pixelmonSendOutList = YmlUtil.limitPokeEventWorld.getStringList("limitRidePoke.world");
            for (String s : pixelmonSendOutList) {
                if (player.getWorld().getName().equals(s)) {
                    String message = YmlUtil.limitPokeEventWorld.getString("limitRidePoke.message").replace("&", "§");
                    player.sendMessage(message);
                    e.getForgeEvent().setCanceled(true);
                }
            }
        }
    }
}
