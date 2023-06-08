package myutils.v10.algorithm;

import java.util.ArrayList;

public class GraphNode<T> implements Comparable<GraphNode<T>> {
	
	public T obj;
	public ArrayList<T> edges;
	
	protected ArrayList<GraphNode<T>> nodeEdges;
	 
	public GraphNode(T obj) {
		this.obj = obj;
		this.edges = new ArrayList<>();
		this.nodeEdges = new ArrayList<>();
	}

	@Override
	public int compareTo(GraphNode<T> o) {
		//maybe TODO
		return 0;
	}
	
}
