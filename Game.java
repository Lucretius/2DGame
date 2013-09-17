package com.lippens.wiggame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.lippens.wiggame.entities.Entity;
import com.lippens.wiggame.entities.HUD;
import com.lippens.wiggame.entities.Player;
import com.lippens.wiggame.entities.PlayerMP;
import com.lippens.wiggame.entities.SpawnGenerator;
import com.lippens.wiggame.gfx.Colors;
import com.lippens.wiggame.gfx.Font;
import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.gfx.SpriteSheet;
import com.lippens.wiggame.level.Level;
import com.lippens.wiggame.net.GameClient;
import com.lippens.wiggame.net.GameServer;
import com.lippens.wiggame.net.packets.Packet00Login;
import com.lippens.wiggame.net.packets.Packet05Items;
import com.lippens.wiggame.playeritems.Cornbread;
import com.lippens.wiggame.playeritems.MachineGun;
import com.lippens.wiggame.playeritems.Pistol;
import com.lippens.wiggame.playeritems.projectiles.Projectile;

public class Game extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH/12*9;
	public static final int SCALE = 3;
	public static final String NAME = "Game";
	
	public JFrame frame;
	
	public boolean isRunning = false;
	public int tickCount = 0;
	
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private int[] colors = new int[6*6*6];
	
	public static Game game;
	public Screen screen;
	public InputHandler input;
	public WindowHandler windowHandler;
	public static Level level;
	public Player player;
	public SpawnGenerator spawner;
	public GameSounds gameSounds;
	public Cornbread cb1;
	public Pistol p1;
	public MachineGun m1;
	public HUD hud;
	public GameClient socketClient;
	public GameServer socketServer;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this,BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public Screen getScreen() {
		return this.screen;
	}
	
	public void init() {
		game = this;
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r*255/5);
					int gg = (g*255/5);
					int bb = (b*255/5);
					
					colors[index++] = rr<<16|gg<<8|bb;
				}
			}
		}
		
		screen = new Screen(WIDTH,HEIGHT, new SpriteSheet("/sprite_sheet.png"));
		input = new InputHandler(this);
		windowHandler = new WindowHandler(this);
		level = new Level("/levels/big_level.png");
		spawner = new SpawnGenerator(level);
		gameSounds = new GameSounds(level);
		player = new PlayerMP(level, 100, 100, input, JOptionPane.showInputDialog(this, "Please enter a username"), null, -1);
		cb1 = new Cornbread(level,20,50);
		p1 = new Pistol(level,20,100,50);
		m1 = new MachineGun(level, 150,150);
		level.addItems(m1);
		level.addItems(p1);
		//level.addItems(cb1);
		level.addEntity(player);
		Packet00Login loginPacket = new Packet00Login(player.getUserName(), player.x, player.y, player.getMovingDir(), player.getWeaponType(), player.hasWeapon(), player.getWeaponName());
		hud = new HUD(player);
		if (socketServer != null) {
			socketServer.addConnection((PlayerMP) player, loginPacket);
		}
		loginPacket.writeData(socketClient);
	}
	
	public synchronized void start() {
		isRunning = true;
		new Thread(this).start();
		
		if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
			socketServer = new GameServer(this);
			socketServer.start();
		}
		socketClient = new GameClient(this, "50.47.3.140");
		socketClient.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int frames = 0;
		int ticks = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while (delta >=1) {
				ticks++;
				tick();
				delta -=1;
				shouldRender = true;
			}
			
			try {
			Thread.sleep(2);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if (shouldRender) {
			frames++;
			render();
			}
			
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				frame.setTitle(ticks + "ticks ," + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private int x=0, y=0;
	
	public void tick() {
		tickCount++;
		level.tick();
		if (player!= null && player.isAlive())
		spawner.mobGenerator();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		int xOffset = player.x - (screen.width/2);
		int yOffset = player.y - (screen.height/2); 
		
		level.renderTile(screen, xOffset, yOffset);

		for (int x = 0; x < level.width; x++) {
			int color = Colors.get(-1, -1, -1, 000);
			if (x % 10 == 0 && x!= 0) {
				color = Colors.get(-1, -1, -1, 500);
			}

		}
		level.renderItems(screen);
		level.renderEntities(screen);
		level.renderProjectiles(screen);
		for (Entity e : level.safeEntities) {
			synchronized (level.safeEntities) {
			if (e instanceof Player || e.isAlive == true) {
				e.render(screen);
				hud.render(screen);
			}
			}
		}
		
		
		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int colorCode = screen.pixels[x+y * screen.width];
				if (colorCode < 255) pixels[x+y * WIDTH] = colors[colorCode];
			}	
		}
		
		Graphics g = bs.getDrawGraphics();

		g.drawImage(image,0,0,getWidth(),getHeight(),null);
		
		g.dispose();
		bs.show();

		level.clearUpProjectiles();
		level.clearUpEntities();
		spawner.itemGenerator();
	}
	
	public static void main(String[] args) {
		new Game().start();
	}



}
