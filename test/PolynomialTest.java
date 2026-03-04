import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    private Polynomial polynom;

    @BeforeEach
    void setUp() {
        polynom = new Polynomial(1.5, 2.0, 3.0);
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

//        assertTrue(copy instanceof TreeMap,
//                "copyList должен возвращать экземпляр TreeMap");
    }

    @Test
    void testToString() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void getPower() {
    }

    @Test
    void plus() {
    }

    @Test
    void minus() {
    }

    @Test
    void times() {
    }

    @Test
    void testTimes() {
    }

    @Test
    void div() {
    }

    @Test
    void calc() {
    }
}