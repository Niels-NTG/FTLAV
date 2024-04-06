package net.blerf.ftl.parser.random;

import java.math.BigInteger;


/**
 * An implementation of FTLGame.srandom32()/random32().
 * <p>
 * FTL 1.6.1 introduced a new hard-coded RNG, so results would be consistent
 * across platforms. Previous editions relied on native OS calls to
 * srand()/rand().
 * <p>
 * FTL 1.6.1+ will fall back to using the native RNG for legacy saved
 * games migrated from an older edition.
 * <p>
 * TODO: Determine whether FTL 1.6.1+ resorts to native RNGs based on a flag in
 * the saved game format or on a moment-to-moment basis as exceptions happen.
 */
public class FTL_1_6_Random implements RandRNG {

    protected static final BigInteger UINT_MASK = new BigInteger("ffffffff", 16);
    protected static final BigInteger HIGH_MASK = new BigInteger("ffffffff00000000", 16);
    protected static final BigInteger LOW_MASK = new BigInteger("00000000ffffffff", 16);
    protected static final BigInteger FULL_MASK = new BigInteger("ffffffffffffffff", 16);

    // Math is gonna reach 64bit unsigned long territory.
    protected BigInteger seed = new BigInteger("1");

    protected String name = null;


    public FTL_1_6_Random() {
        this(null);
    }

    public FTL_1_6_Random(String name) {
        this.name = name;
    }

    @Override
    public void srand(int newSeed) {
        seed = new BigInteger(Integer.toString(newSeed));
    }

    @Override
    public int rand() {
        BigInteger seedHigh = seed.shiftRight(32).and(UINT_MASK);
        BigInteger seedLow = seed.and(UINT_MASK);

        // Isolate high bits, then mask and bitwise-or them back onto the var.
        BigInteger z = new BigInteger("1284865837").multiply(seedLow);
        BigInteger zHigh = z.shiftRight(32).and(UINT_MASK);
        zHigh = zHigh.add(new BigInteger("1481765933").multiply(seedLow));
        zHigh = zHigh.add(new BigInteger("1284865837").multiply(seedHigh));

        z = zHigh.shiftLeft(32).and(HIGH_MASK).or(z.and(LOW_MASK));
        z = z.add(new BigInteger("1")).and(FULL_MASK);

        seed = z;
        return z.shiftRight(32).shiftRight(1).intValue();

        // After masking 64 bits, shifting 32, and shifting 1 bit more,
        // return value should be 31 bits, safe to hold in a 32bit signed int.
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
