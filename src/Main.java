import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.lang.Math;

public class Main {
    public static void main(String[] args) {
        Polynomial polinomFirst = new Polynomial(1.5, 4.0);
        Polynomial polinomSecond = new Polynomial(1.5, 4.0, 8.0, 6.0, 1.0);
        Polynomial polinomThird = new Polynomial(1.5, 4.0, 9.0, 3.0, 1.0, 9, 11);

        String result = polinomFirst.toString();
        String result2 = polinomSecond.toString();
        String result3 = polinomThird.toString();

        System.out.println("first polynom: " + result.toString());
        System.out.println("third polynom: " + result3.toString());
        System.out.println("Calc first polynom in dot 3: " + polinomFirst.calc(3));


        //System.out.printf(result);
//        System.out.println("First and second equals: " + polinomFirst.equals(polinomSecond));
//        System.out.println("First and third equals: " + polinomFirst.equals(polinomThird));
//        System.out.println("HashCodeFirst: " + polinomFirst.hashCode());
//        System.out.println("HashCodeSecond: " + polinomSecond.hashCode());
//        System.out.println("HashCodeThird: " + polinomThird.hashCode());
//        System.out.println("First + third " + polinomFirst.plus(polinomThird));
//        System.out.println("First - third " + polinomFirst.minus(polinomThird));






//        final Map<Character,Integer> ls = new TreeMap<>();
//
//        Scanner in = new Scanner(System.in);
//        String word = in.nextLine();
//        if(word.isEmpty()){
//            System.out.printf("your word is empty");
//            return;
//        }
//        in.close();
//
//        for(int i = 0; i < word.length();i++){
//            char symb = word.charAt(i);
//            int count = ls.getOrDefault(symb, 0);
//            ls.put(symb , count + 1);
//        }
//
//        System.out.println(ls);


    }
}

