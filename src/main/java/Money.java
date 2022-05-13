import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Currency;
import java.util.Objects;

/**
 * Represents a monetary amount of a certain currency.
 * Follows the conventions defined in P of EEA.
 *
 * The main driver for the making of this implementation is the fact that
 * money should not ever be represented by floating point data-types due to
 * rounding errors; it should instead be of an integral (e.g. long) or fixed point
 * (e.g. BigDecimal) data-type representing the smallest complete unit of the amount (cents, normally).
 * The currency attribute and the operation methods open up to the possible future
 * development of an interface and usage of the factory pattern, allowing implementations using other data-types,
 * conversion methods, etc., in order to more easily adapt to the requirements of multi-currency and
 * transactions of high amounts, would this application ever expand.
 *
 */
public class Money {
    /**
     * The currency, not null.
     */
    private Currency currency;
    /**
     * The amount, not null.
     */
    private long amount;

    private Money() {}

    /**
     * Creates new instance of {@link Money}
     * @param amount the amount, in major complete units (e.g. 2 dollars)
     * @param currency the currency, not {@code null}
     */
    public Money(long amount, Currency currency) {
        if (currency == null) throw new NullPointerException("Currency must not be null");
        this.currency = currency;
        this.amount = amount * centFactor();
    }

    /**
     * Creates new instance of {@link Money}
     * @param amount the amount, in major units (e.g. 2.50 dollars)
     * @param currency the currency, not {@code null}
     */
    public Money(double amount, Currency currency) {
        if (currency == null) throw new NullPointerException("Currency must not be null");
        this.currency = currency;
        this.amount = Math.round(amount * centFactor());
    }

    private static final int[] cents = { 1, 10, 100, 1000 };
    /**
     * Used to convert a decimal major unit to complete minor units.
     * @return How many minor units make a major unit
     */
    private int centFactor() {
        return cents[currency.getDefaultFractionDigits()];
    }

    /**
     * Factory method to create new {@link Money} without the amount conversion
     * @param amount the amount, in minor units (e.g. cents)
     * @return new {@link Money} with the amount
     */
    private Money newMoney(long amount) {
        Money money = new Money();
        money.currency = currency;
        money.amount = amount;
        return money;
    }

    /**
     * Factory method to create new {@link Money} from a {@link BigDecimal} amount
     * @param amount the amount, in major units (e.g. 2.50 dollars)
     * @return new {@link Money} with the amount
     */
    private Money newMoney(BigDecimal amount, RoundingMode roundingMode) {
        assert amount != null : "Bug: null amount";
        assert roundingMode != null : "Bug: null rounding mode";
        Money money = new Money();
        money.currency = currency;
        money.amount = amount.setScale(currency.getDefaultFractionDigits(), roundingMode).multiply(BigDecimal.valueOf(centFactor())).longValue();
        return money;
    }

    /**
     * Static factory method to create new {@link Money} with preset currency "BRL"
     * @param amount the amount, in major units (e.g. 2.50 dollars)
     * @return a new {@link Money} with BRL currency
     */
    public static Money reais(double amount) {
        return new Money(amount, Currency.getInstance("BRL"));
    }

    /**
     * Checks if the currency is the same as another {@link Money}
     * @param o the other {@link Money} object
     */
    private void assertSameCurrencyAs(Money o) {
        if (currency != o.currency) {
            throw new IllegalArgumentException("Both Money objects must have the same currency");
        }
    }

    /**
     * Adds the amount with another {@link Money}'s
     * @param o the other {@link Money}
     * @return new {@link Money} containing the total amount
     */
    public Money add(Money o) {
        if (o == null) return this;
        assertSameCurrencyAs(o);
        return newMoney(amount + o.amount);
    }

    /**
     * Subtracts the amount of another {@link Money}
     * @param o the other {@link Money}
     * @return new {@link Money} containing the total amount
     */
    public Money subtract(Money o) {
        if (o == null) return this;
        assertSameCurrencyAs(o);
        return newMoney(amount - o.amount);
    }

    /**
     * Multiplies the amount by a scalar value
     * @param factor the factor to multiply by
     * @return new {@link Money} containing the total amount
     */
    public Money multiply(long factor) {
        if (factor == 1L) return this;
        return multiply(BigDecimal.valueOf(factor));
    }

    /**
     * Multiplies the amount by a scalar value
     * @param factor the factor to multiply by
     * @return new {@link Money} containing the total amount
     */
    public Money multiply(double factor) {
        if (factor == 1.0d) return this;
        return multiply(BigDecimal.valueOf(factor));
    }

    /**
     * Multiplies the amount by a scalar value
     * @param factor the factor to multiply by, not {@code null}
     * @return new {@link Money} containing the total amount
     */
    public Money multiply(BigDecimal factor) {
        if (factor == null) throw new NullPointerException("Amount should not be null");
        if (Objects.equals(factor, BigDecimal.ONE)) return this;
        return multiply(factor, RoundingMode.HALF_EVEN);
    }

