package conversionhelper;

import characterhelper.CharacterIdentifyHelper;

import java.util.ArrayDeque;
import java.util.Deque;

public class PostfixConversionHelper {

    public static final String INVALID_INPUT = "Invalid Expression";
    private static int Precedence(char ch) {
        if (ch == '*') {
            return 3;
        } else if (ch == '.') {
            return 2;
        } else if (ch == '+') {
            return 1;
        } else {
            return -1;
        }
    }

    public static String convertToPostFix(String input) {
        StringBuilder result = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();

        for (int i=0; i < input.length(); i++) {
            char ch = input.charAt(i);

            //scanned character is an operand, add it to result
            if (CharacterIdentifyHelper.isLatinLetter(ch)) {
                result.append(ch);
            }

            // if the scanned character is '(',
            // push it to the stack
            else if (ch == '(') {
                stack.push(ch);
            }

            // if the scanned character is an ')',
            // pop and output from the stack, until an '(' is encountered
            else if (ch== ')') {
                while(!stack.isEmpty()  &&  (stack.peek() != '(')) {
                    result.append(stack.peek());
                    stack.pop();
                }
                stack.pop();
            }

            // an operator is encountered
            else {
                while (!stack.isEmpty()  &&  (Precedence(ch) <= Precedence(stack.peek()))) {
                    result.append(stack.peek());
                    stack.pop();
                }
                stack.push(ch);
            }
        }

        // pop all the operators from the stack
        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                return INVALID_INPUT;
            }
            result.append(stack.peek());
            stack.pop();
        }

        return result.toString();
    }
}
