package myutils.v10.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

import myutils.v10.misc.Pair;

public class GraphUtils {
	
	//TODO move this into a graph objects
	
	private static <T> void buildGraph(ArrayList<GraphNode<T>> a) {
		HashMap<T, GraphNode<T>> objToNode = new HashMap<>();
		for(GraphNode<T> n : a) {
			objToNode.put(n.obj, n);
			n.nodeEdges.clear();
		}
		
		//build node edges
		for(GraphNode<T> n : a) {
			for(T o : n.edges) {
				n.nodeEdges.add(objToNode.get(o));
			}
		}
	}
	
	/**
	 * Given an adjacency list c, returns the strongly connected components. 
	 * 
	 * Nodes in the adjacency list should be normalized, so that all nodes have an id between 0 to n - 1, where
	 * n is the amount of nodes. 
	 * 
	 * TODO make this take graph nodes
	 * @param c
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> kosajaru(ArrayList<ArrayList<Integer>> c) {
		int n = c.size();
		ArrayList<ArrayList<Integer>> rc = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			rc.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < c.get(i).size(); j++) {
				int a = i;
				int b = c.get(i).get(j);
				rc.get(b).add(a);
			}
		}
		boolean[] v = new boolean[n];
		Stack<Integer> s = new Stack<>();
		for (int i = 0; i < n; i++) {
			if (!v[i]) {
				kosajaru1(i, v, s, c);
			}
		}
		ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			v[i] = false;
		}
		while (s.size() != 0) {
			int i = s.pop();
			if (!v[i]) {
				ans.add(new ArrayList<Integer>());
				kosajaru2(i, v, ans, rc);
			}
		}
		return ans;
	}

	private static void kosajaru1(int x, boolean[] v, Stack<Integer> s, ArrayList<ArrayList<Integer>> c) {
		if (v[x]) {
			return;
		}
		v[x] = true;
		for (int i = 0; i < c.get(x).size(); i++) {
			kosajaru1(c.get(x).get(i), v, s, c);
		}
		s.push(x);
	}

	private static void kosajaru2(int x, boolean[] v, ArrayList<ArrayList<Integer>> ans, ArrayList<ArrayList<Integer>> rc) {
		if (v[x]) {
			return;
		}
		v[x] = true;
		ans.get(ans.size() - 1).add(x);
		for (int i = 0; i < rc.get(x).size(); i++) {
			kosajaru2(rc.get(x).get(i), v, ans, rc);
		}
	}
	
	/**
	 * Topologically sorts the given graph, and returns true.
	 * 
	 * A graph is considered topologically sorted if for every pair of indices i, j, where i < j, the node
	 * at j is never an ancestor of the node at i. 
	 * 
	 * In the case that a topological sort is impossible, (in that case, the graph is not a DAG), it will return false. 
	 * @param <T>
	 * @param a
	 */
	public static <T> boolean topologicalSort(ArrayList<GraphNode<T>> a) {
		GraphUtils.buildGraph(a);
		
		//reverse all edges
		
		
		ArrayList<GraphNode<T>> sorted = new ArrayList<>();
		PriorityQueue<Pair<Integer, GraphNode<T>>> q = new PriorityQueue<>();
		while(q.size() != 0) {
			
		}
		
		return true;
	}

}
