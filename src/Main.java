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
//        String regularExpression = takeInputRegularExpression();
//        System.out.println(regularExpression);

        String regularExpression = "(a.b).c";

        //convert to postfix expression
        String postfix = PostfixConversionHelper.convertToPostFix(regularExpression);

        if (postfix.equals(PostfixConversionHelper.INVALID_INPUT)) {
            System.out.println("The given expression " + regularExpression + " is invalid");
        } else {
            System.out.println("Postfix Expression is " + postfix);
        }
    }
}