package com.thegreatrambler.baritonetas;

import baritone.api.*;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.pathing.calc.*;
import baritone.api.pathing.goals.*;
import baritone.api.pathing.path.*;
import baritone.api.utils.*;
import baritone.api.utils.input.Input;
import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.exceptions.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.*;
import java.util.stream.Collectors;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraftforge.event.TickEvent.*;

public class TASCreator {
	static final Minecraft minecraft = Minecraft.getInstance();

	private enum SpeedrunStates {
		NONE,
		START_TAS,
		RUNNING,
	}

	public final List<Block> Woods = Arrays.asList(new Block[] {
		Blocks.ACACIA_LOG,
		Blocks.BIRCH_LOG,
		Blocks.DARK_OAK_LOG,
		Blocks.JUNGLE_LOG,
		Blocks.OAK_LOG,
		Blocks.SPRUCE_LOG,
		Blocks.STRIPPED_ACACIA_WOOD,
		Blocks.STRIPPED_BIRCH_WOOD,
		Blocks.STRIPPED_DARK_OAK_WOOD,
		Blocks.STRIPPED_JUNGLE_WOOD,
		Blocks.STRIPPED_OAK_WOOD,
		Blocks.STRIPPED_SPRUCE_WOOD,
	});

	private SpeedrunStates state = SpeedrunStates.NONE;

	public TASCreator() { }

	public void onTick(WorldTickEvent event) {
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

		if(state == SpeedrunStates.START_TAS) {
			System.out.println(
				"Oh no: " + Boolean.toString(minecraft.isOnExecutionThread()));
			// Run on main thread
			minecraft.runImmediately(() -> {
				BaritoneAPI.getProvider()
					.getPrimaryBaritone()
					.getMineProcess()
					.mine(2, new BlockOptionalMetaLookup(Woods));
			});
			state = SpeedrunStates.RUNNING;
			return;
		}

		if(state == SpeedrunStates.RUNNING) {
			if(pathingBehavior.isPathing()) {
				inputOverrideHandler.setInputForceState(Input.JUMP, true);
			} else {
				inputOverrideHandler.setInputForceState(Input.JUMP, false);
			}
			return;
		}
	}

	public void startNewTAS() {
		minecraft.player.sendChatMessage("Starting TAS");
		state = SpeedrunStates.START_TAS;
	}
}
