package io.github.swagree.relimitpoke.PokeListener.limit;

import catserver.api.bukkit.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import io.github.swagree.relimitpoke.YmlUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PokeLimitNumReleaseListener implements Listener {
    private static HashMap<Player, List<Pokemon>> hashMap = new HashMap<>();

    @EventHandler
    public void onPokeSendOutEvent(ForgeEvent e) {
        if (e.getForgeEvent() instanceof PixelmonSendOutEvent) {
            boolean enable = YmlUtil.limitPokeEventWorld.getBoolean("limitSendOutNumWorld.enable");
            if(!enable){
                return;
            }

            List<String> worldList = YmlUtil.limitPokeEventWorld.getStringList("limitSendOutNumWorld.world");
            for (String w : worldList) {
                PixelmonSendOutEvent forgeEvent = (PixelmonSendOutEvent) e.getForgeEvent();


                Player player = Bukkit.getPlayer(forgeEvent.player.getUniqueID());


                if (player.getWorld().getName().equals(w)) {
                    Pokemon pokemon = forgeEvent.pokemon;
                    if (pokemon.getHealth() == 0) {
                        return;
                    }

                    if (!hashMap.containsKey(player)) {
                        hashMap.put(player, new ArrayList<>());
                    }

                    List<Pokemon> pokemons = hashMap.get(player);
                    String message = YmlUtil.limitPokeEventWorld.getString("limitSendOutNumWorld.message").replace("&", "§");
                    if (pokemons.contains(pokemon) && pokemon.getPixelmonIfExists() != null) {
                        pokemons.remove(pokemon);
                    }
                    if (!pokemons.contains(pokemon) && pokemon.getPixelmonIfExists() == null) {
                        pokemons.add(pokemon);
                        int put = pokemons.size();
                        int limit = YmlUtil.limitPokeEventWorld.getInt("limitSendOutNumWorld.num");
                        if (put > limit) {
                            pokemons.remove(pokemon);
                            e.getForgeEvent().setCanceled(true);
                            player.sendMessage(message);
                            return;
                        }
                    }
                    hashMap.put(player, pokemons);
                }
            }
        }
    }

    @EventHandler
    public void onPokeQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (hashMap.containsKey(player)) {
            hashMap.remove(player);
        }

    }
}
