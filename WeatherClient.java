import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

public class WeatherClient {
    private static final String API_KEY = "8566644ccdae9157653e5a09470214c4"; // Replace with your OpenWeatherMap API
                                                                              // key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();
        scanner.close();

        fetchWeatherData(city);
    }

    private static void fetchWeatherData(String city) {
        try {
            String urlString = BASE_URL + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                parseWeatherData(response.toString());
            } else {
                System.out.println("❌ Failed to fetch weather data. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void parseWeatherData(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        String cityName = jsonObject.getString("name");
        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
        String weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        System.out.println("\n🌤️ Weather Report for " + cityName);
        System.out.println("🌡️ Temperature: " + temperature + "°C");
        System.out.println("🌦️ Condition: " + weatherDescription);
    }
}
