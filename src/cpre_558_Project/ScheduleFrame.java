package cpre_558_Project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScheduleFrame extends javax.swing.JFrame {
	
	public static final int SCALE = 50;
	
	private class SchedulePanel extends JPanel {
		
		private final int OFFSET = 50;
		
		private List<TaskBox> boxList;
		private List<ProcessorId> processorList;
		
		private int dimX;
		private int dimY;

		public SchedulePanel() {
			// basic constructor
			boxList = new ArrayList<>();
			processorList = new ArrayList<>();
			
			dimX = 0;
			dimY = 0;
		}
		
		public SchedulePanel(Map<Integer, List<ScheduledTask>> map) {
			this();
			
			this.buildSchedule(map);
			dimX += 0; dimY += 25;
		}
		
		/* Public Methods */
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// draw the Processor Ids
			for(ProcessorId id : processorList) {
				id.paint(g);
			}
			
			// draw the rectangles here
			for(TaskBox box : boxList) {
				box.paint(g);
			}
			
			// add the timeline to the bottom
			paintTimeline(g);
		}
		
		@Override
		public Dimension getPreferredSize() {
			// so that our GUI is big enough
			return new Dimension(dimX + 15, dimY + 7);
		}
		
		/* Private Methods */
		
		private void buildSchedule(Map<Integer, List<ScheduledTask>> map) {
			for(Integer p : map.keySet()) {
				buildSchedule(p, map.get(p));
				buildProcessorId(p);
			}
		}
		
		private void buildSchedule(Integer p, List<ScheduledTask> list) {
			for(ScheduledTask st : list) {
				buildBox(p, st);
			}
		}
		
		private void buildBox(Integer Processor, ScheduledTask st) {
			
			int x = 5 + ((st.startTime + 1) * SCALE) - SCALE;
			int y = ((Processor - 1) * SCALE) + (5 * Processor);
			int w = ((st.endTime - st.startTime + 1) * SCALE) - SCALE;
			int h = SCALE;
			
			x += OFFSET; // offset for the ProccesorId
			
			TaskBox box = new TaskBox(x, y, w, h, st.t);
			this.boxList.add(box);
			
			if (dimX < (x + w)) {
				this.dimX = (x + w);
			}
			
			if (dimY < (y + h)) {
				this.dimY = (y + h);
			}
		}
		
		private void buildProcessorId(Integer p) {
			
			int x = 15;
			int y = ((p - 1) * SCALE) + (5 * p) + (SCALE/2);
			String id = "P" + p.toString();
			
			ProcessorId pId = new ProcessorId(id, x, y);
			this.processorList.add(pId);
		}
		
		private void paintTimeline(Graphics g) {
			g.drawLine(OFFSET, dimY - 20, dimX, dimY - 20);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 10));
			
			int x = OFFSET;
			for(int i = 0; x < dimX; i++) {
				g.drawString("" + i, x, dimY - 5);
				
				x += SCALE;
			}
			
		}
		
	} // end SchedulePanel
	
	public ScheduleFrame(Map<Integer, List<ScheduledTask>> map) {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Schedule");
		this.getContentPane().add(new SchedulePanel(map));
		this.pack();
	}
	
	
}
