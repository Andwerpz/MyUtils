package myutils.math;

public class Mat3 {

	public float[][] mat = new float[3][3];

	public Mat3() {

	}

	public Mat3(float[][] mat) {
		this.set(mat);
	}

	public Mat3(Mat3 m) {
		this.set(m.mat);
	}

	public void set(float[][] mat) {
		assert mat.length == 3 && mat[0].length == 3 : "input matrix must be 3x3";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.mat[i][j] = mat[i][j];
			}
		}
	}

	public Mat3 transpose() {
		float[][] n_mat = new float[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				n_mat[i][j] = mat[j][i];
			}
		}
		this.mat = n_mat;
		return this;
	}

	public Mat3 addi(Mat3 m) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.mat[i][j] += m.mat[i][j];
			}
		}
		return this;
	}

	public Mat3 add(Mat3 m) {
		return new Mat3(this).addi(m);
	}

	public Vec3 mul(Vec3 v) {
		Vec3 ret = new Vec3(0);
		ret.x = mat[0][0] * v.x + mat[0][1] * v.y + mat[0][2] * v.z;
		ret.y = mat[1][0] * v.x + mat[1][1] * v.y + mat[1][2] * v.z;
		ret.z = mat[2][0] * v.x + mat[2][1] * v.y + mat[2][2] * v.z;
		return ret;
	}

}
