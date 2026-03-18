package service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AIService {

    private static final String API_KEY = "AIzaSyA-fwTGlJdS1wd9Y7S11Mkj85ZvChlwLVE";

    public static String predictMarketType(String marketName) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

        String jsonBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "Identify the store category for '%s' in exactly one word (e.g., Food, Home, Electronics, Pharmacy, Fuel, Clothes, Entertainment, Books, Travel, Sports) and be consistent, if market type is Food dont answer as Grocery. No prose."
                }
              ]
            }
          ]
        }
        """.formatted(marketName);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
                System.out.println("Response: " + body);
                return "Unknown";
            }

            String[] parts = body.split("\"text\": \"");
            if (parts.length < 2) return "Parse Error";

            String result = parts[1].split("\"")[0];
            String cleanResult = result.replace("\\n", "").trim().split("\\s+")[0];

            System.out.println("AI Prediction market type: " + cleanResult);
            return cleanResult.replaceAll("[^a-zA-Z]", "");

        } catch (IOException | InterruptedException e) {
            return "Error: " + e.getMessage();
        }
    }

}
