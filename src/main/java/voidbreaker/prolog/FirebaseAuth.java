package voidbreaker.prolog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FirebaseAuth {
    // Replace with your Firebase Realtime Database URL
    private static final String DATABASE_URL = "https://prolog-void-default-rtdb.firebaseio.com/users.json";

    public static boolean checkLogin(String username, String password) {
        try {
            // Make GET request to Firebase
            HttpURLConnection conn = (HttpURLConnection) new URL(DATABASE_URL).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String json = reader.lines().reduce("", (a, b) -> a + b);
            reader.close();

            JSONObject users = new JSONObject(json);
            if (users.has(username)) {
                String storedPassword = users.getJSONObject(username).getString("password");
                return storedPassword.equals(password);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
