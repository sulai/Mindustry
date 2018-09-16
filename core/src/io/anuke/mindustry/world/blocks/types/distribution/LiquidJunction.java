package io.anuke.mindustry.world.blocks.types.distribution;

import io.anuke.mindustry.resource.Liquid;
import io.anuke.mindustry.world.Tile;
import io.anuke.mindustry.world.blocks.types.LiquidBlock;
import io.anuke.ucore.graphics.Draw;

public class LiquidJunction extends Conduit{

	public LiquidJunction(String name) {
		super(name);
		update = true;
		solid = true;
		rotate = false;
	}
	
	@Override
	public void draw(Tile tile){
		Draw.rect(name(), tile.worldx(), tile.worldy());
	}
	
	@Override
	public float handleLiquid(Tile dest, Tile source, Liquid liquid, float amount){
		int dir = source.relativeTo(dest.x, dest.y);
		dir = (dir+4)%4;
		Tile to = dest.getNearby(dir);

		if(to!=null && to.block() instanceof LiquidBlock)
			return ((LiquidBlock)to.block()).handleLiquid(to, dest, liquid, amount);
		return 0;
	}

}
