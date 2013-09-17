package com.lippens.wiggame.entities;

import kuusisto.tinysound.Sound;

import com.lippens.wiggame.GameSounds;
import com.lippens.wiggame.gfx.Colors;
import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.level.Level;
import com.lippens.wiggame.net.packets.Packet02Move;
import com.lippens.wiggame.playeritems.Cornbread;
import com.lippens.wiggame.playeritems.MachineGun;

public class EnemyStrong extends Mob {
	
	private boolean isAlive = true;
	private boolean justDied = false;
	private int color = Colors.get(-1, 000, 114, 543);
	private int deadColor = Colors.get(-1,000,300,114);
	private int scale = 1;
	private int absX = 0;
	private int absY = 0;
	private int health = 5;
	private static long timeDamaged = System.currentTimeMillis();
	private long timeMoved = System.currentTimeMillis();
	private Sound sorry = GameSounds.sorry;
	private long timeOfDeath = 0L;
	private Player player = getClosestPlayer();

	
	public EnemyStrong(Level level, int x, int y) {
		super(level, "enemyStrong", x, y, 1, 1,1);
		this.absX = x;
		this.absY = y;
		this.isAlive = true;

	}

	@Override
	public boolean aliveStatus() {
		return isAlive;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getRodX() {
		return absX;
	}
	
	public int getRodY() {
		return absY;
	}
	
	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}
		return false;
	}

	public void tick() {
		if (isAlive) {
			Player player = getClosestPlayer();
			int playerX = player.getX();
			int playerY = player.getY();
	if (System.currentTimeMillis()-timeMoved > 60) {
		int xa = 0;
		int ya = 0;
		int max = 0;
		
		
		if (Math.abs(playerY - absY) > Math.abs(playerX - absX)) {
			max = 0;
		} else {
			max = 1;
		}

	positiontest: {
		if (max == 0) {
			if ((playerY - absY)>0) {
				ya++;
				break positiontest; }
			else if ((playerY - absY)<0) {
				ya--;
				break positiontest;
			}
		}
		else if (max == 1) {
			if ((playerX - absX)>0) {
				xa++;
				break positiontest;}
			else if ((playerX - absX)<0) {
				xa--;
				break positiontest;}
		}
	}
		if (xa !=0 || ya != 0) {
			
			if (!hasCollided(xa,ya)) {
				move(xa,ya);
				this.absX += xa;
				this.absY += ya;
				}
			isMoving = true;
		} else {
			isMoving = false;
		}
		timeMoved = System.currentTimeMillis();
	}
	if (Math.abs(playerX - absX) < 16 && Math.abs(playerY - absY) < 16) {
		doDamage();
		}
		}
		destroy();
	}

	private String getUserName() {
		return "enemyStrong";
	}

	public void doDamage() {
		for (Entity e : level.getEntities()) {
			if (System.currentTimeMillis()-timeDamaged > 1000) {
				if (e instanceof Player)
					if (e.getHealth()>0) {
						player.setHealth(e.getHealth()-this.damageCanDeal);
						timeDamaged = System.currentTimeMillis();
						
					}
				}
			}
	}

	public void getDamaged() {
		boolean playSoundOK = false;
		for (Entity e : level.entities) {
		if (this.health > 0 && e instanceof Player) {
		this.health -= ((Player) e).getDamage();
		}
		if (this.health == 0) {
			isAlive = false;

			if (e instanceof Player) {
				if ( ((Player) e).getUlting() == false && ((Player) e).getKillCount() % 50 != 0) {
				((Player) e).setKillCount(1,1);
				} else {
					((Player) e).setKillCount(1,0);
				}
			}
				}
		}
		
		for (Entity e : level.entities) {
			if (e instanceof Player && ((Player) e).getUlting() == false) {
				playSoundOK = true;
			}
		}
		
		if (this.health == 0 && this.justDied == false) {
			this.justDied = true;
			this.timeOfDeath = System.currentTimeMillis();
			if (this.justDied == true && playSoundOK) {
			this.sorry.play(6);
			int dice = (int) (Math.random() * (50+1));
			System.out.println(dice);
			if (dice % 2 == 0) {
			level.items.add(new Cornbread(level,absX,absY));
			} else if (dice % 3 == 0) {
				level.items.add(new MachineGun(level,this.absX,this.absY));
			}
			}
		}
	}
	
	public void destroy() {
		if (System.currentTimeMillis()-this.timeOfDeath > 30000 && this.isAlive == false) {
			this.delete = true;
		}
	}
	
	public void render(Screen screen) {
		
		int walkingSpeed = 1;
		int xTile = 0;
		int yTile = 26;
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
		if (isAlive) {
		screen.render(xOffset+(modifier * flipTop), yOffset,xTile + yTile * 32, color, flipTop, scale);
		screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(xTile + 1) + yTile * 32, color, flipTop, scale);
		screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,xTile + (yTile+1) * 32, color, flipBottom, scale);
		screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(xTile+1) + (yTile+1) * 32, color, flipBottom, scale);
		}
		if (!isAlive){
			screen.render(xOffset+(modifier * flipTop), yOffset,8 + yTile * 32, deadColor, flipTop, scale);
			screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(8 + 1) + yTile * 32, deadColor, flipTop, scale);
			screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,8 + (yTile+1) * 32, deadColor, flipBottom, scale);
			screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(8+1) + (yTile+1) * 32, deadColor, flipBottom, scale);

		}
		
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
