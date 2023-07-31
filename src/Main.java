import conversionhelper.EpsilonNFAToNFAConversionHelper;
import conversionhelper.PostFixREToEpsilonNFAConversionHelper;
import conversionhelper.PostfixConversionHelper;
import model.NFA;

import java.util.Scanner;

public class Main {
    private static String takeInputRegularExpression() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the regular expression: ");
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }

    public static void main(String[] args) {
        String regularExpression = takeInputRegularExpression();

//        String regularExpression = "(a.b+a)*";

        System.out.println("\nGiven regular expression: " + regularExpression);

        //convert to postfix expression
        String postfix = PostfixConversionHelper.convertToPostFix(regularExpression);

        if (postfix.equals(PostfixConversionHelper.INVALID_INPUT)) {
            System.out.println("The given expression " + regularExpression + " is invalid");
        } else {
            System.out.println("\nPostfix Expression is " + postfix);
            NFA epsilonNFA = PostFixREToEpsilonNFAConversionHelper.PostFixREToEpsilonNFA(postfix);

            System.out.println("\nThe result Epsilon NFA is ");
            epsilonNFA.printAdjList();

            NFA nfa = EpsilonNFAToNFAConversionHelper.epsilonNFAtoNFA(epsilonNFA);
            System.out.println("\nThe result NFA is ");
            nfa.printAdjList();

            // TODO: 31-07-2023 Optional part: Conversion to DFA
        }
    }
}