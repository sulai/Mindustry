package io.anuke.mindustry.game;

import com.badlogic.gdx.utils.Array;
import io.anuke.mindustry.entities.enemies.EnemyTypes;

public class WaveCreator{
	
	public static Array<EnemySpawn> getSpawns(){

		return Array.with(
			
			// initial starting waves 1-3
			new EnemySpawn(EnemyTypes.standard){{
				scaling = 1;
				before = 3;
			}},
			
			// tank & healer (adds emp later) wave 4, 9, 14, ...
			new EnemySpawn(EnemyTypes.tank){{
				after = 3;
				spacing = 5;
				scaling = 2;
				amount = 1;
			}},
			new EnemySpawn(EnemyTypes.healer){{
				after = 8;
				spacing = 5;
				scaling = 1;
				amount = 1;
			}},
			new EnemySpawn(EnemyTypes.tank){{
				after = 13;
				spacing = 5;
				scaling = 3;
				amount = 1;
			}},
			new EnemySpawn(EnemyTypes.emp){{
				after = 13;
				amount = 1;
				spacing = 5;
				scaling = 2;
			}},
			
			// exceptional fast wave 5, as minor boss wave would be too early
			new EnemySpawn(EnemyTypes.fast){{
				after = 4;
				scaling = 1;
				amount = 1;
				before=4; // wave 10, 15, ... are minor boss waves
			}},
			
			// fast wave 6, 11, 16, ...
			new EnemySpawn(EnemyTypes.fast){{
				after = 5;
				scaling = 1;
				spacing = 5;
				amount = 3;
				tierscaleback = 0;
			}},
			// add standard later
			new EnemySpawn(EnemyTypes.standard){{
				after = 10;
				amount=2;
				scaling = 1;
				spacing = 5;
				tier = 2;
			}},
			
			// blast wave 7, 12, 17, ...
			new EnemySpawn(EnemyTypes.blast){{
				after = 6;
				amount = 3;
				spacing = 5;
				scaling = 2;
				tierscaleback = 1;
			}},
			new EnemySpawn(EnemyTypes.blast){{
				after = 6 + 5 + 5;
				amount = 2;
				spacing = 5;
				scaling = 2;
				tierscaleback = 0;
			}},
			
			// rapid (add flamer later) wave 8, 13, 18, ...
			new EnemySpawn(EnemyTypes.rapid){{
				after = 7;
				spacing = 5;
				scaling = 2;
				amount = 3;
			}},
			new EnemySpawn(EnemyTypes.flamer){{
				after = 12;
				amount = 2;
				spacing = 5;
				scaling = 3;
			}},
			
			// fast/standard wave kicks in wave 9
			
			// first boss wave 10: titan
			new EnemySpawn(EnemyTypes.titan){{
				after = 9;
				amount = 2;
				spacing = 5;
				scaling = 3;
			}},
			
			//boss wave 15, 20, 25, ...
			new EnemySpawn(EnemyTypes.fortress){{
				after = 14;
				amount = 1;
				spacing = 5;
				scaling = 1;
			}},
			
			new EnemySpawn(EnemyTypes.titan){{
				after = 14;
				amount = 1;
				spacing = 5;
				scaling = 2;
				tierscaleback = 0;
			}},
			
			new EnemySpawn(EnemyTypes.healer){{
				after = 19;
				spacing = 5;
				scaling = 2;
				amount = 2;
			}},
			//end boss wave
			
			//enhanced boss wave 20, 30, 40, ...
			new EnemySpawn(EnemyTypes.titan){{
				after = 14 + 5;
				amount = 1;
				spacing = 10;
				scaling = 1;
				tierscaleback = 0;
			}},
			new EnemySpawn(EnemyTypes.mortar){{
				after = 14 + 5;
				amount = 1;
				spacing = 10;
				scaling = 3;
			}},
			new EnemySpawn(EnemyTypes.emp){{
				after = 14 + 5;
				amount = 1;
				spacing = 10;
				scaling = 3;
			}}
			//end enhanced boss wave

		);
	}

	public static void main(String[] args) {
		testWaves(1, 50, Difficulty.normal);
	}
	
	public static void testWaves(int from, int to, Difficulty difficulty){
		Array<EnemySpawn> spawns = getSpawns();
		for(int i = from; i <= to; i ++){
			System.out.print(i+": ");
			int total = 0;
			for(EnemySpawn spawn : spawns){
				int a = spawn.evaluate(i-1, 0, difficulty.enemyScaling);
				int t = spawn.tier(i-1, 0);
				total += a;
				
				if(a > 0){
					System.out.print(a + "x " + spawn.type.name + "-" + t + " ");
				}
			}
			System.out.print(" (" + total + ")");
			System.out.println();
		}
	}
}
