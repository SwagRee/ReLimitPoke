package io.github.swagree.relimitpoke.PokeListener;


import catserver.api.bukkit.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import io.github.swagree.relimitpoke.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class ListenerBreed implements Listener {
    @EventHandler
    public void onEgg(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BreedEvent.MakeEgg) {
            BreedEvent.MakeEgg forgeEvent = (BreedEvent.MakeEgg) event.getForgeEvent();

            Pokemon pokemon = forgeEvent.getEgg().makeEgg();


            if(getTotalV(pokemon)>= getConfigV("Breed.unBreed.V")){
                pokemon.addSpecFlag("unbreedable");
                getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.unBreed.message"));
            }
            if(getTotalV(pokemon)>= getConfigV("Breed.bind.V")){
                pokemon.addSpecFlag("untradeable");
                getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.bind.message"));
            }

            Pokemon parent1 = forgeEvent.parent1;
            if(getTotalV(parent1)>= getConfigV("Breed.unBreedFamily.V")){
                parent1.addSpecFlag("unbreedable");
                getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.unBreedFamily.message"));
            }

            Pokemon parent2 = forgeEvent.parent2;
            if(getTotalV(parent2)>= getConfigV("Breed.unBreedFamily.V")){
                parent1.addSpecFlag("unbreedable");
                getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.unBreedFamily.message"));
            }


            if(getConfigBoolean("Breed.Once.enable")){

                if(isOnceHeldItem(parent1)){
                    getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.Once.message"));

                }
                if(isOnceHeldItem(parent2)){
                    getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.Once.message"));

                }
            }
            if(!Bukkit.getPlayer(forgeEvent.owner).hasPermission("")){

                if(getConfigStringList("Breed.blackListMakeEgg.list").contains(pokemon.getLocalizedName())) {
                    getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.blackListMakeEgg.message").replace("%pokemon%", pokemon.getLocalizedName()));
                    forgeEvent.setCanceled(true);
                }
            }
        }
        if(event.getForgeEvent() instanceof BreedEvent.AddPokemon){
            BreedEvent.AddPokemon forgeEvent = (BreedEvent.AddPokemon) event.getForgeEvent();
            Pokemon pokemon = forgeEvent.pokemon;
            if(getTotalV(pokemon)>=getConfigV("Breed.joinRanch.V")){
                List<String> configStringList = getConfigStringList("Breed.joinRanch.whitelist");
                if(configStringList.contains(pokemon.getLocalizedName()) || configStringList.contains(pokemon.getUnlocalizedName())){
                    return;
                }
                forgeEvent.setCanceled(true);
                getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.joinRanch.message"));
            }

            Boolean configBoolean = getConfigBoolean("Breed.mtBreed.enable");
            if(configBoolean){
                if(pokemon.getAbilitySlot() == 2){
                    forgeEvent.setCanceled(true);
                    getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.mtBreed.message"));
                }
            }

            if(!Bukkit.getPlayer(forgeEvent.owner).hasPermission("")){
                if(getConfigStringList("Breed.blackListJoin.list").contains(pokemon.getLocalizedName())){
                    forgeEvent.setCanceled(true);
                    getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.blackListJoin.message").replace("%pokemon%",pokemon.getLocalizedName()));
                }
            }
            if(getConfigBoolean("Breed.onlyOwner.enable")){
                String playerName1 = getBreedEventPlayer(forgeEvent).getName();
                String playerName2 = pokemon.getOriginalTrainer();

                if(!playerName1.equals(playerName2)){
                    getBreedEventPlayer(forgeEvent).sendMessage(getConfigStringMessage("Breed.onlyOwner.message").replace("%pokemon%",pokemon.getLocalizedName()));
                    forgeEvent.setCanceled(true);
                }
            }
        }
    }

    private static Player getBreedEventPlayer(BreedEvent.AddPokemon forgeEvent) {
        return Bukkit.getPlayer(forgeEvent.owner);
    }

    private static Player getBreedEventPlayer(BreedEvent.MakeEgg forgeEvent) {
        return Bukkit.getPlayer(forgeEvent.owner);
    }

    private static String getConfigString(String path) {
        return Main.plugin.getConfig().getString(path);
    }

    private static String getConfigStringMessage(String path) {
        return Main.plugin.getConfig().getString(path).replace("&","§");
    }

    private static List<String> getConfigStringList(String path) {
        return Main.plugin.getConfig().getStringList(path);
    }

    private static Integer getConfigV(String path) {
        return Main.plugin.getConfig().getInt(path);
    }

    private static Boolean getConfigBoolean(String path) {
        return Main.plugin.getConfig().getBoolean(path);
    }


    private boolean isOnceHeldItem(Pokemon pokemon) {
        ItemHeld heldItemAsItemHeld = pokemon.getHeldItemAsItemHeld();
        EnumHeldItems heldItemType = heldItemAsItemHeld.getHeldItemType();
        if(heldItemType == EnumHeldItems.destinyKnot){
            pokemon.setHeldItem(null);
            return true;
        }

        return false;
    }


    private int getTotalV(Pokemon pokemon) {
        int totalV = 0;
        if (pokemon.getIVs().getStat(StatsType.HP) == 31) totalV++;
        if (pokemon.getIVs().getStat(StatsType.Attack) == 31) totalV++;
        if (pokemon.getIVs().getStat(StatsType.SpecialAttack) == 31) totalV++;
        if (pokemon.getIVs().getStat(StatsType.Defence) == 31) totalV++;
        if (pokemon.getIVs().getStat(StatsType.SpecialDefence) == 31) totalV++;
        if (pokemon.getIVs().getStat(StatsType.Speed) == 31) totalV++;
        return totalV;
    }


}
