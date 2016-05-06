package net.blerf.ftl.parser.random;


public interface RandRNG {

	void srand(int newSeed);

	int rand();
}
