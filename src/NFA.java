import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// This will be the graph
public class NFA {
    private int numberOfNodes;
    private ArrayList<HashMap<Integer, Character>> adjList;

    private int startNode;
    private int finalNode;

    public NFA (int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        adjList = new ArrayList<>(this.numberOfNodes);

        for (int i=0; i<this.numberOfNodes; i++) {
            HashMap<Integer, Character> map = new HashMap<>();
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

    public List<HashMap<Integer, Character>> getAdjList() {
        return Collections.unmodifiableList(adjList);
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public void setFinalNode(int finalNode) {
        this.finalNode = finalNode;
    }

    public void addEdge (int from, int to, char weight) {
        adjList.get(from).put(to, weight);
    }

    public void addAllEdge (List<HashMap<Integer, Character>> list) {
        this.addAllEdgeWithOffset(list, 0);
    }

    public void addAllEdgeWithOffset (List<HashMap<Integer, Character>> list, int offset) {
        for (int i=0; i<list.size(); i++) {
            int finalI = i;
            list.get(i).forEach((key, value) -> {
                int from = finalI;
                int to = key;
                char weight = value;

                this.addEdge(from + offset, to + offset, value);
            });
        }
    }

    public void printAdjList () {
        System.out.println("Start Node = " + this.startNode);
        System.out.println("Final Node = " + this.finalNode);
        System.out.println("Following is the adjacency list:");
        for (int i=0; i<this.numberOfNodes; i++) {
            System.out.println("Node "  + i + " makes an edge with ");
            this.adjList.get(i).forEach((key, value) -> System.out.println("\tNode " + key + " with edge weight " + value));
//            for (Map.Entry<Integer, Character> mapElement: this.adjList.get(i).entrySet()) {
//                mapElement
//            }
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
        nfa.printAdjList();
    }
}
