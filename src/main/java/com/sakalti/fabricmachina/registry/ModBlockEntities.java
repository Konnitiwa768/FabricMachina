package com.sakalti.fabricmachina.registry;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.sakalti.fabricmachina.ModMain;
import com.sakalti.fabricmachina.block.*;

public class ModBlockEntities {
    public static BlockEntityType<WaterGeneratorBlock.GeneratorEntity> WATER_GENERATOR;
    public static BlockEntityType<CrusherBlock.CrusherEntity> CRUSHER;
    public static BlockEntityType<PowerPipeBlock.PipeEntity> POWER_PIPE;

    public static void register() {
        WATER_GENERATOR = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(ModMain.MOD_ID, "water_generator"),
            BlockEntityType.Builder.create(WaterGeneratorBlock.GeneratorEntity::new, ModBlocks.WATER_GENERATOR).build(null)
        );
        CRUSHER = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(ModMain.MOD_ID, "crusher"),
            BlockEntityType.Builder.create(CrusherBlock.CrusherEntity::new, ModBlocks.CRUSHER).build(null)
        );
        POWER_PIPE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(ModMain.MOD_ID, "power_pipe"),
            BlockEntityType.Builder.create(PowerPipeBlock.PipeEntity::new, ModBlocks.POWER_PIPE).build(null)
        );
    }
}
