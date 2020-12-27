package com.thegreatrambler.baritonetas;

import baritone.api.*;
import baritone.api.pathing.goals.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;

public class TASCreator {
	static final Minecraft minecraft = Minecraft.getInstance();

	public final Block[] Woods = new Block[] {
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
	};
	public final ArrayList<Block> WoodsList
		= new ArrayList<>(Arrays.asList(Woods));

	public TASCreator() { }

	public void startNewTAS() {
		minecraft.player.sendChatMessage("Starting TAS");

		// Mine blocks to
		BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().mine(
			Woods);
	}
}
