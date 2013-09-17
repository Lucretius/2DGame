package com.lippens.wiggame.entities;

import kuusisto.tinysound.Sound;

import com.lippens.wiggame.Game;
import com.lippens.wiggame.GameSounds;
import com.lippens.wiggame.gfx.Colors;
import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.level.Level;

public class Fred extends Mob{

	private boolean isAlive = true;
	private boolean justDied = false;
	private boolean didDamage = false;
	private int color = Colors.get(-1, 000, 124, 543);
	private int deadColor = Colors.get(-1,000,300,543);
	private int scale = 1;
	private int absX = 0;
	private int absY = 0;
	private int health = 1;
	private static long timeDamaged = System.currentTimeMillis();
	private long timeMoved = System.currentTimeMillis();
	private Sound rodSqueal = GameSounds.Squeal;
	int playerX;
	int playerY;
	private final int startX;
	private final int startY;
	private Player player = getClosestPlayer();
	
	public Fred(Level level, int x, int y) {
		super(level, "fred", x, y, 2, 1, 1);
		this.startX = x;
		this.startY = y;
		this.absX = x;
		this.absY = y;
		this.isAlive = true;
		// TODO Auto-generated constructor stub
	}


	public int getFredX() {
		return absX;
	}
	
	public int getFredY() {
		return absY;
	}
	
	public boolean hasCollided(int xa, int ya) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void tick() {
		
		Player player = getClosestPlayer();
		int playerX = player.getX();
		int playerY = player.getY();
		if (isAlive) {
			if (System.currentTimeMillis()-timeMoved > 60) {
				int xa = 0;
				int ya = 0;
				if (Math.abs(Game.WIDTH - this.startX) > Math.abs(Game.HEIGHT-this.startY))  {
					xa++;
					

					if (Math.abs(playerX - this.absX) < 16 && Math.abs(playerY - this.absY) < 10 && !this.didDamage) {
						doDamage();
						this.didDamage = true;
						System.out.println("HIT" + playerX + " " + this.absX);
						}
				}
				else {
					ya++;
					
					if (Math.abs(playerX - this.absX) < 10 && Math.abs(playerY - this.absY) < 16 && !this.didDamage) {
						doDamage();
						this.didDamage = true;
						System.out.println("HIT" + playerY + " " + this.absY);
						}
				}
				move(xa,ya);
				this.absX += xa*speed;
				this.absY += ya*speed;
			}

		}
		if (this.absX >  level.width*8+8|| this.absY > level.height*8+8 || this.absX < 0 || this.absY < 0) {
			this.isAlive = false;
		}
	}

	@Override
	public void getDamaged() {
		for (Entity e : level.entities) {
			if (this.health > 0 && e instanceof Player) {
			this.health -= ((Player) e).getDamage();
			}
			if (this.health == 0) {
				this.isAlive = false;

				if (e instanceof Player) {
					if ( ((Player) e).getUlting() == false && ((Player) e).getKillCount() % 50 != 0) {
					((Player) e).setKillCount(1,1);
					} else {
						((Player) e).setKillCount(1,0);
					}
				}
				e.timeOfDeath = System.currentTimeMillis();
			}
			if (this.health == 0 && this.justDied == false) {
				this.justDied = true;
				if (this.justDied == true) {
				this.rodSqueal.play(.5);
				}
			}
		}
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public void render(Screen screen) {
		
		int walkingSpeed = 1;
		int xTile = 0;
		int yTile = 24;
		int flipTop = (numSteps >> walkingSpeed ) & 1;
		int flipBottom = (numSteps >> walkingSpeed ) & 1;
		
		if (movingDir ==1) {
			xTile+=2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) %2;
		}
		
		int modifier = 8*scale;
		int xOffset = x - modifier/2;
		int yOffset = y - modifier/2 - 4;
		if (this.isAlive) {
		screen.render(xOffset+(modifier * flipTop), yOffset,xTile + yTile * 32, color, flipTop, scale);
		screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(xTile + 1) + yTile * 32, color, flipTop, scale);
		screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,xTile + (yTile+1) * 32, color, flipBottom, scale);
		screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(xTile+1) + (yTile+1) * 32, color, flipBottom, scale);
		}
		if (!this.isAlive){
			screen.render(xOffset+(modifier * flipTop), yOffset,8 + yTile * 32, deadColor, flipTop, scale);
			screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(8 + 1) + yTile * 32, deadColor, flipTop, scale);
			screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,8 + (yTile+1) * 32, deadColor, flipBottom, scale);
			screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(8+1) + (yTile+1) * 32, deadColor, flipBottom, scale);

		}
	}
	public void doDamage() {
		for (Entity e : level.entities) {
			if (System.currentTimeMillis()-timeDamaged > 1000) {
				if (e instanceof Player)
					if (e.getHealth()>0) {
						player.setHealth(e.getHealth()-this.damageCanDeal);
						timeDamaged = System.currentTimeMillis();
						
					}
				}
			}
	}


	@Override
	public boolean aliveStatus() {
		return isAlive;
	}
	
	@Override
	public Player getClosestPlayer() {
		int minX = level.width*10;
		int minY = level.height*10;
		Player p = null;
		for (Entity e : level.entities) {
			if (e instanceof Player) {
				p = ((Player) e).getPlayer();
				if ((Math.abs(p.getX() - this.absX) < minX && Math.abs(p.getY() - this.absY) < minY)) {
					minX = p.getX();
					minY = p.getY();
				}
			}
		}
		return p;
	}
	}
