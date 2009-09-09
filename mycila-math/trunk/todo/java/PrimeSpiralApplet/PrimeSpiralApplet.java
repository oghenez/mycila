/* File:  PrimeSpiralApplet.java - Applet class for 'PrimeSpiral'
 *
 * By:  Christopher Lane <cdl at cdl dot best dot vwh dot net>
 *
 * Date: 23 February 1997
 *
 * 11/16/03 replaced bounds() with getBounds() perl JDK 1.1
 * 11/16/03 replaced thread.stop() with advisory boolean
 *
 * This program may be distributed without restriction for non-commercial use.
 */

import java.awt.*;
import java.applet.Applet;

public class PrimeSpiralApplet extends Applet implements Runnable {
	private long time;
	private Point point;
	private Image image;
	private Rectangle bounds;
	private Thread thread = null;
	private PrimeGenerator prime;
	private SpiralGenerator spiral;
	private Graphics bitmap, screen;
	private boolean running = false;

	public void init() {
		prime = new PrimeGenerator();
		spiral = new SpiralGenerator();

		bounds = getBounds();
		point = new Point(bounds.width / 2, bounds.height / 2);
		image = createImage(bounds.width, bounds.height);

		screen = getGraphics();
		bitmap = image.getGraphics();

		bitmap.setColor(getBackground()); {
			bitmap.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
			} bitmap.setColor(getForeground());

		time = System.currentTimeMillis();
		}
	
	public void start() {
		if (!running && thread == null) {
			running = true;
			(thread = new Thread(this)).start();
			}
		}
	
	public void stop() {
		running = false;
		}

	public void run() {
		while (running) {
			if (point.x > bounds.x && point.x < bounds.width && point.y > bounds.y && point.y < bounds.height) {
				spiral.generate(prime.generate(), point);
				bitmap.drawLine(point.x, point.y, point.x, point.y);
				screen.drawLine(point.x, point.y, point.x, point.y);
				try { thread.sleep(0); } catch(InterruptedException e) { showStatus(e.toString()); }
				}
			else {
				time = (System.currentTimeMillis() - time) / 1000;
				showStatus("Spiral completed in " + (time / 60) + ":" + ((time % 60) < 10 ? "0" : "") + (time % 60));
				init();
				repaint();
				}
			}
		thread = null;
		}

	public void paint(Graphics graphics) { graphics.drawImage(image, bounds.x, bounds.y, this); }
	}
