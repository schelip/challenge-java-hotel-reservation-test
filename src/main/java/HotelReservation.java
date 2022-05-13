import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotelReservation {
    private final List<Hotel> hotels = new ArrayList<>(List.of(
            Hotel.withWeekendSchedule("Lakewood", 3,110, 90,80, 80),
            Hotel.withWeekendSchedule("Bridgewood", 4,160, 60, 110, 50),
            Hotel.withWeekendSchedule("Ridgewood", 5, 220, 150, 100, 40)
    ));

    public List<Hotel> getHotels() {
        return List.copyOf(hotels);
    }

    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
    }

    public void removeHotel(Hotel hotel) {
        hotels.remove(hotel);
    }

    public String getCheapestHotel (String input) throws IllegalStateException {
        String rewardsString = input.split(":", 2)[0];
        boolean rewards = Objects.equals(input.split(":")[0], "Rewards");
        if (!rewards && !Objects.equals(input.split(":")[0], "Regular")) throw new IllegalStateException();

        Pattern datePattern = Pattern.compile("([0-9]{2})([A-z]{3,4})([0-9]{4})\\([a-z]{3,4}\\)");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
        List<LocalDate> dates = new ArrayList<>();
        Matcher matcher = datePattern.matcher(input);
        while (matcher.find())
            dates.add(LocalDate.parse(matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3), formatter));

        return hotels.stream().min(
                (o1, o2) -> o1.comparePrice(o2, rewards, dates)
        ).orElseThrow().getName();
    }
}
