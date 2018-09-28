package io.anuke.mindustry.world.blocks.types.production;

import com.badlogic.gdx.utils.Array;
import io.anuke.mindustry.entities.TileEntity;
import io.anuke.mindustry.graphics.Fx;
import io.anuke.mindustry.resource.Liquid;
import io.anuke.mindustry.world.Tile;
import io.anuke.mindustry.world.blocks.types.LiquidAcceptor;
import io.anuke.mindustry.world.blocks.types.LiquidBlock;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.Strings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LiquidPowerGenerator extends Generator implements LiquidAcceptor{
	public int generateTime = 15;
	public Liquid generateLiquid;
	public float powerPerLiquid = 0.13f;
	/**Maximum liquid used per frame.*/
	public float maxLiquidGenerate = 0.4f;
	public float liquidCapacity = 30f;
	public Effect generateEffect = Fx.generatespark;

	public LiquidPowerGenerator(String name) {
		super(name);
		outputOnly = true;
	}
	
	@Override
	public void getStats(Array<String> list){
		super.getStats(list);
		list.add("[liquidinfo]Liquid Capacity: " + (int)liquidCapacity);
		list.add("[liquidinfo]Power/Liquid: " + Strings.toFixed(powerPerLiquid, 2) + " power/liquid");
		list.add("[liquidinfo]Max liquid/second: " + Strings.toFixed(maxLiquidGenerate * 60f, 2) + " liquid/s");
		list.add("[liquidinfo]Input: " + generateLiquid);
	}
	
	@Override
	public String getPowerGenerationPerSecond() {
		return Strings.toFixed(maxLiquidGenerate*60f*powerPerLiquid, 2);
	}
	
	@Override
	public void draw(Tile tile){
		super.draw(tile);
		
		LiquidPowerEntity entity = tile.entity();
		
		if(entity.liquidEntity.liquid == null) return;
		
		Draw.color(entity.liquidEntity.liquid.color);
		Draw.alpha(entity.liquidEntity.liquidAmount / liquidCapacity);
		drawLiquidCenter(tile);
		Draw.color();
	}
	
	public void drawLiquidCenter(Tile tile){
		Draw.rect("blank", tile.drawx(), tile.drawy(), 2, 2);
	}
	
	@Override
	public void update(Tile tile){
		LiquidPowerEntity entity = tile.entity();
		
		if(entity.liquidEntity.liquidAmount > 0){
			float used = Math.min(entity.liquidEntity.liquidAmount, maxLiquidGenerate * Timers.delta());
			used = Math.min(used, (powerCapacity - entity.power)/powerPerLiquid);
			
			entity.liquidEntity.liquidAmount -= used;
			entity.power += used * powerPerLiquid;
			
			if(used > 0.001f && Mathf.chance(0.05 * Timers.delta())){
				
				Effects.effect(generateEffect, tile.drawx() + Mathf.range(3f), tile.drawy() + Mathf.range(3f));
			}
		}
		
		distributeLaserPower(tile);
		
	}
	
	@Override
	public TileEntity getEntity(){
		return new LiquidPowerEntity();
	}
	
	@Override
	public float handleLiquid(Tile tile, Tile source, Liquid liquid, float amount){
		
		// needs liquid to run, but its not the right type
		if( generateLiquid!=null && liquid!=generateLiquid )
			return 0;
		
		return LiquidBlock.handleLiquid(liquidCapacity, ((LiquidPowerEntity)tile.entity()).liquidEntity, liquid, amount);
	}
	
	@Override
	public float getLiquid(Tile tile){
		LiquidPowerEntity entity = tile.entity();
		return entity.liquidEntity.liquidAmount;
	}

	@Override
	public float getLiquidCapacity(Tile tile){
		return liquidCapacity;
	}
	
	public static class LiquidPowerEntity extends PowerEntity{
		
		public LiquidBlock.LiquidEntity liquidEntity = new LiquidBlock.LiquidEntity();
		
		@Override
		public void write(DataOutputStream stream) throws IOException{
			super.write(stream);
			liquidEntity.write(stream);
		}
		
		@Override
		public void read(DataInputStream stream) throws IOException{
			super.read(stream);
			liquidEntity.read(stream);
		}
	}

}
