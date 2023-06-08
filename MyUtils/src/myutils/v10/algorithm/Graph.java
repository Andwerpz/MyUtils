package myutils.v10.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph<T> {
	
	private HashSet<GraphNode<T>> nodes;
	
	private HashMap<T, GraphNode<T>> objToNode;
	
	private HashMap<GraphNode<T>, HashSet<GraphNode<T>>> adjList;
	private HashMap<GraphNode<T>, HashSet<GraphNode<T>>> revAdjList;
	
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
		
		this.adjList.put(n, new HashSet<>());
		this.revAdjList.put(n, new HashSet<>());
	}
	
	private GraphNode<T> getNodeFromObj(T a) {
		if(!this.doesNodeExist(a)) {
			return null;
		}
		return this.objToNode.get(a);
	}
	
	public boolean doesEdgeExist(T a, T b) {
		if(!this.doesNodeExist(a) || !this.doesNodeExist(b)) {
			return false;
		}
		return this.adjList.get(a).contains(b);
	}
	
	public void addEdge(T a, T b) {
		if(this.doesEdgeExist(a, b)) {
			return;
		}
		
		GraphNode<T> u = this.getNodeFromObj(a);
		GraphNode<T> v = this.getNodeFromObj(b);
		
		this.adjList.get(u).add(v);
		this.revAdjList.get(v).add(u);
	}
	
}
