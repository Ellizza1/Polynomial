import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    private Polynomial polynom, polynomsec, emptypoly, p3, peq1, p5;

    @BeforeEach
    void setUp() {
        polynom = new Polynomial(1.5, 2.0, 3.0);
        polynomsec = new Polynomial(0,0,1.1,3,4);
        p3 = new Polynomial(1, -2, -3);
        emptypoly = new Polynomial();
        peq1 = new Polynomial(1.5, 2.0, 3.0);
        p5 = new Polynomial(0,1.0,-9.9,3.7,6.9,-8.43);
    }

    @Test
    @DisplayName("copyList: копия независима от оригинала")
    void copyList_createsIndependentCopy() {
        Map<Integer, Double> original = polynom.copyList(polynom.coeffs);
        polynom.coeffs.put(0, 999.0);// изменяем оригинал

        assertEquals(1.5, original.get(0), 1e-9,
                "Копия должна быть независимой от оригинала");
    }

    @Test
    @DisplayName("copyList: возвращает отсортированный TreeMap")
    void copyList_returnsSortedMap() {

        Map<Integer, Double> copy = polynom.copyList(polynom.coeffs);

        Integer[] expectedKeys = {0, 1, 2};
        Integer[] actualKeys = copy.keySet().toArray(new Integer[0]);

        assertArrayEquals(expectedKeys, actualKeys,
                "copyList должен возвращать TreeMap (отсортированный)");

        assertTrue(copy instanceof TreeMap,
                "copyList должен возвращать экземпляр TreeMap");
    }

    @Test
    void testToString() {
        String resultSec = polynomsec.toString();
        System.out.println("polynom with 0: " + resultSec);
        assertNotNull(resultSec, "не должен возвращать null");
        assertFalse(resultSec.isEmpty(), "не должен возвращать пустую строку");
        assertTrue(resultSec.contains("1.1"), "Должен содержать коэффициент 1.1");

        String resultEmpty = emptypoly.toString();
        System.out.println("empty polynomial: " + resultEmpty);
        assertNotNull(resultEmpty, "не должен возвращать null");
        assertFalse(resultEmpty.isEmpty(), "не должен возвращать пустую строку");

        String resultP3 = p3.toString();
        System.out.println("pol with - coeffs: " + resultP3);
        assertNotNull(resultP3, "не должен возвращать null");
        assertFalse(resultP3.isEmpty(), "не должен возвращать пустую строку");
        assertTrue(resultP3.contains("- 2"), "Должен содержать коэффициент - 2");
        assertTrue(resultP3.contains("2.0x"), "Должен содержать коэффициент 2.0x");
    }

    @Test
    void testEquals() {
        Polynomial r = null;
        assertTrue(peq1.equals(polynom), "Одинаковые полиномы ");
        assertFalse(peq1.equals(polynomsec), "Разные полиномы ");
        assertFalse(p3.equals(emptypoly), "Непустой полином ");
        assertFalse(p3.equals(r), "Полином null");
    }

    @Test
    void testHashCode() {
        var hashpolynom = polynom.hashCode();
        var hashpeq = peq1.hashCode();
        var hashp3 = p3.hashCode();
        var hashempty = emptypoly.hashCode();
        assertEquals(polynom.hashCode(), peq1.hashCode());
        assertEquals(polynom.hashCode(), polynom.hashCode());

        Polynomial anotherEmpty = new Polynomial();
        assertEquals(emptypoly.hashCode(), anotherEmpty.hashCode());

    }

    @Test
    void getPower() {
        assertEquals(emptypoly.getPower(), 0);
        assertEquals(peq1.getPower(),2);
        //assertEquals(polynom.getPower(),2);

        Polynomial pwithzeropower = new Polynomial(1.98);
        assertEquals(pwithzeropower.getPower(), 0, "тут");

        Polynomial pwithonepower = new Polynomial(0, 4.5);
        assertEquals(pwithonepower.getPower(), 1);

        assertEquals(p5.getPower(),5, "большой полином");

    }

    @Test
    void plus() {
        Polynomial testp0 = new Polynomial(3.0, 4.0, 6.0);
        Polynomial testp1 = new Polynomial(1.5, 2.0, 4.1, 3, 4);
        Polynomial testp2 = new Polynomial(1, -1.0, -12.9,3.7,6.9,-8.43);
//        polynom = new Polynomial(1.5, 2.0, 3.0);
//        polynomsec = new Polynomial(0,0,1.1,3,4);
//        p3 = new Polynomial(1, -2, -3);
//        emptypoly = new Polynomial();
//        peq1 = new Polynomial(1.5, 2.0, 3.0);
//        p5 = new Polynomial(0,1.0,-9.9,3.7,6.9,-8.43);
        assertEquals(polynom.plus(peq1), testp0, "сложение двух одинаковых полиномов");
        assertEquals(polynom.plus(polynomsec),testp1, "сложение с разными степенями с 0 коэфф-ами вначале");
        assertEquals(p5.plus(p3), testp2, "сложение полиномов с разными степенями и отриц.коэфф-ами");
        assertEquals(p5.plus(emptypoly), p5, "сложение с пустым полиномом");

    }

    @Test
    void minus() {
        Polynomial testp0 = new Polynomial(0);
        Polynomial testp1 = new Polynomial(-1.0, 3.0, -6.9, 3.7, 6.9, -8.43);
        Polynomial testp2 = new Polynomial(-1.5, -2.0 ,-3.0);

//        polynom = new Polynomial(1.5, 2.0, 3.0);
//        polynomsec = new Polynomial(0,0,1.1,3,4);
//        p3 = new Polynomial(1, -2, -3);
//        emptypoly = new Polynomial();
//        peq1 = new Polynomial(1.5, 2.0, 3.0);
//        p5 = new Polynomial(0,1.0,-9.9,3.7,6.9,-8.43);

        assertEquals(polynom.minus(peq1), testp0, "вычитаение одинаковых полиномов");
        assertEquals(testp1, p5.minus(p3), "полиномы с разными степенями и отриц коэфф-ами");
        assertEquals(emptypoly.minus(polynom), testp2, "вычитание из пустого полинома непустого");
    }

    //умножение полнима на число
    @Test
    void times() {
        Polynomial testp0 = new Polynomial(3.0, 4.0, 6.0);
        Polynomial testp1 = new Polynomial(0);
        Polynomial testp2 = new Polynomial(-1, 2, 3);

//        polynom = new Polynomial(1.5, 2.0, 3.0);
//        polynomsec = new Polynomial(0,0,1.1,3,4);
//        p3 = new Polynomial(1, -2, -3);
//        emptypoly = new Polynomial();
//        peq1 = new Polynomial(1.5, 2.0, 3.0);
//        p5 = new Polynomial(0,1.0,-9.9,3.7,6.9,-8.43);

        assertEquals(polynom.times(2.0), testp0, "умножение полинома на число");
        assertEquals(emptypoly.times(-4.0), testp1,"умножение пустого полинома на число" );
        assertEquals(p3.times(-1.0), testp2, "умножение полинома с отриц.коэфф-ами");
    }

    //умножение полинома на полином
    @Test
    void testTimes() {
        Polynomial testp0 = new Polynomial(2.25, 6.0, 13.0, 12.0, 9.0);
        Polynomial testp1 = new Polynomial(0);
        Polynomial testp2 = new Polynomial(0, 0, 1.1, 0.8, -5.3, -17.0, -12.0);

//        polynom = new Polynomial(1.5, 2.0, 3.0);
//        polynomsec = new Polynomial(0,0,1.1,3,4);
//        p3 = new Polynomial(1, -2, -3);
//        emptypoly = new Polynomial();
//        peq1 = new Polynomial(1.5, 2.0, 3.0);
//        p5 = new Polynomial(0,1.0,-9.9,3.7,6.9,-8.43);

        assertEquals(polynom.times(peq1), testp0, "умножение двух одинаковых полиномов");
        assertEquals(p5.times(emptypoly), testp1, "умножение полинома на пустой полином");
        assertEquals(polynomsec.times(p3), testp2, "умножение полиномов с разными степенями и отриц.коэфф-ами");
    }

    @Test
    void div() {
        Polynomial testp0 = new Polynomial(1.5, 2.0, 3.0);
        Polynomial testp1 = new Polynomial(0);
        Polynomial testp2 = new Polynomial(0, 0.5, -4.95, 1.85, 3.45, -4.215);
        Polynomial testp3 = new Polynomial(-1, 2, 3);
//        polynom = new Polynomial(1.5, 2.0, 3.0);
//        polynomsec = new Polynomial(0,0,1.1,3,4);
//        p3 = new Polynomial(1, -2, -3);
//        emptypoly = new Polynomial();
//        peq1 = new Polynomial(1.5, 2.0, 3.0);
//        p5 = new Polynomial(0,1.0,-9.9,3.7,6.9,-8.43);
        assertEquals(polynom.div(1), testp0, "деление полинома на 1");
        assertEquals(emptypoly.div(4.0), testp1, "деление пустого полинома на число");
        assertEquals(p5.div(2.0), testp2);
        assertEquals(p3.div(-1), testp3, "деление на отриц.число");

    }

    @Test
    void calc() {
        assertEquals(25.25, polynom.calc(2.5), "вычисление полинома в точке");
        assertEquals(0, emptypoly.calc(3), "вычисление пустого полинома ");
        assertEquals(-64, p3.calc(-5));
    }
}