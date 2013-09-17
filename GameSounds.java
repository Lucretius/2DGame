package com.lippens.wiggame;

import java.io.File;
import java.io.IOException;

import com.lippens.wiggame.level.Level;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class GameSounds {
	protected Level level;
	public static Sound Squeal;
	public static Sound sorry;
	public static Sound machineGun;
	public static Sound pistol;
	public static Sound emptyWeapon;
	public static Sound moreAmmo;
	public static Music gameMusic;
	public static Sound ultimate;
	
	
	public GameSounds(Level level) {
		init(level);
		TinySound.init();
		Squeal = TinySound.loadSound("/sounds/pig_squeal2.wav");
		machineGun = TinySound.loadSound("/sounds/MachineGun.wav");
		pistol = TinySound.loadSound("/sounds/pistol.wav");
		ultimate = TinySound.loadSound("/sounds/RonnieColeman.wav");
		sorry = TinySound.loadSound("/sounds/im-sorry.wav");
		emptyWeapon = TinySound.loadSound("/sounds/emptyGun.wav");
		moreAmmo = TinySound.loadSound("/sounds/reload.wav");
		gameMusic = TinySound.loadMusic("/sounds/matrix_music.wav");
		//gameMusic.setLoop(true);
		//gameMusic.play(true);
	}
	
	public final void init(Level level) {
		this.level = level;

	}
	
	public static void gameSounds() {
			
	}
}
