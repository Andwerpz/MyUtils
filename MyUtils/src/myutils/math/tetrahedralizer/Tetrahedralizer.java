package myutils.math.tetrahedralizer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;

import myutils.math.MathUtils;
import myutils.math.Vec3;
import myutils.misc.Triple;

public class Tetrahedralizer {
	//NOTE: don't rely on this to tetrahedralize meshes. This has many issues that still need to be worked out.
	// use pygalmesh instead. Much easier, and also can write out to an XML 

	//input is a bunch of faces creating a closed polygon in 3D, and output should be a bunch of 
	//tetrahedra partitioning the polygon. 

	//uses incremental delaunay tetrahedralization to generate. 

	//how to compute bounding tetrahedron:
	//https://computergraphics.stackexchange.com/questions/10533/how-to-compute-a-bounding-tetrahedron

	public Tetrahedralizer() {
		//yay
	}

	//these aren't set until after a call to tetrahedralize()
	private ArrayList<Vec3> vertices;
	private ArrayList<int[]> tetrahedra;

	public ArrayList<Vec3> getVertices() {
		return this.vertices;
	}

	public ArrayList<int[]> getTetrahedra() {
		return this.tetrahedra;
	}

	public void tetrahedralize(ArrayList<Vec3> vertices) {
		if (vertices.size() == 0) {
			return;
		}

		this.vertices = new ArrayList<>();
		this.tetrahedra = new ArrayList<>();
		HashSet<Tetrahedron> tet_list = new HashSet<>();
		HashMap<Face, HashSet<Tetrahedron>> adj = new HashMap<>();

		// -- generate bounding tetrahedra
		//find bounding box
		Vec3 bb_min = new Vec3(vertices.get(0));
		Vec3 bb_max = new Vec3(vertices.get(0));
		for (Vec3 v : vertices) {
			bb_min = MathUtils.min(bb_min, v);
			bb_max = MathUtils.max(bb_max, v);
		}

		//add some slop
		bb_min.subi(new Vec3(1));
		bb_max.addi(new Vec3(1));

		//find bounding cube
		Vec3 bc_center = MathUtils.lerp(bb_min, 0, bb_max, 1, 0.5f);
		float bc_sl = Math.max(Math.max(bb_max.x - bb_min.x, bb_max.y - bb_min.y), bb_max.z - bb_min.z) / 2.0f;
		bc_sl *= 3; //scale up so tetrahedra fits everything
		Vec3 bc_min = bc_center.sub(new Vec3(bc_sl));
		Vec3 bc_max = bc_center.add(new Vec3(bc_sl));

		//find bounding tetrahedra
		this.vertices.add(new Vec3(bc_min.x, bc_min.y, bc_min.z));
		this.vertices.add(new Vec3(bc_max.x, bc_min.y, bc_max.z));
		this.vertices.add(new Vec3(bc_max.x, bc_max.y, bc_min.z));
		this.vertices.add(new Vec3(bc_min.x, bc_max.y, bc_max.z));
		tet_list.add(new Tetrahedron(0, 1, 2, 3, adj));

		// -- add vertices one by one
		for (Vec3 v : vertices) {
			//add vertex to vertex list
			int cv_ind = this.vertices.size();
			this.vertices.add(v);

			//find current tetrahedra
			Tetrahedron cur_tet = null;
			for (Tetrahedron t : tet_list) {
				if (t.containsPoint(v)) {
					cur_tet = t;
					break;
				}
			}

			if (cur_tet == null) {
				System.err.println("Failed to add vertex : " + v);
				continue;
			}

			//find all bad tetrahedra.
			HashSet<Tetrahedron> vis_tet = new HashSet<>();
			HashSet<Face> vis_face = new HashSet<>();
			Queue<Face> q = new ArrayDeque<>();
			vis_tet.add(cur_tet);
			for (int i = 0; i < 4; i++) {
				vis_face.add(cur_tet.getFaceList()[i]);
				q.add(cur_tet.getFaceList()[i]);
			}
			while (q.size() != 0) {
				Face cur_face = q.poll();
				for (Tetrahedron next : adj.get(cur_face)) {
					if (vis_tet.contains(next)) {
						continue;
					}
					Vec3 circumcenter = next.calculateCircumcenter();
					float circumradius = circumcenter == null ? 1e9f : MathUtils.dist(circumcenter, this.vertices.get(next.a));
					if (circumcenter == null || MathUtils.dist(circumcenter, v) < circumradius) {
						//found bad tetrahedron
						vis_tet.add(next);

						//go to other faces
						for (Face next_face : next.getFaceList()) {
							if (vis_face.contains(next_face)) {
								continue;
							}
							vis_face.add(next_face);
							q.add(next_face);
						}
					}
				}
			}

			//remove all bad tetrahedra
			HashSet<Face> end_face = new HashSet<>();
			for (Tetrahedron t : vis_tet) {
				for (Face f : t.getFaceList()) {
					boolean is_end_face = false;
					if (f.c < 4) {
						//this is a boundary face, add it to end faces
						is_end_face = true;
					}

					//see if this borders a non-visited tetrahedron
					for (Tetrahedron other : adj.get(f)) {
						if (!vis_tet.contains(other)) {
							is_end_face = true;
						}
					}

					if (is_end_face) {
						end_face.add(f);
					}
				}
				t.kill();
				tet_list.remove(t);
			}

			//create tetrahedral fan to fill the void
			for (Face f : end_face) {
				Tetrahedron t = new Tetrahedron(cv_ind, f.a, f.b, f.c, adj);
				tet_list.add(t);
			}

		}

		// -- generate result
		//remove bounding vertices
		this.vertices.remove(0);
		this.vertices.remove(0);
		this.vertices.remove(0);
		this.vertices.remove(0);

		for (Tetrahedron t : tet_list) {
			if (Math.min(Math.min(t.a, t.b), Math.min(t.c, t.d)) < 4) {
				continue;
			}
			this.tetrahedra.add(new int[] { t.a - 4, t.b - 4, t.c - 4, t.d - 4 });
		}
	}

