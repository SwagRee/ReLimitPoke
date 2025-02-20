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


public class LimitPokeRideInWorldListener implements Listener {

    @EventHandler
    public void onPokeSendOutEvent(ForgeEvent e) {
        if (e.getForgeEvent() instanceof RidePokemonEvent) {
            boolean enable = YmlUtil.limitPokeEventWorld.getBoolean("limitRidePoke.enable");
            if(!enable){
                return;
            }
            RidePokemonEvent forgeEvent = (RidePokemonEvent) e.getForgeEvent();
            UUID uniqueID = forgeEvent.player.getUniqueID();
            Player player = Bukkit.getPlayer(uniqueID);
            List<String> worlds = YmlUtil.limitPokeEventWorld.getStringList("limitRidePoke.world");
            for (String world : worlds) {
                if(world.equals(player.getWorld().getName())){
                    forgeEvent.setCanceled(true);
                    player.sendMessage(YmlUtil.limitPokeEventWorld.getString("limitRidePoke.message").replace("&","§"));
                    break;
                }
            }
        }
    }


}
