import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchPodcastTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "FetchPodcastTask";
    private static final String API_KEY = "69a27af18f0848d6968210073133948e";

    protected JSONObject doInBackground(String... params) {
        String podcastId = params[0];
        JSONObject result = null;

        try {
            URL url = new URL("https://listen-api.listennotes.com/api/v2/podcasts/" + podcastId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-ListenAPI-Key", API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            result = new JSONObject(response.toString());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error fetching podcast: " + e.getMessage());
        }

        return result;
    }

    protected void onPostExecute(JSONObject result) {
        if (result != null) {
            try {
                String title = result.getString("title");
                String publisher = result.getString("publisher");
                int totalEpisodes = result.getInt("total_episodes");

                Log.d(TAG, "Podcast title: " + title);
                Log.d(TAG, "Podcast publisher: " + publisher);
                Log.d(TAG, "Total episodes: " + totalEpisodes);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing podcast JSON: " + e.getMessage());
            }
        }
    }
}