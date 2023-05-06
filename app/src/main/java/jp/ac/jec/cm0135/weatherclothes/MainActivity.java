package jp.ac.jec.cm0135.weatherclothes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText editCity;
    private TextView txtTemperature;
    private TextView txtDescription;
    private Button buttonSearch;
    private LinearLayout linearLayout;

    private static final String API_KEY = "cb61ebe4ad446ac4a81c8bbe5986c9fe";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editCity = findViewById(R.id.editTextCity);
        txtTemperature = findViewById(R.id.textViewTemperature);
        buttonSearch = findViewById(R.id.buttonSearch);
        linearLayout = findViewById(R.id.relativeLayout);
        txtDescription = findViewById(R.id.textDescri);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editCity.getText().toString();
                String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY;

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject main = jsonObject.getJSONObject("main");
                            double temperature = main.getDouble("temp");

                            JSONArray weatherArray = jsonObject.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String description = weatherObject.getString("icon");
                            int celsius = (int) (temperature - 273.15);
                            txtTemperature.setText(celsius + "℃");
                            txtDescription.setText(description);

                            if (description.contains("d")) {
                                 //그라데이션배경설정
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("#0033FF"), Color.parseColor("#FFFFFF")}
                                );
                                linearLayout.setBackground(gradientDrawable);
//                                linearLayout.setBackgroundResource(R.drawable.afternoon);
                            } else {
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("#182848"), Color.parseColor("#4B6CB7")}
                                );
                                linearLayout.setBackground(gradientDrawable);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: City not found", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", error.toString());
                        Log.i("aaa", "aaa");
                    }
                });

                queue.add(stringRequest);
            }
        });
    }
}
