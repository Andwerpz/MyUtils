package myutils.noise;

public class NoiseManager extends NoiseGenerator {
	//this should allow you to chain noise up and add it and stuff
	
	private NoiseGenerator root;
	
	public NoiseManager() {
		
	}

	@Override
	protected float _sampleNoise(float x, float y, float z) {
		return root.sampleNoise(x, y, z);
	}
	
}
