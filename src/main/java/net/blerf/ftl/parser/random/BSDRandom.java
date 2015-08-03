package net.blerf.ftl.parser.random;

import net.blerf.ftl.parser.random.RandRNG;


/**
 * An implementation of srand()/rand() from BSD.
 *
 * The algorithm is contained within this class, so no external libraries
 * are involved. Each instance has its own separate state.
 *
 * It is identical to the GNU Standard Library's TYPE_0 rand().
 *
 * http://rosettacode.org/wiki/Linear_congruential_generator#C
 */
public class BSDRandom implements RandRNG {
	public static final int RAND_MAX = 0x7fffffff;  // 31bit.


	protected int seed = 1;


	public BSDRandom() {
	}


	@Override
	public void srand( int newSeed ) {
		seed = newSeed;
	}

	@Override
	public int rand() {
		return seed = (seed * 1103515245 + 12345) & RAND_MAX;
	}
}
