package conversionhelper;

import model.NFA;

import java.util.*;

public class NFAToDFAConversionHelper {
    public static void NFAToDFA (NFA nfa) {
        Map<Set<Integer>, Integer> map = new HashMap<>();
        //this map will store the set of integer -> integer index
        int index = 0;

        Map<Integer, Map<Character, Integer>> tempAdjList = new HashMap<>();

        //will store the new generated state
        Queue<Set<Integer>> q = new ArrayDeque<>();

        // add the first state
        int firstState = nfa.getStartNode();
        Set<Integer> firstStateSet = new HashSet<>();
        firstStateSet.add(firstState);

        q.add(firstStateSet);

        while (!q.isEmpty()) {
            Set<Integer> fromSet = q.poll();

            if (!map.containsKey(fromSet)) {
                map.put(fromSet, index);
                index++;

                for (char ch = 'a'; ch <= 'z'; ch++) {
                    Set<Integer> toSet = new HashSet<>();
                    for (Integer fromElem: fromSet)  {
                        toSet.addAll(nfa.getDestinationNodesWithGivenWeight(fromElem, ch));
                    }

                    if (!map.containsKey(toSet)) {
                        map.put(toSet, index);
                        index++;
                    }

                    int to = map.get(toSet);

                    // TODO: 31-07-2023 Complete the NFA to DFA conversion. This is optional part for assignment, and hence not submitting the complete in assignment. Will update the code on github
                }
            }
        }
    }
}
