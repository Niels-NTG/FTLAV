package net.blerf.ftl.parser.random;

/**
 * An implementation of srand()/rand() from Windows msvcrt.
 * <p>
 * The algorithm is contained within this class, so no external libraries
 * are involved. Each instance has its own separate state.
 * <p>
 * http://rosettacode.org/wiki/Linear_congruential_generator#C
 */
public class MsRandom implements RandRNG {

    public static final int RAND_MAX_32 = 0x7fffffff;  // 31bit.
    public static final int RAND_MAX = 0x7fff;         // 15bit.

    protected int seed = 1;
    protected String name = null;


    public MsRandom() {
        this(null);
    }

    public MsRandom(String name) {
        this.name = name;
    }

    @Override
    public void srand(int newSeed) {
        seed = newSeed;
    }

    @Override
    public int rand() {
        return (seed = (seed * 214013 + 2531011) & RAND_MAX_32) >> 16;
    }

    @Override
    public void setName(String newName) {
        name = newName;
    }

    @Override
    public String toString() {
        return (name != null ? name : super.toString());
    }
}
