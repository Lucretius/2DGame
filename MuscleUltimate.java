package com.lippens.wiggame.entities.abilities;

import com.lippens.wiggame.GameSounds;
import com.lippens.wiggame.entities.Entity;
import com.lippens.wiggame.entities.Player;
import com.lippens.wiggame.entities.EnemyWeak;
import com.lippens.wiggame.level.Level;

import kuusisto.tinysound.Sound;

public class MuscleUltimate {

	private static Sound ultimate = GameSounds.ultimate;
	private boolean isPlaying;
	private int x;
	private int y;
	private Level level;
	
	
	public MuscleUltimate(Level level, int x, int y) {
		this.level = level;
		this.x = x;
		this.y = y;
		ultimate.play(6);
		doDamage();
	}
	
	public void doDamage() {
		for (Entity e : level.entities) {
				if (Math.abs(this.x - e.x) < 100 && Math.abs(this.y - e.y) < 100) {
					for (int i = 0; i < 10; i++) {
						if (e.aliveStatus() == true) {							
						e.getDamaged();
					}
				}
			}
		}
	}
	
	
}
