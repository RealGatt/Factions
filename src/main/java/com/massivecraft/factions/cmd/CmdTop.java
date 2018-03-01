package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionTopHelper;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CmdTop extends FCommand {

    public CmdTop() {
        super();
        this.aliases.add("top");
        this.aliases.add("t");

        //this.requiredArgs.add("");
        this.optionalArgs.put("criteria", "powerDEFAULT");
        this.optionalArgs.put("page", "1");

        this.permission = Permission.TOP.node;
        this.disableOnLock = false;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        // Can sort by: money, members, online, allies, enemies, power, land.
        // Get all Factions and remove non player ones.
        String criteria = argAsString(0);
        Bukkit.getPlayer(sender.getName()).chat("/ftop " + criteria);


    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TOP_DESCRIPTION;
    }
}
