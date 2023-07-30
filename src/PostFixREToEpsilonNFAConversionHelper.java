import java.util.Stack;

public class PostFixREToEpsilonNFAConversionHelper {
    private static EpsilonNFA createSimpleNFA (char ch) {
        EpsilonNFA epsilonNfa = new EpsilonNFA(2);
        epsilonNfa.addEdge(0, 1, ch);
        epsilonNfa.setStartNode(0);
        epsilonNfa.setFinalNode(1);

        //for testing
//        epsilonNfa.printAdjList();

        return epsilonNfa;
    }

    private static EpsilonNFA copyBothNFAToResultNFAForOR (EpsilonNFA first, EpsilonNFA second) {
        int numberOfNodesInResult = first.getNumberOfNodes() + second.getNumberOfNodes() + 2;
        EpsilonNFA resultEpsilonNFA = new EpsilonNFA(numberOfNodesInResult);

        int newStartNode = first.getNumberOfNodes() + second.getNumberOfNodes();
        int newFinalNode = newStartNode + 1;

        resultEpsilonNFA.setStartNode(newStartNode);
        resultEpsilonNFA.setFinalNode(newFinalNode);

        resultEpsilonNFA.addAllEdge(first.getAdjList());
        resultEpsilonNFA.addAllEdgeWithOffset(second.getAdjList(), first.getNumberOfNodes());

//        System.out.println("After adding both graphs:");
//        resultEpsilonNFA.printAdjList();

        return resultEpsilonNFA;
    }

    private static EpsilonNFA applyOROperator (EpsilonNFA first, EpsilonNFA second) {
        EpsilonNFA resultEpsilonNFA = copyBothNFAToResultNFAForOR(first, second);

        resultEpsilonNFA.addEdge(resultEpsilonNFA.getStartNode(), first.getStartNode(), SpecialCharacters.Epsilon);
//        System.out.println("After adding start->firstStart:");
//        resultEpsilonNFA.printAdjList();

        resultEpsilonNFA.addEdge(resultEpsilonNFA.getStartNode(), first.getNumberOfNodes(), SpecialCharacters.Epsilon);
//        System.out.println("After adding start->secondStart");
//        resultEpsilonNFA.printAdjList();

        resultEpsilonNFA.addEdge(first.getFinalNode(), resultEpsilonNFA.getFinalNode(), SpecialCharacters.Epsilon);
//        System.out.println("After adding first.final->newFinal");
//        resultEpsilonNFA.printAdjList();

        resultEpsilonNFA.addEdge(first.getNumberOfNodes() + second.getNumberOfNodes() - 1, resultEpsilonNFA.getFinalNode(), SpecialCharacters.Epsilon);
//        System.out.println("After adding second.final->newFinal");
//        resultEpsilonNFA.printAdjList();

        return resultEpsilonNFA;
    }

    private static EpsilonNFA applyANDOperator (EpsilonNFA first, EpsilonNFA second) { //order matters
        int numberOfNodes = first.getNumberOfNodes() + second.getNumberOfNodes() - 1;
        EpsilonNFA resultEpsilonNFA = new EpsilonNFA(numberOfNodes);

        resultEpsilonNFA.setStartNode(first.getStartNode());
        resultEpsilonNFA.setFinalNode(first.getNumberOfNodes() + second.getNumberOfNodes() - 2);
        //-2: one for index, and other for including the final node of first graph as start node of second graph

        resultEpsilonNFA.addAllEdge(first.getAdjList());
        resultEpsilonNFA.addAllEdgeWithOffset(second.getAdjList(), first.getNumberOfNodes() - 1);
//        System.out.println("After adding both graphs:");
//        resultEpsilonNFA.printAdjList();

        // No epsilon nodes needed

        return resultEpsilonNFA;
    }

    public static void PostFixREToNFA (String input) {
        Stack<EpsilonNFA> stack = new Stack<>();

        //scan all the characters one by one
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // scanned character is an operand,
            // Make a simple EpsilonNFA out of it and push it to stack
            if (Character.isLetterOrDigit(ch)) {
                EpsilonNFA epsilonNfa = createSimpleNFA(ch);
                stack.push(epsilonNfa);
            }

            // scanned character is an operator,
            // pop two elements from stack and apply the operator => +, .
            // pop only one element from stack and apply the operator => *
            else {
                switch (ch) {
                    case SpecialCharacters.OROperator: //OR operator
                    {
                        EpsilonNFA first = stack.pop();
                        EpsilonNFA second = stack.pop();
                        EpsilonNFA resultEpsilonNFA = applyOROperator(first, second);
//                        resultEpsilonNFA.printAdjList();
                        stack.push(resultEpsilonNFA);
                        break;
                    }

                    case SpecialCharacters.ANDOperator: //AND operator
                    {
                        //order matters
                        EpsilonNFA second = stack.pop();
                        EpsilonNFA first = stack.pop();
                        EpsilonNFA resultNFA = applyANDOperator(first, second);
//                        resultNFA.printAdjList();
                        stack.push(resultNFA);
                        break;
                    }

                    case SpecialCharacters.KleeneStarOperator: //Kleene star operator
                    {
                        EpsilonNFA first = stack.pop();
//                        EpsilonNFA resultNFA = applyStarOperator(first);
//                        stack.push(resultNFA);
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
    }


    //for testing
    public static void main(String[] args) {
        PostFixREToNFA("ab.c.");
    }
}
