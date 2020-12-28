package com.thegreatrambler.baritonetas;

import baritone.api.*;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.pathing.calc.*;
import baritone.api.pathing.path.*;
import baritone.api.utils.*;
import baritone.api.utils.input.Input;
import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.exceptions.*;
import java.util.*;
import java.util.regex.*;
import net.minecraft.client.*;
import net.minecraft.command.*;
import net.minecraft.command.arguments.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.TickEvent.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;

@Mod(BaritoneTAS.MODID)
public final class BaritoneTAS {
	private enum SpeedrunStates {
		GET_WOOD,
	}

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

				return 1;
			}));
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		IPathingBehavior pathingBehavior = BaritoneAPI.getProvider()
											   .getPrimaryBaritone()
											   .getPathingBehavior();
		IInputOverrideHandler inputOverrideHandler
			= BaritoneAPI.getProvider()
				  .getPrimaryBaritone()
				  .getInputOverrideHandler();

		IPath current = pathingBehavior.hasPath()
							? pathingBehavior.getCurrent().getPath()
							: null;

		if(pathingBehavior.isPathing()) {
			inputOverrideHandler.setInputForceState(Input.JUMP, true);
		} else {
			inputOverrideHandler.setInputForceState(Input.JUMP, false);
		}
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
