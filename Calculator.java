import java.util.Scanner;

public class Calculator {
    private static char[] ops = {'(', ')', '^', '*', '/', '+', '-'};

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        boolean solved = false;

        boolean solving = true;
        // START SOLVING
        while(solving){
            System.out.println("Enter equation or \"end\" to exit");
            String equation = in.nextLine();

            if(equation.equalsIgnoreCase("end")){
                solving = false;
            }

            if(solving){
                System.out.println("Result: " + evaluate(equation));
            }
        }
    }

    public static String evaluate(String equation){
        int pos = -1;
        colorNum = colorNum==5 ? 0 : colorNum+1;
        System.out.println(colors[colorNum]+equation+Colors.ANSI_RESET);

        // PARENTHESES
        {
            int openPos = -1, closePos = -1;
            int openNum = 0, closeNum = 0;
            for(int i = 0; i < equation.length(); i++){
                if(equation.charAt(i) == '('){
                    openNum++;
                    if(openNum == 1){
                        openPos = i;
                    }
                }else if(equation.charAt(i) == ')'){
                    closeNum++;
                    if(closeNum == openNum){
                        closePos = i;
                    }
                }
                int[] parenRange = {openPos+1, closePos-1};

                if(closePos > -1){
                    boolean operation = false;
                    for(int j = openPos+1; j < closePos; j++){
                        if(!Character.isDigit(equation.charAt(j))){
                            operation = true;
                            j = closePos+100;
                        }
                    }
                    if(operation){
                        equation = replace(equation, parenRange, evaluate(equation.substring(openPos+1, closePos)));
                        colorNum-=1;
                        System.out.println(colors[colorNum]+equation+Colors.ANSI_RESET);
                    }
                    openPos = -1;
                    closePos = -1;
                    openNum = 0;
                    closeNum = 0;
                }
            }
        }

        // EXPONENTS
        for(int i = 0; i < equation.length(); i++){ // Search the entire equation for ^
            if(equation.charAt(i) == '^'){
                pos = i;
            }

            if(pos > -1){
                equation = replace(equation, findRange(equation, pos, true), solve(equation, findRange(equation, pos, false), '^'));
                System.out.println(colors[colorNum]+equation+Colors.ANSI_RESET);
                i = 0; // Added this because shorting the equation would cause i to be out of range
            }

            pos = -1;
        }

        // MULTIPLICATION / DIVISION
        for(int i = 1; i < equation.length(); i++){
            if(equation.charAt(i) == '*' || equation.charAt(i) == '/'){
                pos = i;
            }

            if(equation.charAt(i) == '(' || equation.charAt(i) == ')'){
                if(i < equation.length()-1 && Character.isDigit(equation.charAt(i-1)) && Character.isDigit(equation.charAt(i+1))){
                    pos = i;
                }
            }

            if(pos > -1){
                equation = replace(equation, findRange(equation, pos, true), solve(equation, findRange(equation, pos, false), equation.charAt(i)));
                System.out.println(colors[colorNum]+equation+Colors.ANSI_RESET);
                i = 0;
            }

            pos = -1;
        }

        // ADDITION / SUBTRACTION
        for(int i = 1; i < equation.length(); i++){
            if(equation.charAt(i) == '+' || equation.charAt(i) == '-'){
                pos = i;
            }

            if(pos > -1){
                equation = replace(equation, findRange(equation, pos, true), solve(equation, findRange(equation, pos, false), equation.charAt(i)));
                System.out.println(colors[colorNum]+equation+Colors.ANSI_RESET);
                i = 0;
            }

            pos = -1;
        }

        return equation;
    }

    public static String replace(String equation, int[] range, int result){
        StringBuilder buildEquation = new StringBuilder();

        buildEquation.append(equation.substring(0, range[0]));
        //System.out.println(buildEquation.toString());
        buildEquation.append(result);
        //System.out.println(buildEquation.toString());

        if(equation.length() > range[1]+1)
            buildEquation.append(equation.substring(range[1]+1, equation.length()));

        return buildEquation.toString();
    }

    public static String replace(String equation, int[] range, String value){
        StringBuilder buildEquation = new StringBuilder();

        buildEquation.append(equation.substring(0, range[0]));
        //System.out.println(buildEquation.toString());
        buildEquation.append(value);
        //System.out.println(buildEquation.toString());

        if(equation.length() > range[1]+1)
            buildEquation.append(equation.substring(range[1]+1, equation.length()));

        return buildEquation.toString();
    }

    public static int solve(String equation, int[] range, char operation){
        int num1End = -1, num2Start = -1;
        int num1, num2;
        int result;
        int opPos = -1;

        String set = equation.substring(range[0], range[1]+1);

        for(int i = 0; i < set.length(); i++){
            for(int j = 0; j < ops.length; j++){
                if(set.charAt(i) == ops[j]){
                    opPos = i;
                }
            }
        }

        for(int i = opPos; i >= 0; i--){
            if(Character.isDigit(set.charAt(i))){
                num1End = i;
                i = -100;
            }
        }

        for(int i = opPos; i < set.length(); i++){
            if(Character.isDigit(set.charAt(i))){
                num2Start = i;
                i = set.length()+100;
            }
        }

        num1 = Integer.parseInt(set.substring(0, num1End+1));
        num2 = Integer.parseInt(set.substring(num2Start, set.length()));

        switch(operation){
            case('^'):
                result = (int) Math.pow(num1, num2);
                break;
            case('*'):
                result = num1 * num2;
                break;
            case('/'):
                result = num1 / num2;
                break;
            case('+'):
                result = num1 + num2;
                break;
            case('-'):
                result = num1 - num2;
                break;
            case('('):
                result = num1 * num2;
                break;
            case(')'):
                result = num1 * num2;
                break;
            default:
                result = num1 + num2;
                break;
        }

        return result;
    }

    public static int[] findRange(String equation, int pos, boolean includeParan){
        boolean found = false;
        int start = 0, end = equation.length()-1;

        for(int i = pos; i >= 0; i--){
            if(!found && Character.isDigit(equation.charAt(i))){
                found = true;
            }else if(found && !Character.isDigit(equation.charAt(i)) && !((equation.charAt(i) == '-') && i==0) && (equation.charAt(i) != '(' || !includeParan)){
                start = i+1;
                i = -100;
            }
        }
        found = false;

        for(int i = pos; i < equation.length(); i++){
            if(!found && Character.isDigit(equation.charAt(i))){
                found = true;
            }else if(found && !Character.isDigit(equation.charAt(i)) && (equation.charAt(i) != ')' || !includeParan)){
                end = i-1;
                i = equation.length()+100;
            }
        }

        int[] range = {start, end};

        return range;
    }

    private class Colors{
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
    }

    public static String[] colors = {"\u001B[33m", "\u001B[31m", "\u001B[35m", "\u001B[34m", "\u001B[36m", "\u001B[32m"};
    public static int colorNum = -1;
}