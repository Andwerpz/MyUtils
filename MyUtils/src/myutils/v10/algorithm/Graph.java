package myutils.v10.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph<T> {
	
	private HashSet<GraphNode<T>> nodes;
	
	private HashMap<T, GraphNode<T>> objToNode;
	
	private HashMap<GraphNode<T>, ArrayList<GraphNode<T>>> adjList;
	private HashMap<GraphNode<T>, ArrayList<GraphNode<T>>> revAdjList;
	
	public Graph() {
		this.nodes = new HashSet<>();
		
		this.objToNode = new HashMap<>();
		
		this.adjList = new HashMap<>();
		this.revAdjList = new HashMap<>();
	}
	
	public boolean doesNodeExist(T a) {
		return this.objToNode.containsKey(a);
	}
	
	public void addNode(T a) {
		if(this.doesNodeExist(a)) {
			return;
		}
		
		GraphNode<T> n = new GraphNode<T>(a);
		this.nodes.add(n);
		this.objToNode.put(a, n);
		
		this.adjList.put(n, new ArrayList<>());
		this.revAdjList.put(n, new ArrayList<>());
	}
	
	public void addEdge(T a, T b) {
		if(!this.doesNodeExist(a)) {
			this.addNode(a);
		}
		if(!this.doesNodeExist(b)) {
			this.addNode(b);
		}
		
		this.adjList.
	}
	
}
