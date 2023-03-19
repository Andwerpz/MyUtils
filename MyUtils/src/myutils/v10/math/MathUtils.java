package myutils.v10.math;

import java.util.ArrayList;
import java.util.HashSet;

public class MathUtils {

	// MathTools v2, made with LWGJL in mind

	// -- GENERAL --

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
	 * Linearly interpolates between two points
	 * 
	 * @param x1
	 * @param t1
	 * @param x2
	 * @param t2
	 * @param t3
	 * @return
	 */
	public static float interpolate(float x1, float t1, float x2, float t2, float t3) {
		float v = (x2 - x1) / (t2 - t1);
		return x1 + (t3 - t1) * v;
	}

	/**
	 * Linearly interpolates between two vec3 points
	 * 
	 * @param 
	 */
	public static Vec3 interpolate(Vec3 v1, float t1, Vec3 v2, float t2, float t3) {
		Vec3 v = (v2.sub(v1)).divi(t2 - t1);
		return v1.add(v.mul(t3 - t1));
	}

	// -- LINEAR ALGEBRA --

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
	 * Calculates determinant of 3 vectors
	 * 
	 * Could be useful when determining if a right or left turn occurred at vector b. 
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */

	public static float determinant(Vec2 a, Vec2 b, Vec2 c) {
		return (b.x - a.x) * (c.y - b.y) - (c.x - b.x) * (b.y - a.y);
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
			float det = determinant(a, b, c);
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
			float det = determinant(a, b, c);
			if (det < 0 ^ clockwise) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Wrapper method to calculate convex hull
	 * 
	 * @param verts
	 * @return
	 */

	public static ArrayList<Vec2> calculateConvexHull(ArrayList<Vec2> verts) {
		Vec2[] v = Vec2.arrayOf(verts.size());
		for (int i = 0; i < verts.size(); i++) {
			v[i].set(verts.get(i));
		}
		Vec2[] ans = calculateConvexHull(v);
		ArrayList<Vec2> ret = new ArrayList<>();
		for (int i = 0; i < ans.length; i++) {
			ret.add(ans[i]);
		}
		return ret;
	}

	/**
	 * Given a list of points, will calculate convex hull
	 * 
	 * @param verts
	 * @return
	 */

	public static Vec2[] calculateConvexHull(Vec2[] verts) {
		// Find the right most point on the hull
		int rightMost = 0;
		int vertexCount = 0;
		double highestXCoord = verts[0].x;
		for (int i = 1; i < verts.length; ++i) {
			double x = verts[i].x;

			if (x > highestXCoord) {
				highestXCoord = x;
				rightMost = i;
			}
			// If matching x then take farthest negative y
			else if (x == highestXCoord) {
				if (verts[i].y < verts[rightMost].y) {
					rightMost = i;
				}
			}
		}

		int[] hull = new int[verts.length];
		int outCount = 0;
		int indexHull = rightMost;

		for (;;) {
			hull[outCount] = indexHull;

			// Search for next index that wraps around the hull
			// by computing cross products to find the most counter-clockwise
			// vertex in the set, given the previos hull index
			int nextHullIndex = 0;
			for (int i = 1; i < verts.length; ++i) {
				// Skip if same coordinate as we need three unique
				// points in the set to perform a cross product
				if (nextHullIndex == indexHull) {
					nextHullIndex = i;
					continue;
				}

				// Cross every set of three unique vertices
				// Record each counter clockwise third vertex and add
				// to the output hull
				// See : http://www.oocities.org/pcgpe/math2d.html
				Vec2 e1 = verts[nextHullIndex].sub(verts[hull[outCount]]);
				Vec2 e2 = verts[i].sub(verts[hull[outCount]]);
				double c = Vec2.cross(e1, e2);
				if (c < 0.0f) {
					nextHullIndex = i;
				}

				// Cross product is zero then e vectors are on same line
				// therefore want to record vertex farthest along that line
				if (c == 0.0f && e2.lengthSq() > e1.lengthSq()) {
					nextHullIndex = i;
				}
			}

			++outCount;
			indexHull = nextHullIndex;

			// Conclude algorithm upon wrap-around
			if (nextHullIndex == rightMost) {
				vertexCount = outCount;
				break;
			}
		}

		Vec2[] ans = Vec2.arrayOf(vertexCount);

		// Copy vertices into shape's vertices
		for (int i = 0; i < vertexCount; ++i) {
			ans[i].set(verts[hull[i]]);
		}

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

	public static ArrayList<int[]> calculateConcavePartition(ArrayList<Vec2> points) {
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
	 * Complexity: O(n^2)
	 * 
	 * @param points
	 * @return
	 */

	public static ArrayList<int[]> calculateTrianglePartition(ArrayList<Vec2> points) {
		boolean clockwise = !MathUtils.isCounterClockwiseWinding(points);
		int n = points.size();
		ArrayList<int[]> ans = new ArrayList<>();
		boolean[] v = new boolean[n];
		while (ans.size() != n - 2) {
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
				if (MathUtils.determinant(points.get(next[0]), points.get(next[1]), points.get(next[2])) < 0 ^ clockwise) {
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
	 * Takes in two line segments, and returns the point of intersection, if it exists. Null otherwise.
	 * 
	 * @param a0
	 * @param a1
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static Vec2 lineSegment_lineSegmentIntersect(Vec2 a0, Vec2 a1, Vec2 b0, Vec2 b1) {
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

		// if uA and uB are between 0-1, lines are colliding
		if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
			// calculate the intersection point
			float intersectionX = x1 + (uA * (x2 - x1));
			float intersectionY = y1 + (uA * (y2 - y1));

			return new Vec2(intersectionX, intersectionY);
		}
		return null;
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
	 * Takes in two line segments in 3D, and returns the values for n and m.
	 * Where v is the vector between the two closest points of a and b,
	 * v.x = (a0.x + n * d0.x) - (b0.x + m * d1.x)
	 * v.y = (a0.y + n * d0.y) - (b0.y + m * d1.y)
	 * v.z = (a0.z + n * d0.z) - (b0.z + m * d1.z)
	 * 
	 * d0 is the direction vector of a, d0 is for b. 
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

		float n = 0;
		float m = 0;

		float nExp0 = 0;
		float mExp0 = 0;
		float cnst0 = 0;

		float nExp1 = 0;
		float mExp1 = 0;
		float cnst1 = 0;

		//v is the vector between the two closest points of a and b. 
		//v.x = (a0.x + n * d0.x) - (b0.x + m * d1.x);
		//v.y = (a0.y + n * d0.y) - (b0.y + m * d1.y);
		//v.z = (a0.z + n * d0.z) - (b0.z + m * d1.z);

		//v.dot(d0) = 0
		//v.dot(d1) = 0

		//((a0.x + n * d0.x) - (b0.x + m * d1.x)) * d0.x + 
		//((a0.y + n * d0.y) - (b0.y + m * d1.y)) * d0.y + 
		//((a0.z + n * d0.z) - (b0.z + m * d1.z)) * d0.z = 0

		nExp0 += d0.x * d0.x + d0.y * d0.y + d0.z * d0.z;
		mExp0 -= d1.x * d0.x + d1.y * d0.y + d1.z * d0.z;
		cnst0 -= a0.x * d0.x + a0.y * d0.y + a0.z * d0.z;
		cnst0 += b0.x * d0.x + b0.y * d0.y + b0.z * d0.z;

		//((a0.x + n * d0.x) - (b0.x + m * d1.x)) * d1.x + 
		//((a0.y + n * d0.y) - (b0.y + m * d1.y)) * d1.y + 
		//((a0.z + n * d0.z) - (b0.z + m * d1.z)) * d1.z = 0

		nExp1 += d0.x * d1.x + d0.y * d1.y + d0.z * d1.z;
		mExp1 -= d1.x * d1.x + d1.y * d1.y + d1.z * d1.z;
		cnst1 -= a0.x * d1.x + a0.y * d1.y + a0.z * d1.z;
		cnst1 += b0.x * d1.x + b0.y * d1.y + b0.z * d1.z;

		//nExp0 * n + mExp0 * m = cnst0
		//nExp1 * n + mExp1 * m = cnst1

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
	 * Take in a ray and a plane, and returns the intersection if it exists.
	 * 
	 * @param ray_origin
	 * @param ray_dir
	 * @param plane_origin
	 * @param plane_normal
	 * @return
	 */
	public static Vec3 ray_planeIntersect(Vec3 ray_origin, Vec3 ray_dir, Vec3 plane_origin, Vec3 plane_normal) {
		float ray_dirStepRatio = plane_normal.dot(ray_dir); // for each step in ray_dir, you go ray_dirStepRatio steps
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
	public static Vec2 computeCentroid(ArrayList<Vec2> points) {
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
	public static double sigmoid(double x) {
		return (1d / (1d + Math.pow(Math.E, (-1d * x))));
	}

	/**
	 * Derivative of regular sigmoid function, with center at 0
	 * 
	 * @param x
	 * @return
	 */
	public static double sigmoidDerivative(double x) {
		return sigmoid(x) * (1d - sigmoid(x));
	}

	/**
	 * Regular ReLU (Rectified Linear Unit) function
	 * 
	 * @param x
	 * @return
	 */
	public static double relu(double x) {
		return Math.max(0, x);
	}

	/**
	 * Regular Logit or inverse sigmoid function
	 * 
	 * @param x
	 * @return
	 */
	public static double logit(double x) {
		return Math.log(x / (1d - x));
	}

	/**
	 * Derivative of regular ReLU function
	 * 
	 * @param x
	 * @return
	 */
	public static double reluDerivative(double x) {
		return x <= 0 ? 0 : 1;
	}

	// -- STATS --

	/**
	 * Approximates a normal distribution, in this case, n = 4.
	 * 
	 * @param x
	 * @return
	 */
	public static double irwinHallDistribution(double x) {
		if (-2 < x && x < -1) {
			return 0.25 * Math.pow(x + 2, 3);
		}
		if (-1 < x && x < 1) {
			return 0.25 * (Math.pow(Math.abs(x), 3) * 3 - Math.pow(x, 2) * 6 + 4);
		}
		if (1 < x && x < 2) {
			return 0.25 * Math.pow(2 - x, 3);
		}
		return 0;
	}

}
