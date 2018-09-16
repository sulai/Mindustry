package io.anuke.mindustry.world.blocks.types.defense;

import io.anuke.mindustry.entities.TileEntity;
import io.anuke.mindustry.resource.Liquid;
import io.anuke.mindustry.world.Tile;
import io.anuke.mindustry.world.blocks.types.LiquidAcceptor;
import io.anuke.mindustry.world.blocks.types.LiquidBlock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class LiquidTurret extends Turret implements LiquidAcceptor{
    public Liquid ammoLiquid = Liquid.water;
    public float liquidCapacity = 20f;
    public float liquidPerShot = 1f;

    public LiquidTurret(String name) {
        super(name);
    }

    @Override
    public boolean hasAmmo(Tile tile){
        LiquidTurretEntity entity = tile.entity();
        return entity.liquidEntity.liquidAmount > liquidPerShot;
    }

    @Override
    public void consumeAmmo(Tile tile){
        LiquidTurretEntity entity = tile.entity();
        entity.liquidEntity.liquidAmount -= liquidPerShot;
    }

    @Override
    public float handleLiquid(Tile tile, Tile source, Liquid liquid, float amount){
        if(ammoLiquid==liquid)
            return LiquidBlock.handleLiquid(liquidCapacity, ((LiquidTurretEntity)tile.entity()).liquidEntity, liquid, amount);
        return 0;
    }

    @Override
    public float getLiquid(Tile tile){
        LiquidTurretEntity entity = tile.entity();
        return entity.liquidEntity.liquidAmount;
    }

    @Override
    public float getLiquidCapacity(Tile tile){
        return liquidCapacity;
    }

    @Override
    public TileEntity getEntity() {
        return new LiquidTurretEntity();
    }

    static class LiquidTurretEntity extends TurretEntity{
        
        public LiquidBlock.LiquidEntity liquidEntity = new LiquidBlock.LiquidEntity();
        
        @Override
        public void write(DataOutputStream stream) throws IOException {
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
