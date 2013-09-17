package com.lippens.wiggame.entities;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lippens.wiggame.Game;
import com.lippens.wiggame.GameSounds;
import com.lippens.wiggame.InputHandler;
import com.lippens.wiggame.entities.abilities.MuscleUltimate;
import com.lippens.wiggame.gfx.Colors;
import com.lippens.wiggame.gfx.Font;
import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.level.Level;
import com.lippens.wiggame.net.packets.Packet02Move;
import com.lippens.wiggame.net.packets.Packet03Shoot;
import com.lippens.wiggame.net.packets.Packet04Projectiles;
import com.lippens.wiggame.playeritems.Items;
import com.lippens.wiggame.playeritems.MachineGun;
import com.lippens.wiggame.playeritems.Pistol;
import com.lippens.wiggame.playeritems.projectiles.Bullet;
import com.lippens.wiggame.playeritems.projectiles.Projectile;

public class Player extends Mob{
	
	protected int[] ammo = new int[10];
	private boolean isAlive = true;
	private boolean isFiring;
	protected boolean hasWeapon;
	boolean canUlt;
	private boolean isUlting;
	private long ultAnimationTimer = System.currentTimeMillis();
	private InputHandler input;
	private boolean doingDamage;
	private int color = Colors.get(-1, 000, 145, 543);
	private int deadColor = Colors.get(-1,000,300,543);
	int scale = 1;
	protected int absX = 0;
	protected int absY = 0;
	protected int currenthp = 5;
	protected static int totalhp = 5;
	private int damage;
	private int[] inventory = new int[10];
	protected int weaponType = 0;
	public String weaponName = "BARE HANDS";
	protected int killCount = 0;
	protected int nonUltKillCount = 0;
	private long fireTime = System.currentTimeMillis();
	private MuscleUltimate ultimate;
	private HUD hud;
//	private boolean hasHUD;
	
	public Player(Level level, int x, int y, InputHandler input, String name) {
		super(level, "Player", x, y, 1, 5,0);
		this.input = input;
		absX = x;
		absY = y;
		totalhp = super.health;
		damage = this.damageCanDeal;
		this.username = name;
		hud = new HUD(this);
	}


	public void setKillCount(int killCount, int nonUltKillCount) {
		this.killCount += killCount;
		if (this.nonUltKillCount <= 51) 
		this.nonUltKillCount += nonUltKillCount;
	}
	
	public Player getPlayer() {
		return this;
	}
	
	public boolean isFiring() {
		return isFiring;
	}
	
	public boolean hasWeapon() {
		return hasWeapon;
	}
		
	public void setAmmo(int weaponType, int ammo) {
		this.ammo[weaponType] += ammo;
	}
	
	public int getAmmo(int weaponType) {
		return this.ammo[weaponType];
	}
	
