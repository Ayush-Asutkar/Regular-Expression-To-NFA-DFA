import java.util.*;

// This will be the graph
public class NFA {
    private final int numberOfNodes;
    private final List<Map<Character, Set<Integer>>> adjList;

    private int startNode;
    private Set<Integer> finalNodes;

    public NFA(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        adjList = new ArrayList<>(this.numberOfNodes);

        for (int i=0; i<this.numberOfNodes; i++) {
            Map<Character, Set<Integer>> map = new HashMap<>();
            adjList.add(map);
        }

        this.startNode = -1;
        this.finalNodes = new HashSet<>();
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getStartNode() {
        return startNode;
    }

    public Set<Integer> getFinalNodes() {
        return finalNodes;
    }

    public List<Map<Character, Set<Integer>>> getAdjList() {
        return Collections.unmodifiableList(adjList);
    }

    public Set<Integer> getDestinationNodesWithGivenWeight(int from, char weight) {
        return this.adjList.get(from).get(weight);
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public void addFinalNode(int finalNode) {
        this.finalNodes.add(finalNode);
    }

    public void addEdge (int from, int to, char weight) {
        Map<Character, Set<Integer>> map = this.adjList.get(from);
        if (map.containsKey(weight)) {
            map.get(weight).add(to);
        } else {
            HashSet<Integer> list = new HashSet<>();
            list.add(to);
            map.put(weight, list);
        }
    }

    public void addAllEdge (List<Map<Character, Set<Integer>>> list) {
        this.addAllEdgeWithOffset(list, 0);
    }

    public void addEdgeGivenFromWeightAndToSet(int from, char weight, Set<Integer> toSet) {
        for (Integer to: toSet) {
            this.addEdge(from, to, weight);
        }
    }

    public void addAllEdgeWithOffset (List<Map<Character, Set<Integer>>> list, int offset) {
        for (int i=0; i<list.size(); i++) {
            int finalI = i;
            list.get(i).forEach((key, value) -> {
                int from = finalI;
                char weight = key;
                value.forEach((element) -> {
                    int to = element;
                    this.addEdge(from + offset, to + offset, weight);
                });
            });
        }
    }

    public void printAdjList () {
        System.out.println("Number of Nodes = " + this.numberOfNodes + " (0-based indexing)");
        System.out.println("Start Node = " + this.startNode);
        System.out.println("Final Node = " + this.finalNodes);
        System.out.println("Following is the adjacency list:");
        for (int i=0; i<this.numberOfNodes; i++) {
            if (this.finalNodes.contains(i)) {
                if (this.adjList.get(i).isEmpty()) {
                    System.out.println("Node " + i + " is the final node and does not make any edge");
                } else {
                    System.out.println("Node "  + i + " is the final node and makes an edge with ");
                    this.adjList.get(i).forEach((key, value) ->
                            System.out.println("\tEdge weight '" + key + "' with => " + value.toString()));
                }
                continue;
            }

            if (this.adjList.get(i).isEmpty()) {
                System.out.println("Node " + i + " does not make any edge");
            }
            System.out.println("Node "  + i + " makes an edge with ");
            this.adjList.get(i).forEach((key, value) ->
                    System.out.println("\tEdge weight '" + key + "' with => " + value.toString()));
        }
        System.out.println();
    }

    //for testing
    public static void main(String[] args) {
        NFA nfa = new NFA(5);
        nfa.addEdge(0, 1, 'a');
        nfa.addEdge(0, 4, 'b');
        nfa.addEdge(1, 2, 'c');
        nfa.addEdge(1, 3, 'd');
        nfa.addEdge(1, 4, 'e');
        nfa.addEdge(2, 3, 'f');
        nfa.addEdge(3, 4, 'g');
        nfa.addFinalNode(0);
        nfa.addFinalNode(4);
        nfa.printAdjList();
    }

}
