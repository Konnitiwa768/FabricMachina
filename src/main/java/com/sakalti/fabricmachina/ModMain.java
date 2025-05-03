package com.sakalti.fabricmachina;

import net.fabricmc.api.ModInitializer;

import com.sakalti.fabricmachina.registry.ModBlocks;
import com.sakalti.fabricmachina.registry.ModBlockEntities;

public class ModMain implements ModInitializer {
    public static final String MOD_ID = "fabricmachina";

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModBlockEntities.register();
    }
}
