package com.lippens.wiggame.entities;

import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.level.Level;

public abstract class Entity {
	public boolean isAlive;
	public long timeOfDeath;
	public int x, y;
	protected Level level;
	public boolean delete;
	public int health;
	public String username;
	
	public Entity(Level level) {
		init(level);
	}
	
	public final void init(Level level) {
		this.level = level;
	}
	public abstract boolean aliveStatus();
	public abstract void tick();
	public abstract void getDamaged();
	public abstract int getHealth();
	public abstract Player getClosestPlayer();
	public abstract void render(Screen screen);
}
