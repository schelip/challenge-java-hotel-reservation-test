import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Currency;

public class WeekendPriceSchedule implements PriceSchedule {
    private Money weekdayPrice;
    private Money weekendPrice;

    public WeekendPriceSchedule(Money weekdayPrice, Money weekendPrice) {
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    public WeekendPriceSchedule(long weekdayPrice, long weekendPrice) {
        this(new Money(weekdayPrice, Currency.getInstance("BRL")),
                new Money(weekendPrice, Currency.getInstance("BRL")));
    }

    public WeekendPriceSchedule(long weekdayPrice, long weekendPrice, Currency currency) {
        this(new Money(weekdayPrice, currency),
                new Money(weekendPrice, currency));
    }

    public WeekendPriceSchedule(double weekdayPrice, double weekendPrice) {
        this(new Money(weekdayPrice, Currency.getInstance("BRL")),
                new Money(weekendPrice, Currency.getInstance("BRL")));
    }

    public WeekendPriceSchedule(double weekdayPrice, double weekendPrice, Currency currency) {
        this(new Money(weekdayPrice, currency),
                new Money(weekendPrice, currency));
    }

    private static boolean isWeekend(LocalDate date) {
        DayOfWeek day = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    @Override
    public Money getPrice(LocalDate date) {
        return isWeekend(date) ? weekendPrice : weekdayPrice;
    }

    public Money getWeekdayPrice() {
        return weekdayPrice;
    }

    public void setWeekdayPrice(Money weekdayPrice) {
        this.weekdayPrice = weekdayPrice;
    }

    public Money getWeekendPrice() {
        return weekendPrice;
    }

    public void setWeekendPrice(Money weekendPrice) {
        this.weekendPrice = weekendPrice;
    }
}
