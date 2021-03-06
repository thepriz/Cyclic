package com.lothrazar.cyclic.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IHasClickToggle {

  public final static String NBT_STATUS = "onoff";

  public void toggle(PlayerEntity player, ItemStack held);

  public boolean isOn(ItemStack held);
}
