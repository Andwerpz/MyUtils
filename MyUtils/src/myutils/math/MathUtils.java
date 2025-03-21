package myutils.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

import myutils.misc.Pair;

public class MathUtils {

	public static final float EPSILON = 0.00001f;

	// MathTools v2, made with LWGJL in mind

	// -- GENERAL --

	/**
	 * Returns true if f is non-negative. 
	 * @param f
	 * @return
	 */
	public static boolean sign(float f) {
		return f >= 0;
	}

	/**
	 * Generates random float between low and high
	 * @param low
	 * @param high
	 * @return
	 */
	public static float random(float low, float high) {
		return (float) (Math.random() * (high - low) + low);
	}

	/**
	 * Generates a random vector in the bounding box defined by the two inputs
	 * @param low
	 * @param high
	 * @return
	 */
	public static Vec2 random(Vec2 low, Vec2 high) {
		return new Vec2(random(low.x, high.x), random(low.y, high.y));
	}

	/**
	 * Generates a random vector in the bounding box defined by the two inputs
	 * @param low
	 * @param high
	 * @return
	 */
	public static Vec3 random(Vec3 low, Vec3 high) {
		return new Vec3(random(low.x, high.x), random(low.y, high.y), random(low.z, high.z));
	}

	/**
	 * Generates a random (uniform) point on the unit circle
	 * @return
	 */
	public static Vec2 randomUnitDir2D() {
		while (true) {
			Vec2 dir = random(new Vec2(-1), new Vec2(1));
			if (dir.lengthSq() < 1) {
				dir.normalize();
				return dir;
			}
		}
	}

	/**
	 * Generates a random (uniform) point on the unit sphere
	 * @return
	 */
	public static Vec3 randomUnitDir3D() {
		while (true) {
			Vec3 dir = random(new Vec3(-1), new Vec3(1));
			if (dir.lengthSq() < 1) {
				dir.normalize();
				return dir;
			}
		}
	}

