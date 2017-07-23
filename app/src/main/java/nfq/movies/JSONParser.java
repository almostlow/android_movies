package nfq.movies;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public String getJSONFromUrl(String Stringurl) {

        try {
            URL oracle = new URL(Stringurl); // URL to Parse
            URLConnection is = oracle.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;
    }
}