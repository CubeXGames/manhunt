package net.cubex.manhunt.mixin;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.brigadier.Message;
import com.mojang.serialization.DataResult;

import org.spongepowered.asm.mixin.injection.At;

import net.cubex.manhunt.Main;
import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Inject(method = "interactItem(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;"
			+ "Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
	public void interactItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> info) {
		
		if(stack.getItem() == Items.COMPASS) {
			
			if(Main.trackedPlayers.containsKey(player.getEntityName())) {
				
				ServerPlayerEntity trackedPlayer = world.getServer().getPlayerManager().getPlayer(Main.trackedPlayers.get(player.getEntityName()));
				if(player.getServerWorld().getDimension() != trackedPlayer.getServerWorld().getDimension()) {
					
					player.networkHandler.sendPacket(new OverlayMessageS2CPacket(Texts.toText(new Message() {
						
						@Override
						public String getString() {
							
							return "§c" + trackedPlayer.getEntityName() + " is not in your current dimension!";
						}
					})));
					
					info.setReturnValue(ActionResult.FAIL);
					return;
				}
				
				NbtCompound nbt = stack.getOrCreateNbt();
				
				nbt.put("LodestonePos", NbtHelper.fromBlockPos(trackedPlayer.getBlockPos()));
				DataResult<NbtElement> ennbeetee = World.CODEC.encodeStart(NbtOps.INSTANCE, world.getRegistryKey());
				Logger logger = LOGGER;
				Objects.requireNonNull(logger);
				ennbeetee.resultOrPartial(logger::error).ifPresent((nbtElement) -> {
					nbt.put("LodestoneDimension", nbtElement);
				});
				nbt.putBoolean("LodestoneTracked", true);
				
				player.networkHandler.sendPacket(new OverlayMessageS2CPacket(Texts.toText(new Message() {
					
					@Override
					public String getString() {
						
						return "The compass is tracking " + trackedPlayer.getEntityName();
					}
				})));
			}
			
			info.setReturnValue(ActionResult.SUCCESS);
		}
	}
}