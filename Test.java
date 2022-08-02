public class Test {
    public static void main(String[] args){
        StringBuilder string = new StringBuilder("4+5/5/5");

        string.replace(string.lastIndexOf("5/5"),string.lastIndexOf("5/5")+3, "1");

        System.out.println(string);
    }
}
