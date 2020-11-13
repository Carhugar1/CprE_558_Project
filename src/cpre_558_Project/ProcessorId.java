package cpre_558_Project;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ProcessorId {
	
	private String id;	// Identifier
	private int x;		// X position
	private int y;		// Y position

	public ProcessorId() {
		// basic constructor
	}

	public ProcessorId(String id, int x, int y) {
		this();
		
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void paint(Graphics g) {
		// draw the rectangle here
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.BOLD, 16));
		g.drawString(id, x, y);
	}

}
