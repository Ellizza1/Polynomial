import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class InterpolatingPolynomial extends Polynomial{

    private static class Point{
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
//    private List<Double> differencePoints(){
//
//    }
}
