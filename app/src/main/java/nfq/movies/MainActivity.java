package nfq.movies;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText movie_name_input = (EditText)findViewById(R.id.movie_name);
        String hint = getResources().getString(R.string.main_hint);
        movie_name_input.setHint(hint);
        Button search_btn = (Button)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final EditText movie_name_input = (EditText)findViewById(R.id.movie_name);
        final String movie_name = movie_name_input.getText().toString();
        if (isNetworkAvailable()) {
            if (movie_name.length() > 2) {
                new Thread(new Runnable() {
                    public void run() {
                        Intent i = new Intent(MainActivity.this, Result.class);
                        i.putExtra("movie", movie_name);
                        startActivity(i);
                    }
                }).start();
            } else {
                Toast.makeText(MainActivity.this, "You should type more than 2 symbols", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
