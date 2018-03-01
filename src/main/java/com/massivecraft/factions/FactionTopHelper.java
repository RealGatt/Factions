package com.massivecraft.factions;

import com.google.common.collect.Iterables;
import com.massivecraft.factions.integration.Econ;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FactionTopHelper {

    public static FPlayer getPlayerSlot(Faction f, int slot){

        FPlayer rtn = null;

        if (f.getFPlayersWhereOnline(true).size() >= slot+1){
            rtn = Iterables.get(f.getFPlayersWhereOnline(true), slot);
        }else{
            slot -= f.getFPlayersWhereOnline(true).size();
            if (f.getFPlayersWhereOnline(false).size() >= slot+1) {
                rtn = Iterables.get(f.getFPlayersWhereOnline(false), slot);
            }
        }

        return rtn;

    }

    public static ArrayList<Faction> getTopFactions(String criteria){
        ArrayList<Faction> factionList = Factions.getInstance().getAllFactions();
        factionList.remove(Factions.getInstance().getWilderness());
        factionList.remove(Factions.getInstance().getSafeZone());
        factionList.remove(Factions.getInstance().getWarZone());

        // TODO: Better way to sort?
        if (criteria.equalsIgnoreCase("members")) {
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    int f1Size = f1.getFPlayers().size();
                    int f2Size = f2.getFPlayers().size();
                    if (f1Size < f2Size) {
                        return 1;
                    } else if (f1Size > f2Size) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (criteria.equalsIgnoreCase("start")) {
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    long f1start = f1.getFoundedDate();
                    long f2start = f2.getFoundedDate();
                    // flip signs because a smaller date is farther in the past
                    if (f1start > f2start) {
                        return 1;
                    } else if (f1start < f2start) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (criteria.equalsIgnoreCase("power")) {
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    int f1Size = f1.getPowerRounded();
                    int f2Size = f2.getPowerRounded();
                    if (f1Size < f2Size) {
                        return 1;
                    } else if (f1Size > f2Size) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (criteria.equalsIgnoreCase("land")) {
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    int f1Size = f1.getLandRounded();
                    int f2Size = f2.getLandRounded();
                    if (f1Size < f2Size) {
                        return 1;
                    } else if (f1Size > f2Size) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (criteria.equalsIgnoreCase("online")) {
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    int f1Size = f1.getFPlayersWhereOnline(true).size();
                    int f2Size = f2.getFPlayersWhereOnline(true).size();
                    if (f1Size < f2Size) {
                        return 1;
                    } else if (f1Size > f2Size) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else if (criteria.equalsIgnoreCase("money") || criteria.equalsIgnoreCase("balance") || criteria.equalsIgnoreCase("bal")) {
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    double f1Size = Econ.getBalance(f1.getAccountId());
                    // Lets get the balance of /all/ the players in the Faction.
                    for (FPlayer fp : f1.getFPlayers()) {
                        f1Size = f1Size + Econ.getBalance(fp.getAccountId());
                    }
                    double f2Size = Econ.getBalance(f2.getAccountId());
                    for (FPlayer fp : f2.getFPlayers()) {
                        f2Size = f2Size + Econ.getBalance(fp.getAccountId());
                    }
                    if (f1Size < f2Size) {
                        return 1;
                    } else if (f1Size > f2Size) {
                        return -1;
                    }
                    return 0;
                }
            });
        }else if (criteria.equalsIgnoreCase("spawners") || criteria.equalsIgnoreCase("spawner") || criteria.equalsIgnoreCase("spawn")){
            Collections.sort(factionList, new Comparator<Faction>() {
                @Override
                public int compare(Faction f1, Faction f2) {
                    int f1Size = getFactionSpawners(f1);
                    int f2Size = getFactionSpawners(f2);
                    if (f1Size < f2Size) {
                        return 1;
                    } else if (f1Size > f2Size) {
                        return -1;
                    }
                    return 0;
                }
            });
        } else {
            return null;
        }

        return factionList;
    }

    public static double getFactionBalance(Faction f1){
        double f1Size = Econ.getBalance(f1.getAccountId());
        for (FPlayer fp : f1.getFPlayers()) {
            f1Size = f1Size + Econ.getBalance(fp.getAccountId());
        }
        return f1Size;
    }

    public static int getFactionSpawners(Faction f1){
        int spawnerCount = 0;
        for (FLocation l : f1.getAllClaims()) {
            Chunk convertedLoc = l.getWorld().getChunkAt(Math.round(l.getX()), Math.round(l.getZ()));
            for (BlockState te : convertedLoc.getTileEntities()){
                if (te.getBlock().getType() == Material.MOB_SPAWNER){
                    spawnerCount++;
                }
            }


        }
        return spawnerCount;
    }

    public static Faction getFactionSpot(String criteria, int spot){
        return getTopFactions(criteria).get(spot < 0 ? 0 : spot);
    }

    public static String getFactionSpotName(String criteria, int spot){
        return getTopFactions(criteria).get(spot < 0 ? 0 : spot).getId();
    }

}
