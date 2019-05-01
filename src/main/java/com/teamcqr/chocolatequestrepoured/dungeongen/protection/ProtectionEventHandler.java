package com.teamcqr.chocolatequestrepoured.dungeongen.protection;

import com.teamcqr.chocolatequestrepoured.API.events.CQDungeonStructureGenerateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Copyright (c) 30.04.2019
 * Developed by MrMarnic
 * GitHub: https://github.com/MrMarnic
 */
public class ProtectionEventHandler {

    @SubscribeEvent
    public void dungeonGenerate(CQDungeonStructureGenerateEvent e) {
        if(e.getDungeon().isProtectedFromModifications()) {
        	ProtectionHandler.PROTECTION_HANDLER.addRegion(new ProtectedRegion(e.getSize().getX(),e.getSize().getY(),e.getSize().getZ(),e.getPos()));
        }
    }

    @SubscribeEvent
    public void save(ChunkDataEvent.Save e) {
        ProtectionHandler.PROTECTION_HANDLER.save(e);

    }

    @SubscribeEvent
    public void load(ChunkDataEvent.Load e) {
        ProtectionHandler.PROTECTION_HANDLER.load(e);
    }

    @SubscribeEvent
    public void livingSpawn(LivingSpawnEvent.CheckSpawn e) {
        ProtectionHandler.PROTECTION_HANDLER.checkSpawn(e);
    }
}