	public void setFiring(boolean firingStatus) {
		this.isFiring = firingStatus;
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	public int getMovingDir() {
		return movingDir;
	}
	public boolean getWeapon() {
		return hasWeapon;
	}
	public int getWeaponType() {
		return weaponType;
	}
	
	public boolean getUlting() {
		return isUlting;
	}

	public void setWeapon(boolean hasWeapon, int weaponType, String weaponName) {
		this.hasWeapon = hasWeapon;
		this.weaponType = weaponType;
		this.weaponName = weaponName;
	}

	public void setHealth(int newHealth) {
		this.currenthp = newHealth;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int newDamage) {
		damage = newDamage;
	}
	
	public static int getTotalHP() {
		return totalhp;
	}
	
	public int getHealth() {
		return currenthp;
	}
	
	public int getX() {
		return absX;
	}
	
	public int getY() {
		return absY;
	}
	
	public void addToInventory(int weaponType) {
		inventory[weaponType]=weaponType;
	}
	
	public int getInventorySlot(int weaponType) {
		return inventory[weaponType];
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

	
	
	public synchronized void tick() {
		int xa = 0;
		int ya = 0;
		if (input!=null) {
		if (isUlting && System.currentTimeMillis() - ultAnimationTimer > 2000) {
			isUlting = false;
		}
		if (nonUltKillCount - 50 > 0 && nonUltKillCount !=0) {
			canUlt = true;
		}
		if (input.f.isPressed() && canUlt  && isAlive) {
			isUlting = true;
			ultAnimationTimer = System.currentTimeMillis();
			ultimate = new MuscleUltimate(level, absX, absY);
			input.f.pressed = false;
			canUlt = false;
			nonUltKillCount=0;
			}
		
		movement: {
				
			if (input.left.isPressed() && isAlive()) { 
				xa--;
				if (input.down.isPressed()) {
					ya++;
				} else if (input.up.isPressed()) {
					ya--;
				}
				break movement;
				}
			
			if (input.right.isPressed() && isAlive()) {
					xa++;
					if (input.down.isPressed()) {
						
						ya++;

					} else if (input.up.isPressed()) {
						ya--;
					}
					break movement;}

		if (input.up.isPressed() && isAlive()) { 
		ya--;
			if (input.left.isPressed()) {
				xa--;
			} else if (input.right.isPressed()) {
				xa++;
			}
		break movement;
		}
		if (input.down.isPressed() && isAlive()) {
			ya++;
			if (input.right.isPressed()) {
				xa++;
			} else if (input.left.isPressed()) {
				xa--;
			}
		break movement;
		}
				}
		
		if ((this.input.one.isReleased() || this.input.one.isPressed()) && this.inventory[1]!=0) {
			this.weaponType = 1;
			this.weaponName = "Pistol";
			this.setWeapon(true,1,this.weaponName);
			Packet03Shoot packet = new Packet03Shoot(this.getUserName(),this.x, this.y,
					this.getMovingDir(), this.getWeaponType(), this.hasWeapon(), this.getWeaponName());
					packet.writeData(Game.game.socketClient);
			this.input.released = false;
			input.space.pressed = false;
			}
		if ((this.input.two.isReleased() || this.input.two.isPressed()) && this.inventory[2]!=0) {
			this.weaponType = 2;
			this.weaponName = "Machine Gun";
			this.setWeapon(true,2,this.weaponName);
			Packet03Shoot packet = new Packet03Shoot(this.getUserName(),this.x, this.y,
					this.getMovingDir(), this.getWeaponType(), this.hasWeapon(), this.getWeaponName());
					packet.writeData(Game.game.socketClient);
			this.input.released = false;
			input.space.pressed = false;
			}
		
		if (xa !=0 || ya != 0) {
			if (!hasCollided(xa,ya)) {
			move(xa,ya);
			absX += xa;
			absY += ya;
			}
			isMoving = true;
			Packet02Move packet = new Packet02Move(this.getUserName(),this.x, this.y, this.numSteps, this.isMoving, this.movingDir);
			packet.writeData(Game.game.socketClient);
			if (xa != 0 && ya != 0) {
				movingDir = input.pressDir;
			}
		} else {
			isMoving = false;
		}
		
		
		if (input.space.isReleased() && isAlive() && hasWeapon && weaponType !=2 && !isUlting) {
			checkAmmo();
			if (ammo[weaponType] > 0) {
			isFiring = true;
			for (Items i : level.items) {
				if (i instanceof Pistol) {
					((Pistol) i).fireWeapon();
				}
			}
			//Bullet b = new Bullet(this.level, this, this.absX, this.absY, this.movingDir);
			//Game.level.addProjectile(b);
			Packet04Projectiles packet = new Packet04Projectiles(this.getUserName(), this.x,this.y, this.movingDir, 0, System.currentTimeMillis(),System.currentTimeMillis(), this.weaponType);
			packet.writeData(Game.game.socketClient);
			ammo[weaponType]--;
			input.space.pressed = false;
			input.space.released = false;
			}
			}
		if (input.space.isPressed() && isAlive() && hasWeapon && !isUlting && weaponType == 2 && System.currentTimeMillis() - fireTime > 100) {
			checkAmmo();
			if (ammo[weaponType] > 0) {
			isFiring = true;
			for (Items i : level.items) {
				if (i instanceof MachineGun) {
					((MachineGun) i).fireWeapon();
					break;
				}
			}
			//Bullet b = new Bullet(this.level, this, this.absX, this.absY, this.movingDir);
			//Game.level.addProjectile(b);
			System.out.println(Game.level.safeProjectiles.size());
			Packet04Projectiles packet = new Packet04Projectiles(this.getUserName(), this.x,this.y, this.movingDir, 0, System.currentTimeMillis(),System.currentTimeMillis(), this.weaponType);
			packet.writeData(Game.game.socketClient);
			fireTime = System.currentTimeMillis();
			ammo[weaponType]--;
			}
		}
		}
	}

	public void checkAmmo() {
		if (ammo[this.weaponType] == 0  && System.currentTimeMillis() - fireTime > 200) {
			GameSounds.emptyWeapon.play();
			fireTime = System.currentTimeMillis();
			input.space.pressed = false;
			input.space.released = false;
		}
	}
	
	public void getDamaged() {
	}
	
	@Override
	public boolean aliveStatus() {
		return isAlive();
	}
	
	public void render(Screen screen) {
		int walkingSpeed = 4;
		int xTile = 0;
		int yTile = 28;
		int flipTop = (numSteps >> walkingSpeed ) & 1;
		int flipBottom = (numSteps >> walkingSpeed ) & 1;
		
		
		
		if (movingDir ==1 && !isUlting) {
			xTile+=2;
		} else if (movingDir >  1 && !isUlting) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) %2;
		}
		
		int modifier = 8*scale; 
		int xOffset = x - modifier/2;
		int yOffset = y - modifier/2 - 4;	
		Font.render(username, screen, xOffset-20, yOffset-20, Colors.get(-1, -1, -1, 555),1);
		if (this.currenthp == 0 && this.isUlting == false) {
			setAlive(false);

			screen.render(xOffset+(modifier * flipTop), yOffset+4,8 + yTile * 32, deadColor, flipTop, scale);
			screen.render(xOffset+modifier - (modifier * flipTop), yOffset+4,(8 + 1) + yTile * 32, deadColor, flipTop, scale);
			screen.render(xOffset+(modifier * flipBottom), yOffset + modifier+4 ,8 + (yTile+1) * 32, deadColor, flipBottom, scale);
			screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier+4,(8+1) + (yTile+1) * 32, deadColor, flipBottom, scale);
			Font.render("YOU ARE DEAD", screen, screen.xOffset+40, screen.yOffset+40, Colors.get(000,-1,-1,555),1);
		}
		if (isAlive() && !hasWeapon && isUlting == false) {
		screen.render(xOffset+(modifier * flipTop), yOffset,xTile + yTile * 32, color, flipTop, scale);
		screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(xTile + 1) + yTile * 32, color, flipTop, scale);
		screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,xTile + (yTile+1) * 32, color, flipBottom, scale);
		screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(xTile+1) + (yTile+1) * 32, color, flipBottom, scale);
		}
		
		if(isAlive() && hasWeapon && weaponType == 1 && isUlting == false) {
			screen.render(xOffset+(modifier * flipTop), yOffset,10+xTile + yTile * 32, color, flipTop, scale);
			screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(10+xTile + 1) + yTile * 32, color, flipTop, scale);
			screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,10+xTile + (yTile+1) * 32, color, flipBottom, scale);
			screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(10+xTile+1) + (yTile+1) * 32, color, flipBottom, scale);
		}
		
		if(isAlive() && hasWeapon && weaponType == 2 && isUlting == false) {
			screen.render(xOffset+(modifier * flipTop), yOffset,18+xTile + yTile * 32, color, flipTop, scale);
			screen.render(xOffset+modifier - (modifier * flipTop), yOffset,(18+xTile + 1) + yTile * 32, color, flipTop, scale);
			screen.render(xOffset+(modifier * flipBottom), yOffset + modifier ,18+xTile + (yTile+1) * 32, color, flipBottom, scale);
			screen.render(xOffset+modifier- (modifier * flipBottom), yOffset+modifier,(18+xTile+1) + (yTile+1) * 32, color, flipBottom, scale);
		}
		
		if (isAlive() && isUlting && System.currentTimeMillis() - ultAnimationTimer <= 1000) {
			screen.render(xOffset+(modifier), yOffset,(26+xTile) + yTile * 32, color, 1, scale);
			screen.render(xOffset+modifier - (modifier), yOffset,(26+xTile + 1) + yTile * 32, color, 1, scale);
			screen.render(xOffset+(modifier), yOffset + modifier ,(26+xTile) + (yTile+1) * 32, color, 1, scale);
			screen.render(xOffset+modifier- (modifier), yOffset+modifier,(26+xTile+1) + (yTile+1) * 32, color, 1, scale);

		}
		
		if (isAlive() && isUlting && System.currentTimeMillis() - ultAnimationTimer <= 2000 && System.currentTimeMillis() - ultAnimationTimer > 1000) {
			screen.render(xOffset+(modifier), yOffset,(28+xTile) + yTile * 32, color, 1, scale);
			screen.render(xOffset+modifier - (modifier), yOffset,(28+xTile + 1) + yTile * 32, color, 1, scale);
			screen.render(xOffset+(modifier), yOffset + modifier ,(28+xTile) + (yTile+1) * 32, color, 1, scale);
			screen.render(xOffset+modifier- (modifier), yOffset+modifier,(28+xTile+1) + (yTile+1) * 32, color, 1, scale);
		}

	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	@Override
	public Player getClosestPlayer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getUserName() {
		return this.username;
	}


	public String getWeaponName() {
		return weaponName;
	}


	public void setHasWeapon(boolean hasWeapon) {
		this.hasWeapon = hasWeapon;
	}


	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}


	public void setWeaponName(String weaponName) {
		this.weaponName = weaponName;
	}

}
