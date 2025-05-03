package com.sakalti.fabricmachina.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.registry.ModBlockEntities;
import com.sakalti.fabricmachina.FabricMachinaMod;

public class ModBlocks {
    public static final Block WATER_GENERATOR = new WaterGeneratorBlock(FabricBlockSettings.of(Material.STONE).strength(1.5F, 6.0F));
    public static final Block CRUSHER = new CrusherBlock(FabricBlockSettings.of(Material.STONE).strength(2.0F, 6.0F));
    public static final Block POWER_PIPE = new PowerPipeBlock(FabricBlockSettings.of(Material.STONE).strength(1.0F, 2.0F));
    public static final Block THERMAL_GENERATOR = new ThermalGeneratorBlock(FabricBlockSettings.of(Material.STONE).strength(3.0F, 9.0F));
    public static final Block ELECTRONIC_SHAPER = new ELECTRONIC_SHAPERBlock(FabricBlockSettings.of(Material.STONE).strength(3.0F, 9.0F));
    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(FabricMachinaMod.MODID, "water_generator"), WATER_GENERATOR);
        Registry.register(Registry.BLOCK, new Identifier(FabricMachinaMod.MODID, "crusher"), CRUSHER);
        Registry.register(Registry.BLOCK, new Identifier(FabricMachinaMod.MODID, "power_pipe"), POWER_PIPE);
        Registry.register(Registry.BLOCK, new Identifier(FabricMachinaMod.MODID, "thermal_generator"), THERMAL_GENERATOR);
        Registry.register(Registry.BLOCK, new Identifier(FabricMachinaMod.MODID, "electronic_shaper"), ELECTRONIC_SHAPER);
    }

    public static void registerBlockItems() {
        Registry.register(Registry.ITEM, new Identifier(FabricMachinaMod.MODID, "water_generator"),
                new BlockItem(WATER_GENERATOR, new Item.Settings()));
        Registry.register(Registry.ITEM, new Identifier(FabricMachinaMod.MODID, "crusher"),
                new BlockItem(CRUSHER, new Item.Settings()));
        Registry.register(Registry.ITEM, new Identifier(FabricMachinaMod.MODID, "power_pipe"),
                new BlockItem(POWER_PIPE, new Item.Settings()));
        Registry.register(Registry.ITEM, new Identifier(FabricMachinaMod.MODID, "thermal_generator"),
                new BlockItem(THERMAL_GENERATOR, new Item.Settings()));
       Registry.register(Registry.ITEM, new Identifier(FabricMachinaMod.MODID, "electronic_shaper"),
                new BlockItem(ELECTRONIC_SHAPER, new Item.Settings()));
    }
}
