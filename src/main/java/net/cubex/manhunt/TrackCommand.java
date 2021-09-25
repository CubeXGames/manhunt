package net.cubex.manhunt;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Texts;

public class TrackCommand {
	
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		
		System.out.println("yay at least this is working");
		
		dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("track").requires((source) -> {
			return source.hasPermissionLevel(0);
		})).then((RequiredArgumentBuilder)CommandManager.argument("player", EntityArgumentType.player()).executes((context) -> {
			return execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayer(context, "player"));
		}))));
	}
	
	public static int execute(ServerCommandSource source, ServerPlayerEntity target) {
		
		try {
			
			Main.trackedPlayers.put(source.getPlayer().getEntityName(), target.getEntityName());
			
			source.getPlayer().networkHandler.sendPacket(new OverlayMessageS2CPacket(Texts.toText(new Message() {
				
				@Override
				public String getString() {
					
					return "§2Your compass is now tracking " + target.getEntityName() + ". Right click it to point it to them.";
				}
			})));
			
			if(!source.getPlayer().getInventory().contains(new ItemStack(Items.COMPASS)))
				source.getPlayer().giveItemStack(new ItemStack(Items.COMPASS));
		} catch (CommandSyntaxException e) { return 0; }
		
		return 1;
	}
}
