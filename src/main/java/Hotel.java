import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class Hotel {
    private String name;
    private int rank;
    private PriceSchedule regularSchedule;
    private PriceSchedule rewardsSchedule;

    public Hotel(String name, int rank, PriceSchedule regularSchedule, PriceSchedule rewardsSchedule) {
        this.name = name;
        this.rank = rank;
        this.regularSchedule = regularSchedule;
        this.rewardsSchedule = rewardsSchedule;
    }

    public static Hotel withWeekendSchedule(String name, int rank, long regularWeekday, long regularWeekend, long rewardsWeekday, long rewardsWeekend) {
        return new Hotel(name, rank,
                new WeekendPriceSchedule(regularWeekday, regularWeekend),
                new WeekendPriceSchedule(rewardsWeekday, rewardsWeekend));
    }

    public static Hotel withWeekendSchedule(String name, int rank, double regularWeekday, double regularWeekend, double rewardsWeekday, double rewardsWeekend) {
        return new Hotel(name, rank,
                new WeekendPriceSchedule(regularWeekday, regularWeekend),
                new WeekendPriceSchedule(rewardsWeekday, rewardsWeekend));
    }

    /**
     * TODO Generalize client type by changing rewards from boolean to enum
     */
    public Money getPrice(boolean rewards, List<LocalDate> dates) {
        PriceSchedule schedule = rewards ? rewardsSchedule : regularSchedule;
        Money total = new Money(0, schedule.getPrice(dates.get(0)).getCurrency());
        for (LocalDate date : dates) total = total.add(schedule.getPrice(date));
        return total;
    }

    public int comparePrice(Hotel other, boolean rewards, List<LocalDate> dates) {
        int priceComparison = getPrice(rewards, dates).compareTo(other.getPrice(rewards, dates));
        return priceComparison == 0 ? -1 * Integer.compare(rank, other.getRank()) : priceComparison;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public PriceSchedule getRegularSchedule() {
        return regularSchedule;
    }

    public void setRegularSchedule(PriceSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
    }

    public PriceSchedule getRewardsSchedule() {
        return rewardsSchedule;
    }

    public void setRewardsSchedule(PriceSchedule rewardsSchedule) {
        this.rewardsSchedule = rewardsSchedule;
    }
}
