import java.util.Scanner;

public class Calc {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        System.out.println("Enter a number");
        int num1 = in.nextInt();

        System.out.println("Enter a number");
        int num2 = in.nextInt();

        int sum = num1 + num2;

        System.out.println(num1 + " + " + num2 + " = " + sum);

    }
}