package myutils.noise.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import myutils.math.Vec3;

public class PerlinNoise extends SeededNoiseGenerator {
	//mostly copied from the original ken perlin implementation

	static final int permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125,
			136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164,
			100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110,
			79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24,
			72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };

	//frequency : positive value controlling how fine the noise is. Higher values are more fine
	private float frequency = 1;

	//amplitude : real value multiplied with the result of the noise. Range of noise is from -amplitude to amplitude
	private float amplitude = 1;

	//persistence : 0 - 1 controlling how quickly later octaves die out. 0.5 is good
	private float persistence = 0.5f;

	//lacunarity : greater than 1 controlling how much finer the later octaves get, 2 is good
	private float lacunarity = 2;

	//octaves : integer value giving the number of layers of noise. 
	private int octaves = 4;

	//the permutation that this instance of noise is running on.
	private int[] p;

	public PerlinNoise() {

	}

	public PerlinNoise(int seed) {
		super(seed);
	}

	@Override
	protected void _setSeed() {
		if (this.p == null) {
			this.p = new int[512];
		}

		//regenerate permutation
		Random rand = new Random();
		rand.setSeed(this.getSeed());
		for (int i = 0; i < 256; i++) {
			this.p[i] = permutation[i];
		}
		for (int i = 255; i >= 0; i--) {
			int ind = rand.nextInt(i + 1);
			int tmp = this.p[i];
			this.p[i] = this.p[ind];
			this.p[ind] = tmp;
		}
		for (int i = 0; i < 256; i++) {
			p[256 + i] = p[i];
		}
	}

	@Override
	protected float _sampleNoise(Vec3 v) {
		return this.noise(v.x, v.y, v.z, this.frequency, this.amplitude, this.persistence, this.lacunarity, this.octaves);
	}

	private float noise(float x, float y, float z, float frequency, float amplitude, float persistence, float lacunarity, int octaves) {
		assert octaves >= 1 : "Octaves must be greater than or equal to 1";

		float ans = 0;
		for (int i = 0; i < octaves; i++) {
			ans += _noise(x * frequency, y * frequency, z * frequency) * amplitude;
			amplitude *= persistence;
			frequency *= lacunarity;
		}
		return ans;
	}

	private float _noise(float x, float y, float z) {
		int X = (int) Math.floor(x) & 255, // FIND UNIT CUBE THAT
				Y = (int) Math.floor(y) & 255, // CONTAINS POINT.
				Z = (int) Math.floor(z) & 255;
		x -= Math.floor(x); // FIND RELATIVE X,Y,Z
		y -= Math.floor(y); // OF POINT IN CUBE.
		z -= Math.floor(z);
		float u = fade(x), // COMPUTE FADE CURVES
				v = fade(y), // FOR EACH OF X,Y,Z.
				w = fade(z);
		int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, // HASH COORDINATES OF
				B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z; // THE 8 CUBE CORNERS,

		return lerp(w, lerp(v, lerp(u, grad(p[AA], x, y, z), // AND ADD
				grad(p[BA], x - 1, y, z)), // BLENDED
				lerp(u, grad(p[AB], x, y - 1, z), // RESULTS
						grad(p[BB], x - 1, y - 1, z))), // FROM  8
				lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), // CORNERS
						grad(p[BA + 1], x - 1, y, z - 1)), // OF CUBE
						lerp(u, grad(p[AB + 1], x, y - 1, z - 1), grad(p[BB + 1], x - 1, y - 1, z - 1))));
	}

	private static float fade(float t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static float lerp(float t, float a, float b) {
		return a + t * (b - a);
	}

	private static float grad(int hash, float x, float y, float z) {
		int h = hash & 15; // CONVERT LO 4 BITS OF HASH CODE
		float u = h < 8 ? x : y, // INTO 12 GRADIENT DIRECTIONS.
				v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getPersistence() {
		return persistence;
	}

	public void setPersistence(float persistence) {
		this.persistence = persistence;
	}

	public float getLacunarity() {
		return lacunarity;
	}

	public void setLacunarity(float lacunarity) {
		this.lacunarity = lacunarity;
	}

	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
	}
}