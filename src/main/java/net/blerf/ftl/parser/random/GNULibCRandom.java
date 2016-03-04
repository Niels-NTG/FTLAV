package net.blerf.ftl.parser.random;


/**
 * An implementation of srand()/rand() from the GNU C Standard Library v2.21.
 *
 * The algorithms are contained within this class, so no external libraries
 * are involved. Each instance has its own separate state.
 *
 * There are several algorithms to choose from. TYPE_3 is the default.
 *
 * Based on: "rand.c", "random.c", and "random_r.c"
 *
 * https://sourceware.org/git/?p=glibc.git;a=tree;f=stdlib;hb=refs/heads/release/2.21/master
 */
public class GNULibCRandom implements RandRNG {

	/* For each of the currently supported random number generators, we have a
	   break value on the amount of state information (you need at least this many
	   bytes of state info to support this random number generator), a degree for
	   the polynomial (actually a trinomial) that the R.N.G. is based on, and
	   separation between the two lower order coefficients of the trinomial. */

	/* Linear congruential. */
	protected static final int TYPE_0 = 0;
	protected static final int BREAK_0 = 8;
	protected static final int DEG_0 = 0;
	protected static final int SEP_0 = 0;

	/* x**7 + x**3 + 1. */
	protected static final int TYPE_1 = 1;
	protected static final int BREAK_1 = 32;
	protected static final int DEG_1 = 7;
	protected static final int SEP_1 = 3;

	/* x**15 + x + 1. */
	protected static final int TYPE_2 = 2;
	protected static final int BREAK_2 = 64;
	protected static final int DEG_2 = 15;
	protected static final int SEP_2 = 1;

	/* x**31 + x**3 + 1. */
	protected static final int TYPE_3 = 3;
	protected static final int BREAK_3 = 128;
	protected static final int DEG_3 = 31;
	protected static final int SEP_3 = 3;

	/* x**63 + x + 1. */
	protected static final int TYPE_4 = 4;
	protected static final int BREAK_4 = 256;
	protected static final int DEG_4 = 63;
	protected static final int SEP_4 = 1;

	protected final RandState unsafeState = new RandState();


	@Override
	public void srand(int newSeed) {
		srandom_r(newSeed, unsafeState);
	}

	@Override
	public int rand() {
		return random_r(unsafeState);
	}


	/**
	 * Initialize the random number generator based on the given seed.
	 *
	 * If the type is the trivial no-state-information type, just remember the
	 * seed. Otherwise, initializes state[] based on the given "seed" via a
	 * linear congruential generator. Then, the pointers are set to known
	 * locations that are exactly rand_sep places apart. Lastly, it cycles the
	 * state information a given number of times to get rid of any initial
	 * dependencies introduced by the L.C.R.N.G.  Note that the initialization
	 * of randtbl[] for default usage relies on values produced by this
	 * routine.
	 */
	public void srandom_r(int newSeed, RandState buf) {
		int statePtr = buf.getStatePtr();

		// We must make sure the seed is not 0. Take arbitrarily 1 in this case.
		if (newSeed == 0) newSeed = 1;

		buf.setTbl(statePtr, newSeed);
		if (buf.getRandType() == TYPE_0) return;

		int dstPtr = statePtr;
		int word = newSeed;
		int kc = buf.getRandDeg();

		for (int i=1; i < kc; i++) {
			// This does:
			//   state[i] = (16807 * state[i - 1]) % 2147483647;
			// but avoids overflowing 31 bits.
			long hi = word / 127773;
			long lo = word % 127773;
			word = (int) (16807 * lo - 2836 * hi);
			if (word < 0) {
				word += 2147483647;
			}
			buf.setTbl(++dstPtr, word);
		}
		buf.setFPtr(statePtr + buf.getRandSep());
		buf.setRPtr(statePtr + 0);
		kc *= 10;
		while (--kc >= 0) {
			int discard = random_r(buf);
		}
	}

	/**
	 * Returns a 31-bit random number.
	 *
	 * If we are using the trivial TYPE_0 R.N.G., just do the old linear
	 * congruential bit. Otherwise, we do our fancy trinomial stuff, which is
	 * the same in all the other cases due to all the global variables that
	 * have been set up. The basic operation is to add the number at the rear
	 * pointer into the one at the front pointer. Then both pointers are
	 * advanced to the next location cyclically in the table. The value
	 * returned is the sum generated, reduced to 31 bits by throwing away the
	 * "least random" low bit.
	 *
	 * Note: The code takes advantage of the fact that both the front and rear
	 * pointers can't wrap on the same call by not testing the rear pointer if
	 * the front one has wrapped.
	 */
	public int random_r(RandState buf) {
		int result;

		int statePtr = buf.getStatePtr();

		if (buf.randType == TYPE_0) {
			int val = ((buf.getTbl(statePtr + 0) * 1103515245) + 12345) & 0x7fffffff;
			buf.setTbl(statePtr + 0, val);
			result = val;
		}
		else {
			int fPtr = buf.getFPtr();
			int rPtr = buf.getRPtr();
			int endPtr = buf.getEndPtr();

			buf.setTbl(fPtr, (buf.getTbl(fPtr) + buf.getTbl(rPtr)));
			int val = buf.getTbl(fPtr);
			/* Chucking least random bit. */
			result = (val >> 1) & 0x7fffffff;

			++fPtr;
			if (fPtr >= endPtr) {
				fPtr = statePtr;
				++rPtr;
			}
			else {
				++rPtr;
				if (rPtr >= endPtr) {
					rPtr = statePtr;
				}
			}
			buf.setFPtr(fPtr);
			buf.setRPtr(rPtr);
		}

		return result;
	}



	public static class RandState {
		protected int[] randtbl = new int[] {
			TYPE_3,
			-1726662223, 379960547, 1735697613, 1040273694, 1313901226,
			1627687941, -179304937, -2073333483, 1780058412, -1989503057,
			-615974602, 344556628, 939512070, -1249116260, 1507946756,
			-812545463, 154635395, 1388815473, -1926676823, 525320961,
			-1009028674, 968117788, -123449607, 1284210865, 435012392,
			-2017506339, -911064859, -370259173, 1132637927, 1398500161,
			-205601318
		};

		// Ptr vars here are array indeces in randtbl. See setTbl()/getTbl().

		protected int statePtr = 1;
		protected int fPtr = SEP_3+1;
		protected int rPtr = 1;

		protected int randType = TYPE_3;
		public int randDeg = DEG_3;
		public int randSep = SEP_3;

		protected int endPtr = randtbl.length;

		public RandState() {
		}

		public void setRandType(int type) { randType = type; }
		public int getRandType() { return randType; }

		public void setRandDeg(int n) { randDeg = n; }
		public int getRandDeg() { return randDeg; }

		public void setRandSep(int n) { randSep = n; }
		public int getRandSep() { return randSep; }

		public void setTbl(int index, int n) { randtbl[index] = n; }
		public int getTbl(int index) { return randtbl[index]; }

		public void setStatePtr(int index) { statePtr = index; }
		public int getStatePtr() { return statePtr; }

		public void setFPtr(int index) { fPtr = index; }
		public int getFPtr() { return fPtr; }

		public void setRPtr(int index) { rPtr = index; }
		public int getRPtr() { return rPtr; }

		public int getEndPtr() { return endPtr; }
	}
}
