package com.thegreatrambler.baritonetas;

import baritone.api.*;
import java.lang.reflect.Method;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

// TODO use https://www.geeksforgeeks.org/robot-class-java-awt/
public class CraftingHelper {
	// https://github.com/AdvancedMacros/AdvancedMacros/blob/1.15.2/src/main/java/com/theincgi/advancedMacros/lua/functions/Action.java
	static final Method clickMouse = ObfuscationReflectionHelper.findMethod(
		Minecraft.class, "func_147116_af");
	static final Method rightClickMouse
		= ObfuscationReflectionHelper.findMethod(
			Minecraft.class, "func_147121_ag");
	static final Minecraft minecraft = Minecraft.getInstance();

	public CraftingHelper() {
		clickMouse.setAccessible(true);
		rightClickMouse.setAccessible(true);
	}

	public void craftItem(World world, ItemStack goalStack) {
		Screen currentScreen = minecraft.currentScreen;
		if(currentScreen instanceof CraftingScreen) {
			CraftingScreen crafting = (CraftingScreen)currentScreen;
			Optional<? extends IRecipe<?>> recipe
				= world.getRecipeManager().getRecipe(
					goalStack.getItem().getRegistryName());
			if(recipe.isPresent()) {
				crafting.getRecipeGui();
				for(Ingredient ingredient : recipe.get().getIngredients()) {
				}
			}
		}
	}
}