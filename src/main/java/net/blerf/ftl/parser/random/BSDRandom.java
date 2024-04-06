package net.blerf.ftl.parser.random;

/**
 * An implementation of srand()/rand() from BSD.
 * <p>
 * The algorithm is contained within this class, so no external libraries
 * are involved. Each instance has its own separate state.
 * <p>
 * It is identical to the GNU Standard Library's TYPE_0 rand().
 * <p>
 * http://rosettacode.org/wiki/Linear_congruential_generator#C
 */
public class BSDRandom implements RandRNG {

    public static final int RAND_MAX = 0x7fffffff;  // 31bit.

    protected int seed = 1;
    protected String name = null;


    public BSDRandom() {
        this(null);
    }

    public BSDRandom(String name) {
        this.name = name;
    }

    @Override
    public void srand(int newSeed) {
        seed = newSeed;
    }

    @Override
    public int rand() {
        return seed = (seed * 1103515245 + 12345) & RAND_MAX;
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
