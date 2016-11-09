import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
	public static class Node implements Comparable<Node>{
		private Collection<Node> edges = new TreeSet<Node>();
		private final String id;
		private Map<String, Object> properties = new TreeMap<String, Object>();
		public Node(String id) {
			super();
			this.id = id;
		}
		public void connectTo(Node second) {
			edges.add(second);
			second.edges.add(this);
		}
		
		public void disconnectFrom(Node second) {
			edges.remove(second);
			second.edges.remove(this);
		}
		@Override
		public int hashCode() {
			return id.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
		public void setProperty(String string, boolean b) {
			properties.put(string, b);
		}
		
		public boolean hasProperty(String string) {
			return properties.containsKey(string);
		}
		
		@Override public String toString() {
			StringBuilder propertiesText = new StringBuilder();
			for(Map.Entry<String, Object> entry : properties.entrySet()) {
				propertiesText.append(String.format("[%s=%s]", entry.getKey(), entry.getValue()));
			}
		    return String.format("Node [id=%s%s]", id, propertiesText);
		}
		public Collection<Deque<Node>> findPatshToExit() {
			return findPathsToExit(new int[] {Integer.MAX_VALUE}, new ArrayDeque<Node>());
		}
		private Collection<Deque<Node>> findPathsToExit(int[] depth,Deque<Node> parentPath) {
			Deque<Node> path = new ArrayDeque<>(parentPath);
			path.add(this);
			Collection<Deque<Node>> paths = new ArrayList<Deque<Node>>();
			if(hasProperty("exit")) {
				depth[0] = Math.min(depth[0], path.size());
				paths.add(path);
			} else {
				if(path.size()<depth[0]) {
					for(Node n : edges) {
						if(!parentPath.contains(n)) {
							paths.addAll(n.findPathsToExit(depth, path));
						}
					}
				}
			}
			return paths;
		}
		@Override
		public int compareTo(Node arg0) {
			int returned = 0;
			returned = (int) Math.signum(properties.size()-arg0.properties.size());
			if(returned==0) {
				returned = id.compareTo(arg0.id);
			}
			return returned;
		}
	}
	
	public static class Graph {
		Collection<Node> nodes = new LinkedList<Node>();
		
		public void connect(String firstNodeId, String secondNodeId) {
			Node first = findOrCreate(firstNodeId);
			Node second = findOrCreate(secondNodeId);
			first.connectTo(second);
		}

		private Node findOrCreate(String firstNodeId) {
			Node found = find(firstNodeId);
			if(found==null) {
				return create(firstNodeId);
			} else {
				return found;
			}
		}

		private Node create(String firstNodeId) {
			Node created = new Node(firstNodeId);
			nodes.add(created);
			return created;
		}

		public Node find(String nodeId) {
			for(Node n : nodes) {
				if(n.id.equals(nodeId)) {
					return n;
				}
			}
			return null;
		}
	}

    public static void main(String args[]) {
    	Graph g = new Graph();
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            g.connect(Integer.toString(N1), Integer.toString(N2));
        }
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            g.find(Integer.toString(EI)).setProperty("exit", true);
            System.err.println(g.find(Integer.toString(EI))+" is an exit node");
        }

        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            Node toUnplug = g.find(Integer.toString(SI));
            // Always cut the shortest branch exiting from node
            Collection<Deque<Node>> paths = toUnplug.findPatshToExit();
            System.err.println("Found paths\n"+paths);
            Deque<Node> firstPath = null;
            
            for (Deque<Node> deque : paths) {
				if(firstPath==null) {
					firstPath = deque;
				} else {
					if(deque.size()<firstPath.size()) {
						firstPath = deque;
					}
				}
			}

            System.err.println("Best path is\n"+firstPath);

            Node last = firstPath.removeLast();
            Node previous = firstPath.removeLast();
            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            System.out.println(String.format("%s %s", last.id, previous.id));
        }
    }
}