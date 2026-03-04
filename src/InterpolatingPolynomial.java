import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class InterpolatingPolynomial extends Polynomial{

    protected static class Point{
        double x, y;
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    //создание коллекции точек
    private Collection<Point> points = new ArrayList<>();

    public InterpolatingPolynomial(){
        this.points = new ArrayList<>();
    }

    public InterpolatingPolynomial(Collection<Point> dots){
        this.points = new ArrayList<>(dots);
    }

    public void addPoint(double x, double y){
        points.add(new Point(x ,y));
    }

    public Collection<Point> getPoints() {
        Collection<Point> copyPoints = new ArrayList<>();
        copyPoints.addAll(points);

        return copyPoints;
    }

    //вычисление разделенных разностей
    protected double[] calculateDividedDifferences() {
        List<Point> sortedPoint = new ArrayList<>(points);
        sortedPoint.sort(Comparator.comparingDouble(p -> p.x));

        int n = sortedPoint.size();
        if(n == 0) return new double[0];

        double[][] table = new double[n][n];

        //заполняем первый столбец значениями y
        for(int i =0; i<n; i++){
            table[i][0] = sortedPoint.get(i).y;
        }

        //вычисляем порядки разности
        for(int j = 1; j< n; j++){ //столбец
            for(int i = 0; i < n - j; i++){
                //числитель
                double numerator = table[i + 1][j - 1] - table[i][j - 1];
                //знаменатель
                double denominator = sortedPoint.get(i + j).x - sortedPoint.get(i).x;
                if (Math.abs(denominator) < 1e-12) {
                    throw new IllegalArgumentException("Duplicate x-values");
                }
                table[i][j] = numerator / denominator;
            }
        }

        double[] coeff = new double[n];
        for(int i = 0; i < n; i++ ){
            coeff[i] = table[0][i];
        }

        return coeff;
    }

}
