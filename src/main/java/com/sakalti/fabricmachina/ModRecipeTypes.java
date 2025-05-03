package com.sakalti.fabricmachina.registry;

import com.sakalti.fabricmachina.recipe.ElectronicShapeRecipe;
import com.google.gson.JsonObject;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

public class ModRecipeTypes {
    public static final RecipeType<ElectronicShapeRecipe> ELECTRONIC_SHAPE =
        Registry.register(Registry.RECIPE_TYPE, new Identifier("fabricmachina", "electronic_shape"), new RecipeType<>() {});

    public static final RecipeSerializer<ElectronicShapeRecipe> ELECTRONIC_SHAPE_SERIALIZER =
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("fabricmachina", "electronic_shape"),
            new RecipeSerializer<>() {
                @Override
                public ElectronicShapeRecipe read(Identifier id, JsonObject json) {
                    Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "input"));
                    ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
                    return new ElectronicShapeRecipe(id, input, output);
                }

                @Override
                public JsonObject toJson(ElectronicShapeRecipe recipe) {
                    // JSON書き出し不要
                    return new JsonObject();
                }

                @Override
                public ElectronicShapeRecipe read(Identifier id, PacketByteBuf buf) {
                    return null;
                }

                @Override
                public void write(PacketByteBuf buf, ElectronicShapeRecipe recipe) {
                }
            });
}
