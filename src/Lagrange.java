import java.util.HashMap;
import java.util.Map;

public class Lagrange extends Polynomial {
    private final Map<Double, Double> points;

    public Lagrange(Map<Double,Double> points){
        this.points = new HashMap<>(points);
        createLagrange();
    }

    private Polynomial createFundamental(double xk){
        var lk = new Polynomial(1.0);
        for(var xj: points.keySet()){
            if(xk == xj) continue;
            // Расчет множителя: (x - xj) / (xk - xj)
            var p1 = new Polynomial(-xj, 1.0).div(xk - xj);
            lk = lk.times(p1);
        }
        return lk;
    }

    private void createLagrange(){
        var sum = new Polynomial();
        for(var p: points.entrySet()){
            sum = sum.plus(createFundamental(p.getKey()).times(p.getValue()));

        }
        coeffs.clear();
        coeffs.putAll(sum.coeffs);
    }

}
