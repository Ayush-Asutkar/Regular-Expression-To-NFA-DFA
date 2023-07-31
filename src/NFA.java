import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// This will be the graph
public class NFA {
    private final int numberOfNodes;
    private final ArrayList<HashMap<Character, ArrayList<Integer>>> adjList;

    private int startNode;
    private int finalNode;

    public NFA(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        adjList = new ArrayList<>(this.numberOfNodes);

        for (int i=0; i<this.numberOfNodes; i++) {
            HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
            adjList.add(map);
        }

        this.startNode = -1;
        this.finalNode = -1;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getFinalNode() {
        return finalNode;
    }

    public List<HashMap<Character, ArrayList<Integer>>> getAdjList() {
        return Collections.unmodifiableList(adjList);
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public void setFinalNode(int finalNode) {
        this.finalNode = finalNode;
    }

    public void addEdge (int from, int to, char weight) {
        HashMap<Character, ArrayList<Integer>> map = this.adjList.get(from);
        if (map.containsKey(weight)) {
            map.get(weight).add(to);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(to);
            map.put(weight, list);
        }
    }

    public void addAllEdge (List<HashMap<Character, ArrayList<Integer>>> list) {
        this.addAllEdgeWithOffset(list, 0);
    }

    public void addAllEdgeWithOffset (List<HashMap<Character, ArrayList<Integer>>> list, int offset) {
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
        System.out.println("Final Node = " + this.finalNode);
        System.out.println("Following is the adjacency list:");
        for (int i=0; i<this.numberOfNodes; i++) {
            if (i == this.finalNode) {
                System.out.println("Node " + i + " is the final node");
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
        nfa.setFinalNode(0);
        nfa.setFinalNode(4);
        nfa.printAdjList();
    }
}