	/**
	 * Returns the dot product between two vectors
	 * @param a
	 * @param b
	 * @return
	 */
	public static float dot(Vec3 a, Vec3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	/**
	 * Returns the cross product between two vectors. 
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec3 cross(Vec3 a, Vec3 b) {
		Vec3 result = new Vec3(0);
		result.x = a.y * b.z - a.z * b.y;
		result.y = a.z * b.x - a.x * b.z;
		result.z = a.x * b.y - a.y * b.x;
		return result;
	}

	/**
	 * Takes in two points and returns the squared distance. 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float distSq(Vec3 a, Vec3 b) {
		return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z);
	}

	/**
	 * Takes in two points and returns the distance between them. 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float dist(Vec3 a, Vec3 b) {
		return (float) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
	}

	/**
	 * Takes in a value, and returns it clamped to two other inputs
	 * 
	 * @param low
	 * @param high
	 * @param val
	 * @return
	 */
	public static float clamp(float low, float high, float val) {
		return val < low ? low : (val > high ? high : val);
	}

	/**
	 * Takes in a value, and returns it clamped to two other inputs
	 * 
	 * @param low
	 * @param high
	 * @param val
	 * @return
	 */
	public static double clamp(double low, double high, double val) {
		return val < low ? low : (val > high ? high : val);
	}

	/**
	 * Takes in a value, and returns it clamped to two other inputs
	 * 
	 * @param low
	 * @param high
	 * @param val
	 * @return
	 */
	public static int clamp(int low, int high, int val) {
		return val < low ? low : (val > high ? high : val);
	}

	/**
	 * Takes in a value, and returns it clamped to two other inputs
	 * 
	 * @param low
	 * @param high
	 * @param val
	 * @return
	 */
	public static long clamp(long low, long high, long val) {
		return val < low ? low : (val > high ? high : val);
	}

	/**
	 * Does component wise clamping on vector
	 * @param low
	 * @param high
	 * @param val
	 * @return
	 */
	public static Vec3 clamp(float low, float high, Vec3 val) {
		return new Vec3(clamp(low, high, val.x), clamp(low, high, val.y), clamp(low, high, val.z));
	}

	/**
	 * Rounds floating point down to the closest int 
	 * 
	 * @param val
	 * @return
	 */
	public static int floor(float val) {
		return (int) Math.floor(val);
	}

	/**
	 * Rounds floating point up to closest int
	 * 
	 * @param val
	 * @return
	 */
	public static int ceil(float val) {
		return (int) Math.ceil(val);
	}

	/**
	 * Generates an IVec3 based on the componentwise floor of the input Vec3
	 * @param v
	 * @return
	 */
	public static IVec3 floor(Vec3 v) {
		return new IVec3((int) Math.floor(v.x), (int) Math.floor(v.y), (int) Math.floor(v.z));
	}

	/**
	 * Generates an IVec3 based on the componentwise ceil of the input Vec3
	 * @param v
	 * @return
	 */
	public static IVec3 ceil(Vec3 v) {
		return new IVec3((int) Math.ceil(v.x), (int) Math.ceil(v.y), (int) Math.ceil(v.z));
	}

	/**
	 * Returns a component wise minimum vector
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec3 min(Vec3 a, Vec3 b) {
		return new Vec3(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
	}

	/**
	 * Returns a component wise minimum vector
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec2 min(Vec2 a, Vec2 b) {
		return new Vec2(Math.min(a.x, b.x), Math.min(a.y, b.y));
	}

	/**
	 * Returns a component wise minimum vector
	 * @param a
	 * @param b
	 * @return
	 */
	public static IVec2 min(IVec2 a, IVec2 b) {
		return new IVec2(Math.min(a.x, b.x), Math.min(a.y, b.y));
	}

	/**
	 * Returns a component wise maximum vector
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec3 max(Vec3 a, Vec3 b) {
		return new Vec3(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
	}

	/**
	 * Returns a component wise maximum vector
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec2 max(Vec2 a, Vec2 b) {
		return new Vec2(Math.max(a.x, b.x), Math.max(a.y, b.y));
	}

	/**
	 * Returns a component wise maximum vector
	 * @param a
	 * @param b
	 * @return
	 */
	public static IVec2 max(IVec2 a, IVec2 b) {
		return new IVec2(Math.max(a.x, b.x), Math.max(a.y, b.y));
	}

	/**
	 * Returns a component wise minimum vector out of all the vectors in the array
	 * @param a
	 * @param b
	 * @return
	 */
	public static IVec2 min(IVec2[] arr) {
		IVec2 ret = arr[0];
		for (int i = 1; i < arr.length; i++) {
			ret = min(ret, arr[i]);
		}
		return ret;
	}

	/**
	 * Returns a component wise maximum vector out of all the vectors in the array
	 * @param a
	 * @param b
	 * @return
	 */
	public static IVec2 max(IVec2[] arr) {
		IVec2 ret = arr[0];
		for (int i = 1; i < arr.length; i++) {
			ret = max(ret, arr[i]);
		}
		return ret;
	}

	/**
	 * Returns a component wise minimum vector out of all the vectors in the array
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec3 min(Vec3[] arr) {
		Vec3 ret = arr[0];
		for (int i = 1; i < arr.length; i++) {
			ret = min(ret, arr[i]);
		}
		return ret;
	}

	/**
	 * Returns a component wise maximum vector out of all the vectors in the array
	 * @param a
	 * @param b
	 * @return
	 */
	public static Vec3 max(Vec3[] arr) {
		Vec3 ret = arr[0];
		for (int i = 1; i < arr.length; i++) {
			ret = max(ret, arr[i]);
		}
		return ret;
	}

	/**
	 * Linearly interpolates between two points
	 * 
	 * @param x1
	 * @param t1
	 * @param x2
	 * @param t2
	 * @param t3
	 * @return
	 */
	public static float lerp(float x1, float t1, float x2, float t2, float t3) {
		float v = (x2 - x1) / (t2 - t1);
		return x1 + (t3 - t1) * v;
	}

	/**
	 * Linear interpolation between two points
	 * @param v1
	 * @param t1
	 * @param v2
	 * @param t2
	 * @param t3
	 * @return
	 */
	public static Vec2 lerp(Vec2 v1, float t1, Vec2 v2, float t2, float t3) {
		Vec2 v = (v2.sub(v1)).divi(t2 - t1);
		return v1.add(v.mul(t3 - t1));
	}

	/**
	 * Linear interpolation between two points
	 * @param v1
	 * @param t1
	 * @param v2
	 * @param t2
	 * @param t3
	 * @return
	 */
	public static Vec3 lerp(Vec3 v1, float t1, Vec3 v2, float t2, float t3) {
		Vec3 v = (v2.sub(v1)).divi(t2 - t1);
		return v1.add(v.mul(t3 - t1));
	}

	/**
	 * Spherical interpolation between two 3d points with t ranging between 0 and 1. 
	 * @param v1
	 * @param v2
	 * @param t
	 * @return
	 */
	public static Vec3 slerp(Vec3 v1, Vec3 v2, float t) {
		Vec3 v1n = new Vec3(v1);
		Vec3 v2n = new Vec3(v2);
		v1n.normalize();
		v2n.normalize();
		float omega = (float) Math.acos(v1n.dot(v2n));

		return v1.mul((float) (Math.sin((1 - t) * omega) / Math.sin(omega))).add(v2.mul((float) (Math.sin(t * omega) / Math.sin(omega))));
	}
	
	/**
	 * Linear interpolation of rotation quaternions where t is 0 to 1. 
	 * 
	 * q1, q2 need to be unit quaternions for this to work.
	 * 
	 * (q1^{-1} q2) represents rotation from q1 -> q2, then take it to the power t and multiply it with q1. 
	 * @param q1
	 * @param q2
	 * @param t
	 * @return
	 */
	public static Quaternion slerp(Quaternion q1, Quaternion q2, float t) {
		// If the dot product is negative, the quaternions have opposite handedness
	    // and slerp won't take the shorter path. So invert one quaternion.
		float dot = q1.s * q2.s + q1.i * q2.i + q1.j * q2.j + q1.k * q2.k;
		if (dot < 0.0f) {
	        q2 = new Quaternion(-q2.s, -q2.i, -q2.j, -q2.k);
	        dot = -dot;
	    }
		Quaternion tmp = q1.inv().mul(q2);
		tmp.powi(t);
		return q1.mul(tmp);
	}
	
	/**
	 * Linear interpolation of rotation quaternions. q1, q2 need to be unit quaternions for this to work.
	 * @param q1
	 * @param t1
	 * @param q2
	 * @param t2
	 * @param t
	 * @return
	 */
	public static Quaternion slerp(Quaternion q1, float t1, Quaternion q2, float t2, float t) {
		t = (t - t1) / (t2 - t1);	//normalize to [0, 1]
		return slerp(q1, q2, t);
	}

	// -- QUATERNIONS --

	/**
	 * Creates the quaternion representing the rotation from vector u to v. 
	 * @param u
	 * @param v
	 * @return
	 */
	public static Quaternion quaternionRotationUToV(Vec3 u, Vec3 v) {
		Vec3 a = u.cross(v);
		Quaternion ret = new Quaternion();
		ret.s = (float) Math.sqrt(u.lengthSq() + v.lengthSq()) + u.dot(v);
		ret.i = a.x;
		ret.j = a.y;
		ret.k = a.z;
		ret.normalize();
		return ret;
	}

	/**
	 * Returns the quaternion describing a rotation of theta around the axis defined by the vector (x, y, z). 
	 * 
	 * Note that you need to do conjugation by the returned quaternion, that is if the returned quaternion is q, and
	 * you want to rotate p by q, then you need to do qpq*, where q* is the conjugate of q. 
	 * @param theta
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Quaternion quaternionRotationAxisAngle(float theta, float x, float y, float z) {
		float c = (float) Math.cos(theta / 2);
		float s = (float) Math.sin(theta / 2);
		return new Quaternion(c, s * x, s * y, s * z);
	}

	/**
	 * Takes in a rotation quaternion and a vec3, and returns the vec3 rotated by the transform described by the quaternion. 
	 * @param q
	 * @param v
	 * @return
	 */
	public static Vec3 quaternionRotateVec3(Quaternion q, Vec3 v) {
		Quaternion qv = new Quaternion(0, v.x, v.y, v.z);
		qv = q.mul(qv).mul(q.inv());
		return new Vec3(qv.i, qv.j, qv.k);
	}

	/**
	 * Returns the quaternion described by the rotation matrix
	 * 
	 * Source : https://d3cw3dd2w32x2b.cloudfront.net/wp-content/uploads/2015/01/matrix-to-quat.pdf
	 * 
	 * Note that the article uses quaternions in the form (i, j, k, s), but I have it implemented like (s, i, j, k). 
	 * @param m
	 * @return
	 */
	public static Quaternion quaternionFromRotationMat4(Mat4 a) {
		Mat4 m = new Mat4(a);
		Quaternion q = null;
		float t = 0;
		if (m.mat[2][2] < 0) {
			if (m.mat[0][0] > m.mat[1][1]) {
				t = 1 + m.mat[0][0] - m.mat[1][1] - m.mat[2][2];
				q = new Quaternion(m.mat[1][2] - m.mat[2][1], t, m.mat[0][1] + m.mat[1][0], m.mat[2][0] + m.mat[0][2]);
			}
			else {
				t = 1 - m.mat[0][0] + m.mat[1][1] - m.mat[2][2];
				q = new Quaternion(m.mat[2][0] - m.mat[0][2], m.mat[0][1] + m.mat[1][0], t, m.mat[1][2] + m.mat[2][1]);
			}
		}
		else {
			if (m.mat[0][0] < -m.mat[1][1]) {
				t = 1 - m.mat[0][0] - m.mat[1][1] + m.mat[2][2];
				q = new Quaternion(m.mat[0][1] - m.mat[1][0], m.mat[2][0] + m.mat[0][2], m.mat[1][2] + m.mat[2][1], t);
			}
			else {
				t = 1 + m.mat[0][0] + m.mat[1][1] + m.mat[2][2];
				q = new Quaternion(t, m.mat[1][2] - m.mat[2][1], m.mat[2][0] - m.mat[0][2], m.mat[0][1] - m.mat[1][0]);
			}
		}
		q.muli(0.5f / (float) Math.sqrt(t));
		return q;
	}

	/**
	 * Returns the rotation matrix described by the quaternion. The quaternion must be a unit quaternion, so it is normalized first. 
	 * 
	 * The matrix describes the transformation qpq*, so the rotation stored in the quaternion must have theta / 2. 
	 * @param a
	 * @return
	 */
	public static Mat4 quaternionToRotationMat4(Quaternion _a) {
		float[][] mat = new float[4][4];

		Quaternion a = new Quaternion(_a);
		a.normalize();

		mat[0][0] = 1 - 2 * a.j * a.j - 2 * a.k * a.k;
		mat[0][1] = 2 * a.i * a.j - 2 * a.s * a.k;
		mat[0][2] = 2 * a.i * a.k + 2 * a.s * a.j;
		mat[0][3] = 0;

		mat[1][0] = 2 * a.i * a.j + 2 * a.s * a.k;
		mat[1][1] = 1 - 2 * a.i * a.i - 2 * a.k * a.k;
		mat[1][2] = 2 * a.j * a.k - 2 * a.s * a.i;
		mat[1][3] = 0;

		mat[2][0] = 2 * a.i * a.k - 2 * a.s * a.j;
		mat[2][1] = 2 * a.j * a.k + 2 * a.s * a.i;
		mat[2][2] = 1 - 2 * a.i * a.i - 2 * a.j * a.j;
		mat[2][3] = 0;

		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 1;

		return new Mat4(mat);
	}

	/**
	 * Returns the rotation matrix described by the quaternion. The quaternion must be a unit quaternion, so it is normalized first. 
	 * 
	 * The matrix describes the transformation qpq*, so the rotation stored in the quaternion must have theta / 2. 
	 * @param a
	 * @return
	 */
	public static Mat3 quaternionToRotationMat3(Quaternion _a) {
		float[][] mat = new float[3][3];

		Quaternion a = new Quaternion(_a);
		a.normalize();

		mat[0][0] = 1 - 2 * a.j * a.j - 2 * a.k * a.k;
		mat[0][1] = 2 * a.i * a.j - 2 * a.s * a.k;
		mat[0][2] = 2 * a.i * a.k + 2 * a.s * a.j;

		mat[1][0] = 2 * a.i * a.j + 2 * a.s * a.k;
		mat[1][1] = 1 - 2 * a.i * a.i - 2 * a.k * a.k;
		mat[1][2] = 2 * a.j * a.k - 2 * a.s * a.i;

		mat[2][0] = 2 * a.i * a.k - 2 * a.s * a.j;
		mat[2][1] = 2 * a.j * a.k + 2 * a.s * a.i;
		mat[2][2] = 1 - 2 * a.i * a.i - 2 * a.j * a.j;

		return new Mat3(mat);
	}

	/**
	 * Gives the angle between two unit quaternions. 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float quaternionAngleBetween(Quaternion a, Quaternion b) {
		Quaternion c = a.mul(b.inv());
		return c.s;
	}

	// -- GEOMETRY --

	/**
	 * Generates a random unit vector that is perpendicular to the given vector. 
	 * @param dir
	 * @return
	 */
	public static Vec3 generateRandomPerpendicularVec3(Vec3 dir) {
		//select vector that is not parallel with dir. 
		Vec3 u = new Vec3(dir.y, -dir.x, 0);
		if (Math.abs(1.0f - u.dot(dir)) < EPSILON) {
			u = new Vec3(dir.z, 0, -dir.x);
			if (Math.abs(1.0f - u.dot(dir)) < EPSILON) {
				u = new Vec3(0, dir.z, -dir.y);
			}
		}

		//generate vector that is perpendicular to dir
		Vec3 v = dir.cross(u);
		v.normalize();
		u.normalize();

		float theta = (float) (Math.random() * Math.PI * 2);
		Vec3 res = u.mul((float) Math.cos(theta)).add(v.mul((float) Math.sin(theta)));

		return res;
	}

	/**
	 * Calculates the normal of a triangle given the three points.
	 * 
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 computeTriangleNormal(Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 d0 = new Vec3(t0, t1);
		Vec3 d1 = new Vec3(t0, t2);
		return d0.cross(d1).normalize();
	}

	/**
	 * Returns difference between polar angle of b -> c from a -> b
	 * Positive orientation means CCW turn
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static float orientation(Vec2 a, Vec2 b, Vec2 c) {
		return (b.x - a.x) * (c.y - b.y) - (c.x - b.x) * (b.y - a.y);
	}

	/**
	 * Returns true if the three vectors are collinear
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static boolean collinear(Vec2 a, Vec2 b, Vec2 c) {
		return orientation(a, b, c) == 0;
	}

	/**
	 * Returns true if polygon is wound counterclockwise
	 * 
	 * @param points
	 * @return
	 */
	public static boolean isCounterClockwiseWinding(ArrayList<Vec2> points) {
		float sum = 0;
		for (int i = 0; i < points.size(); i++) {
			Vec2 a = points.get(i);
			Vec2 b = points.get((i + 1) % points.size());
			Vec2 c = points.get((i + 2) % points.size());
			float det = orientation(a, b, c);
			sum += det;
		}
		return sum > 0;
	}

	/**
	 * Returns true if the polygon is convex
	 * 
	 * @param points
	 * @return
	 */
	public static boolean isConvex(ArrayList<Vec2> points) {
		boolean clockwise = !isCounterClockwiseWinding(points);
		for (int i = 0; i < points.size(); i++) {
			Vec2 a = points.get(i);
			Vec2 b = points.get((i + 1) % points.size());
			Vec2 c = points.get((i + 2) % points.size());
			float det = orientation(a, b, c);
			if (det < 0 ^ clockwise) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSimplePolygon(ArrayList<Vec2> points) {
		return isSimplePolygon(points, 0.01f);
	}

	/**
	 * Returns true if the given points form into a simple polygon
	 * A simple polygon is a polygon that does not intersect itself and does not have any holes. 
	 * Currently to check, i'm just checking every pair of non-adjacent lines to see if they intersect. 
	 * 
	 * TODO implement O(nlogn) with sweep line
	 * 	- Sort the endpoints according to their x component
	 *  - For each point, keep track of whether it is a left endpoint or right endpoint for it's line
	 *  - For each point, if it is a left endpoint, add the corresponding line to a bst, else remove the line. 
	 *  - Each time we add a new line, check for intersections against the lines right above and below it. 
	 * TODO figure out a way to remove epsilon
	 * 
	 * @param points
	 * @return
	 */
	public static boolean isSimplePolygon(ArrayList<Vec2> points, float epsilon) {
		for (int i = 0; i < points.size() - 1; i++) {
			Vec2 u = new Vec2(points.get(i));
			Vec2 v = new Vec2(points.get(i + 1));
			u.addi(new Vec2(u, v).mul(epsilon));
			v.addi(new Vec2(v, u).mul(epsilon));

			for (int j = i + 2; j < points.size(); j++) {
				Vec2 a = new Vec2(points.get(j));
				Vec2 b = new Vec2(points.get((j + 1) % points.size()));
				a.addi(new Vec2(a, b).mul(epsilon));
				b.addi(new Vec2(b, a).mul(epsilon));

				if (lineSegment_lineSegmentIntersect(a, b, u, v) != null) {
					//one line crosses another line
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Takes in a polygon and a point, and returns true if the point is within the polygon. 
	 * @param poly
	 * @param p
	 * @return
	 */
	public static boolean pointInsidePolygon(Vec2[] poly, Vec2 p) {
		ArrayList<Vec2> list = new ArrayList<>();
		for (Vec2 v : poly) {
			list.add(v);
		}
		return pointInsidePolygon(list, p);
	}

	/**
	 * Takes in a polygon and a point, and returns true if the point is within the polygon. 
	 * TODO implement O(logn) version using bsearch
	 * @param poly
	 * @param p
	 * @return
	 */
	public static boolean pointInsidePolygon(ArrayList<Vec2> poly, Vec2 p) {
		//if a point is inside a polygon, then a ray drawn from the point must intersect the polygon an odd amount of times. 
		Vec2 rayOrigin = new Vec2(p);
		Vec2 rayDir = new Vec2(1, 0).rotate((float) (Math.random() * Math.PI * 2));

		int cnt = 0;
		for (int i = 0; i < poly.size(); i++) {
			Vec2 a = poly.get(i);
			Vec2 b = poly.get((i + 1) % poly.size());

			if (MathUtils.ray_lineSegmentIntersect(rayOrigin, rayDir, a, b) != null) {
				cnt++;
			}
		}
		return cnt % 2 == 1;
	}

	/**
	 * Takes a point and a bounding box and returns whether or not the point is inside the bounding box
	 * @param pt
	 * @param bb_min
	 * @param bb_max
	 * @return
	 */
	public static boolean pointInsideAABB(Vec2 pt, Vec2 bb_min, Vec2 bb_max) {
		float epsilon = 0.0001f;
		return pt.x + epsilon > bb_min.x && pt.y + epsilon > bb_min.y && pt.x - epsilon < bb_max.x && pt.y - epsilon < bb_max.y;
	}

	/**
	 * Takes a point and a bounding box, and returns whether or not the point is inside the bounding box
	 * @param point
	 * @param bb_min
	 * @param bb_max
	 * @return
	 */
	public static boolean pointInsideAABB(Vec3 pt, Vec3 bb_min, Vec3 bb_max) {
		float epsilon = 0.0001f;
		return pt.x + epsilon > bb_min.x && pt.y + epsilon > bb_min.y && pt.z + epsilon > bb_min.z && pt.x - epsilon < bb_max.x && pt.y - epsilon < bb_max.y && pt.z - epsilon < bb_max.z;
	}

	/**
	 * Returns true if the two AABBs are intersecting
	 * @return
	 */
	public static boolean AABB_AABBIntersect(Vec3 a_min, Vec3 a_max, Vec3 b_min, Vec3 b_max) {
		boolean xint = (a_min.x <= b_min.x && b_min.x <= a_max.x) || (b_min.x <= a_min.x && a_min.x <= b_max.x);
		boolean yint = (a_min.y <= b_min.y && b_min.y <= a_max.y) || (b_min.y <= a_min.y && a_min.y <= b_max.y);
		boolean zint = (a_min.z <= b_min.z && b_min.z <= a_max.z) || (b_min.z <= a_min.z && a_min.z <= b_max.z);
		return xint && yint && zint;
	}

	/**
	 * Takes in a collection of points, and returns the convex hull fitting these points in CCW winding.  
	 * Should be able to handle degenerate cases such as lines and single points.
	 * @param pts
	 * @return
	 */
	public static ArrayList<Vec2> calculateConvexHull(Vec2[] pts) {
		ArrayList<Vec2> tmp_list = new ArrayList<>();
		for (Vec2 v : pts) {
			tmp_list.add(new Vec2(v));
		}
		return calculateConvexHull(tmp_list);
	}

	/**
	 * Takes in a collection of points, and returns the convex hull fitting these points in CCW winding
	 * Should be able to handle degenerate cases such as lines and single points.
	 * @param pts
	 * @return
	 */
	public static ArrayList<Vec2> calculateConvexHull(ArrayList<Vec2> pts) {
		ArrayList<Vec2> ans = new ArrayList<>();
		if (pts.size() == 0) {
			return ans;
		}
		if (pts.size() == 1) {
			ans.add(pts.get(0));
			return ans;
		}

		//find top-right most element
		final Vec2 pivot = new Vec2(pts.get(0));
		for (int i = 1; i < pts.size(); i++) {
			Vec2 next = new Vec2(pts.get(i));
			if (next.y < pivot.y || (next.y == pivot.y && next.x < pivot.x)) {
				pivot.set(next);
			}
		}

		//sort points according to angle from pivot
		Collections.sort(pts, (Vec2 a, Vec2 b) -> {
			float o = orientation(pivot, a, b);
			if (o == 0) {
				//tiebreak by distance to pivot
				return Float.compare(pivot.distanceSq(a), pivot.distanceSq(b));
			}
			return o < 0 ? -1 : 1;
		});

		ArrayList<Vec2> st = new ArrayList<>();
		for (int i = 0; i < pts.size(); i++) {
			while (st.size() > 1) {
				float o = orientation(st.get(st.size() - 2), st.get(st.size() - 1), pts.get(i));
				if (o < 0) {
					break;
				}
				st.remove(st.size() - 1);
			}
			st.add(pts.get(i));
		}

		for (int i = 0; i < st.size(); i++) {
			Vec2 v0 = st.get(i);
			Vec2 v1 = st.get((i + 1) % st.size());
			if (v0.x == v1.x && v0.y == v1.y) {
				continue;
			}
			ans.add(v0);
		}
		if (ans.size() == 0) {
			ans.add(pivot);
		}

		Collections.reverse(ans);
		return ans;
	}

	/**
	 * Takes in a simple polygon, and returns n concave polygons, where the union of the n polygons is equal to
	 * the original polygon.
	 * 
	 * n is at most 4 times the optimal amount of concave polygons. 
	 * 
	 * Additionally, all of the vertices of the concave polygons will belong to the original polygon, meaning no
	 * additional points are added. 
	 * 
	 * Complexity: O(n^3)
	 * 
	 * @param points
	 * @return
	 */
	public static ArrayList<int[]> calculateConvexPartition(ArrayList<Vec2> points) {
		ArrayList<int[]> ans = new ArrayList<>();
		ArrayList<int[]> tri = calculateTrianglePartition(points);
		boolean[] v = new boolean[tri.size()];

		for (int i = 0; i < tri.size(); i++) {
			if (v[i]) {
				continue;
			}

			HashSet<Integer> vSet = new HashSet<>();
			vSet.add(tri.get(i)[0]);
			vSet.add(tri.get(i)[1]);
			vSet.add(tri.get(i)[2]);

			ArrayList<Integer> vList = new ArrayList<>();
			vList.add(tri.get(i)[0]);
			vList.add(tri.get(i)[1]);
			vList.add(tri.get(i)[2]);

			v[i] = true;

			//greedily try to add on triangles
			for (int j = i + 1; j < tri.size(); j++) {
				if (v[j]) {
					continue;
				}

				int[] next = tri.get(j);

				//if 2 of the 3 vertices in the next triangle belong to the current one, then we can add it. 
				int sum = 0;
				sum += vSet.contains(next[0]) ? 1 : 0;
				sum += vSet.contains(next[1]) ? 1 : 0;
				sum += vSet.contains(next[2]) ? 1 : 0;

				if (sum != 2) { //next triangle is not adjacent
					continue;
				}

				int vNotContained = -1;
				if (!vSet.contains(next[0])) {
					vNotContained = next[0];
				}
				if (!vSet.contains(next[1])) {
					vNotContained = next[1];
				}
				if (!vSet.contains(next[2])) {
					vNotContained = next[2];
				}

				//construct new list of vertices to check whether it is convex
				ArrayList<Integer> nextVList = new ArrayList<>();
				int startInd = 0;
				for (int k = 0; k < vList.size(); k++) {
					if (!(vList.get(k) == next[0] || vList.get(k) == next[1] || vList.get(k) == next[2])) {
						startInd = k;
						break;
					}
				}
				for (int k = 0; k < vList.size(); k++) {
					nextVList.add(vList.get((k + startInd) % vList.size()));
				}
				for (int k = 0; k < nextVList.size(); k++) {
					if (!(nextVList.get(k) == next[0] || nextVList.get(k) == next[1] || nextVList.get(k) == next[2])) {
						continue;
					}
					nextVList.add(k + 1, vNotContained);
					break;
				}

				ArrayList<Vec2> tmp = new ArrayList<>();
				for (int k = 0; k < nextVList.size(); k++) {
					tmp.add(points.get(nextVList.get(k)));
				}

				if (!MathUtils.isConvex(tmp)) {
					continue;
				}

				//save new vertex list
				vList = nextVList;
				vSet.add(vNotContained);
				v[j] = true;
			}

			//save answer
			int[] next = new int[vList.size()];
			for (int j = 0; j < vList.size(); j++) {
				next[j] = vList.get(j);
			}

			ans.add(next);
		}

		return ans;
	}

	/**
	 * Takes in a simple polygon, and returns n - 2 triangles, where n is the amount of vertices. 
	 * The union of the triangles is equal to the original polygon
	 * 
	 * Uses ear clipping technique
	 * 
	 * try not to have 3 adjacent colinear points in the input polygon or else a degenerate triangle
	 * will be produced
	 * 
	 * Complexity: O(n^3)
	 * 
	 * @param points
	 * @return
	 */
	public static ArrayList<int[]> calculateTrianglePartition(ArrayList<Vec2> points) {
		boolean clockwise = !MathUtils.isCounterClockwiseWinding(points);
		int n = points.size();
		ArrayList<int[]> ans = new ArrayList<>();
		boolean[] v = new boolean[n];
		while (ans.size() < n - 2) {
			int[] next = new int[3];

			//look for next triangle to remove
			//im assuming there's at least one valid triangle for us to remove
			outer:
			for (int i = 0; i < n; i++) {
				if (v[i]) {
					continue;
				}

				next[0] = i;
				for (int j = i + 1;; j++) {
					if (!v[j % n]) {
						next[1] = j % n;
						break;
					}
				}
				for (int j = next[1] + 1;; j++) {
					if (!v[j % n]) {
						next[2] = j % n;
						break;
					}
				}

				//if angle is not acute, then it's not a triangle
				if (orientation(points.get(next[0]), points.get(next[1]), points.get(next[2])) < 0 ^ clockwise) {
					continue;
				}

				//if any of the vertices are inside the triangle, then it is invalid
				for (int j = 0; j < n; j++) {
					if (j == next[0] || j == next[1] || j == next[2]) {
						continue;
					}
					if (MathUtils.pointInsideTriangle(points.get(j), points.get(next[0]), points.get(next[1]), points.get(next[2]))) {
						continue outer;
					}
				}

				v[next[1]] = true;
				break;
			}

			ans.add(next);
		}

		return ans;
	}

	/**
	 * Returns the area covered by the polygon. Winding order doesn't matter
	 * @param poly
	 * @return
	 */
	public static float polygonArea(Vec2[] poly) {
		ArrayList<Vec2> list = new ArrayList<>();
		for (Vec2 v : poly) {
			list.add(v);
		}
		return polygonArea(list);
	}

	/**
	 * Returns the area covered by the polygon. Winding order doesn't matter
	 * @param poly
	 * @return
	 */
	public static float polygonArea(ArrayList<Vec2> poly) {
		float area = 0;
		for (int i = 0; i < poly.size(); i++) {
			Vec2 v0 = poly.get(i);
			Vec2 v1 = poly.get((i + 1) % poly.size());
			area += v0.cross(v1);
		}
		return Math.abs(area / 2.0f);
	}

	/**
	 * Returns the unsigned 2D area of the triangle defined by these three points
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static float triangleArea(Vec3 a, Vec3 b, Vec3 c) {
		return (cross(new Vec3(a, b), new Vec3(b, c))).length() / 2.0f;
	}

	/**
	 * Returns the signed volume of the given tetrahedron. 
	 * The volume is positive if cross(ab, ac) is pointing in the same direction as ad. 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static float signedTetrahedronVolume(Vec3 a, Vec3 b, Vec3 c, Vec3 d) {
		return dot(new Vec3(a, d), cross(new Vec3(a, b), new Vec3(a, c))) / 6.0f;
	}

	/**
	 * Tests if the given point is contained by the given tetrahedron. 
	 * Does a check on the signed volume. 
	 * @param pt
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static boolean pointInsideTetrahedron(Vec3 pt, Vec3 a, Vec3 b, Vec3 c, Vec3 d) {
		boolean o = sign(signedTetrahedronVolume(a, b, c, d));
		boolean sa = sign(signedTetrahedronVolume(pt, b, c, d));
		boolean sb = sign(signedTetrahedronVolume(a, pt, c, d));
		boolean sc = sign(signedTetrahedronVolume(a, b, pt, d));
		boolean sd = sign(signedTetrahedronVolume(a, b, c, pt));
		return o == sa && o == sb && o == sc && o == sd;
	}

	/**
	 * Computes the point that is equidistant to all 4 given points, or the circumcenter.
	 * In the case where all 4 points are coplanar, will return null. Note that circumcenter is unstable when det A is small. 
	 * http://rodolphe-vaillant.fr/entry/127/find-a-tetrahedron-circumcenter
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return
	 */
	public static Vec3 calculateCircumcenter(Vec3 a, Vec3 b, Vec3 c, Vec3 d) {
		Vec3 ab = new Vec3(a, b);
		Vec3 ac = new Vec3(a, c);
		Vec3 ad = new Vec3(a, d);
		Mat3 A = new Mat3();
		A.mat[0][0] = ab.x;
		A.mat[0][1] = ab.y;
		A.mat[0][2] = ab.z;
		A.mat[1][0] = ac.x;
		A.mat[1][1] = ac.y;
		A.mat[1][2] = ac.z;
		A.mat[2][0] = ad.x;
		A.mat[2][1] = ad.y;
		A.mat[2][2] = ad.z;
		Mat3 Ainv = A.inverse();
		if (Ainv == null) {
			return null;
		}

		Vec3 B = new Vec3(b.lengthSq() - a.lengthSq(), c.lengthSq() - a.lengthSq(), d.lengthSq() - a.lengthSq());
		B.muli(0.5f);

		return Ainv.mul(B);
	}

	/**
	 * Takes in a triangle defined by 3 points and a point, and computes the barycentric coordinates of the point. 
	 * Assumes that the supplied point is on the triangle.
	 * @param a
	 * @param b
	 * @param c
	 * @param pt
	 * @return
	 */
	public static Vec3 barycentricCoords(Vec3 a, Vec3 b, Vec3 c, Vec3 pt) {
		float inv_tri_area = 1.0f / triangleArea(a, b, c);
		Vec3 result = new Vec3(0);
		result.x = triangleArea(pt, b, c) * inv_tri_area;
		result.y = triangleArea(pt, c, a) * inv_tri_area;
		result.z = triangleArea(pt, a, b) * inv_tri_area;
		return result;
	}

	/**
	 * Takes in a tetrahedron defined by 4 points and a point, and computes the barycentric coordinates of the point.
	 * The supplied tetrahedron's winding order is assumed to be counterclockwise, or dot(d - a, cross(b - a, c - a)) >= 0
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param pt
	 * @return
	 */
	public static Vec4 barycentricCoords(Vec3 a, Vec3 b, Vec3 c, Vec3 d, Vec3 pt) {
		float tot_area = MathUtils.signedTetrahedronVolume(a, b, c, d);
		Vec4 res = new Vec4(0);
		res.x = MathUtils.signedTetrahedronVolume(pt, b, c, d) / tot_area;
		res.y = MathUtils.signedTetrahedronVolume(a, pt, c, d) / tot_area;
		res.z = MathUtils.signedTetrahedronVolume(a, b, pt, d) / tot_area;
		res.w = MathUtils.signedTetrahedronVolume(a, b, c, pt) / tot_area;
		return res;
	}

	/**
	 * Takes in two lines, and returns their intersection points. 
	 * 
	 * Doesn't really handle parallel lines very well. 
	 * 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */

	public static Vec2 line_lineIntersect(Vec2 a0, Vec2 a1, Vec2 b0, Vec2 b1) {
		float x1 = a0.x;
		float x2 = a1.x;
		float x3 = b0.x;
		float x4 = b1.x;

		float y1 = a0.y;
		float y2 = a1.y;
		float y3 = b0.y;
		float y4 = b1.y;

		// calculate the distance to intersection point
		float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
		float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

		if (uA == Float.NEGATIVE_INFINITY || uA == Float.POSITIVE_INFINITY) {
			//the two lines are parallel
			return null;
		}

		if (Float.isNaN(uA)) {
			//two lines are the same line
			return a0.add(new Vec2(a0, a1).mul(0.5f));
		}

		// calculate the intersection point
		float intersectionX = x1 + (uA * (x2 - x1));
		float intersectionY = y1 + (uA * (y2 - y1));

		return new Vec2(intersectionX, intersectionY);
	}

	/**
	 * Takes in two lines, and returns true if they are the same line. 
	 * 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */

	public static boolean isSameLine(Vec2 a0, Vec2 a1, Vec2 b0, Vec2 b1) {
		float x1 = a0.x;
		float x2 = a1.x;
		float x3 = b0.x;
		float x4 = b1.x;

		float y1 = a0.y;
		float y2 = a1.y;
		float y3 = b0.y;
		float y4 = b1.y;

		// calculate the distance to intersection point
		float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
		float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

		return Float.isNaN(uA);
	}

	/**
	 * Takes in a line segment and a point, and returns true if the point falls on the line segment
	 * 
	 * @param a0
	 * @param a1
	 * @param p
	 * @return
	 */

	public static boolean isPointOnLineSegment(Vec2 a0, Vec2 a1, Vec2 p) {
		Vec2 d1 = new Vec2(a0, a1);
		Vec2 d2 = new Vec2(a0, p);

		if (d1.lengthSq() < d2.lengthSq()) {
			//p has to be farther away from a0 than a1.
			return false;
		}

		d1.normalize();
		d2.normalize();

		if (Math.abs(1 - Vec2.dot(d1, d2)) > EPSILON) {
			//p is not on the same line
			return false;
		}

		if (Vec2.dot(d1, d2) < 0) {
			//p is behind a0
			return false;
		}

		return true;
	}

	/**
	 * Takes in two line segments, and returns the point of intersection, if it exists. Null otherwise.
	 * 
	 * If the two line segments are the same, then returns some point on the line
	 * 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static Vec2 lineSegment_lineSegmentIntersect(Vec2 a0, Vec2 a1, Vec2 b0, Vec2 b1) {
		if (isSameLine(a0, a1, b0, b1)) {
			//check both endpoints and midpoint
			if (isPointOnLineSegment(a0, a1, b0)) {
				return new Vec2(b0);
			}
			if (isPointOnLineSegment(a0, a1, b1)) {
				return new Vec2(b1);
			}
			Vec2 mid = b0.add(new Vec2(b0, b1).mul(0.5f));
			if (isPointOnLineSegment(a0, a1, mid)) {
				return mid;
			}
			return null;
		}

		//calculate intersection of line segments extended into lines
		Vec2 lineIntersection = line_lineIntersect(a0, a1, b0, b1);

		if (lineIntersection == null) {
			return null;
		}

		//check if intersection point is along both of the line segments. 
		{
			Vec2 ai = new Vec2(a0, lineIntersection);
			Vec2 da = new Vec2(a0, a1);
			if (ai.dot(da) < 1e-3) {
				return null;
			}
			if (ai.lengthSq() > da.lengthSq() - 1e-3) {
				return null;
			}
		}

		{
			Vec2 bi = new Vec2(b0, lineIntersection);
			Vec2 db = new Vec2(b0, b1);
			if (bi.dot(db) < 1e-3) {
				return null;
			}
			if (bi.lengthSq() > db.lengthSq() - 1e-3) {
				return null;
			}
		}

		return lineIntersection;
	}

	/**
	 * Takes in a ray and a line segment, and returns the location of the intersection, if they intersect
	 * Null if they don't intersect
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static Vec2 ray_lineSegmentIntersect(Vec2 ray_origin, Vec2 ray_dir, Vec2 b0, Vec2 b1) {
		//calculate intersection of line segment and ray extended into lines
		Vec2 lineIntersection = line_lineIntersect(ray_origin, ray_origin.add(ray_dir), b0, b1);

		if (lineIntersection == null) {
			return null;
		}

		//check if intersection point is behind ray
		if (Vec2.dot(ray_dir, new Vec2(ray_origin, lineIntersection)) < 0) {
			return null;
		}

		//check if intersection point is along the line segment;
		{
			Vec2 bi = new Vec2(b0, lineIntersection);
			Vec2 db = new Vec2(b0, b1);
			if (bi.dot(db) < 0) {
				return null;
			}
			if (bi.lengthSq() > db.lengthSq()) {
				return null;
			}
		}

		return lineIntersection;
	}

	/**
	 * Takes in a point and a line segment, and returns the minimum distance from any point on the line segment to the first point. 
	 * @param pt
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static float point_lineSegmentDistance(Vec3 pt, Vec3 b0, Vec3 b1) {
		return MathUtils.dist(pt, MathUtils.point_lineSegmentProjectClamped(pt, b0, b1));
	}

	/**
	 * Take in two lines in 3D, and returns the shortest distance between them.
	 * @param a_point
	 * @param a_dir
	 * @param b_point
	 * @param b_dir
	 * @return
	 */
	public static float line_lineDistance(Vec3 a_point, Vec3 a_dir, Vec3 b_point, Vec3 b_dir) {
		float[] nm = line_lineDistanceNM(a_point, a_point.add(a_dir), b_point, b_point.add(b_dir));
		Vec3 v = new Vec3(a_point.add(a_dir.mul(nm[0])), b_point.add(b_dir.mul(nm[1])));
		return v.length();
	}

	/**
	 * Takes in a ray and a line segment, and returns the minimum distance between them. 
	 * @param ray_origin
	 * @param ray_dir
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static float ray_lineSegmentDistance(Vec3 ray_origin, Vec3 ray_dir, Vec3 b0, Vec3 b1) {
		float[] nm = line_lineDistanceNM(ray_origin, ray_origin.add(ray_dir), b0, b1);
		nm[0] = Math.max(0, nm[0]);
		nm[1] = clamp(0, 1, nm[1]);
		Vec3 b_dir = new Vec3(b0, b1);
		Vec3 v = new Vec3(ray_origin.add(ray_dir.mul(nm[0])), b0.add(b_dir.mul(nm[1])));
		return v.length();
	}

	/**
	 * Takes in a ray and a line segment, and returns the minimum distance between them. 
	 * @param ray_origin
	 * @param ray_dir
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static float ray_lineSegmentDistance(Vec3 ray_origin, Vec3 ray_dir, Vec3 b0, Vec3 b1, Vec3 out_ray_point, Vec3 out_line_point) {
		float[] nm = line_lineDistanceNM(ray_origin, ray_origin.add(ray_dir), b0, b1);
		nm[0] = Math.max(0, nm[0]);
		nm[1] = clamp(0, 1, nm[1]);
		Vec3 b_dir = new Vec3(b0, b1);
		out_ray_point.set(ray_origin.add(ray_dir.mul(nm[0])));
		out_line_point.set(b0.add(b_dir.mul(nm[1])));
		Vec3 v = new Vec3(out_ray_point, out_line_point);
		return v.length();
	}

	/**
	 * Takes in two line segments and returns the minimum distance between them. 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static float lineSegment_lineSegmentDistance(Vec3 a0, Vec3 a1, Vec3 b0, Vec3 b1) {
		float[] nm = line_lineDistanceNM(a0, a1, b0, b1);
		nm[0] = clamp(0, 1, nm[0]);
		nm[1] = clamp(0, 1, nm[1]);
		Vec3 p0 = a0.add(new Vec3(a0, a1).mul(nm[0]));
		Vec3 p1 = b0.add(new Vec3(b0, b1).mul(nm[1]));
		return dist(p0, p1);
	}

	/**
	 * Takes in two line segments and returns the corresponding pair of points on the two line segments that minimizes the distance
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static Pair<Vec3, Vec3> lineSegment_lineSegmentClosestPoints(Vec3 a0, Vec3 a1, Vec3 b0, Vec3 b1) {
		float[] nm = line_lineDistanceNM(a0, a1, b0, b1);
		nm[0] = clamp(0, 1, nm[0]);
		nm[1] = clamp(0, 1, nm[1]);
		Vec3 p0 = a0.add(new Vec3(a0, a1).mul(nm[0]));
		Vec3 p1 = b0.add(new Vec3(b0, b1).mul(nm[1]));
		return new Pair<>(p0, p1);
	}

	/**
	 * Takes in two line segments in 3D, and returns the values for n and m.
	 * Where v is the vector between the two closest points of a and b,
	 * v = (a0 + n * d0) - (b0 + m * d1)
	 * 
	 * d0 is the direction vector of a, d1 is for b. 
	 * v.dot(d0) = 0
	 * v.dot(d1) = 0
	 * 
	 * thus we can get the two equations: 
	 * ((a0.x + n * d0.x) - (b0.x + m * d1.x)) * d0.x + 
	 * ((a0.y + n * d0.y) - (b0.y + m * d1.y)) * d0.y + 
	 * ((a0.z + n * d0.z) - (b0.z + m * d1.z)) * d0.z = 0
	 * 
	 * ((a0.x + n * d0.x) - (b0.x + m * d1.x)) * d1.x + 
	 * ((a0.y + n * d0.y) - (b0.y + m * d1.y)) * d1.y + 
	 * ((a0.z + n * d0.z) - (b0.z + m * d1.z)) * d1.z = 0
	 * 
	 * finally, solve for n and m.
	 * 
	 * closest point on line a : a0 + n * d0. 
	 * for line b : b0 + m * d0. 
	 * 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */
	private static float[] line_lineDistanceNM(Vec3 a0, Vec3 a1, Vec3 b0, Vec3 b1) {
		Vec3 d0 = new Vec3(a0, a1);
		Vec3 d1 = new Vec3(b0, b1);

		if (MathUtils.dot(d0, d1) == 0) {
			//they are exactly perpendicular
			float n = MathUtils.dot(b0, d0) - MathUtils.dot(a0, d0);
			float m = -MathUtils.dot(b0, d1) + MathUtils.dot(a0, d1);
			n /= d0.lengthSq();
			m /= d1.lengthSq();
			return new float[] { n, m };
		}

		//TODO handle the case in which they are exactly parallel
		if (MathUtils.cross(d0, d1).lengthSq() == 0) {
			//TODO
		}

		float n = 0;
		float m = 0;

		float nExp0 = 0;
		float mExp0 = 0;
		float cnst0 = 0;

		float nExp1 = 0;
		float mExp1 = 0;
		float cnst1 = 0;

		nExp0 += d0.x * d0.x + d0.y * d0.y + d0.z * d0.z;
		mExp0 -= d1.x * d0.x + d1.y * d0.y + d1.z * d0.z;
		cnst0 -= a0.x * d0.x + a0.y * d0.y + a0.z * d0.z;
		cnst0 += b0.x * d0.x + b0.y * d0.y + b0.z * d0.z;

		nExp1 += d0.x * d1.x + d0.y * d1.y + d0.z * d1.z;
		mExp1 -= d1.x * d1.x + d1.y * d1.y + d1.z * d1.z;
		cnst1 -= a0.x * d1.x + a0.y * d1.y + a0.z * d1.z;
		cnst1 += b0.x * d1.x + b0.y * d1.y + b0.z * d1.z;

		//cancel out nExp0
		float ratio = nExp0 / nExp1;
		nExp0 -= nExp1 * ratio; //nExp1 * (nExp0 / nExp1) = nExp0
		mExp0 -= mExp1 * ratio;
		cnst0 -= cnst1 * ratio;

		//now, nExp0 should = 0. Solve for m. 
		m = cnst0 / mExp0;

		//now solve for n. n = (cnst1 - mExp1 * m) / nExp1;
		n = (cnst1 - mExp1 * m) / nExp1;

		return new float[] { n, m };
	}

	/**
	 * Takes in 3 planes and returns the point at which they all intersect. 
	 * Returns null if such an intersection point doesn't exist. 
	 * 
	 * https://gdbooks.gitbooks.io/3dcollisions/content/Chapter1/three_plane_intersection.html
	 * @param p1_origin
	 * @param p1_normal
	 * @param p2_origin
	 * @param p2_normal
	 * @param p3_origin
	 * @param p3_normal
	 * @return
	 */
	public static Vec3 plane_plane_planeIntersect(Vec3 p1_origin, Vec3 p1_normal, Vec3 p2_origin, Vec3 p2_normal, Vec3 p3_origin, Vec3 p3_normal) {
		Vec3 m1 = new Vec3(p1_normal.x, p2_normal.x, p3_normal.x);
		Vec3 m2 = new Vec3(p1_normal.y, p2_normal.y, p3_normal.y);
		Vec3 m3 = new Vec3(p1_normal.z, p2_normal.z, p3_normal.z);
		float d1 = MathUtils.dot(p1_normal, p1_origin);
		float d2 = MathUtils.dot(p2_normal, p2_origin);
		float d3 = MathUtils.dot(p3_normal, p3_origin);
		Vec3 d = new Vec3(d1, d2, d3);

		Vec3 u = MathUtils.cross(m2, m3);
		Vec3 v = MathUtils.cross(m1, d);
		float denom = MathUtils.dot(m1, u);

		if (Math.abs(denom) < 0.00001) {
			return null;
		}
		return new Vec3(dot(d, u) / denom, dot(m3, v) / denom, -dot(m2, v) / denom);
	}

	/**
	 * Takes in two planes and returns the line on which they are intersecting. 
	 * Line is returned in the form {origin, direction}, and direction is normalized. 
	 * If the two planes are parallel, returns null
	 * 
	 * https://mathworld.wolfram.com/Plane-PlaneIntersection.html
	 * @param p1_origin
	 * @param p1_normal
	 * @param p2_origin
	 * @param p2_normal
	 * @return
	 */
	public static Pair<Vec3, Vec3> plane_planeIntersect(Vec3 p1_origin, Vec3 p1_normal, Vec3 p2_origin, Vec3 p2_normal) {
		p1_normal.normalize();
		p2_normal.normalize();
		if (MathUtils.cross(p1_normal, p2_normal).length() == 0) {
			return null;
		}

		float d1 = MathUtils.dot(p1_normal, p1_origin);
		float d2 = MathUtils.dot(p2_normal, p2_origin);
		Vec3 line_dir = MathUtils.cross(p1_normal, p2_normal);
		line_dir.normalize();

		//TODO finish this
		return null;
	}

	/**
	 * Take in a ray and a plane, and returns the intersection if it exists.
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param plane_origin
	 * @param plane_normal
	 * @return
	 */
	public static Vec3 ray_planeIntersect(Vec3 ray_origin, Vec3 ray_dir, Vec3 plane_origin, Vec3 plane_normal) {
		float ray_dirStepRatio = plane_normal.dot(ray_dir); // for each step in ray_dir, you go ray_dirStepRatio steps towards the plane
		// in plane_normal
		if (ray_dirStepRatio == 0) {
			// ray is parallel to plane, no intersection
			return null;
		}
		float t = plane_origin.sub(ray_origin).dot(plane_normal) / ray_dirStepRatio;
		if (t < 0) {
			// the plane intersection is behind the ray origin
			return null;
		}
		return ray_origin.add(ray_dir.mul(t));
	}

	/**
	 * Take in a line and a plane, and returns the intersection if it exists.
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param plane_origin
	 * @param plane_normal
	 * @return
	 */
	public static Vec3 line_planeIntersect(Vec3 line_origin, Vec3 line_dir, Vec3 plane_origin, Vec3 plane_normal) {
		float line_dirStepRatio = plane_normal.dot(line_dir); // for each step in line_dir, you go line_dirStepRatio
		// steps in plane_normal
		if (line_dirStepRatio == 0) {
			// line is parallel to plane, no intersection
			return null;
		}
		// Conceptually, a line is equivalent to a double-sided ray. This means that
		// it's fine if t is negative.
		float t = plane_origin.sub(line_origin).dot(plane_normal) / line_dirStepRatio;
		return line_origin.add(line_dir.mul(t));
	}

	/**
	 * Take in a point and a plane, and returns the point projected onto the plane
	 * 
	 * @param point
	 * @param plane_origin
	 * @param plane_normal
	 * @return
	 */
	public static Vec3 point_planeProject(Vec3 point, Vec3 plane_origin, Vec3 plane_normal) {
		return point.sub(new Vec3(plane_origin, point).projectOnto(plane_normal));
	}

	/**
	 * Take in a point and a line, and returns the point projected onto the line
	 * 
	 * @param point
	 * @param line_origin
	 * @param line_dir
	 * @return
	 */
	public static Vec3 point_lineProject(Vec3 point, Vec3 line_origin, Vec3 line_dir) {
		Vec3 lineToPoint = new Vec3(line_origin, point);
		return line_origin.add(lineToPoint.projectOnto(line_dir));
	}

	/**
	 * Take in a point and a line segment, and if the projection of the point onto the line is within the segment, 
	 * returns the point projected onto the line, else returns null.
	 * 
	 * @param point
	 * @param line_a
	 * @param line_b
	 * @return
	 */
	public static Vec3 point_lineSegmentProject(Vec3 point, Vec3 line_a, Vec3 line_b) {
		Vec3 line_ab = new Vec3(line_a, line_b);
		Vec3 lineToPoint = new Vec3(line_a, point);
		float mul = lineToPoint.dot(line_ab) / line_ab.dot(line_ab);
		if (mul < 0 || mul > 1) {
			return null;
		}
		return line_a.add(line_ab.mul(mul));
	}

	/**
	 * Take in a point and a line segment, and if the projection of the point onto the line is within the segment, 
	 * returns the point projected onto the line, else clamps the point to the line segment, and returns the clamped point.
	 * 
	 * @param point
	 * @param line_a
	 * @param line_b
	 * @return
	 */
	public static Vec3 point_lineSegmentProjectClamped(Vec3 point, Vec3 line_a, Vec3 line_b) {
		Vec3 line_ab = new Vec3(line_a, line_b);
		Vec3 lineToPoint = new Vec3(line_a, point);
		float mul = lineToPoint.dot(line_ab) / line_ab.dot(line_ab);
		mul = clamp(0f, 1f, mul);
		return line_a.add(line_ab.mul(mul));
	}

	/**
	 * Take in a point and a triangle defined by 3 points, and returns the closest point on the triangle to the plane
	 * @param pt
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 point_triangleProjectClamped(Vec3 pt, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 tri_norm = MathUtils.cross(new Vec3(t0, t1), new Vec3(t0, t2));
		tri_norm.normalize();

		//check if directly projecting onto the plane will get us a point in the triangle. 
		Vec3 plane_proj = point_planeProject(pt, t0, tri_norm);
		if (MathUtils.pointInsideTriangularColumn(plane_proj, t0, t1, t2)) {
			return plane_proj;
		}

		//otherwise, have to clamp to a line segment. 
		Vec3 s0 = MathUtils.point_lineSegmentProjectClamped(pt, t0, t1);
		Vec3 s1 = MathUtils.point_lineSegmentProjectClamped(pt, t1, t2);
		Vec3 s2 = MathUtils.point_lineSegmentProjectClamped(pt, t2, t0);

		Vec3 ans = s0;
		if (MathUtils.dist(pt, s1) < MathUtils.dist(pt, ans)) {
			ans = s1;
		}
		if (MathUtils.dist(pt, s2) < MathUtils.dist(pt, ans)) {
			ans = s2;
		}
		return ans;
	}

	/**
	 * Take in a point and a triangle, and returns true if the point is contained within the triangle. 
	 * 
	 * @param point
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */

	public static boolean pointInsideTriangle(Vec2 point, Vec2 t0, Vec2 t1, Vec2 t2) {
		Vec2 centroid = new Vec2((t0.x + t1.x + t2.x) / 3.0, (t0.y + t1.y + t2.y) / 3.0);
		boolean ans = true;
		ans &= pointsOnSameSideOfLine(t0, new Vec2(t0, t1), centroid, point);
		ans &= pointsOnSameSideOfLine(t1, new Vec2(t1, t2), centroid, point);
		ans &= pointsOnSameSideOfLine(t2, new Vec2(t2, t0), centroid, point);
		return ans;
	}

	/**
	 * Takes a point and a triangle and returns whether or not the point, when projected down into the plane defined by the triangle,
	 * is inside of the triangle. 
	 * @param pt
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean pointInsideTriangularColumn(Vec3 pt, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 d0 = new Vec3(t0, t1).normalize();
		Vec3 d1 = new Vec3(t1, t2).normalize();
		Vec3 d2 = new Vec3(t2, t0).normalize();

		Vec3 plane_normal = d0.cross(d1).normalize();
		Vec3 plane_intersect = MathUtils.point_planeProject(pt, t0, plane_normal);

		Vec3 n0 = d0.cross(plane_normal);
		Vec3 n1 = d1.cross(plane_normal);
		Vec3 n2 = d2.cross(plane_normal);

		return n0.dot(t0.sub(plane_intersect)) >= 0 && n1.dot(t1.sub(plane_intersect)) >= 0 && n2.dot(t2.sub(plane_intersect)) >= 0;
	}

	/**
	 * Take in a ray and a triangle, and returns the intersection if it exists.
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 ray_triangleIntersect(Vec3 ray_origin, Vec3 ray_dir, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 d0 = new Vec3(t0, t1).normalize();
		Vec3 d1 = new Vec3(t1, t2).normalize();
		Vec3 d2 = new Vec3(t2, t0).normalize();

		Vec3 plane_origin = new Vec3(t0);
		Vec3 plane_normal = d0.cross(d1).normalize();

		Vec3 plane_intersect = ray_planeIntersect(ray_origin, ray_dir, plane_origin, plane_normal);
		if (plane_intersect == null) {
			// if it doesn't intersect the plane, then theres no way it intersects the
			// triangle.
			return null;
		}

		// now, we just have to make sure that the intersection point is inside the
		// triangle.
		Vec3 n0 = d0.cross(plane_normal);
		Vec3 n1 = d1.cross(plane_normal);
		Vec3 n2 = d2.cross(plane_normal);

		if (n0.dot(t0.sub(plane_intersect)) < 0 || n1.dot(t1.sub(plane_intersect)) < 0 || n2.dot(t2.sub(plane_intersect)) < 0) {
			// intersection point is outside of the triangle.
			return null;
		}

		return plane_intersect;
	}

	/**
	 * Takes a line segment defined by its endpoints, and a triangle defined by its 3 vertices, and returns the intersection point 
	 * if it exists. 
	 * @param a0
	 * @param a1
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 lineSegment_triangleIntersect(Vec3 a0, Vec3 a1, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 dir = new Vec3(a0, a1).normalize();
		Vec3 ret = ray_triangleIntersect(a0, dir, t0, t1, t2);
		if (ret == null || MathUtils.dist(a0, ret) > MathUtils.dist(a0, a1)) {
			return null;
		}
		return ret;
	}

	/**
	 * Take in a ray and a sphere, and returns the first point where the ray intersects with the sphere. 
	 * Assumes that the sphere is not filled, so will only consider when the ray enters and exits the sphere
	 * 
	 * Can reduce ray sphere to a quadratic equation
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param sphere_center
	 * @param sphere_radius
	 * @return
	 */
	public static Vec3 ray_sphereIntersect(Vec3 ray_origin, Vec3 ray_dir, Vec3 sphere_center, float sphere_radius) {
		Vec3 offset_ray_origin = ray_origin.sub(sphere_center);

		float a = ray_dir.dot(ray_dir);
		float b = 2.0f * ray_dir.dot(offset_ray_origin);
		float c = offset_ray_origin.dot(offset_ray_origin) - sphere_radius * sphere_radius;

		float d = b * b - 4.0f * a * c;

		if (d >= 0) {
			float dist = (float) ((-b - Math.sqrt(d)) / (2.0 * a));
			if (dist > 0.0001) {
				//entry collision
				return ray_origin.add(ray_dir.mul(dist));
			}
			else {
				dist = (float) ((-b + Math.sqrt(d)) / (2.0 * a));
				if (dist > 0.0001) {
					//exit collision
					return ray_origin.add(ray_dir.mul(dist));
				}
			}
		}
		return null; //no collision
	}

	/**
	 * A bounding box is an axis aligned parallelipiped. It can be defined by a minimum and maximum point.
	 * Returns the closest point to the ray origin where the ray intersects with the boundary of the bounding box
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param bb_min
	 * @param bb_max
	 * @return
	 */
	public static Vec3 ray_AABBIntersect(Vec3 ray_origin, Vec3 ray_dir, Vec3 bb_min, Vec3 bb_max) {
		float[] dir_component = new float[] { ray_dir.x, ray_dir.y, ray_dir.z };
		float[] pos_component = new float[] { ray_origin.x, ray_origin.y, ray_origin.z };
		float[] min_component = new float[] { bb_min.x, bb_min.y, bb_min.z };
		float[] max_component = new float[] { bb_max.x, bb_max.y, bb_max.z };
		boolean did_hit = false;
		boolean inside_box = pointInsideAABB(ray_origin, bb_min, bb_max);
		float hit_dist = (float) 1e9; //some large number
		for (int i = 0; i < 3; i++) {
			if (dir_component[i] == 0) {
				continue;
			}
			float tgt = dir_component[i] < 0 ^ inside_box ? max_component[i] : min_component[i];
			float dist = tgt - pos_component[i];
			float ray_mul = dist / dir_component[i];
			Vec3 test_pos = ray_origin.add(ray_dir.mul(ray_mul));
			if (pointInsideAABB(test_pos, bb_min, bb_max) && ray_mul >= 0) {
				did_hit = true;
				hit_dist = Math.min(hit_dist, ray_mul);
			}
		}
		return did_hit ? ray_origin.add(ray_dir.mul(hit_dist)) : null;
	}

	/**
	 * Take in a line and a triangle, and returns the intersection if it exists.
	 * 
	 * @param line_origin
	 * @param line_dir
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 line_triangleIntersect(Vec3 line_origin, Vec3 line_dir, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 d0 = new Vec3(t0, t1).normalize();
		Vec3 d1 = new Vec3(t1, t2).normalize();
		Vec3 d2 = new Vec3(t2, t0).normalize();

		Vec3 plane_origin = new Vec3(t0);
		Vec3 plane_normal = d0.cross(d1).normalize();

		Vec3 plane_intersect = line_planeIntersect(line_origin, line_dir, plane_origin, plane_normal);
		if (plane_intersect == null) {
			// if it doesn't intersect the plane, then theres no way it intersects the
			// triangle.
			return null;
		}

		// now, we just have to make sure that the intersection point is inside the
		// triangle.
		Vec3 n0 = d0.cross(plane_normal);
		Vec3 n1 = d1.cross(plane_normal);
		Vec3 n2 = d2.cross(plane_normal);

		if (n0.dot(t0.sub(plane_intersect)) < 0 || n1.dot(t1.sub(plane_intersect)) < 0 || n2.dot(t2.sub(plane_intersect)) < 0) {
			// intersection point is outside of the triangle.
			return null;
		}

		return plane_intersect;
	}

	/**
	 * Take in a sphere and a triangle, and returns the point on the triangle, p, where dist(p, sphere_origin) is minimal.
	 * 
	 * @param sphere_origin
	 * @param sphere_radius
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 sphere_triangleIntersect(Vec3 sphere_origin, float sphere_radius, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 d0 = new Vec3(t0, t1).normalize();
		Vec3 d1 = new Vec3(t1, t2).normalize();
		Vec3 d2 = new Vec3(t2, t0).normalize();

		Vec3 plane_origin = new Vec3(t0);
		Vec3 plane_normal = d0.cross(d1).normalize();

		// first check if the sphere intersects the plane the triangle defines
		Vec3 plane_intersect = line_planeIntersect(sphere_origin, plane_normal, plane_origin, plane_normal);
		if (new Vec3(plane_intersect, sphere_origin).length() > sphere_radius) {
			// sphere doesn't intersect the plane
			return null;
		}

		// check if sphere_origin projects to a point in the triangle.
		// If true, it means that the intersection point isn't a corner or edge of the
		// triangle.
		Vec3 triangle_intersect = line_triangleIntersect(sphere_origin, plane_normal, t0, t1, t2);
		if (triangle_intersect != null) {
			if (new Vec3(triangle_intersect, sphere_origin).length() < sphere_radius) {
				return triangle_intersect;
			}
			//this shouldn't happen i think
			return null;
		}

		// else, check if sphere_origin projects onto a line segment of the triangle.
		// if we project the point clamped onto the line segment, then we don't have to
		// check the vertices.
		Vec3 minS_p = null;
		float minDist = -1f;
		Vec3 s0_p = point_lineSegmentProjectClamped(sphere_origin, t0, t1);
		Vec3 s1_p = point_lineSegmentProjectClamped(sphere_origin, t1, t2);
		Vec3 s2_p = point_lineSegmentProjectClamped(sphere_origin, t2, t0);
		if (s0_p != null) {
			float dist = new Vec3(s0_p, sphere_origin).length();
			if (minS_p == null || dist < minDist) {
				minS_p = s0_p;
				minDist = dist;
			}
		}
		if (s1_p != null) {
			float dist = new Vec3(s1_p, sphere_origin).length();
			if (minS_p == null || dist < minDist) {
				minS_p = s1_p;
				minDist = dist;
			}
		}
		if (s2_p != null) {
			float dist = new Vec3(s2_p, sphere_origin).length();
			if (minS_p == null || dist < minDist) {
				minS_p = s2_p;
				minDist = dist;
			}
		}
		if (minS_p != null && minDist < sphere_radius) {
			return minS_p;
		}

		// else, the sphere doesn't intersect the triangle
		return null;
	}

	/**
	 * Takes a capsule and a triangle, and determines the point on the triangle, p, where dist(p, c) is minimal. 
	 * Point c is a point in the capsule bound to the line segment defined by capsule_top and capsule_bottom.
	 * 
	 * @param capsule_top
	 * @param capsule_bottom
	 * @param capsule_radius
	 * @param t0
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static Vec3 capsule_triangleIntersect(Vec3 capsule_bottom, Vec3 capsule_top, float capsule_radius, Vec3 t0, Vec3 t1, Vec3 t2) {
		Vec3 capsule_tangent = new Vec3(capsule_bottom, capsule_top).normalize();
		Vec3 capsule_a = capsule_bottom.add(capsule_tangent.mul(capsule_radius));
		Vec3 capsule_b = capsule_top.sub(capsule_tangent.mul(capsule_radius));

		Vec3 d0 = new Vec3(t0, t1).normalize();
		Vec3 d1 = new Vec3(t1, t2).normalize();
		Vec3 d2 = new Vec3(t2, t0).normalize();

		Vec3 plane_normal = d0.cross(d1).normalize();

		Vec3 n0 = d0.cross(plane_normal);
		Vec3 n1 = d1.cross(plane_normal);
		Vec3 n2 = d2.cross(plane_normal);

		Vec3 referencePoint = new Vec3(0);
		Vec3 plane_intersect = line_planeIntersect(capsule_bottom, capsule_tangent, t0, plane_normal);
		if (plane_intersect == null) {
			// capsule_tangent is parallel to the plane, plane_intersect doesn't exist.
			referencePoint = new Vec3(t0);
		}
		else if (n0.dot(t0.sub(plane_intersect)) < 0 || n1.dot(t1.sub(plane_intersect)) < 0 || n2.dot(t2.sub(plane_intersect)) < 0) {
			// plane_intersect point is outside of the triangle.
			// find closest point to plane_intersect that is on the triangle.
			Vec3 minS_p = null;
			float minDist = -1f;
			Vec3 s0_p = point_lineSegmentProjectClamped(plane_intersect, t0, t1);
			Vec3 s1_p = point_lineSegmentProjectClamped(plane_intersect, t1, t2);
			Vec3 s2_p = point_lineSegmentProjectClamped(plane_intersect, t2, t0);

			float dist = new Vec3(s0_p, plane_intersect).length();
			if (minS_p == null || dist < minDist) {
				minS_p = s0_p;
				minDist = dist;
			}

			dist = new Vec3(s1_p, plane_intersect).length();
			if (minS_p == null || dist < minDist) {
				minS_p = s1_p;
				minDist = dist;
			}

			dist = new Vec3(s2_p, plane_intersect).length();
			if (minS_p == null || dist < minDist) {
				minS_p = s2_p;
				minDist = dist;
			}

			referencePoint = minS_p;
		}
		else {
			// plane intersection is inside the triangle
			referencePoint = plane_intersect;
		}

		Vec3 capsule_c = point_lineSegmentProjectClamped(referencePoint, capsule_a, capsule_b);
		return sphere_triangleIntersect(capsule_c, capsule_radius, t0, t1, t2);
	}

	/**
	 * Takes a ray and a capsule, and returns the point on the ray that is the deepest inside the capsule if they intersect. 
	 * @param ray_origin
	 * @param ray_dir
	 * @param capsule_bottom
	 * @param capsule_top
	 * @param capsule_radius
	 * @return
	 */
	public static Vec3 ray_capsuleIntersect(Vec3 ray_origin, Vec3 ray_dir, Vec3 capsule_bottom, Vec3 capsule_top, float capsule_radius) {
		Vec3 capsule_tangent = new Vec3(capsule_bottom, capsule_top).normalize();
		Vec3 capsule_a = capsule_bottom.add(capsule_tangent.mul(capsule_radius));
		Vec3 capsule_b = capsule_top.sub(capsule_tangent.mul(capsule_radius));

		Vec3 ray_close = new Vec3(0);
		Vec3 capsule_close = new Vec3(0);

		ray_lineSegmentDistance(ray_origin, ray_dir, capsule_a, capsule_b, ray_close, capsule_close);

		float dist = new Vec3(ray_close, capsule_close).length();
		if (dist < capsule_radius) {
			return ray_close;
		}
		return null;
	}

	/**
	 * Takes a line and two points, and returns true if the two points are on the same side of the line.
	 * 
	 * @param lineP
	 * @param lineVec
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean pointsOnSameSideOfLine(Vec2 lineP, Vec2 lineVec, Vec2 a, Vec2 b) {
		// copy a and b
		Vec2 p1 = new Vec2(a);
		Vec2 p2 = new Vec2(b);

		// first offset lineP, p1, and p2 so that lineP is at the origin
		p1.subi(lineP);
		p2.subi(lineP);

		// calculate the line perpendicular to the input line
		// rotate the line 90 deg
		Vec2 perpendicular = new Vec2(lineVec.y, -lineVec.x);

		// now take dot product of the perpendicular vector with both points
		// if both dot products are negative or positive, then the points are on the
		// same side of the line
		Vec2 v1 = new Vec2(p1);
		Vec2 v2 = new Vec2(p2);
		float d1 = perpendicular.dot(v1);
		float d2 = perpendicular.dot(v2);

		return d1 * d2 >= 0;
	}

	/**
	 * Assuming that the points are arranged in a convex hull, it calculates the centroid of the points.
	 * 
	 * @param points
	 * @return
	 */
	public static Vec2 calculateCentroid(ArrayList<Vec2> points) {
		double accumulatedArea = 0.0f;
		double centerX = 0.0f;
		double centerY = 0.0f;

		for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
			double temp = points.get(i).x * points.get(j).y - points.get(j).x * points.get(i).y;
			accumulatedArea += temp;
			centerX += (points.get(i).x + points.get(j).x) * temp;
			centerY += (points.get(i).y + points.get(j).y) * temp;
		}

		if (Math.abs(accumulatedArea) < 1E-7f) {
			return new Vec2(0, 0);
		}

		accumulatedArea *= 3f;
		return new Vec2(centerX / accumulatedArea, centerY / accumulatedArea);
	}

	// -- ML --

	/**
	 * Regular sigmoid function
	 * 
	 * @param x
	 * @return
	 */
	public static float sigmoid(double x) {
		return (float) (1f / (1f + Math.pow(Math.E, (-1f * x))));
	}

	/**
	 * Derivative of regular sigmoid function, with center at 0
	 * 
	 * @param x
	 * @return
	 */
	public static float sigmoidDerivative(double x) {
		return sigmoid(x) * (1f - sigmoid(x));
	}

	/**
	 * Regular ReLU (Rectified Linear Unit) function
	 * 
	 * @param x
	 * @return
	 */
	public static float relu(double x) {
		return (float) Math.max(0, x);
	}

	/**
	 * Regular Logit or inverse sigmoid function
	 * 
	 * @param x
	 * @return
	 */
	public static float logit(double x) {
		return (float) Math.log(x / (1d - x));
	}

	/**
	 * Derivative of regular ReLU function
	 * 
	 * @param x
	 * @return
	 */
	public static float reluDerivative(double x) {
		return x <= 0 ? 0 : 1;
	}

	/**
	 * Hyperbolic tangent function
	 * 
	 * @param x
	 * @return
	 */
	public static float tanh(double x) {
		return (float) ((Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x)));
	}

	/**
	 * Derivative of hyperbolic tangent function
	 * 
	 * @param x
	 * @return
	 */
	public static float tanhDerivative(double x) {
		return (float) (1.0 - Math.pow(MathUtils.tanh(x), 2));
	}

	/**
	 * Inverse of hyperbolic tangent
	 * Defined on the range (-1, 1)
	 * @param x
	 * @return
	 */
	public static float invtanh(double x) {
		System.out.println(x);
		if (x <= -1) {
			return (float) -1e9;
		}
		else if (x >= 1) {
			return (float) 1e9;
		}
		return (float) ((Math.log((1.0 + x) / (1.0 - x))) / 2.0f);
	}

	// -- STATS --

	/**
	 * Approximates a normal distribution, in this case, n = 4.
	 * 
	 * @param x
	 * @return
	 */
	public static float irwinHallDistribution(double x) {
		if (-2 < x && x < -1) {
			return (float) (0.25f * Math.pow(x + 2, 3));
		}
		if (-1 < x && x < 1) {
			return (float) (0.25f * (Math.pow(Math.abs(x), 3) * 3 - Math.pow(x, 2) * 6 + 4));
		}
		if (1 < x && x < 2) {
			return (float) (0.25f * Math.pow(2 - x, 3));
		}
		return 0;
	}

	// -- NUMBER THEORY --

	private static Pair<int[], ArrayList<Integer>> _primeSieve(int n) {
		int[] lp = new int[n + 1];
		ArrayList<Integer> primes = new ArrayList<>();
		for (int i = 2; i <= n; i++) {
			if (lp[i] == 0) {
				lp[i] = i;
				primes.add(i);
			}
			for (int j = 0; i * primes.get(j) <= n; j++) {
				lp[i * primes.get(j)] = primes.get(j);
				if (primes.get(j) == lp[i]) {
					break;
				}
			}
		}
		return new Pair<>(lp, primes);
	}

	/**
	 * Generates a list of all primes in the range [0, n]. 
	 * 
	 * Complexity: O(n)
	 * @param n
	 * @return
	 */
	public static ArrayList<Integer> primeSieve(int n) {
		return _primeSieve(n).second;
	}

	/**
	 * For all integers in the range [0, n], generates the lowest prime factor for that integer. 
	 * 
	 * Complexity: O(n)
	 * @param n
	 * @return
	 */
	public static int[] calculateRangeLPF(int n) {
		return _primeSieve(n).first;
	}

	/**
	 * Cantor pairing function, uniquely maps a pair of integers back to the set of integers. 
	 * Mod is included for the purpose of hashing. 
	 * 
	 * @param a
	 * @param b
	 * @param m
	 * @return
	 */
	public static long cantor(long a, long b, long m) {
		return ((a + b) * (a + b + 1) / 2 + b) % m;
	}

}
