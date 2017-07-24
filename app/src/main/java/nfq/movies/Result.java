package nfq.movies;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result extends AppCompatActivity {

    TextView movie_name;
    String movie;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final ProgressDialog progress;
        progress = ProgressDialog.show(this, "Info",
                "Retrieving data.....", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONParser jParser = new JSONParser();
                String title = getIntent().getStringExtra("movie");
                title = title.replace(" ", "%20");
                String json = jParser.getJSONFromUrl("http://www.theimdbapi.org/api/find/movie?title="+title);
                setText(json, progress);
            }
        }).start();
    }
        //new JSONParse().execute();
    private void setText(final String value, final ProgressDialog progress){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (value.equals("null")) {
                    TextView error = (TextView)findViewById(R.id.error);
                    error.setText("No movies found");
                    progress.dismiss();
                } else {
                    JSONArray jsonarr = null;
                    try {
                        GridView grid = (GridView) findViewById(R.id.gridview);
                        jsonarr = new JSONArray(value.toString());
                        Integer counter = 0;
                        List<Map<String, String>> items = new ArrayList<>();

                        for (int i = 0; i < jsonarr.length(); i++) {
                            JSONObject jsonobj = jsonarr.getJSONObject(i);
                            String title = jsonobj.getString("title");
                            JSONObject poster = jsonobj.getJSONObject("poster");
                            String posterUrlThumb = poster.getString("thumb");
                            String posterUrlLarge = poster.getString("large");
                            String rating = jsonobj.getString("rating");
                            String imdb_id = jsonobj.getString("imdb_id");
                            if (posterUrlThumb != null && !posterUrlThumb.isEmpty() && posterUrlLarge != null && !posterUrlLarge.isEmpty()
                                    && rating != null && !rating.isEmpty()) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("title", title);
                                map.put("image", posterUrlLarge);
                                map.put("rating", rating);
                                map.put("id", imdb_id);
                                items.add(counter, map);
                                counter = counter + 1;
                                if (counter == 20) {
                                    break;
                                }
                            }
                        }
                        grid.setAdapter(new ImageAdapter(Result.this, items));
                        progress.dismiss();
                        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Intent i = new Intent(Result.this, showInner.class);
                                i.putExtra("id", String.valueOf(v.getId()));
                                startActivity(i);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<Map<String, String>> items1;

        public ImageAdapter(Context c, List<Map<String, String>> items) {
            mContext = c;
            items1 = items;
        }
        public int getCount() {
            return items1.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override


        // create a new grid item for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_grid,
                        null);
                holder.textView= (TextView) convertView.findViewById(R.id.text);
                holder.rating= (RatingBar) convertView.findViewById(R.id.ratingBar);
                holder.imageView= (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String url = null;
            url = items1.get(position).get("image");
            Picasso.with(Result.this).load(url).into(holder.imageView);
            holder.textView.setText(items1.get(position).get("title"));
            holder.rating.setRating(Float.parseFloat(items1.get(position).get("rating"))/2);
            String id = items1.get(position).get("id").replaceAll("[A-Za-z]", "");
            convertView.setId(Integer.parseInt(id));
            return convertView;
        }
        class ViewHolder {
            TextView textView;
            RatingBar rating;
            ImageView imageView;
        }
    }
}