    /**
     * Multiplies the amount by a scalar value
     * @param factor the factor to multiply by, not {@code null}
     * @param roundingMode the rounding mode for the multiplication, not {@code null}
     * @return new {@link Money} containing the total amount
     */
    public Money multiply(BigDecimal factor, RoundingMode roundingMode) {
        if (factor == null) throw new NullPointerException("Amount should not be null");
        if (roundingMode == null) throw new NullPointerException("Rouding mode should not be null");
        if (Objects.equals(factor, BigDecimal.ONE)) return this;
        return newMoney(getAmount().multiply(factor), roundingMode);
    }

    /**
     * Divides the amount by a scalar value. Should not be used to distribute the amount, instead use {@link Money#allocate(int)}
     * @param factor the factor to divide by, not {@code 0}
     * @return new {@link Money} containing the total amount
     */
    public Money divide(long factor) {
        if (factor == 1L) return this;
        if (factor == 0) throw new IllegalArgumentException("Divisor must not be 0");
        return divide(BigDecimal.valueOf(factor));
    }

    /**
     * Divides the amount by a scalar value. Should not be used to distribute the amount, instead use {@link Money#allocate(int)}
     * @param factor the factor to divide by, not {@code 0}
     * @return new {@link Money} containing the total amount
     */
    public Money divide(double factor) {
        if (factor == 1.0d) return this;
        if (factor == 0) throw new IllegalArgumentException("Divisor must not be 0");
        return divide(BigDecimal.valueOf(factor));
    }

    /**
     * Divides the amount by a scalar value. Should not be used to distribute the amount, instead use {@link Money#allocate(int)}
     * @param factor the factor to divide by, not {@link BigDecimal#ZERO} and not {@code null}
     * @return new {@link Money} containing the total amount
     */
    public Money divide(BigDecimal factor) {
        if (Objects.equals(factor, BigDecimal.ONE)) return this;
        if (Objects.equals(factor, BigDecimal.ZERO)) throw new IllegalArgumentException("Divisor must not be 0");
        return divide(factor, RoundingMode.HALF_EVEN);
    }

    /**
     * Divides the amount by a scalar value. Should not be used to distribute the amount, instead use {@link Money#allocate(int)}
     * @param factor the factor to divide by, not {@link BigDecimal#ZERO} and not {@code null}
     * @param roundingMode the rounding mode used by the division, not {@code null}
     * @return new {@link Money} containing the total amount
     */
    public Money divide(BigDecimal factor, RoundingMode roundingMode) {
        if (Objects.equals(factor, BigDecimal.ONE)) return this;
        if (Objects.equals(factor, BigDecimal.ZERO)) throw new IllegalArgumentException("Divisor must not be 0");
        return newMoney(getAmount().divide(factor, roundingMode), roundingMode);
    }

    /**
     * Distributes the amount between {@code n} {@link Money} objects.
     * @param n the amount of {@link Money} objects to distribute to
     * @return the new {@link Money} objects, each with a fraction of the amount
     */
    public Money[] allocate(int n) {
        Money lowResult = newMoney(amount / n);
        Money highResult = newMoney(lowResult.amount + 1);
        Money[] results = new Money[n];
        int remainder = (int) amount % n;
        for (int i = 0; i < remainder; i++) results[i] = lowResult;
        for (int i = remainder; i < n; i++) results[i] = highResult;
        return results;
    }

    /**
     * Distributes the amount between {@link Money} objects according to a list of ratios.
     * @param ratios the list of ratios for the distribution. The object at the position {@code i}
     *               of the resulting list will have an amount with the same proportion to the total
     *               as the {@code i} value on the list has to the sum of the values.
     * @return the new {@link Money} objects, each with a proportionate fraction of the amount.
     */
    public Money[] allocate(long[] ratios) {
        long total = Arrays.stream(ratios).sum();
        long remainder = amount;
        Money[] results = new Money[ratios.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = newMoney(amount * ratios[i] / total);
            remainder -= results[i].amount;
        }
        for (int i = 0; i < remainder; i++) results[i].amount++;
        return results;
    }

    /**
     * Compares the amount to another {@link Money}
     * @param other The other {@link Money}
     * @return the value 0 if the amounts are equal; a value less than 0 if the first amount is smaller;
     * and a value greater than 0 if the second amount is smaller;
     */
    public int compareTo(Object other) {
        Money o = (Money) other;
        assertSameCurrencyAs(o);
        return Long.compare(amount, o.amount);
    }

    /**
     * Checks if the amount is bigger than another {@link Money}'s
     * @param o The other {@link Money}
     * @return True if it is bigger
     */
    public boolean greaterThan(Money o) {
        return compareTo(o) > 0;
    }

    /**
     * Getter for the amount
     * @return the amount as a {@link BigDecimal}
     */
    public BigDecimal getAmount() {
        return BigDecimal.valueOf(amount, currency.getDefaultFractionDigits());
    }

    /**
     * Getter for the currency
     * @return the currency
     */
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return Objects.equals(getAmount(), money.getAmount()) && Objects.equals(getCurrency(), money.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrency(), getAmount());
    }

    @Override
    public String toString() {
        return currency.getSymbol() + getAmount().toString();
    }
}
