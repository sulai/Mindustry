package io.anuke.mindustry.world.blocks.types;

import io.anuke.mindustry.resource.Liquid;
import io.anuke.mindustry.world.Tile;

public interface LiquidAcceptor{
	
	public float handleLiquid(Tile tile, Tile source, Liquid liquid, float amount);
	public float getLiquid(Tile tile);
	public float getLiquidCapacity(Tile tile);
}
