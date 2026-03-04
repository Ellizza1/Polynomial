import java.util.*;

public class Newton extends InterpolatingPolynomial {

    public Newton(Collection<InterpolatingPolynomial.Point> dots) {
        super(dots);
        createNewton();
    }

    private void createNewton() {
        Polynomial sum = new Polynomial();
        Polynomial basis = new Polynomial(1.0);

        //получаем отсортированые x
        List<InterpolatingPolynomial.Point> sortedPoints = new ArrayList<>(getPoints());
        sortedPoints.sort(Comparator.comparingDouble(p -> p.x));

        //получаем коэфф-ы a0 a1 a2 и тд
        double[] coefficients = calculateDividedDifferences();

        if (coefficients.length == 0) {
            coeffs.clear();
            coeffs.put(0, 0.0);
            return;
        }

        for (int i = 0; i < coefficients.length; i++) {
            sum = sum.plus(basis.times(coefficients[i]));

            // обновляем базис для следующего шага basis *= (x - xᵢ)
            if (i < sortedPoints.size() - 1) {
                double xi = sortedPoints.get(i).x;
                // создаем полином (x - xᵢ) = -xᵢ + 1×x
                Polynomial factor = new Polynomial(-xi, 1.0);
                basis = basis.times(factor);
            }
        }

        coeffs.clear();
        coeffs.putAll(sum.coeffs);
    }
}