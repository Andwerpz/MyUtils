package myutils.math.triangulator;

import java.util.ArrayList;

import myutils.math.MathUtils;
import myutils.math.Vec2;

public class Triangulator {
	//should be able to take in any non self-intersecting polygon and triangulate it. 
	//triangulate as in return as set of non-intersecting triangles such that the union is the input polygon. 
	//yes, the polygon can have holes. 

	//since we're dealing with polygons with holes, i'm going to return triangles as 3 points in 2D space, not references
	//back to the original list as in 3 indices. 

	//the points in the polygon should be enumerated in CCW winding, and points representing
	//the holes should be wound CW, or opposite of the polygon points. 

	//currently, the strategy is to merge all the holes into the polygons that contain them, and triangulate
	//the polygons 

	//should be a list of mutually non-intersecting polygons. 
	private ArrayList<ArrayList<Vec2>> polygons;

	//stores all current polygon edges. Useful for merging holes
	private ArrayList<Vec2[]> polygonSegments;

	public Triangulator() {
		this.polygons = new ArrayList<>();
		this.polygonSegments = new ArrayList<>();
	}

	public void clear() {
		this.polygons.clear();
	}

	/**
	 * Points given should be wound CCW
	 * @param p
	 */
	public void addPolygon(ArrayList<Vec2> p) {
		this.polygons.add(p);
		for (int i = 0; i < p.size(); i++) {
			this.polygonSegments.add(new Vec2[] { p.get(i), p.get((i + 1) % p.size()) });
		}
	}

	/**
	 * Points given should be wound CW
	 * Hole should merge with nearest polygon. 
	 * @param p
	 */
	public void addHole(ArrayList<Vec2> h) {
		//to test which polygon the hole should be merged with, we can just try to draw a line from every
		//polygon vertex that is outside of the hole. If the line doesn't intersect with any other polygons, 
		//then it's a valid merge. 
		for (ArrayList<Vec2> p : this.polygons) {
			//first check if polygon is inside hole. If it is, then we don't consider this one
			//since we assume the hole is non-intersecting with any polygon, if one point is inside, all points should
			//be inside. 
			if (MathUtils.pointInsidePolygon(h, p.get(0))) {
				continue;
			}

			int p_mergeInd = -1;
			int h_mergeInd = -1;
			for (int i = 0; i < p.size() && p_mergeInd == -1; i++) {
				for (int j = 0; j < h.size() && p_mergeInd == -1; j++) {
					Vec2 pv = new Vec2(p.get(i));
					Vec2 hv = new Vec2(h.get(j));
					Vec2 p2h = new Vec2(pv, hv);
					pv.addi(p2h.mul(0.0001));
					hv.addi(p2h.mul(-0.0001));
					if (!this.line_polygonsIntersect(pv, hv)) {
						p_mergeInd = i;
						h_mergeInd = j;
					}
				}
			}

			if (p_mergeInd != -1) {
				//we've found a suitable polygon to merge with
				//duplicate merge point in polygon
				p.add(p_mergeInd + 1, new Vec2(p.get(p_mergeInd)));

				//add rest of hole to polygon between duplicated points. 
				//duplicate the start point of hole as well. 
				int ptr = p_mergeInd + 1;
				for (int i = 0; i < h.size() + 1; i++) {
					p.add(ptr++, h.get((h_mergeInd + i) % h.size()));
				}
				return;
			}
		}

		//if we couldn't find a place for this hole, idk what to do. 
		//perhaps we hold on to it? or just throw some sort of error
	}

	/**
	 * Returns true if the input line intersects any line in polygonSegments
	 * @param l0
	 * @param l1
	 * @return
	 */
	private boolean line_polygonsIntersect(Vec2 l0, Vec2 l1) {
		for (Vec2[] seg : this.polygonSegments) {
			if (MathUtils.lineSegment_lineSegmentIntersect(l0, l1, seg[0], seg[1]) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fills should be wound CCW, and holes should be wound CW. 
	 * I add fills first, then holes last. 
	 * All holes should be contained by a fill
	 * @param polys
	 */
	public void addPolygons(ArrayList<ArrayList<Vec2>> polys) {
		ArrayList<ArrayList<Vec2>> fills = new ArrayList<>();
		ArrayList<ArrayList<Vec2>> holes = new ArrayList<>();
		for (ArrayList<Vec2> p : polys) {
			if (MathUtils.isCounterClockwiseWinding(p)) {
				fills.add(p);
			}
			else {
				holes.add(p);
			}
		}
		for (ArrayList<Vec2> p : polys) {
			this.polygons.add(p);
		}
		for (ArrayList<Vec2> p : holes) {
			this.addHole(p);
		}
	}

	/**
	 * Returns triangle representation of stored polygons
	 * @return
	 */
	public ArrayList<Vec2[]> triangulate() {
		ArrayList<Vec2[]> tris = new ArrayList<>();

		for (ArrayList<Vec2> p : this.polygons) {
			ArrayList<int[]> tri_inds = MathUtils.calculateTrianglePartition(p);
			for (int[] inds : tri_inds) {
				Vec2[] tri = new Vec2[3];
				tri[0] = new Vec2(p.get(inds[0]));
				tri[1] = new Vec2(p.get(inds[1]));
				tri[2] = new Vec2(p.get(inds[2]));
				tris.add(tri);
			}
		}
		return tris;
	}

}