	//faces should form a closed polygon in 3D
	public void tetrahedralize(ArrayList<Vec3> vertices, ArrayList<int[]> faces) {
		this.tetrahedralize(vertices);

		//remove all tetrahedra that are outside of the polygon
		ArrayList<int[]> n_tetrahedra = new ArrayList<>();
		for (int i = this.tetrahedra.size() - 1; i >= 0; i--) {
			int[] t = this.tetrahedra.get(i);
			Vec3 P = new Vec3(0);
			for (int j = 0; j < 4; j++) {
				P.addi(this.vertices.get(t[j]));
			}
			P.muli(0.25);

			//all axes majority vote
			Vec3[] dirs = new Vec3[] { new Vec3(1, 0, 0), new Vec3(-1, 0, 0), new Vec3(0, 1, 0), new Vec3(0, -1, 0), new Vec3(0, 0, 1), new Vec3(0, 0, -1) };
			int good_cnt = 0;
			for (int dptr = 0; dptr < dirs.length; dptr++) {
				Vec3 dir = dirs[dptr];
				int intersect_cnt = 0;
				for (int j = 0; j < faces.size(); j++) {
					Vec3 A = vertices.get(faces.get(j)[0]);
					Vec3 B = vertices.get(faces.get(j)[1]);
					Vec3 C = vertices.get(faces.get(j)[2]);

					intersect_cnt += MathUtils.ray_triangleIntersect(P, dir, A, B, C) != null ? 1 : 0;
				}

				good_cnt += intersect_cnt % 2;
			}

			if (good_cnt >= dirs.length / 2) {
				n_tetrahedra.add(t);
			}

			//			//do something with winding order. 
			//			double angsum = 0;
			//			for (int j = 0; j < faces.size(); j++) {
			//				Vec3 A = vertices.get(faces.get(j)[0]);
			//				Vec3 B = vertices.get(faces.get(j)[1]);
			//				Vec3 C = vertices.get(faces.get(j)[2]);
			//
			//				Vec3 a = A.sub(P);
			//				Vec3 b = B.sub(P);
			//				Vec3 c = C.sub(P);
			//
			//				a.normalize();
			//				b.normalize();
			//				c.normalize();
			//
			//				double numer = a.dot(b.cross(c));
			//				double denom = 1.0 + b.dot(c) + c.dot(a) + a.dot(b);
			//				//				double denom = a.length() * b.length() * c.length() + (a.dot(b)) * c.length() + (b.dot(c)) * a.length() + (c.dot(a)) * b.length();
			//				double solid_angle = 2.0f * (float) Math.atan2(numer, denom);
			//				angsum += solid_angle;
			//			}
			//
			//			System.out.println("ANGSUM : " + angsum);
			//			if (angsum > 6) {
			//				n_tetrahedra.add(t);
			//			}
		}

		this.tetrahedra = n_tetrahedra;
	}

