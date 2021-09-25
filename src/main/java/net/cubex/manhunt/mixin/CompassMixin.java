package net.cubex.manhunt.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(CompassItem.class)
public class CompassMixin {
	
	//removes mechanic where if there is no lodestone at the place the compass is pointing to, it will stop pointing to it
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		
		return;
	}
}
