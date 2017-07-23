package nfq.movies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class showInner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_inner);
        final ProgressDialog progress;
        progress = ProgressDialog.show(this, "Info",
                "Loading...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONParser jParser = new JSONParser();
                String id = getIntent().getStringExtra("id");
                String json = jParser.getJSONFromUrl("http://www.theimdbapi.org/api/movie?movie_id=tt"+id);
                setText(json, progress);
            }
        }).start();
    }
    //new JSONParse().execute();
    private void setText(final String value, final ProgressDialog progress){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonobj = null;
                try {
                    jsonobj = new JSONObject(value.toString());
                    String title = jsonobj.getString("title");
                    JSONObject poster = jsonobj.getJSONObject("poster");
                    String posterUrlThumb = poster.getString("thumb");
                    String posterUrlLarge = poster.getString("large");
                    String rating = jsonobj.getString("rating");
                    String description = jsonobj.getString("description");
                    String year = jsonobj.getString("year");
                    if(posterUrlThumb != null && !posterUrlThumb.isEmpty() && posterUrlLarge != null && !posterUrlLarge.isEmpty()
                            && rating != null && !rating.isEmpty()) {
                        ImageView imageView = (ImageView)findViewById(R.id.image);
                        Picasso.with(showInner.this).load(posterUrlLarge).into(imageView);
                        TextView textView = (TextView)findViewById(R.id.text);
                        textView.setText(title);
                        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
                        ratingBar.setRating(Float.parseFloat(rating));
                        TextView descriptionView = (TextView)findViewById(R.id.description);
                        descriptionView.setText(description);
                        TextView yearView = (TextView)findViewById(R.id.year);
                        yearView.setText(year);
                        RelativeLayout rel = (RelativeLayout)findViewById(R.id.relative);
                        rel.setVisibility(View.VISIBLE);
                    }
                    progress.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
