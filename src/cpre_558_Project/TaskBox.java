package cpre_558_Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class TaskBox {
	
	private int x; // X position
	private int y; // Y position
	private int w; // width
	private int h; // height
	
	private Task t;

	public TaskBox() {
		// basic constructor
	}
	
	public TaskBox(int x, int y, int w, int h) {
		this();
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.t = null;
	}
	
	public TaskBox(int x, int y, int w, int h, Task t) {
		this();
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.t = t;
	}
	
	public void paint(Graphics g) {
		// draw the rectangle here
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(x, y, w, h, 5, 5);
		g.setColor(Color.BLACK);
		g.drawRoundRect(x, y, w, h, 5, 5);
		
		if (t != null) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 12));
			g.drawString(t.Id, x + 15, y + (h/2));
		}
	}
}
