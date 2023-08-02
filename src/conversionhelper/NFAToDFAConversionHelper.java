package conversionhelper;

import model.DFA;
import model.NFA;

import java.util.*;

public class NFAToDFAConversionHelper {

    public static DFA NFAToDFA (NFA nfa) {
        Map<Set<Integer>, Map<Character, Set<Integer>>> tempAdjList = new HashMap<>();

        //will store the new generated state
        Queue<Set<Integer>> q = new ArrayDeque<>();

        //add the first state
        int firstState = nfa.getStartNode();
        Set<Integer> firstStateSet = new HashSet<>();
        firstStateSet.add(firstState);

        q.add(firstStateSet);

        Set<Set<Integer>> alreadyCovered = new HashSet<>();

        while (!q.isEmpty()) {
            Set<Integer> fromSet = q.poll();
//            System.out.println("This is the fromSet: " + fromSet);
            alreadyCovered.add(fromSet);

            for (char ch='a'; ch<='z'; ch++) {
                Set<Integer> toSet = new HashSet<>();
                for (Integer fromElem: fromSet) {
                    Set<Integer> currFinalSetUsingEdge = nfa.getDestinationNodesWithGivenWeight(fromElem, ch);
                    if (currFinalSetUsingEdge != null) {
                        toSet.addAll(currFinalSetUsingEdge);
                    }
                }

                if (toSet.isEmpty()) {
                    continue;
                }
                System.out.println("Char: " + ch + " ToSet: " + toSet);

                if(!alreadyCovered.contains(toSet)) {
                    q.add(toSet);
                    alreadyCovered.add(toSet);
                }

                if (tempAdjList.containsKey(fromSet)) {
                    tempAdjList.get(fromSet).put(ch, toSet);
                } else {
                    Map<Character, Set<Integer>> currMappingOfEdges = new HashMap<>();
                    currMappingOfEdges.put(ch, toSet);
                    tempAdjList.put(fromSet, currMappingOfEdges);
                }
            }

            for (char ch='A'; ch<='Z'; ch++) {
                Set<Integer> toSet = new HashSet<>();
                for (Integer fromElem: fromSet) {
                    Set<Integer> currFinalSetUsingEdge = nfa.getDestinationNodesWithGivenWeight(fromElem, ch);
                    if (currFinalSetUsingEdge != null) {
                        toSet.addAll(currFinalSetUsingEdge);
                    }
                }

                if (toSet.isEmpty()) {
                    continue;
                }
                System.out.println("Char: " + ch + " ToSet: " + toSet);

                if(!alreadyCovered.contains(toSet)) {
                    q.add(toSet);
                    alreadyCovered.add(toSet);
                }

                if (tempAdjList.containsKey(fromSet)) {
                    tempAdjList.get(fromSet).put(ch, toSet);
                } else {
                    Map<Character, Set<Integer>> currMappingOfEdges = new HashMap<>();
                    currMappingOfEdges.put(ch, toSet);
                    tempAdjList.put(fromSet, currMappingOfEdges);
                }
            }
        }
//        System.out.println(tempAdjList);

        return convertToDFAFromTemp(tempAdjList, nfa);
    }

    private static DFA convertToDFAFromTemp(Map<Set<Integer>, Map<Character, Set<Integer>>> tempAdjList, NFA nfa) {
        Map<Set<Integer>, Integer> mappingToIndex = new HashMap<>();

        int numberOfNodes = tempAdjList.size();
        DFA resultDFA = new DFA(numberOfNodes);

        int ind = 0;

        for (Map.Entry<Set<Integer>, Map<Character, Set<Integer>>> mapElem: tempAdjList.entrySet()) {
            Set<Integer> key = mapElem.getKey();
            mappingToIndex.put(key, ind++);
        }

        for (Map.Entry<Set<Integer>, Map<Character, Set<Integer>>> mapElem: tempAdjList.entrySet()) {
            Set<Integer> key = mapElem.getKey();
            Map<Character, Set<Integer>> value = mapElem.getValue();

            int from = mappingToIndex.get(key);
            for (Map.Entry<Character, Set<Integer>> edgeElem: value.entrySet()) {
                char weight = edgeElem.getKey();
                Set<Integer> toSet = edgeElem.getValue();
                int to = mappingToIndex.get(toSet);

                resultDFA.addEdge(from, to, weight);
            }
        }
        resultDFA.setStartNode(nfa.getStartNode());

        Set<Integer> finalNodes = new HashSet<>();

        //evaluate final nodes
        for (Map.Entry<Set<Integer>, Integer> mapElem: mappingToIndex.entrySet()) {
            Set<Integer> key = mapElem.getKey();
            int value = mapElem.getValue();

            for (Integer keyElem: key) {
                if (resultDFA.getFinalNodes().contains(keyElem)) {
                    finalNodes.add(value);
                }
            }
        }
        resultDFA.addAllFinalNodes(finalNodes);

        return resultDFA;
    }

    // for testing
    public static void main(String[] args) {
        NFA nfa = new NFA(4);
        nfa.setStartNode(0);
        nfa.addFinalNode(3);
        nfa.addEdge(0,0, 'a');
        nfa.addEdge(0, 0, 'b');
        nfa.addEdge(0, 1, 'b');
        nfa.addEdge(0, 2, 'b');

        nfa.addEdge(1, 2, 'a');
        nfa.addEdge(1, 3, 'b');

        nfa.addEdge(2, 3, 'b');

        nfa.addEdge(3, 3, 'a');
        nfa.addEdge(3, 3, 'b');

        nfa.printAdjList();
        DFA result = NFAToDFA(nfa);
        result.printAdjList();
    }
}
