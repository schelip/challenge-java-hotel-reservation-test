import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HotelReservation hr = new HotelReservation();
        String ans;
        do {
            System.out.println("Entrada:");
            try {
                String name = hr.getCheapestHotel(scanner.nextLine());
                System.out.println("Saida:");
                System.out.println(name);
            } catch (IllegalStateException e) {
                System.out.println("Formato inválido (<Regular|Rewards>: <data1(ddMMMyyyy(EEE))>, <data2>, <data3>, ...)");
            }
            System.out.println("\nExecutar novamente? (S)");
            ans = scanner.nextLine();
        } while (Objects.equals(ans, "S") || Objects.equals(ans, "s"));
        System.out.println("Programa finalizado");
    }
}
