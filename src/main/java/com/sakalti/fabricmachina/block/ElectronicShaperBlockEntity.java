package com.sakalti.fabricmachina.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import com.sakalti.fabricmachina.energy.MachinaEnergy;
import com.sakalti.fabricmachina.recipe.ElectronicShapeRecipe;
import com.sakalti.fabricmachina.registry.ModBlockEntities;
import com.sakalti.fabricmachina.registry.ModRecipeTypes;

import java.util.Optional;

public class ElectronicShaperBlockEntity extends BlockEntity {
    private ItemStack input = ItemStack.EMPTY;
    private int progress = 0;
    private final MachinaEnergy energy = new MachinaEnergy();

    public ElectronicShaperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRONIC_SHAPER, pos, state);
    }

    public void tick() {
        if (world == null || world.isClient || input.isEmpty()) return;

        if (energy.getRf() >= 70 && energy.getTq() >= 9) {
            progress++;
            energy.consumeRf(70);

            if (progress >= 100) {
                RecipeManager manager = world.getRecipeManager();
                Optional<ElectronicShapeRecipe> match = manager.getFirstMatch(ModRecipeTypes.ELECTRONIC_SHAPE, new SingleStackInventory(input), world);

                if (match.isPresent()) {
                    ItemStack result = match.get().getOutput().copy();
                    Block.dropStack(world, pos, result);
                }

                input = ItemStack.EMPTY;
                progress = 0;
            }
        } else {
            progress = 0;
        }
    }

    public boolean onUse(ItemStack stack) {
        if (input.isEmpty()) {
            input = stack.copyWithCount(1);
            stack.decrement(1);
            return true;
        }
        return false;
    }

    public MachinaEnergy getEnergy() {
        return energy;
    }
}
