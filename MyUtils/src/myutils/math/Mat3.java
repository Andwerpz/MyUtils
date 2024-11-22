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

	public void set(Mat3 m) {
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

	public Mat3 muli(Mat3 m) {
		Mat3 result = new Mat3();
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				float sum = 0f;
				for (int e = 0; e < 3; e++) {
					sum += this.mat[e][y] * m.mat[x][e];
				}
				result.mat[x][y] = sum;
			}
		}
		this.set(result);
		return this;
	}

	public Mat3 mul(Mat3 m) {
		return new Mat3(this).muli(m);
	}

	public Mat3 muli(float s) {
		Mat3 result = new Mat3();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				result.mat[i][j] = this.mat[i][j] * s;
			}
		}
		this.set(result);
		return this;
	}

	public Mat3 mul(float s) {
		return new Mat3(this).muli(s);
	}

	public Vec3 mul(Vec3 v) {
		Vec3 ret = new Vec3(0);
		ret.x = mat[0][0] * v.x + mat[0][1] * v.y + mat[0][2] * v.z;
		ret.y = mat[1][0] * v.x + mat[1][1] * v.y + mat[1][2] * v.z;
		ret.z = mat[2][0] * v.x + mat[2][1] * v.y + mat[2][2] * v.z;
		return ret;
	}

	public Mat3 inverse() {
		float det = determinant();
		if (det == 0) {
			return null; // The matrix is not invertible
		}
		float invDet = 1.0f / det;

		float[][] invMat = new float[3][3];

		// Calculate the cofactors and multiply by the inverse of the determinant
		invMat[0][0] = invDet * (mat[1][1] * mat[2][2] - mat[1][2] * mat[2][1]);
		invMat[0][1] = invDet * (mat[0][2] * mat[2][1] - mat[0][1] * mat[2][2]);
		invMat[0][2] = invDet * (mat[0][1] * mat[1][2] - mat[0][2] * mat[1][1]);

		invMat[1][0] = invDet * (mat[1][2] * mat[2][0] - mat[1][0] * mat[2][2]);
		invMat[1][1] = invDet * (mat[0][0] * mat[2][2] - mat[0][2] * mat[2][0]);
		invMat[1][2] = invDet * (mat[0][2] * mat[1][0] - mat[0][0] * mat[1][2]);

		invMat[2][0] = invDet * (mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0]);
		invMat[2][1] = invDet * (mat[0][1] * mat[2][0] - mat[0][0] * mat[2][1]);
		invMat[2][2] = invDet * (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]);

		return new Mat3(invMat);
	}

	// Helper method to calculate the determinant of the matrix
	public float determinant() {
		return mat[0][0] * (mat[1][1] * mat[2][2] - mat[1][2] * mat[2][1]) - mat[0][1] * (mat[1][0] * mat[2][2] - mat[1][2] * mat[2][0]) + mat[0][2] * (mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0]);
	}

	public static Mat3 identity() {
		Mat3 m = new Mat3();
		m.mat[0][0] = 1;
		m.mat[1][1] = 1;
		m.mat[2][2] = 1;
		return m;
	}

	@Override
	public String toString() {
		String out = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				out += mat[i][j] + " ";
			}
			out += "\n";
		}
		return out;
	}

}
