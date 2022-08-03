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
                System.out.println("Result: " + evaluate(equation, false));
            }
        }
    }

    public static String evaluate(String equation, boolean p){
        int pos = -1;

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
                        equation = replace(equation, parenRange, evaluate(equation.substring(openPos+1, closePos), true));
                        System.out.println(equation);
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
                equation = replace(equation, findRange(equation, pos), solve(findSet(equation, pos), '^'));
                if(!p)
                    System.out.println(equation);
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
                equation = replace(equation, findRange(equation, pos), solve(findSet(equation, pos), equation.charAt(i)));
                if(!p)
                    System.out.println(equation);
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
                equation = replace(equation, findRange(equation, pos), solve(findSet(equation, pos), equation.charAt(i)));
                if(!p)
                    System.out.println(equation);
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

    public static int solve(String set, char operation){
        int num1End = -1, num2Start = -1;
        int num1, num2;
        int result;
        int opPos = -1;

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

    public static String findSet(String equation, int pos){
        boolean found = false;
        int start = 0, end = equation.length()-1;

        for(int i = pos; i >= 0; i--){
            if(!found && Character.isDigit(equation.charAt(i))){
                found = true;
            }else if(found && !Character.isDigit(equation.charAt(i)) && !((equation.charAt(i) == '-') && i==0)){
                start = i+1;
                i = -100;
            }
        }
        found = false;

        for(int i = pos; i < equation.length(); i++){
            if(!found && Character.isDigit(equation.charAt(i))){
                found = true;
            }else if(found && !Character.isDigit(equation.charAt(i))){
                end = i-1;
                i = equation.length()+100;
            }
        }

        return equation.substring(start, end+1);
    }

    public static int[] findRange(String equation, int pos){
        boolean found = false;
        int start = 0, end = equation.length()-1;

        for(int i = pos; i >= 0; i--){
            if(!found && Character.isDigit(equation.charAt(i))){
                found = true;
            }else if(found && !Character.isDigit(equation.charAt(i)) && !((equation.charAt(i) == '-') && i==0) && equation.charAt(i) != '('){
                start = i+1;
                i = -100;
            }
        }
        found = false;

        for(int i = pos; i < equation.length(); i++){
            if(!found && Character.isDigit(equation.charAt(i))){
                found = true;
            }else if(found && !Character.isDigit(equation.charAt(i)) && equation.charAt(i) != ')'){
                end = i-1;
                i = equation.length()+100;
            }
        }

        int[] range = {start, end};

        return range;
    }
}
