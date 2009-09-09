/* File:  PrimeGenerator.java - Prime generator for 'PrimeSpiral'
 *
 * By:  Christopher Lane <cdl at cdl dot best dot vwh dot net>
 *
 * Date: 26 February 1997
 *
 * This program may be distributed without restriction for non-commercial use.
 */

public final class PrimeGenerator extends Object {
	int number, index, primes[], squares[];

	PrimeGenerator() {
		index = 0;
		number = 1;
		primes = new int[1];
		squares = new int[1];
		}

	public int generate() {
		if (number > 2) {
loop:
			while(true) {
				number += 2;
				for (int i = 1; i < index && number >= squares[i]; i++) {
					if ((number % primes[i]) == 0) continue loop;
					}
				break loop;
				}
			}
		else number++;

		squares[index] = (primes[index] = number) * number;

		if (++index == primes.length) reallocate();

		return number;
		}

	void reallocate() {
		int array[];

		System.arraycopy(primes, 0, (array = new int[primes.length * 2]), 0, primes.length);
		primes = array;

		System.arraycopy(squares, 0, (array = new int[squares.length * 2]), 0, squares.length);
		squares = array;
		}
	}
