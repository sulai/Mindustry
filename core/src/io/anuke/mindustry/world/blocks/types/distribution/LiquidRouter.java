package io.anuke.mindustry.world.blocks.types.distribution;

import java.util.ArrayList;
import java.util.List;

import io.anuke.mindustry.resource.Liquid;
import io.anuke.mindustry.world.Tile;
import io.anuke.mindustry.world.blocks.types.LiquidAcceptor;
import io.anuke.ucore.graphics.Draw;

public class LiquidRouter extends Conduit{

	public LiquidRouter(String name) {
		super(name);
		rotate = false;
		solid = true;
		liquidCapacity = 80f;
	}
	
	@Override
	public void update(Tile tile){
		LiquidEntity entity = tile.entity();
		
		// even out potential between this and all accepting pipes
		List<Tile> nearby = new ArrayList<>(4);
		for(byte rotation=0; rotation<4; rotation++) {
			if( tile.getExtra() != rotation ) {
				Tile next = tile.getNearby(rotation);
				if( next != null && next.block() instanceof LiquidAcceptor ) {
					nearby.add(next);
				}
			}
		}
		
		if( nearby.size() > 0 ) {
			float totalLiquid = entity.liquidAmount;
			for( Tile next : nearby ) {
				totalLiquid+=((LiquidAcceptor)next.block()).getLiquid(next);
			}
			float targetPotential = totalLiquid / (nearby.size()+1);
			for( Tile next : nearby ) {
				float amount = targetPotential - ((LiquidAcceptor)next.block()).getLiquid(next);
				if(amount>0)
					tryMoveLiquid(tile, next, amount);
			}
		}
		
	}
	
	@Override
	public float handleLiquid(Tile tile, Tile source, Liquid liquid, float amount) {
		// don't move liquids back to supplier
		tile.setExtra(tile.relativeTo(source.x, source.y));
		return super.handleLiquid(tile, source, liquid, amount);
	}
	
	@Override
	public void draw(Tile tile){
		LiquidEntity entity = tile.entity();
		Draw.rect(name(), tile.worldx(), tile.worldy());
		
		if(entity.liquid == null) return;
		
		Draw.color(entity.liquid.color);
		Draw.alpha(entity.liquidAmount / liquidCapacity);
		Draw.rect("blank", tile.worldx(), tile.worldy(), 2, 2);
		Draw.color();
	}

}
