import java.time.LocalDate;
import java.util.Currency;

public interface PriceSchedule {
    Money getPrice(LocalDate date);
    default Money getPrice(LocalDate startingDate, LocalDate endingDate) {
        LocalDate date = startingDate;
        Money total = new Money(0, getPrice(date).getCurrency());
        while (!date.isAfter(endingDate)) {
            total = total.add(getPrice(date));
            date = date.plusDays(1);
        }
        return total;
    }
    default Money getPrice(LocalDate[] dates) {
        Money total = new Money(0, getPrice(dates[0]).getCurrency());
        for (LocalDate date : dates) total = total.add(getPrice(date));
        return total;
    }
}
