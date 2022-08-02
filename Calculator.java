import java.lang.Math;
import java.util.Scanner;

public class Calculator {
    public static char[] ops = {'^', '*', '/', '+', '-'};
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args){
        System.out.println("Enter equation");
        //parse(in.nextLine());
        solveEquation(in.nextLine());
    }

    public static void solveEquation(String equation){
        String[] sets = parseSets(equation);
        StringBuilder buildEquation = new StringBuilder(equation);

        //buildEquation.lastIndexOf(str)

        for(int i = 0; i < sets.length; i++){
            System.out.println("Set " + i + ": (" + sets[i] + ")");
        }

        for(int i = 0; i < ops.length; i++){
            for(int j = 0; j < sets.length; j++){
                boolean contains = false;
                for(int k = 0; k < sets.length; k++){
                    if(sets[j].charAt(k) == ops[i]){
                        contains = true;
                    }
                }
                if(contains){
                    double result = parse(sets[j]);
                    //equation.replace()
                }
            }
        }

        //equation.replace(target, replacement)
    }

    public static String[] parseSets(String equation){
        String[] sets = new String[equation.length()/2];
        int numbers = 0;
        int set = 0;
        int num1Start = 0, num2Start = 0;

        for(int i = 1; i <= equation.length(); i++){
            if(Character.isDigit(equation.charAt(i-1)) && !Character.isDigit(equation.charAt(i))){
                numbers++;
            }
            if(!Character.isDigit(equation.charAt(i-1)) && Character.isDigit(equation.charAt(i))){
                num2Start = i;
            }
            if(i == equation.length()-1){
                numbers++;
                i = equation.length();
            }
            if(numbers == 2){
                sets[set] = equation.substring(num1Start, i);
                set++;
                numbers = 1;
                num1Start = num2Start;
            }
        }

        int fSets = 0;
        for(int i = 0; i < sets.length; i++){
            if(sets[i] != null){
                fSets++;
            }
        }
        String[] fullSets = new String[fSets];
        for(int i = 0; i < fullSets.length; i++){
            fullSets[i] = sets[i];
        }

        return fullSets;
    }

    public static double parse(String set){
        char[] setArray = set.toCharArray();
        double num1 = 0, num2 = 0, sum = 0;
        char operation = 0;
        int opPosition = set.length(), num1End = 0, num2Start = 0;

        boolean cont = true;
        for(int i = 0; i < set.length(); i++){
            if(setArray[i] == ' ' && cont){
                cont = false;
                num1End = i;
            }

            for(int j = 0; j < ops.length; j++){
                if(setArray[i] == ops[j]){
                    operation = ops[j];
                    opPosition = j;

                    if(cont){
                        num1End = i;
                        cont = false;
                    }
                }
            }

            if(opPosition != set.length() && Character.isDigit(setArray[i]) && num2Start == 0){
                num2Start = i;
            }
        }

        num1 = Integer.parseInt(set.substring(0, num1End));
        num2 = Integer.parseInt(set.substring(num2Start, set.length()));

        return solve(num1, num2, operation);
    }

    public static double solve(double num1, double num2, char op){
        double result;
        switch(op){
            case('^'):
                result = Math.pow(num1, num2);
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
            default:
                result = num1 + num2;
                break;
        }

        return result;
    }

    // TODO, just test current character and previous character to determine end of number
}
