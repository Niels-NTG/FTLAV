package net.blerf.ftl.parser.random;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;


/**
 * This class calls srand()/rand() from platform-dependent C libraries.
 * <p>
 * The RNG state is global for the entire process, so multiple instances will
 * interfere with each other. Instances on different OSs will yield different
 * random results for a given seed.
 */
public class NativeRandom implements RandRNG {

    protected int seed = 1;
    protected String name = null;


    public NativeRandom() {
        this(null);
    }

    public NativeRandom(String name) {
        this.name = name;
    }

    @Override
    public void srand(int newSeed) {
        CLibrary.INSTANCE.srand(newSeed);
        seed = newSeed;
    }

    static int count = 0;

    @Override
    public int rand() {
        return CLibrary.INSTANCE.rand();
    }

    @Override
    public void setName(String newName) {
        name = newName;
    }

    @Override
    public String toString() {
        return (name != null ? name : super.toString());
    }


    public interface CLibrary extends Library {
        /**
         * A singleton to use for making native function calls.
         */
        CLibrary INSTANCE = Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        /**
         * Returns a random int from 0 to RAND_MAX.
         * <p>
         * Afterward modulo (range limiting) and addition (shifting min) may be
         * performed on the result.
         * <p>
         * C++ has some additional funcs to generate random numbers.
         * <p>
         * This method is not thread-safe!
         * <p>
         * If multiple classes/objects load the underlying native library to call
         * RNG funcs, they will interfere with each other.
         */
        int rand();

        /**
         * Sets the seed for subsequent rand() calls.
         * <p>
         * This method is not thread-safe!
         * <p>
         * If multiple classes/objects load the underlying native library to call
         * RNG funcs, they will interfere with each other.
         */
        void srand(int seed);
    }
}
