package conversionhelper;

import model.NFA;
import characterhelper.SpecialCharacters;
import java.util.*;

public class EpsilonNFAToNFAConversionHelper {
    private static List<Set<Integer>> findEpsilonClosureOfNodes(NFA epsilonNFA) {
        int numberOfNodes = epsilonNFA.getNumberOfNodes();
        List<Set<Integer>> result = new ArrayList<>(numberOfNodes);

        for (int i=0; i<numberOfNodes; i++) {
            //find epsilon closure for each node
            Set<Integer> closure = findEpsilonClosureOfEachNode(epsilonNFA, i);
            result.add(closure);
        }
        return result;
    }

    private static Set<Integer> findEpsilonClosureOfEachNode(NFA epsilonNFA, int node) {
        //do a bfs and find the connected edges with Epsilon weight
        Set<Integer> result = new HashSet<>();
        result.add(node);

        Queue<Integer> q = new ArrayDeque<>();
        q.add(node);

        Map<Integer, Boolean> traversed = new HashMap<>();
        traversed.put(node, true);

        while (!q.isEmpty()) {
            int curr = q.poll();
            Set<Integer> list = epsilonNFA.getDestinationNodesWithGivenWeight(curr, SpecialCharacters.Epsilon);
            if (list == null) {
                continue;
            }
            for (Integer elem: list) {
                if (!traversed.containsKey(elem)) {
                    // element is not yet traversed
                    result.add(elem);
                    traversed.put(elem, true);
                    q.add(elem);
                }
            }
        }

        return result;
    }

    private static Set<Integer> evaluateForSingleWeight(NFA epsilonNFA, Set<Integer> epsilonClosure, List<Set<Integer>> epsilonClosureOfAllNodes, char weight) {
        Set<Integer> newSetForEnding = new HashSet<>();

        for (Integer elem: epsilonClosure) {
            Set<Integer> prevSet = epsilonNFA.getDestinationNodesWithGivenWeight(elem, weight);
            if (prevSet == null) {
                continue;
            }

            for (Integer inPrevSet: prevSet) {
                Set<Integer> epsilonClosuresOfAfterDirection = epsilonClosureOfAllNodes.get(inPrevSet);
                newSetForEnding.addAll(epsilonClosuresOfAfterDirection);
            }
        }

        return newSetForEnding;
    }

    private static Set<Integer> findAllFinalNodes(List<Set<Integer>> epsilonClosureOfAllNodes, Set<Integer> finalOfOriginal) {
        Set<Integer> result = new HashSet<>();

        for(int i=0; i<epsilonClosureOfAllNodes.size(); i++) {
            int finalI = i;
            epsilonClosureOfAllNodes.get(i).forEach((node) -> {
                if (finalOfOriginal.contains(node)) {
                    result.add(finalI);
                }
            });
        }

        return result;
    }

    public static NFA epsilonNFAtoNFA (NFA epsilonNFA) {
        //find the closure for all the nodes
        List<Set<Integer>> epsilonClosureOfAllNodes = findEpsilonClosureOfNodes(epsilonNFA);
//        epsilonClosureOfAllNodes.forEach(System.out::println);

        NFA resultNFA = new NFA(epsilonNFA.getNumberOfNodes());
        resultNFA.setStartNode(epsilonNFA.getStartNode());

        //find the final states
        resultNFA.addAllFinalNodes(findAllFinalNodes(epsilonClosureOfAllNodes, epsilonNFA.getFinalNodes()));

        for (int i=0; i<resultNFA.getNumberOfNodes(); i++) {
            int from = i;
            Set<Integer> epsilonClosure = epsilonClosureOfAllNodes.get(from);

            // consider all the lowercase letters
            for (char ch = 'a'; ch <= 'z'; ch++) {
                Set<Integer> newSetForEnding = evaluateForSingleWeight(epsilonNFA, epsilonClosure, epsilonClosureOfAllNodes, ch);

                resultNFA.addEdgeGivenFromWeightAndToSet(from, ch, newSetForEnding);
            }

            // consider all the uppercase letters
            for (char ch = 'A'; ch <= 'Z'; ch++) {
                Set<Integer> newSetForEnding = evaluateForSingleWeight(epsilonNFA, epsilonClosure, epsilonClosureOfAllNodes, ch);

                resultNFA.addEdgeGivenFromWeightAndToSet(from, ch, newSetForEnding);
            }

        }

        return resultNFA;
    }

    //for testing
    public static void main(String[] args) {
//        model.NFA epsilonNFA = new model.NFA(4);
//        epsilonNFA.addEdge(0, 0, 'a');
//        epsilonNFA.addEdge(0, 0, 'b');
//        epsilonNFA.addEdge(0, 1, 'b');
//        epsilonNFA.addEdge(1, 2, 'a');
//        epsilonNFA.addEdge(1, 2, characterhelper.SpecialCharacters.Epsilon);
//        epsilonNFA.addEdge(2, 3, 'b');
//        epsilonNFA.addEdge(3, 3, 'a');
//        epsilonNFA.addEdge(3, 3, 'b');
//        epsilonNFA.setStartNode(0);
//        epsilonNFA.addFinalNode(3);
//        epsilonNFA.printAdjList();

//        model.NFA epsilonNFA = new model.NFA(4);
//        epsilonNFA.addEdge(0, 0, 'a');
//        epsilonNFA.addEdge(0, 1, characterhelper.SpecialCharacters.Epsilon);
//        epsilonNFA.addEdge(1, 2, 'a');
//        epsilonNFA.addEdge(1, 3, characterhelper.SpecialCharacters.Epsilon);
//        epsilonNFA.addEdge(2, 1, 'b');
//        epsilonNFA.addEdge(3, 3, 'a');
//        epsilonNFA.setStartNode(0);
//        epsilonNFA.addFinalNode(3);
//        epsilonNFA.printAdjList();

        NFA epsilonNFA = new NFA(3);
        epsilonNFA.addEdge(0, 0, 'a');
        epsilonNFA.addEdge(0, 1, SpecialCharacters.Epsilon);
        epsilonNFA.addEdge(1, 1, 'b');
        epsilonNFA.addEdge(1, 2, SpecialCharacters.Epsilon);
        epsilonNFA.addEdge(2, 2, 'c');
        epsilonNFA.setStartNode(0);
        epsilonNFA.addFinalNode(2);
        epsilonNFA.printAdjList();

        epsilonNFAtoNFA(epsilonNFA);
    }
}
