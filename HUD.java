package com.lippens.wiggame.entities;

import com.lippens.wiggame.Game;
import com.lippens.wiggame.InputHandler;
import com.lippens.wiggame.gfx.Colors;
import com.lippens.wiggame.gfx.Font;
import com.lippens.wiggame.gfx.Screen;
import com.lippens.wiggame.level.Level;

public class HUD {

	private final Player player;
	private Screen screen = Game.game.screen;
	private int energyColor = Colors.get(-1,300,-1,-1);
	private int healthColor = Colors.get(-1,000,300,-1);
	private int losthpColor = Colors.get(-1, 000, -1, -1);
	
	public HUD(Player player) {
		this.player = player;
		//render(screen);
	}

	public final void render(Screen screen) {
		Font.render("WEAPON:"+ player.weaponName, screen, screen.xOffset, screen.yOffset+110, Colors.get(-1,-1,-1,555), 1);
		Font.render("KILLS:"+ player.killCount, screen, screen.xOffset+85, screen.yOffset, Colors.get(-1,-1,-1,555), 1);
		Font.render("AMMO:"+ player.ammo[player.weaponType], screen, screen.xOffset, screen.yOffset+102, Colors.get(-1,-1,-1,555), 1);
		
		if (player.canUlt == false) {
			for (int i = 0; i <= player.nonUltKillCount; i++) {
				screen.render(1+(i*1)+screen.xOffset, 10+screen.yOffset,4+(1 * 32), energyColor, 1, player.scale);
			}
			}
			else if (player.canUlt == true) { 
				for (int i = 0; i <= 50; i++) {
					screen.render(1+(i*1)+screen.xOffset, 10+screen.yOffset,4+(1 * 32), Colors.get(-1, 050, -1, -1), 1, player.scale);
				}
			}
		
		for (int i = 0; i <= 49; i++) {
			screen.render(1+(i)+screen.xOffset, 8+screen.yOffset,3+(1 * 32), healthColor, 1, player.scale);
			screen.render(1+(i)+screen.xOffset, 12+screen.yOffset,3+(1 * 32), healthColor, 1, player.scale);
			}
		screen.render(screen.xOffset, 10+screen.yOffset,4+(1 * 32), healthColor, 1, player.scale);
		screen.render(screen.xOffset+52, 10+screen.yOffset,4+(1 * 32), healthColor, 1, player.scale);
		
		for (int i = 0; i < player.totalhp; i++) {
		screen.render((i*10)+screen.xOffset, 0+screen.yOffset,1 * 32, losthpColor, 1, player.scale);
		}
		for (int i = 0; i < player.currenthp; i++) {
		screen.render((i*10)+screen.xOffset, 0+screen.yOffset,1 * 32, healthColor, 1, player.scale);
		}
		
			}
}
