import java.util.Stack;

public class PostFixREToEpsilonNFAConversionHelper {
    private static NFA createSimpleNFA (char ch) {
        NFA nfa = new NFA(2);
        nfa.addEdge(0, 1, ch);
        nfa.setStartNode(0);
        nfa.addFinalNode(1);

        //for testing
//        nfa.printAdjList();

        return nfa;
    }

    private static NFA copyBothNFAToResultNFAForOR (NFA first, NFA second) {
        int numberOfNodesInResult = first.getNumberOfNodes() + second.getNumberOfNodes() + 2;
        NFA resultNFA = new NFA(numberOfNodesInResult);

        int newStartNode = first.getNumberOfNodes() + second.getNumberOfNodes();
        int newFinalNode = newStartNode + 1;

        resultNFA.setStartNode(newStartNode);
        resultNFA.addFinalNode(newFinalNode);

        resultNFA.addAllEdge(first.getAdjList());
        resultNFA.addAllEdgeWithOffset(second.getAdjList(), first.getNumberOfNodes());

//        System.out.println("After adding both graphs:");
//        resultNFA.printAdjList();

        return resultNFA;
    }

    private static NFA applyOROperator (NFA first, NFA second) {
        NFA resultNFA = copyBothNFAToResultNFAForOR(first, second);

        resultNFA.addEdge(resultNFA.getStartNode(), first.getStartNode(), SpecialCharacters.Epsilon);
//        System.out.println("After adding start->firstStart:");
//        resultNFA.printAdjList();

        resultNFA.addEdge(resultNFA.getStartNode(), first.getNumberOfNodes(), SpecialCharacters.Epsilon);
//        System.out.println("After adding start->secondStart");
//        resultNFA.printAdjList();

        for (Integer firstFinal: first.getFinalNodes()) {
            for (Integer resultFinal: resultNFA.getFinalNodes()) {
                resultNFA.addEdge(firstFinal, resultFinal, SpecialCharacters.Epsilon);
            }
        }
//        System.out.println("After adding first.final->newFinal");
//        resultNFA.printAdjList();

        for (Integer resultFinal: resultNFA.getFinalNodes()) {
            resultNFA.addEdge(first.getNumberOfNodes() + second.getNumberOfNodes() - 1, resultFinal, SpecialCharacters.Epsilon);
        }
//        System.out.println("After adding second.final->newFinal");
//        resultNFA.printAdjList();

        return resultNFA;
    }

    private static NFA applyANDOperator (NFA first, NFA second) { //order matters
        int numberOfNodes = first.getNumberOfNodes() + second.getNumberOfNodes() - 1;
        NFA resultNFA = new NFA(numberOfNodes);

        resultNFA.setStartNode(first.getStartNode());
        resultNFA.addFinalNode(first.getNumberOfNodes() + second.getNumberOfNodes() - 2);
        //-2: one for index, and other for including the final node of first graph as start node of second graph

        resultNFA.addAllEdge(first.getAdjList());
        resultNFA.addAllEdgeWithOffset(second.getAdjList(), first.getNumberOfNodes() - 1);
//        System.out.println("After adding both graphs:");
//        resultNFA.printAdjList();

        // No epsilon nodes needed

        return resultNFA;
    }

    private static NFA applyKleeneStarOperator (NFA first) {
        int numberOfNodesInResult = first.getNumberOfNodes() + 2;
        NFA resultNFA = new NFA(numberOfNodesInResult);

        int newStartNode = first.getNumberOfNodes();
        int newFinalNode = first.getNumberOfNodes() + 1;

        resultNFA.setStartNode(newStartNode);
        resultNFA.addFinalNode(newFinalNode);

        resultNFA.addAllEdge(first.getAdjList());
//        System.out.println("After adding the graph:");
//        resultNFA.printAdjList();

        for (Integer firstFinal: first.getFinalNodes()) {
            resultNFA.addEdge(firstFinal, first.getStartNode(), SpecialCharacters.Epsilon);
        }
//        System.out.println("After adding firstFinal->firstStart:");
//        resultNFA.printAdjList();

        resultNFA.addEdge(resultNFA.getStartNode(), first.getStartNode(), SpecialCharacters.Epsilon);
//        System.out.println("After adding start->firstStart");
//        resultNFA.printAdjList();

        for (Integer resultFinal: resultNFA.getFinalNodes()) {
            resultNFA.addEdge(resultNFA.getStartNode(), resultFinal, SpecialCharacters.Epsilon);
        }
//        System.out.println("After adding start->final");
//        resultNFA.printAdjList();

        for (Integer firstFinal: first.getFinalNodes()) {
            for (Integer resultFinal: resultNFA.getFinalNodes()) {
                resultNFA.addEdge(firstFinal, resultFinal, SpecialCharacters.Epsilon);
            }
        }
//        System.out.println("After adding firstFinal->final");
//        resultNFA.printAdjList();

        return resultNFA;
    }

    public static NFA PostFixREToEpsilonNFA(String input) {
        Stack<NFA> stack = new Stack<>();

        //scan all the characters one by one
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // scanned character is an operand,
            // Make a simple NFA out of it and push it to stack
            if (CharacterIdentifyHelper.isLatinLetter(ch)) {
                NFA nfa = createSimpleNFA(ch);
                stack.push(nfa);
            }

            // scanned character is an operator,
            // pop two elements from stack and apply the operator => +, .
            // pop only one element from stack and apply the operator => *
            else {
                switch (ch) {
                    case SpecialCharacters.OROperator: //OR operator
                    {
                        NFA first = stack.pop();
                        NFA second = stack.pop();
                        NFA resultNFA = applyOROperator(first, second);
//                        resultNFA.printAdjList();
                        stack.push(resultNFA);
                        break;
                    }

                    case SpecialCharacters.ANDOperator: //AND operator
                    {
                        //order matters
                        NFA second = stack.pop();
                        NFA first = stack.pop();
                        NFA resultNFA = applyANDOperator(first, second);
//                        resultNFA.printAdjList();
                        stack.push(resultNFA);
                        break;
                    }

                    case SpecialCharacters.KleeneStarOperator: //Kleene star operator
                    {
                        NFA first = stack.pop();
                        NFA resultNFA = applyKleeneStarOperator(first);
//                        resultNFA.printAdjList();
                        stack.push(resultNFA);
                        break;
                    }

                    default:
                    {
                        System.err.print("Invalid operator");
                        System.exit(-1);
                    }
                }
            }
        }
//        stack.peek().printAdjList();
        return stack.peek();
    }


    //for testing
    public static void main(String[] args) {
        PostFixREToEpsilonNFA("a*");
    }
}
