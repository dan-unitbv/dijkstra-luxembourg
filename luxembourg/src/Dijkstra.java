import java.util.*;
class Node
{
    int vertex, cost;
    public Node(int vertex, int cost)
    {
        this.vertex = vertex;
        this.cost = cost;
    }
}
class Dijkstra
{
    private static int sourceId;
    private static int destinationId;
    private static int nrOfNodes;
    private static List<Integer> path = new ArrayList<>();
    private static List<List<Arc>> adjacencyList;
    public Dijkstra(int sourceId, int destinationId, int nrOfNodes, List<List<Arc>> adjacencyList)
    {
        this.sourceId = sourceId;
        this.destinationId=destinationId;
        this.nrOfNodes = nrOfNodes;
        this.adjacencyList = adjacencyList;
    }
    public static void findShortestPath()
    {
        PriorityQueue<Node> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        minHeap.add(new Node(sourceId, 0));

        List<Integer> distance;
        distance = new ArrayList<>(Collections.nCopies(nrOfNodes, Integer.MAX_VALUE));
        distance.set(sourceId, 0);

        boolean[] done = new boolean[nrOfNodes];
        done[sourceId] = true;

        int[] previous = new int[nrOfNodes];
        previous[sourceId] = -1;

        while(!minHeap.isEmpty())
        {
            Node node = minHeap.poll();
            int node1 = node.vertex;
            for (Arc arc : adjacencyList.get(node1))
            {
                int node2 = arc.getDestinationId();
                int cost = arc.getCost();
                if (!done[node2] && (distance.get(node1) + cost) < distance.get(node2))
                {
                    distance.set(node2, distance.get(node1) + cost);
                    previous[node2] = node1;
                    minHeap.add(new Node(node2, distance.get(node2)));
                }
            }
            done[node1] = true;
        }
        while(destinationId >= 0)
        {
            path.add(destinationId);
            destinationId = previous[destinationId];
        }
    }
    public static List<Integer> getPath()
    {
        return path;
    }
}