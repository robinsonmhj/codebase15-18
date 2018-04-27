/**
 * 
 */
package com.barclaycard.us.util;


import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import com.barclaycard.us.model.Conveyor;
import com.barclaycard.us.model.Gate;
import com.barclaycard.us.model.Path;

/**
 * @author Haojie Ma Mar 24, 2018
 */


public class PathUtil {

	private  static Logger log = Logger.getLogger(PathUtil.class);
	private  static int gateCount;
	private  static Map<Conveyor, Float> edges;
	private  static Path[][] paths;// each row contains the path from rowId(source:gateId) to all the other gates
	
	private PathUtil(){
		
	}
	
	static {
		FindShortest();
		getAllPath();
	}
	
	
	private static class Singleton{
		public static final PathUtil INSTANCE= new PathUtil();
	}
	
	public static PathUtil getInstance(){
		
		return Singleton.INSTANCE;
	}
	
	
	//method to recalculate when the configuration is changed
	//in the real world, if conveyor is broken between gates or gates closed
	//the path needs to be recalculated
	public void reCalculate(){
		FindShortest();
		getAllPath();
	}
	
	/*
	 * this method is used to handle the case when there are multiple paths between the same 2 gates
	 * it only keep the shortest one, all the others will be ignored
	 * 
	 * for example, there are 3 path between node 1 and 2,
	 * 1--2, wight 100
	 * 1--2, weight 10
	 * 1--2, weight 9
	 * 
	 * Only 1--2 with weight 9 will be kept, as it is the shortest one
	 * 
	 * in the mean while, the following cases will be handled too
	 * 
	 *2--1 with weight 10 will be processed as 1--2 with weight 10 before adding into the edges
	 *so it is making sure that there is only 1 path between the same 2 nodes
	 * 	 
	 * 
	 * */
	
	private static  void FindShortest() {
		List<Gate> g=AuxiliaryUtil.getGates();
		Set<Gate> gates = new HashSet<Gate>(g);
		gateCount = gates.size();
		edges = new HashMap<Conveyor, Float>();
		List<Conveyor> conveyors = AuxiliaryUtil.getConveyors();
		Iterator<Conveyor> ite = conveyors.iterator();
		while (ite.hasNext()) {
			Conveyor c = ite.next();
			log.debug("c=" + c.toString());
			short from = c.getFrom();
			short to = c.getTo();
			float time = c.getTime();
			if (from > to) {
				short temp = from;
				from = to;
				to = temp;
			}
			Conveyor edge = new Conveyor(from, to, time);
			Float existTime = edges.get(edge);
			if (existTime == null) {
				edges.put(edge, time);
			} else if (existTime != null && existTime > time) {
				edges.remove(edge);
				edges.put(edge, time);
			}
		}

	}

	/*
	 * 
	 * 
	 * Dijkstra's algorithm, complexity is ELogV,
	 * V is the vertex count and E is the edge count
	 * 
	 */
	private static Path[] getShortestPath(int source) {

		Path[] res = new Path[gateCount];

		Queue<Path> q = new PriorityQueue<Path>(gateCount,
				new Comparator<Path>() {
					public int compare(Path p1, Path p2) {

						float t1 = p1.getTime();
						float t2 = p2.getTime();

						if (t1 < t2)
							return -1;
						else if (t1 == t2)
							return 0;
						else
							return 1;
					}

				});

		for (int i = 0; i < gateCount; i++) {
			Path p = new Path(source, i);
			if (i == source) {
				p.setTime(0);
				Queue<Integer> path = new LinkedList<Integer>();
				path.add(source);
				p.setPath(path);
			}
			q.offer(p);
			res[i] = p;
		}

		while (!q.isEmpty()) {
			Path shortest = q.poll();
			for (Conveyor e : edges.keySet()) {
				Path from = res[e.getFrom()];
				Path to = res[e.getTo()];
				
				if (from.getTime() != Float.MAX_VALUE
						&& from.getTime() + e.getTime() < to.getTime()) {
					to.setTime(from.getTime() + e.getTime());
					Queue<Integer> tPath = new LinkedList<Integer>(
							from.getPath());
					tPath.add(to.getTo());
					to.setPath(tPath);
					// resort the queue
					q.remove(to);
					q.offer(to);
				}
				//as the graph is bi-directional, reverse the two nodes and find path
				if (to.getTime() != Float.MAX_VALUE
						&& to.getTime() + e.getTime() < from.getTime()) {
					from.setTime(to.getTime() + e.getTime());
					Queue<Integer> tPath = new LinkedList<Integer>(to.getPath());
					tPath.add(from.getTo());
					from.setPath(tPath);
					// resort the queue
					q.remove(from);
					q.offer(from);
				}
			}

			res[shortest.getTo()] = shortest;

		}

		return res;
	}

	/*
	 * Calculate the the shortest path from all the vertex in the graph to the the other vertex
	 * 
	 * 
	 */
	private static void getAllPath() {
		paths = new Path[gateCount][gateCount];
		for (int sourceId = 0; sourceId < gateCount; sourceId++) {

			paths[sourceId] = getShortestPath(sourceId);

		}

	}
	
	public int getGateCount(){
		
		return gateCount;
	}
 
	public String getShortTime(int from, int to) {
		if (from < 0 || from >= gateCount || to < 0 || to >= gateCount)
			return "No such gate";
		Path p = paths[from][to];
		if (p.getTime() == Float.MAX_VALUE)
			return "Not Reachable";
		StringBuilder res = new StringBuilder();
		Queue<Integer> q = p.getPath();
		Iterator<Integer> ite = q.iterator();
		while (ite.hasNext())
			res.append(ite.next()).append("->");
		res.delete(res.length() - 2, res.length());
		res.append(",TotalTime:");
		res.append(p.getTime());
		return res.toString();

	}

}
