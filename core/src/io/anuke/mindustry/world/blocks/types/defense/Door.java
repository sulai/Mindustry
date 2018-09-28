package io.anuke.mindustry.world.blocks.types.defense;

import com.badlogic.gdx.math.Rectangle;
import io.anuke.mindustry.entities.TileEntity;
import io.anuke.mindustry.graphics.Fx;
import io.anuke.mindustry.world.Block;
import io.anuke.mindustry.world.Tile;
import io.anuke.mindustry.world.blocks.types.Wall;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;
import io.anuke.ucore.entities.Entities;
import io.anuke.ucore.entities.EntityGroup;
import io.anuke.ucore.entities.SolidEntity;
import io.anuke.ucore.graphics.Draw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static io.anuke.mindustry.Vars.*;

public class Door extends Wall{
	protected final Rectangle rect = new Rectangle();

	protected Effect openfx = Fx.dooropen;
	protected Effect closefx = Fx.doorclose;

	public Door(String name) {
		super(name);
		solid = false;
		solidifes = true;
		update = true;
	}
	
	@Override
	public void draw(Tile tile){
		DoorEntity entity = tile.entity();
		
		if(!entity.open){
			Draw.rect(name, tile.drawx(), tile.drawy());
		}else{
			Draw.rect(name + "-open", tile.drawx(), tile.drawy());
		}
	}
	
	@Override
	public boolean isSolidFor(Tile tile){
		DoorEntity entity = tile.entity();
		return !entity.open;
	}

	@Override
	public void tapped(Tile tile){
		DoorEntity entity = tile.entity();
		
		// don't close if locked by any entity
		if( doorLocked(tile) && entity.open){
			return;
		}
		
		entity.open = !entity.open;
		if(!entity.open){
			Effects.effect(closefx, tile.drawx(), tile.drawy());
		}else{
			Effects.effect(openfx, tile.drawx(), tile.drawy());
		}
	}
	
	@Override
	public void update(Tile tile) {
		DoorEntity entity = tile.entity();
		// automatic door: open and close when a player is close
		if( anyEntityNearby(tile, playerGroup, tilesize)) {
			if( !entity.open ) {
				tapped(tile);
			}
		}
		else if( entity.open ) {
			tapped(tile);
		}
	}
	
	boolean doorLocked(Tile tile){
		return anyEntityNearby(tile, enemyGroup, 0)
				|| anyEntityNearby(tile, playerGroup, 0);
		
	}
	
	private boolean anyEntityNearby(Tile tile, EntityGroup<?> group, int r) {
		int x = tile.x, y = tile.y;
		Block type = tile.block();
		rect.setSize(type.width * tilesize, type.height * tilesize);
		rect.setCenter(tile.drawx(), tile.drawy());
		
		for(SolidEntity e : Entities.getNearby(group, x * tilesize-r, y * tilesize-r, tilesize * 2f+r)){
			Rectangle rect = e.hitbox.getRect(e.x, e.y);

			if(this.rect.overlaps(rect)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public TileEntity getEntity(){
		return new DoorEntity();
	}
	
	public class DoorEntity extends TileEntity{
		public boolean open = false;
		
		@Override
		public void write(DataOutputStream stream) throws IOException{
			super.write(stream);
			stream.writeBoolean(open);
		}
		
		@Override
		public void read(DataInputStream stream) throws IOException{
			super.read(stream);
			open = stream.readBoolean();
		}
	}

}
