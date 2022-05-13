import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoneyTest {
    @Test
    public void testMoney() {
        Money m1 = Money.reais(10.12745);
        Money m2 = Money.reais(200);
        assertEquals(m1.toString(), "R$10.13");
        assertEquals(m2.toString(), "R$200.00");
    }

    @Test
    public void testSum() {
        Money m1 = Money.reais(10.12745);
        Money m2 = Money.reais(200);
        assertEquals(m1.add(m2).toString(), "R$210.13");
    }

    @Test
    public void testSubtraction() {
        Money m1 = Money.reais(10.12745);
        Money m2 = Money.reais(200);
        assertEquals(m1.subtract(m2).toString(), "R$-189.87");

    }

    @Test
    public void testMultiplication() {
        Money m = Money.reais(200);
        assertEquals(m.multiply(10/9d).toString(), "R$222.22");
        assertEquals(m.multiply(4/3d).toString(), "R$266.67");
    }

    @Test
    public void testDivision() {
        Money m = Money.reais(200);
        assertEquals(m.divide(9).toString(), "R$22.22");
        assertEquals(m.divide(7).toString(), "R$28.57");
    }

    @Test
    public void testAllocation() {
        Money m = Money.reais(200);
        Money[] nAll = m.allocate(3);
        assertEquals(nAll[0].toString(), "R$66.66");
        assertEquals(nAll[1].toString(), "R$66.66");
        assertEquals(nAll[2].toString(), "R$66.67");
        Money[] nProp = m.allocate(new long[]{3, 3, 7});
        assertEquals(nProp[0].toString(), "R$46.16");
        assertEquals(nProp[1].toString(), "R$46.15");
        assertEquals(nProp[2].toString(), "R$107.69");
    }
}
