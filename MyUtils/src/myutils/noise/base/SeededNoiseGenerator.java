package myutils.noise.base;

import myutils.noise.NoiseGenerator;

public abstract class SeededNoiseGenerator extends NoiseGenerator {
	//this generates noise, but also guarantees that the noise produced at a coordinate will be the same, if the seed is the same. 

	//it should be guaranteed that _setSeed() is called at least once when the instance gets created. 

	private int seed;

	public SeededNoiseGenerator() {
		//WYSI!!!
		this.setSeed(727);
	}

	public SeededNoiseGenerator(int seed) {
		this.setSeed(seed);
	}

	public void setSeed(int seed) {
		this.seed = seed;
		this._setSeed();
	}

	//to notify the child class of the seed potentially changing. 
	protected abstract void _setSeed();

	public int getSeed() {
		return this.seed;
	}

}
