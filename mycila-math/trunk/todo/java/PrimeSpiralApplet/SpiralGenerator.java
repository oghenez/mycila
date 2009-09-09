/* File:  SpiralGenerator.java - Spiral generator for 'PrimeSpiral'
 *
 * By:  Christopher Lane <cdl at cdl dot best dot vwh dot net>
 *
 * Date: 15 February 1997
 *
 * This program may be distributed without restriction for non-commercial use.
 */
 
import java.awt.*;

public final class SpiralGenerator extends Object {
	static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	int count, remain, distance, direction;

	SpiralGenerator() {
		count = 1;
		remain = 2;
		distance = 1;
		direction = RIGHT;
		}

	public void generate(int number, Point point) {
		int dx = 0, dy = 0;

		for (; count <= number; count++) {
			if (--remain == 0) {
				switch (direction) {
					case UP: distance++; direction = LEFT; break;
					case DOWN: distance++;
					default: direction--; break;
					}
				remain = distance;
				}
			switch (direction) {
				case LEFT: --dx; break;
				case RIGHT: ++dx; break;
				case UP: --dy; break;
				case DOWN: ++dy; break;
				}
			}

		point.translate(dx, dy);
		}
	}
