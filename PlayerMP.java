package com.lippens.wiggame.entities;

import java.net.InetAddress;

import com.lippens.wiggame.InputHandler;
import com.lippens.wiggame.level.Level;

public class PlayerMP extends Player {

	public InetAddress ipAddress;
	public int port;
	
	public PlayerMP(Level level, int x, int y, InputHandler input, String name, InetAddress ipAddress, int port) {
		super(level, x, y, input, name);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public PlayerMP(Level level, int x, int y, int movingDir, int weaponType, boolean hasWeapon, String weaponName, String name, InetAddress ipAddress, int port) {
		super(level, x, y, null, name);
		this.ipAddress = ipAddress;
		this.port = port;
		this.weaponType = weaponType;
		this.hasWeapon = hasWeapon;
		this.weaponName = weaponName;
	}

	public void tick() {
		super.tick();
	}
	
}
