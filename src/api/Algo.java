package api;
import java.util.*;

public class Algo
{
    private Graph graph;

    public Algo(String jsonString)
    {
        Graph g = new Graph(jsonString);
        init(g);
    }

    /**
     * This function init the algo with a new graph
     */
    public void init(Graph g) {graph = g;}

    /**
     * return the graph
     * @return the graph
     */
    public Graph getGraph()
    {
        return graph;
    }

    /**
     * This function return the total weight of the shortest path dist
     * @param src - start node
     * @param dest - end (target) node
     * @return the weight of the best path
     */
    public double shortestPathDist(int src, int dest)
    {// final complexity is: o(ElogV)
        if(src == dest)
            return 0.0;
        HashMap<Integer, ArrayList<AdjListNode>> ew = new HashMap<>(); //sore for every node (key is node id) the list of adjust nodes
        //in the form of AdjlistNode object (look up in the static class for explanation).
        HashMap<Integer, Double> dist = new HashMap<>(); //store distance from the src to every other node.
        Iterator<Node> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
        {
            Node node = nodeIter.next();
            dist.put(node.getKey(), Double.MAX_VALUE);
            ew.put(node.getKey(), new ArrayList<>());
        }
        dist.put(src, 0.0);

        Iterator<Edge> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            Edge edge = edgeIter.next();
            ew.get(edge.getSrc()).add(new AdjListNode(edge.getDest(), edge.getWeight()));
        }

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AdjListNode::getWeight));
        pq.add(new AdjListNode(src, 0.0));

        while (pq.size() > 0)
        {
            AdjListNode current = pq.poll();
            for (AdjListNode n : ew.get(current.getVertex()))
            {
                if (dist.get(current.vertex) + n.weight < dist.get(n.vertex))
                {
                    dist.put(n.vertex, n.weight + dist.get(current.vertex));
                    pq.add(new AdjListNode(n.getVertex(), dist.get(n.vertex)));
                }
            }
        }
        return dist.get(dest);
    }

    /**
     * This function return linked list contain the nodes we should go through
     * to arrive with the shortest path between to nodes
     * @param src - start node
     * @param dest - end (target) node
     * @return Node list of the best path
     */
    public LinkedList<Node> shortestPath(int src, int dest)
    {// final complexity is: o(ElogV)
        HashMap<Integer, ArrayList<AdjListNode>> ew = new HashMap<>();
        HashMap<Integer, Double> dist = new HashMap<>();
        Iterator<Node> nodeIter = graph.nodeIter();
        HashMap<Integer, Integer> preNode = new HashMap<>();
        while (nodeIter.hasNext())
        {
            Node node = nodeIter.next();
            dist.put(node.getKey(), Double.MAX_VALUE);
            ew.put(node.getKey(), new ArrayList<>());
        }
        dist.put(src, 0.0);

        Iterator<Edge> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            Edge edge = edgeIter.next();
            ew.get(edge.getSrc()).add(new AdjListNode(edge.getDest(), edge.getWeight()));
        }

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AdjListNode::getWeight));
        pq.add(new AdjListNode(src, 0.0));

        while (pq.size() > 0)
        {
            AdjListNode current = pq.poll();
            for (AdjListNode n : ew.get(current.getVertex()))
                if (dist.get(current.vertex) + n.weight < dist.get(n.vertex))
                {
                    dist.put(n.vertex, n.weight + dist.get(current.vertex));
                    pq.add(new AdjListNode(n.getVertex(), dist.get(n.vertex)));
                    preNode.put(n.vertex, current.vertex);
                    if (n.vertex == dest)
                        return checkPath(preNode, src, dest);
                }
        }
        return null;
    }

    private LinkedList<Node> checkPath(HashMap<Integer, Integer> preNode, int src, int dest)
    {//the value of each cell in the hash map is the node id we need to reach before going to the node id represented
        // by its key (therefore we start from the dest id).
        LinkedList<Node> list = new LinkedList<>();
        while (dest != src)
        {
            list.addFirst(graph.getNode(dest));
            dest = preNode.get(dest);
        }
        list.addFirst(graph.getNode(src));
        return list;
    }


    /**
     * This function return a node that from him to the farthest node in graph
     * it will be the shortest from all the nodes
     * @return the chosen Node
     */
    public Node center()
    {
        HashMap<Integer, ArrayList<AdjListNode>> ew = new HashMap<>(); //save all the edges weights

        Iterator<Node> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
            ew.put(nodeIter.next().getKey(), new ArrayList<>());

        Iterator<Edge> edgeIter = graph.edgeIter();
        while (edgeIter.hasNext())
        {
            Edge edge = edgeIter.next();
            ew.get(edge.getSrc()).add(new AdjListNode(edge.getDest(), edge.getWeight()));
        }

        int id=0; //represent the center node id.
        double lowestWeight= Double.MAX_VALUE;
        Iterator<Node> nodesIter = graph.nodeIter();
        while (nodesIter.hasNext())
        { //go over all the nodes and find the lowest value.
            Node node = nodesIter.next();
            double newWeight = dijkstra(node.getKey(), ew);
            if(newWeight == -1)
                return null;
            if (lowestWeight > newWeight)
            {
                id = node.getKey();
                lowestWeight = newWeight;
            }
        }
        return graph.getNode(id);
    }

    private double dijkstra(int src, HashMap<Integer, ArrayList<AdjListNode>> ew)
    {
        // the function is for the center algorithm. its build like the previous versions but here we
        // will return the highest result instead of result for a given destination value.

        HashMap<Integer, Double> dist = new HashMap<>();
        Iterator<Node> nodeIter = graph.nodeIter();
        while (nodeIter.hasNext())
        {
            Node node = nodeIter.next();
            dist.put(node.getKey(), Double.MAX_VALUE);
        }
        dist.put(src, 0.0);

        PriorityQueue<AdjListNode> pq = new PriorityQueue<>(Comparator.comparingDouble(AdjListNode::getWeight));
        pq.add(new AdjListNode(src, 0.0));

        while (pq.size() > 0)
        {
            AdjListNode current = pq.poll();
            for (AdjListNode n : ew.get(current.getVertex()))
            {
                if (dist.get(current.vertex) + n.weight < dist.get(n.vertex))
                {
                    dist.put(n.vertex, n.weight + dist.get(current.vertex));
                    pq.add(new AdjListNode(n.getVertex(), dist.get(n.vertex)));
                }
            }
        }
        double max = -1;
        for (Double weight: dist.values()) //check for the max distance value.
        {
            if(weight > max)
                max = weight;
            if(weight == Double.MAX_VALUE) //represent that the graph is not connected. so we will return -1 because
                // all nodes id are positive integers.
                return -1;
        }
        return max;
    }
}
