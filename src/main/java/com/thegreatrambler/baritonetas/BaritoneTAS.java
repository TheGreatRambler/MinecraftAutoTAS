package com.thegreatrambler.baritonetas;

import baritone.api.*;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BaritoneTAS.MODID)
public final class BaritoneTAS {
	public static final String MODID  = "baritonetas";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	static final Minecraft minecraft = Minecraft.getInstance();

	private TASCreator tasCreator;

	public BaritoneTAS() {
		LOGGER.debug("Hello BaritoneTAS!");

		tasCreator = new TASCreator();

		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		final IEventBus modEventBus
			= FMLJavaModLoadingContext.get().getModEventBus();

		// Register Deferred Registers (Does not need to be before Configs)
		/*
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModContainerTypes.CONTAINER_TYPES.register(modEventBus);
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);
		ModTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
		// Register Configs (Does not need to be after Deferred Registers)
		modLoadingContext.registerConfig (ModConfig.Type.CLIENT,
		ConfigHolder.CLIENT_SPEC); modLoadingContext.registerConfig
		(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
		*/

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void init(FMLCommonSetupEvent event) {
		configureDefaultBaritoneSettings();
	}

	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent event) {
		event.getDispatcher().register(
			Commands.literal("createtas").executes(command -> {
				tasCreator.startNewTAS();

				return 0;
			}));
	}

	@SubscribeEvent
	public void chatMessage(ClientChatEvent event) { }

	private void configureDefaultBaritoneSettings() {
		final Settings baritoneSettings = BaritoneAPI.getSettings();
		// Allow sprinting
		baritoneSettings.allowSprint.value = true;
		// Maximum fall without water bucket
		baritoneSettings.maxFallHeightNoWater.value = 5;
		// Maximum fall with water bucket
		baritoneSettings.maxFallHeightBucket.value = 100;
		// Walk while breaking blocks in front of you
		baritoneSettings.walkWhileBreaking.value            = true;
		baritoneSettings.allowParkour.value                 = true;
		baritoneSettings.allowParkourPlace.value            = true;
		baritoneSettings.sprintAscends.value                = true;
		baritoneSettings.rightClickContainerOnArrival.value = true;
		baritoneSettings.enterPortal.value                  = true;
		baritoneSettings.exploreForBlocks.value             = true;
		// TODO add more
	}
}
