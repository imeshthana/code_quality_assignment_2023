import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TravelCostCalculator {

    static Map<String, Double> hotelMap = new HashMap<>();
    static Map<String, Double> exchangeMap = new HashMap<>();
    static Map<String, Double> flightMap = new HashMap<>();

    static void hotelRates(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String i; 

        while ((i = reader.readLine()) != null) {
            String[] p = i.split(",");
            hotelMap.put(p[0].toUpperCase(), Double.parseDouble(p[1]));
        }
    }

    static void exchangeRates(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String i;

        while ((i = reader.readLine()) != null) {
            String[] p = i.split(",");
            exchangeMap.put(p[0].toUpperCase(), Double.parseDouble(p[1]));
        }
    }

    static void flightCosts(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String i;

        while ((i = reader.readLine()) != null) {
            String[] p = i.split(",");
            flightMap.put(p[0].toUpperCase(), Double.parseDouble(p[1]));
        }
    }

    static double totalCostUsd(double flight_cost, double hotel_cost){
        return (flight_cost + hotel_cost);
    }

    static double finalPrice(double total_cost_usd, String selected_currency){
        return (total_cost_usd * exchangeMap.get(selected_currency));
    }

    static void totalHotelCost(double hotel_cost, double stay_duration){
        hotel_cost *= stay_duration;
    }

    public static void main(String[] args) {
        try {
            hotelRates("data/hotel_rates.csv");
            exchangeRates("data/exchange_rates.csv");
            flightCosts("data/flight_costs.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your destination: ");
            String destination = reader.readLine().toUpperCase();

            double flight_cost = flightMap.getOrDefault(destination, 0.0);
            double hotel_cost = hotelMap.getOrDefault(destination, 0.0);

            System.out.print("Enter your stay duration in days: ");
            int stay_duration = Integer.parseInt(reader.readLine());

            totalHotelCost(hotel_cost, stay_duration);

            double total_cost_usd = totalCostUsd(flight_cost, hotel_cost);

            System.out.printf("Flight cost: USD %.2f\n", flight_cost);
            System.out.printf("Hotel cost (%d days): USD %.2f\n", stay_duration, hotel_cost);
            System.out.printf("Total: USD %.2f\n", total_cost_usd);

            String[] available_currencies = exchangeMap.keySet().toArray(new String[0]);

            System.out.print("Select your currency for final price estimation(" + String.join(", ", available_currencies) + "): ");

            String selected_currency = reader.readLine();

            double final_price_local_currency = finalPrice(total_cost_usd, selected_currency);

            System.out.printf("Total in %s: %.2f\n", selected_currency, final_price_local_currency);
        } 

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
