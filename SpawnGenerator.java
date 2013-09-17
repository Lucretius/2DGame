package com.lippens.wiggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.level.Level;
import com.lippens.wiggame.playeritems.Cornbread;
import com.lippens.wiggame.playeritems.Items;

public class SpawnGenerator{
	public int x, y;
	protected Level level;
	List<EnemyWeak> enemyWeak = new ArrayList<EnemyWeak>();
	private long lastSpawnTime = System.currentTimeMillis();
	private long lastItemSpawnTime = System.currentTimeMillis();
	private int spawnSide = 0;
	
	public SpawnGenerator(Level level) {
		init(level);
	}
	
	public final void init(Level level) {
		this.level = level;

	}
	
	public void itemGenerator() {

	}
	
	public synchronized void mobGenerator() {
		if ((System.currentTimeMillis()- lastSpawnTime) > 1000) {
			switch(spawnSide) {
			case 0: level.getEntities().add(new EnemyWeak(level,10, (int)(Math.random() * (((level.height*8)-20) + 1))));
			level.entities.add(new Fred(level,8, (int)(Math.random() * (((level.height*8)-20) + 1))));
				break;
			case 1: level.addEntity(new EnemyStrong(level, (int)(Math.random() * (((level.width*8)-10) + 1)), ((level.height*8) - 10)));
				break;
			case 2: level.addEntity(new EnemyWeakMP(level,(int)(Math.random() * (((level.width*8)-10) + 1)),10, null, 1));
				break;
			case 3: level.addEntity(new EnemyWeakMP(level, level.width*8-10, (int)(Math.random() * (((level.height*8)-10) + 1)), null, 1));
				break;
			}	
			lastSpawnTime = System.currentTimeMillis();
			spawnSide = (spawnSide+1) % 3;
		}
	}
}
