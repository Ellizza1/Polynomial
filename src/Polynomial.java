import java.util.*;
import java.lang.Math;
import java.lang.Integer;

public class Polynomial {
    protected final Map<Integer, Double> coeffs;
    private static final double EPS = 1e-9;

    public Polynomial() {
        coeffs = new TreeMap<>();
        coeffs.put(0, 0.0);
    }

    public Polynomial(Map<Integer, Double> coeffs) {
        this();
        this.coeffs.clear();
        this.coeffs.putAll(coeffs);
        filterCoeffs();
    }

    public Polynomial(double... coeffs) {
        this();
        for (int i = 0; i < coeffs.length; i++) {
            this.coeffs.put(i, coeffs[i]);
        }
        filterCoeffs();
    }

//    public Polynomial(double[] coeffs) {
//        this();
//        for (int i = 0; i < coeffs.length; i++) {
//            this.coeffs.put(i, coeffs[i]);
//        }
//        filterCoeffs();
//    }


    private void filterCoeffs() {
        coeffs.entrySet().removeIf(entry -> entry.getValue() == 0.0);
        if (coeffs.isEmpty()) {
            coeffs.put(0, 0.0);
        }
    }

    public Map<Integer, Double> copyList(Map<Integer, Double> coeffs) {
        final Map<Integer, Double> copyCoeffs = new TreeMap<>();
        copyCoeffs.putAll(this.coeffs);
        return copyCoeffs;
    }

    @Override
    public String toString() {
        if (coeffs.isEmpty()) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for (Map.Entry<Integer, Double> entry : coeffs.entrySet()) {
            int n = entry.getKey();
            double a = entry.getValue();

            if (a == 0.0) continue;

            if (isFirst) {
                if (a < 0.0) sb.append("-");
                isFirst = false;
            } else {
                sb.append(a > 0.0 ? " + " : " - ");
            }

            if (n == 0) {
                sb.append(Math.abs(a));
            } else if (n == 1) {
                if (Math.abs(a) != 1) sb.append(Math.abs(a));
                sb.append("x");
            } else {
                if (Math.abs(a) != 1) sb.append(Math.abs(a));
                sb.append("x^").append(n);
            }
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;

        Polynomial p = (Polynomial) obj; //приводим объект в тип object

        if((getClass() != obj.getClass()) || (GetPower()!= p.GetPower()) ) return false;
        else if(this.coeffs.size() != p.coeffs.size()) return false;
        else {
            for(Integer degree: this.coeffs.keySet()){
                if(Math.abs( this.coeffs.get(degree) - p.coeffs.get(degree) ) > EPS) return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode(){
        int result = 17;

        for(Map.Entry<Integer, Double> entry: coeffs.entrySet()){
            result = 31 * result + entry.getKey().hashCode();

            long bits = Double.doubleToLongBits(entry.getValue());
            result = 31 * result + (int) (bits ^ (bits >>> 32));
        }
        return result;
    }

    //удаление ненужных значений
    private void correctCoeffs(){
        coeffs.entrySet().removeIf(entry -> Math.abs(entry.getValue()) < EPS);
        if(coeffs.isEmpty()) coeffs.put(0, 0.0);
    }

    public int GetPower(){
        if(coeffs.isEmpty() || (coeffs.size() == 1 && coeffs.get(0) == 0.0)) return 0;
        return Collections.max(coeffs.keySet());
    }

    public Polynomial plus(Object obj){
        Polynomial other = (Polynomial) obj;
        Map<Integer, Double> result = new HashMap<>(this.coeffs);
        for(Integer degree : other.coeffs.keySet()) {
            // merge: если ключ есть - суммирует, если нет - добавляет новый
            result.merge(degree, other.coeffs.get(degree), Double::sum);
        }
        return new Polynomial(result);
    }

    public Polynomial minus(Object obj){
        Polynomial other = (Polynomial) obj;
        Map<Integer, Double> result = new HashMap<>(this.coeffs);
        for(Integer degree: other.coeffs.keySet()){
            result.merge(degree, other.coeffs.get(degree)  , (a, b) -> a - b);
        }

        return new Polynomial(result);
    }

    public Polynomial times(double number) {
        Polynomial result = new Polynomial();

        for (Integer degree : this.coeffs.keySet()) {
            result.coeffs.put(degree, this.coeffs.get(degree) * number);
        }

        result.correctCoeffs();

        return result;
    }

    public Polynomial times(Polynomial other) {
        Polynomial result = new Polynomial();

        for (Map.Entry<Integer, Double> entry1 : this.coeffs.entrySet()) {
            for (Map.Entry<Integer, Double> entry2 : other.coeffs.entrySet()) {
                int degree = entry1.getKey() + entry2.getKey();
                double coeff = entry1.getValue() * entry2.getValue();

                result.coeffs.merge(degree, coeff, Double::sum);
            }
        }

        result.correctCoeffs();
        return result;
    }

    public Polynomial div(double number) {
        if (Math.abs(number) < EPS) {
            throw new ArithmeticException("Division by zero");
        }

        Polynomial result = new Polynomial();

        for (Integer degree : this.coeffs.keySet()) {
            result.coeffs.put(degree, this.coeffs.get(degree) / number);
        }

        result.correctCoeffs();

        return result;
    }

    public double calc(int dot){
        double result = 0.0;

        for(Map.Entry<Integer, Double> entry : this.coeffs.entrySet()){
            int degree = entry.getKey();
            double coeff = entry.getValue();
            result += (coeff * Math.pow(dot, degree));
        }

        return result;
    }

}
