package net.blerf.ftl.parser.random;


public interface RandRNG {

	public void srand( int newSeed );

	public int rand();
}
