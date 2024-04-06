package net.blerf.ftl.parser.random;

/**
 * This class calls srand()/rand() from platform-dependent C libraries.
 * <p>
 * The RNG state is global for the entire process, so multiple instances will
 * interfere with each other. Instances on different OSs will yield different
 * random results for a given seed.
 */
public class NativeRandomJNI {
    public native void native_srand(int seed);

    public native int native_rand();

    static {
        System.loadLibrary("rand");
    }
}
