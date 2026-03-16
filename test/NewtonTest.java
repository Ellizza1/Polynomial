import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewtonTest {

    private Newton newton;
    private List<InterpolatingPolynomial.Point> points;

    @BeforeEach
    void setUp() {
        points = new ArrayList<>();
    }

    @Test
    @DisplayName("Интерполяция линейной функции (2 точки)")
    void testLinearInterpolation() {
        // y = 2x + 1
        points.add(new InterpolatingPolynomial.Point(0, 1));
        points.add(new InterpolatingPolynomial.Point(1, 3));

        newton = new Newton(points);

        // Проверка в узлах
        assertEquals(1, newton.evaluate(0), 1e-9);
        assertEquals(3, newton.evaluate(1), 1e-9);

        // Проверка между узлами
        assertEquals(2, newton.evaluate(0.5), 1e-9);
        assertEquals(5, newton.evaluate(2), 1e-9);
    }

    @Test
    @DisplayName("Интерполяция квадратичной функции (3 точки)")
    void testQuadraticInterpolation() {
        // y = x²
        points.add(new InterpolatingPolynomial.Point(0, 0));
        points.add(new InterpolatingPolynomial.Point(1, 1));
        points.add(new InterpolatingPolynomial.Point(2, 4));

        newton = new Newton(points);

        // Проверка в узлах
        assertEquals(0, newton.evaluate(0), 1e-9);
        assertEquals(1, newton.evaluate(1), 1e-9);
        assertEquals(4, newton.evaluate(2), 1e-9);

        // Проверка между узлами
        assertEquals(2.25, newton.evaluate(1.5), 1e-9); // 1.5² = 2.25
    }

    @Test
    @DisplayName("Интерполяция sin(x)")
    void testSinInterpolation() {
        points.add(new InterpolatingPolynomial.Point(0, 0));
        points.add(new InterpolatingPolynomial.Point(Math.PI / 2, 1));
        points.add(new InterpolatingPolynomial.Point(Math.PI, 0));

        newton = new Newton(points);

        // Проверка в узлах
        assertEquals(0, newton.evaluate(0), 1e-9);
        assertEquals(1, newton.evaluate(Math.PI / 2), 1e-9);
        assertEquals(0, newton.evaluate(Math.PI), 1e-9);

        // Проверка в промежуточной точке (приблизительно)
        double x = Math.PI / 4;
        double expected = Math.sin(x);
        double actual = newton.evaluate(x);
        assertEquals(expected, actual, 0.1); // большая погрешность из-за малых точек
    }

    @Test
    @DisplayName("Пустой набор точек")
    void testEmptyPoints() {
        newton = new Newton(points);
        assertEquals(0, newton.evaluate(0), 1e-9);
    }

    @Test
    @DisplayName("Одна точка")
    void testSinglePoint() {
        points.add(new InterpolatingPolynomial.Point(5, 10));
        newton = new Newton(points);

        assertEquals(10, newton.evaluate(5), 1e-9);
        assertEquals(10, newton.evaluate(0), 1e-9); // константа
    }

    @Test
    @DisplayName("Сравнение Newton и Lagrange с выводом в консоль")
    void testCompareWithLagrange() {
        System.out.println("СРАВНЕНИЕ: Newton vs Lagrange");

        // Подготовка данных
        points.add(new InterpolatingPolynomial.Point(0, 1));
        points.add(new InterpolatingPolynomial.Point(1, 3));
        points.add(new InterpolatingPolynomial.Point(2, 2));

        // Вывод узлов интерполяции
        System.out.println("\nУзлы интерполяции:");
        for (InterpolatingPolynomial.Point p : points) {
            System.out.printf("Point(%.1f, %.1f)%n", p.x, p.y);
        }

        // 1. Создаём Newton
        Newton newtonPoly = new Newton(points);
        System.out.println("Полином Ньютона: " + newtonPoly.toString());

        // 2. Создаём Lagrange
        Map<Double, Double> lagrangePoints = points.stream()
                .collect(Collectors.toMap(p -> p.x, p -> p.y));
        Lagrange lagrangePoly = new Lagrange(lagrangePoints);

        System.out.println("Полином Лагранжа: " + lagrangePoly.toString());

        // 3. Сравнение результатов в табличном виде
        System.out.println("\nСравнение значений:");

        double maxDiff = 0.0;

        for (double x = -1; x <= 3; x += 0.5) {
            double newtonVal = newtonPoly.evaluate(x);
            double lagrangeVal = lagrangePoly.evaluate(x);
            double diff = Math.abs(newtonVal - lagrangeVal);

            maxDiff = Math.max(maxDiff, diff);
        }

        System.out.printf("Максимальная разница: %.2e%n", maxDiff);

        // Финальный результат
        if (maxDiff < 1e-8) {
            System.out.println("SUCCESS: Полиномы совпадают ");
        } else if (maxDiff < 1e-6) {
            System.out.println(" WARNING: Небольшие расхождения ");
        } else {
            System.out.println("ERROR: Значительные расхождения между методами");
        }

        assertEquals(0, maxDiff, 1e-8,
                "Значения полиномов Newton и Lagrange должны совпадать");
    }


    //тест на построение полиномов
    private double testFunction(double x) {
        return Math.sin(x);
    }

    //генерация точек на отрезке
    private List<InterpolatingPolynomial.Point> generatePoints(int count) {
        List<InterpolatingPolynomial.Point> pts = new ArrayList<>();
        double step = 2.0 / (count - 1); // от -1 до 1
        for (int i = 0; i < count; i++) {
            double x = -1.0 + i * step;
            pts.add(new InterpolatingPolynomial.Point(x, testFunction(x)));
        }
        return pts;
    }
    @Test
    @DisplayName("Время построения (простой)")
    void benchmarkBuildSimple() {
        int degree = 200; // Степень полинома (количество точек = degree + 1)
        int iterations = 5; // Сколько раз замерить для точности

        System.out.println("\nПостроение полиномов ");
        System.out.println("Степень: " + degree + ", Функция: sin(x)");

        // Подготовка данных
        List<InterpolatingPolynomial.Point> pts = generatePoints(degree + 1);
        Map<Double, Double> lagrangeMap = pts.stream()
                .collect(Collectors.toMap(p -> p.x, p -> p.y));

        // Newton
        long newtonTotal = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            new Newton(pts);
            long end = System.nanoTime();
            newtonTotal += (end - start);
        }

        // Lagrange
        long lagrangeTotal = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            new Lagrange(lagrangeMap);
            long end = System.nanoTime();
            lagrangeTotal += (end - start);
        }

        // Вывод результатов
        double newtonAvg = (newtonTotal / iterations) / 1_000_000.0;
        double lagrangeAvg = (lagrangeTotal / iterations) / 1_000_000.0;

        System.out.printf("Newton:   %.3f ms %n", newtonAvg);
        System.out.printf("Lagrange: %.3f ms %n", lagrangeAvg);
        System.out.printf("Newton быстрее в %.1f раз%n", lagrangeAvg / newtonAvg);

        // Тест проходит, если просто завершился
        assertEquals(1, 1);
    }


    //тест на добавление точки
    @Test
    @DisplayName("Добавление одной точки")
    void benchmarkAddPoint() {
        int baseDegree = 100; // Базовая степень полинома
        int iterations = 20;  // Больше итераций для точности замера

        System.out.println("\nДобавление точки ");
        System.out.println("Базовая степень: " + baseDegree);

        // Подготовка базовых точек
        List<InterpolatingPolynomial.Point> basePts = generatePoints(baseDegree + 1);
        Map<Double, Double> baseMap = basePts.stream()
                .collect(Collectors.toMap(p -> p.x, p -> p.y));

        // Новая точка для добавления
        double newX = 1.5;
        double newY = testFunction(newX);

        // Замер для Newton: добавляем точку к существующему полиному
        long newtonTotal = 0;
        for (int i = 0; i < iterations; i++) {
            Newton n = new Newton(basePts);
            long start = System.nanoTime();
            n.addPoint(newX, newY);  // ← Должен быть реализован в классе Newton
            long end = System.nanoTime();
            newtonTotal += (end - start);
        }

        // Замер для Lagrange: пересоздаём полином с новой точкой
        long lagrangeTotal = 0;
        for (int i = 0; i < iterations; i++) {
            Map<Double, Double> newMap = new java.util.HashMap<>(baseMap);
            newMap.put(newX, newY);
            long start = System.nanoTime();
            new Lagrange(newMap);  // Полное перестроение
            long end = System.nanoTime();
            lagrangeTotal += (end - start);
        }

        // Вывод результатов
        double newtonAvg = (newtonTotal / iterations) / 1_000_000.0;
        double lagrangeAvg = (lagrangeTotal / iterations) / 1_000_000.0;

        System.out.printf("Newton:   %.4f ms %n", newtonAvg);
        System.out.printf("Lagrange: %.4f ms %n", lagrangeAvg);

        if (newtonAvg > 0) {
            System.out.printf("Newton быстрее в %.1f раз при добавлении точки%n",
                    lagrangeAvg / newtonAvg);
        }

        assertEquals(1, 1);
    }
}