	private class Tetrahedron {
		HashMap<Face, HashSet<Tetrahedron>> adj;
		int a, b, c, d; //vertex indices

		public Tetrahedron(int _a, int _b, int _c, int _d, HashMap<Face, HashSet<Tetrahedron>> _adj) {
			this.a = _a;
			this.b = _b;
			this.c = _c;
			this.d = _d;
			this.adj = _adj;
			for (Face f : this.getFaceList()) {
				if (!this.adj.containsKey(f)) {
					this.adj.put(f, new HashSet<>());
				}
				this.adj.get(f).add(this);
			}
		}

		public Face[] getFaceList() {
			return new Face[] { new Face(a, b, c), new Face(a, b, d), new Face(a, c, d), new Face(b, c, d) };
		}

		public boolean containsPoint(Vec3 pt) {
			float tet_volume = Math.abs(MathUtils.signedTetrahedronVolume(vertices.get(a), vertices.get(b), vertices.get(c), vertices.get(d)));
			float test_volume = 0;
			test_volume += Math.abs(MathUtils.signedTetrahedronVolume(pt, vertices.get(b), vertices.get(c), vertices.get(d)));
			test_volume += Math.abs(MathUtils.signedTetrahedronVolume(vertices.get(a), pt, vertices.get(c), vertices.get(d)));
			test_volume += Math.abs(MathUtils.signedTetrahedronVolume(vertices.get(a), vertices.get(b), pt, vertices.get(d)));
			test_volume += Math.abs(MathUtils.signedTetrahedronVolume(vertices.get(a), vertices.get(b), vertices.get(c), pt));
			return Math.abs(test_volume - tet_volume) < 0.001;
		}

		public Vec3 calculateCircumcenter() {
			return MathUtils.calculateCircumcenter(vertices.get(a), vertices.get(b), vertices.get(c), vertices.get(d));
		}

		public Vec3 calculateCentroid() {
			return vertices.get(a).add(vertices.get(b)).add(vertices.get(c)).add(vertices.get(d)).div(4);
		}

		public HashSet<Tetrahedron> getAdjTets() {
			HashSet<Tetrahedron> res = new HashSet<>();
			for (Face f : this.getFaceList()) {
				res.addAll(this.adj.get(f));
			}
			res.remove(this);
			return res;
		}

		public void kill() {
			for (Face f : this.getFaceList()) {
				this.adj.get(f).remove(this);
			}
		}
	}

	private class Face {
		//a < b < c
		int a, b, c;

		public Face(int _a, int _b, int _c) {
			this.a = _a;
			this.b = _b;
			this.c = _c;

			//order face such that a < b < c
			if (this.a > this.b) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			if (this.a > this.c) {
				int tmp = a;
				a = c;
				c = tmp;
			}
			if (this.b > c) {
				int tmp = b;
				b = c;
				c = tmp;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.a, this.b, this.c);
		}

		@Override
		public boolean equals(Object other) {
			if (other == null) {
				return false;
			}
			if (!(other instanceof Face)) {
				return false;
			}
			if (other == this) {
				return true;
			}
			Face f = (Face) other;
			return f.a == this.a && f.b == this.b && f.c == this.c;
		}

	}
}
