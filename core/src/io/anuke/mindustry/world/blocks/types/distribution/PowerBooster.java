package io.anuke.mindustry.world.blocks.types.distribution;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import io.anuke.mindustry.world.Tile;
import io.anuke.mindustry.world.blocks.types.PowerAcceptor;
import io.anuke.mindustry.world.blocks.types.production.Emitter;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.graphics.Lines;
import io.anuke.ucore.util.Strings;

import static io.anuke.mindustry.Vars.tilesize;
import static io.anuke.mindustry.Vars.world;

public class PowerBooster extends Emitter {
	protected final int timerGenerate = timers++;
	
	public int powerRange = 4;

	public PowerBooster(String name) {
		super(name);
		explosive = false;
		hasLasers = false;
		powerSpeed = 0.4f;
	}

	@Override
	public void drawSelect(Tile tile){
		super.drawSelect(tile);

		Draw.color(Color.YELLOW);
		Lines.dashCircle(tile.worldx(), tile.worldy(), powerRange * tilesize);
		Draw.reset();
	}

	@Override
	public void drawPlace(int x, int y, int rotation, boolean valid){
		Draw.color(Color.PURPLE);
		Lines.stroke(1f);
		Lines.dashCircle(x * tilesize, y * tilesize, powerRange * tilesize);
		Draw.reset();
	}

	@Override
	public void getStats(Array<String> list){
		super.getStats(list);
		list.add("[powerinfo]Power Range: " + powerRange + " tiles");
		list.add("[powerinfo]Max power transfer/second: " + Strings.toFixed(powerSpeed * 60, 2));
	}

	@Override
	public void update(Tile tile){
		distributePower(tile);
	}

	@Override
	public void drawLayer(Tile tile){}

	@Override
	public boolean acceptsPower(Tile tile){
		PowerEntity entity = tile.entity();

		return entity.power + 0.001f <= powerCapacity;
	}

	protected void distributePower(Tile tile){
		PowerEntity p = tile.entity();
		
		if(!p.timer.get(timerGenerate, powerTime)){
			return;
		}
		
		// get list of valid power acceptors
		List<Tile> acceptors = new ArrayList<>();
		for(int x = -powerRange; x <= powerRange; x++){
			for(int y = -powerRange; y <= powerRange; y++){

				if(x == 0 && y == 0){
					continue;
				}

				if(Vector2.dst(x, y, 0, 0) < powerRange){
					Tile dest = world.tile(tile.x + x, tile.y + y);
					if(dest != null
							&& dest.block() instanceof PowerAcceptor
							&& ((PowerAcceptor) dest.block()).acceptsPower(dest)
							&& !(dest.block() instanceof Emitter)) { // do not distribute to other power lasers
						
						acceptors.add(dest);
						
					}
				}
			}
		}
		
		// evenly distribute power to all acceptors, flow limited by powerSpeed
		if( acceptors.size() > 0 ) {
			float transmit = Math.min(powerSpeed * Timers.delta(), p.power);
			float amount = transmit / acceptors.size();
			for( Tile acceptor : acceptors ) {
				PowerAcceptor block = (PowerAcceptor) acceptor.block();
				p.power -= block.addPower(acceptor, amount);
			}
		}
		
	}
}
