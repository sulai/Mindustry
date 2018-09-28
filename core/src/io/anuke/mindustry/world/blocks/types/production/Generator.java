package io.anuke.mindustry.world.blocks.types.production;

import com.badlogic.gdx.utils.Array;

import io.anuke.ucore.util.Bundles;

public abstract class Generator extends Emitter {
	
	public Generator(String name) {
		super(name);
	}
	
	@Override
	public void getStats(Array<String> list){
		super.getStats(list);
		list.add("[powerinfo]Power Generation/second: " + getPowerGenerationPerSecond());
	}
	
	@Override
	public CharSequence getKeyStat() {
		return Bundles.get("text.blocks.powersecond")+": " + getPowerGenerationPerSecond();
	}
	
	public abstract String getPowerGenerationPerSecond();
	
}
