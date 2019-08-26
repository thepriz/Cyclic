/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.net;

import java.util.List;
import java.util.function.Supplier;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.ItemScythe;
import com.lothrazar.cyclic.item.ItemScythe.ScytheType;
import com.lothrazar.cyclic.util.UtilScythe;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketScythe {

  private BlockPos pos;
  private ItemScythe.ScytheType type;
  private int radius;

  public PacketScythe() {}

  public PacketScythe(BlockPos mouseover, ItemScythe.ScytheType t, int r) {
    pos = mouseover;
    type = t;
    radius = r;
  }

  public static void handle(PacketScythe message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      World world = player.getEntityWorld();
      List<BlockPos> shape = ItemScythe.getShape(message.pos, message.radius);
      ModCyclic.LOGGER.info("shape " + shape.size());
      for (BlockPos posCurrent : shape) {
        UtilScythe.harvestSingle(world, player, posCurrent, message.type);
      }
    });
  }

  public static PacketScythe decode(PacketBuffer buf) {
    PacketScythe p = new PacketScythe();
    p.radius = buf.readInt();
    p.pos = buf.readBlockPos();
    p.type = ScytheType.values()[buf.readInt()];
    return p;
  }

  public static void encode(PacketScythe msg, PacketBuffer buf) {
    buf.writeInt(msg.radius);
    buf.writeBlockPos(msg.pos);
    buf.writeInt(msg.type.ordinal());
  }
}