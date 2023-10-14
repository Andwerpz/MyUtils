package myutils.algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

import myutils.misc.BiMap;

public class Graph<T> {
	//does not support double edges. 

	//self edges work though. 

	private HashSet<T> nodes;

	private HashMap<T, HashSet<T>> adjList;
	private HashMap<T, HashSet<T>> revAdjList;

	public Graph() {
		this.nodes = new HashSet<>();

		this.adjList = new HashMap<>();
		this.revAdjList = new HashMap<>();
	}

	public boolean doesNodeExist(T a) {
		return this.nodes.contains(a);
	}

	public void addNode(T a) {
		if (this.doesNodeExist(a)) {
			return;
		}

		this.nodes.add(a);

		this.adjList.put(a, new HashSet<>());
		this.revAdjList.put(a, new HashSet<>());
	}

	public void removeNode(T a) {
		if (!this.doesNodeExist(a)) {
			return;
		}

		this.nodes.remove(a);

		for (T node : this.adjList.get(a)) {
			this.revAdjList.get(node).remove(a);
		}
		for (T node : this.revAdjList.get(a)) {
			this.adjList.get(node).remove(a);
		}
		this.adjList.remove(a);
		this.revAdjList.remove(a);
	}

	public boolean doesEdgeExist(T a, T b) {
		if (!this.doesNodeExist(a) || !this.doesNodeExist(b)) {
			return false;
		}
		return this.adjList.get(a).contains(b);
	}

	public void addEdge(T a, T b) {
		if (!this.doesNodeExist(a)) {
			this.addNode(a);
		}
		if (!this.doesNodeExist(b)) {
			this.addNode(b);
		}
		if (this.doesEdgeExist(a, b)) {
			return;
		}

		this.adjList.get(a).add(b);
		this.revAdjList.get(b).add(a);
	}

	public void removeEdge(T a, T b) {
		if (!this.doesEdgeExist(a, b)) {
			return;
		}

		this.adjList.get(a).remove(b);
		this.revAdjList.get(b).remove(a);
	}

	/**
	 * Topologically sorts the given graph, and returns the list. 
	 * 
	 * A graph is considered topologically sorted if for every pair of indices i, j, where i < j, the node
	 * at j is never an ancestor of the node at i. 
	 * 
	 * In the case that a topological sort is impossible, (in that case, the graph is not a DAG), it will return null.  
	 * @return
	 */
	public ArrayList<T> topologicalSort() {
		Queue<T> q = new ArrayDeque<>();
		HashMap<T, Integer> nrIn = new HashMap<>();

		//find root nodes
		for (T node : this.nodes) {
			nrIn.put(node, this.revAdjList.get(node).size());
			if (nrIn.get(node) == 0) {
				q.add(node);
			}
		}

		//go do sort
		ArrayList<T> ans = new ArrayList<>();
		while (q.size() != 0) {
			T cur = q.poll();
			ans.add(cur);
			for (T next : this.adjList.get(cur)) {
				nrIn.put(next, nrIn.get(next) - 1);
				if (nrIn.get(next) == 0) {
					q.add(next);
				}
			}
		}

		if (ans.size() != this.nodes.size()) {
			//there is no topological sort, since the graph contains a cycle. 
			return null;
		}
		return ans;
	}

	/**
	 * Returns a list of strongly connected components in this graph. 
	 * @return
	 */
	public ArrayList<ArrayList<T>> stronglyConnectedComponents() {
		//assign an index to each node
		BiMap<T, Integer> nodeToIndex = new BiMap<>();
		for (T node : this.nodes) {
			nodeToIndex.put(node, nodeToIndex.size());
		}

		//create adjacency list
		ArrayList<ArrayList<Integer>> c = new ArrayList<>();
		for (int i = 0; i < this.nodes.size(); i++) {
			c.add(new ArrayList<Integer>());
		}
		for (T node : this.nodes) {
			int curInd = nodeToIndex.getValue(node);
			for (T adj : this.adjList.get(node)) {
				int nextInd = nodeToIndex.getValue(adj);
				c.get(curInd).add(nextInd);
			}
		}

		//get scc
		ArrayList<ArrayList<Integer>> scc = GraphUtils.kosajaru(c);

		//translate to nodes
		ArrayList<ArrayList<T>> ans = new ArrayList<>();
		for (ArrayList<Integer> i : scc) {
			ArrayList<T> next = new ArrayList<>();
			for (int j : i) {
				next.add(nodeToIndex.getKey(j));
			}
			ans.add(next);
		}

		return ans;
	}

}
