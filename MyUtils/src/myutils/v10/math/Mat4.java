package myutils.v10.math;

public class Mat4 {

	public float[][] mat = new float[4][4];

	public Mat4() {

	}

	public Mat4(float[][] mat) {
		if (mat.length != 4 || mat[0].length != 4) {
			throw new IllegalArgumentException("input matrix must be 4x4");
		}

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.mat[i][j] = mat[i][j];
			}
		}
	}

	public Mat4(Mat4 mat) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.mat[i][j] = mat.mat[i][j];
			}
		}
	}

	public Mat4(Vec3 row1, Vec3 row2, Vec3 row3) {
		mat[0][0] = row1.x;
		mat[0][1] = row1.y;
		mat[0][2] = row1.z;

		mat[1][0] = row2.x;
		mat[1][1] = row2.y;
		mat[1][2] = row2.z;

		mat[2][0] = row3.x;
		mat[2][1] = row3.y;
		mat[2][2] = row3.z;
	}

	public void set(Mat4 m) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.mat[i][j] = m.mat[i][j];
			}
		}
	}

	public Mat4 transpose() {
		float[][] nextMat = new float[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				nextMat[i][j] = this.mat[j][i];
			}
		}
		this.mat = nextMat;
		return this;
	}

	public static Mat4 identity() {
		Mat4 result = new Mat4();

		for (int i = 0; i < 4; i++) {
			result.mat[i][i] = 1;
		}

		return result;
	}

	/**
	 * Makes an orthographic projection matrix.
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 * @return
	 */

	public static Mat4 orthographic(float left, float right, float bottom, float top, float near, float far) {
		Mat4 result = identity();

		result.mat[0][0] = 2f / (right - left);
		result.mat[1][1] = 2f / (top - bottom);
		result.mat[2][2] = -2f / (far - near);
		result.mat[0][3] = -(right + left) / (right - left);
		result.mat[1][3] = -(top + bottom) / (top - bottom);
		result.mat[2][3] = -(far + near) / (far - near);

		return result;
	}

	/**
	 * Makes a perspective projection matrix
	 * 
	 * @param viewAngleRad
	 * @param width
	 * @param height
	 * @param nearClippingPlaneDistance
	 * @param farClippingPlaneDistance
	 * @return
	 */

	public static Mat4 perspective(float viewAngleRad, float width, float height, float nearClippingPlaneDistance, float farClippingPlaneDistance) {
		final float radians = viewAngleRad;

		float halfHeight = (float) (Math.tan(radians / 2) * nearClippingPlaneDistance);

		float halfScaledAspectRatio = halfHeight * (width / height);

		Mat4 projection = perspectiveFrustum(-halfScaledAspectRatio, halfScaledAspectRatio, -halfHeight, halfHeight, nearClippingPlaneDistance, farClippingPlaneDistance);

		return projection;
	}

	private static Mat4 perspectiveFrustum(float left, float right, float bottom, float top, float near, float far) {
		Mat4 result = new Mat4();

		result.mat[0][0] = (2f * near) / (right - left);
		result.mat[2][0] = (right + left) / (right - left);

		result.mat[1][1] = (2 * near) / (top - bottom);
		result.mat[2][1] = (top + bottom) / (top - bottom);

		result.mat[2][2] = -(far + near) / (far - near);
		result.mat[2][3] = -2 * (far * near) / (far - near);

		result.mat[3][2] = -1;
		result.mat[3][3] = 0;

		return result;
	}

	/**
	 * Essentially the same as glm::lookAt(). Eye is the viewing position. Center is
	 * the position that you are looking at.
	 * 
	 * This matrix transforms space so that eye is at the origin, and the vector
	 * from eye to center looks down the -z axis.
	 * 
	 * @param eye
	 * @param pos
	 * @param up
	 * @return
	 */

	public static Mat4 lookAt(Vec3 eye, Vec3 center, Vec3 up) {
		// define our 3 basis vectors for the new space
		Vec3 z = new Vec3(center, eye).normalize(); // Z
		Vec3 x = up.cross(z).normalize();
		Vec3 y = z.cross(x).normalize();

		Mat4 result = Mat4.translate(eye.mul(-1f));
		Mat4 viewSpace = new Mat4(x, y, z);
		viewSpace.mat[3][3] = 1;
		result.muli(viewSpace);

		return result;
	}

	public static Mat4 translate(Vec3 vec) {
		Mat4 result = identity();

		result.mat[0][3] = vec.x;
		result.mat[1][3] = vec.y;
		result.mat[2][3] = vec.z;

		return result;
	}

	public static Mat4 translate(float x, float y, float z) {
		return Mat4.translate(new Vec3(x, y, z));
	}

	/**
	 * Returns a matrix that will rotate around the z axis
	 * @param rad
	 * @return
	 */
	public static Mat4 rotateZ(float rad) {
		Mat4 result = identity();
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		result.mat[0][0] = cos;
		result.mat[1][0] = -sin;
		result.mat[0][1] = sin;
		result.mat[1][1] = cos;

		return result;
	}

	/**
	 * Returns a matrix that will rotate around the x axis
	 * @param rad
	 * @return
	 */
	public static Mat4 rotateX(float rad) {
		Mat4 result = identity();
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		result.mat[1][1] = cos;
		result.mat[2][1] = -sin;
		result.mat[1][2] = sin;
		result.mat[2][2] = cos;

		return result;
	}

	/**
	 * Returns a matrix that will rotate around the y axis
	 * @param rad
	 * @return
	 */
	public static Mat4 rotateY(float rad) {
		Mat4 result = identity();
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		result.mat[0][0] = cos;
		result.mat[2][0] = sin;
		result.mat[0][2] = -sin;
		result.mat[2][2] = cos;

		return result;
	}

	/**
	 * Returns a matrix that will rotate around the given axis, by the given angle
	 * 
	 * https://stackoverflow.com/questions/6721544/circular-rotation-around-an-arbitrary-axis
	 * @param axis
	 * @param rad
	 * @return
	 */
	public static Mat4 rotate(Vec3 axis, float rad) {
		axis.normalize();

		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		Mat4 result = identity();
		result.mat[0][0] = cos + axis.x * axis.x * (1.0f - cos);
		result.mat[0][1] = axis.x * axis.y * (1.0f - cos) - axis.z * sin;
		result.mat[0][2] = axis.x * axis.z * (1.0f - cos) + axis.y * sin;

		result.mat[1][0] = axis.x * axis.y * (1.0f - cos) + axis.z * sin;
		result.mat[1][1] = cos + axis.y * axis.y * (1.0f - cos);
		result.mat[1][2] = axis.y * axis.z * (1.0f - cos) - axis.x * sin;

		result.mat[2][0] = axis.x * axis.z * (1.0f - cos) - axis.y * sin;
		result.mat[2][1] = axis.y * axis.z * (1.0f - cos) + axis.x * sin;
		result.mat[2][2] = cos + axis.z * axis.z * (1.0f - cos);

		return result;
	}

	/**
	 * Returns a matrix that when applied to the input vector 'a', will rotate it so that it is parallel to 'b'. 
	 * 
	 * Does not work in the case where a.dot(b) == -1. 
	 * 
	 * https://math.stackexchange.com/questions/180418/calculate-rotation-matrix-to-align-vector-a-to-vector-b-in-3d
	 * @param basis
	 * @param dir
	 * @return
	 */
	public static Mat4 rotateAToB(Vec3 a, Vec3 b) {
		a.normalize();
		b.normalize();

		Vec3 v = a.cross(b);
		float sin = v.length();
		float cos = a.dot(b);

		Mat4 vx = Mat4.identity();
		vx.mat[0][0] = 0;
		vx.mat[0][1] = -v.z;
		vx.mat[0][2] = v.y;

		vx.mat[1][0] = v.z;
		vx.mat[1][1] = 0;
		vx.mat[1][2] = -v.x;

		vx.mat[2][0] = -v.y;
		vx.mat[2][1] = v.x;
		vx.mat[2][2] = 0;

		vx.mat[3][3] = 0;

		Mat4 ret = Mat4.identity().add(vx).add(vx.mul(vx).mul(1.0f / (1.0f + cos)));
		return ret;
	}

	public static Mat4 scale(float amt) {
		Mat4 result = Mat4.identity();
		for (int i = 0; i < 3; i++) {
			result.mat[i][i] = amt;
		}

		return result;
	}

	public static Mat4 scale(float xAmt, float yAmt, float zAmt) {
		Mat4 result = Mat4.identity();
		result.mat[0][0] = xAmt;
		result.mat[1][1] = yAmt;
		result.mat[2][2] = zAmt;

		return result;
	}

	/**
	 * Returns a new matrix equal to the product between itself and the input
	 * 
	 * @param matrix
	 * @return
	 */
	public Mat4 mul(Mat4 matrix) {
		Mat4 result = new Mat4();

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				float sum = 0f;
				for (int e = 0; e < 4; e++) {
					sum += this.mat[e][y] * matrix.mat[x][e];
				}
				result.mat[x][y] = sum;
			}
		}

		return result;
	}

	/**
	 * Sets itself equal to the product from a multiplication with itself and the
	 * input
	 * 
	 * @param matrix
	 * @return
	 */
	public Mat4 muli(Mat4 matrix) {
		this.mat = this.mul(matrix).mat;
		return this;
	}

	public Mat4 mul(float f) {
		Mat4 result = new Mat4();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.mat[i][j] = this.mat[i][j] * f;
			}
		}

		return result;
	}

	public Mat4 muli(float f) {
		this.mat = this.mul(f).mat;
		return this;
	}

	public Vec3 mul(Vec3 vec, float w) {
		//		return new Vec3(
		//			vec.x * mat[0][0] + vec.y * mat[1][0] + vec.z * mat[2][0] + w * mat[3][0],
		//			vec.x * mat[0][1] + vec.y * mat[1][1] + vec.z * mat[2][1] + w * mat[3][1],
		//			vec.x * mat[0][2] + vec.y * mat[1][2] + vec.z * mat[2][2] + w * mat[3][2]
		//		);

		return new Vec3(vec.x * mat[0][0] + vec.y * mat[0][1] + vec.z * mat[0][2] + w * mat[0][3], vec.x * mat[1][0] + vec.y * mat[1][1] + vec.z * mat[1][2] + w * mat[1][3], vec.x * mat[2][0] + vec.y * mat[2][1] + vec.z * mat[2][2] + w * mat[2][3]);
	}

	public Mat4 add(Mat4 matrix) {
		Mat4 result = new Mat4();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.mat[i][j] = matrix.mat[i][j] + this.mat[i][j];
			}
		}

		return result;
	}

	public Mat4 addi(Mat4 matrix) {
		this.mat = this.add(matrix).mat;
		return this;
	}

	@Override
	public String toString() {
		String out = "";
		for (float[] i : mat) {
			for (float j : i) {
				out += j + " ";
			}
			out += "\n";
		}
		return out;
	}

}
