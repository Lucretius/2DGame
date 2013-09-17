package com.lippens.wiggame.entities;

import java.net.InetAddress;

import com.lippens.wiggame.level.Level;

public class EnemyWeakMP extends EnemyWeak{

	public InetAddress ipAddress;
	public int port;
	
	public EnemyWeakMP(Level level, int x, int y, InetAddress ipAddress, int port) {
		super(level, x, y);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void tick() {
		super.tick();
	}
}
