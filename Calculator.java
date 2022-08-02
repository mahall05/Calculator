import java.lang.Math;
import java.util.Scanner;

public class Calculator {
    public static char[] ops = {'^', '*', '/', '+', '-'};
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args){
        System.out.println("Enter equation");
        parse(in.nextLine());
    }

    public static void parse(String set){
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

        System.out.println(solve(num1, num2, operation));
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
