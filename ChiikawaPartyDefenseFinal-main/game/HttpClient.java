package game;

import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;

public class HttpClient {
    private static final String API_URL = "http://localhost/login-register/api/game_api.php";
    
    public static boolean authenticateUser(String username, String password) {
        try {
            URI uri = URI.create(API_URL);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            String jsonPayload = String.format(
                "{\"action\":\"login\",\"username\":\"%s\",\"password\":\"%s\"}", 
                username, password
            );
            
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(jsonPayload);
                writer.flush();
            }
            
            int responseCode = conn.getResponseCode();
            return responseCode == 200;
            
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Network error. Please check your internet connection.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public static void submitScore(String username, int score) {
        try {
            URI uri = URI.create(API_URL);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            String jsonPayload = String.format(
                "{\"action\":\"submit_score\",\"username\":\"%s\",\"score\":%d}", 
                username, score
            );
            
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(jsonPayload);
                writer.flush();
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Score submitted successfully!");
                JOptionPane.showMessageDialog(null, 
                    "Score saved to online leaderboard!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.err.println("Failed to submit score. Response code: " + responseCode);
            }
            
        } catch (Exception e) {
            System.err.println("Score submission error: " + e.getMessage());
            // Don't show error dialog for score submission failures
        }
    }
}