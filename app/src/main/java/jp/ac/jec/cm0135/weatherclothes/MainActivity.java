package jp.ac.jec.cm0135.weatherclothes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private TextView txtCityName;
    private TextView temperatureMax;
    private TextView temperatureMin;
    private Button buttonSearch;
    private LinearLayout linearLayout;
    ImageView imageView;

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
        txtCityName = findViewById(R.id.cityName);
        temperatureMax = findViewById(R.id.temperatureMax);
        temperatureMin = findViewById(R.id.temperatureMin);
        imageView = findViewById(R.id.sunIcon);

        Drawable drawable = imageView.getDrawable();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼클릭시 자판다운
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);

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
                            double tempMax = main.getDouble("temp_max");
                            double tempMin = main.getDouble("temp_min");

                            JSONArray weatherArray = jsonObject.getJSONArray("weather");
                            JSONObject weatherObject = weatherArray.getJSONObject(0);
                            String description = weatherObject.getString("icon");
                            String cityName = jsonObject.getString("name");
                            int celsius = (int) (temperature - 273.15);
                            int celsiusMax = (int) (tempMax - 273.15);
                            int celsiusMin = (int) (tempMin - 273.15);
                            txtTemperature.setText(celsius + " ℃");
                            temperatureMax.setText("↑" + celsiusMax);
                            temperatureMin.setText("↓" + celsiusMin);
                            txtDescription.setText(description);
                            txtCityName.setText(cityName);

                            if (description.contains("d")) {
                                 //그라데이션배경설정
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("#0033FF"), Color.parseColor("#FFFFFF")}
                                );
                                linearLayout.setBackground(gradientDrawable);
                                editCity.setTextColor(Color.BLACK);
                                editCity.setHintTextColor(Color.BLACK);
                                txtCityName.setTextColor(Color.BLACK);
                                txtTemperature.setTextColor(Color.BLACK);
                                temperatureMax.setTextColor(Color.BLACK);
                                temperatureMin.setTextColor(Color.BLACK);
                                imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                                imageView.setImageResource(R.drawable.sun);
                                weatherStatus(description);

//                                linearLayout.setBackgroundResource(R.drawable.afternoon);
                            } else {
                                GradientDrawable gradientDrawable = new GradientDrawable(
                                        GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{Color.parseColor("#182848"), Color.parseColor("#4B6CB7")}
                                );
                                linearLayout.setBackground(gradientDrawable);
                                editCity.setTextColor(Color.WHITE);
                                editCity.setHintTextColor(Color.WHITE);
                                txtCityName.setTextColor(Color.WHITE);
                                txtTemperature.setTextColor(Color.WHITE);
                                temperatureMax.setTextColor(Color.WHITE);
                                temperatureMin.setTextColor(Color.WHITE);
                                imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                                imageView.setImageResource(R.drawable.moon);
                                weatherStatus(description);
                            }
                            editCity.setText("");
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

    protected void weatherStatus(String description) {
        if(description.contains("02")) {
            imageView.setImageResource(R.drawable.cloud);
        }else if(description.contains("03") || description.contains("04")) {
            imageView.setImageResource(R.drawable.cloudy);
            dayCloudy(description);
        }else if(description.contains("09") || description.contains("10")) {
            imageView.setImageResource(R.drawable.rain);
            dayCloudy(description);
        }else if(description.contains("11")) {
            imageView.setImageResource(R.drawable.lightning);
            dayCloudy(description);
        }else if(description.contains("13")) {
            imageView.setImageResource(R.drawable.snow);
        }else if(description.contains("50")){
            imageView.setImageResource(R.drawable.fog);
        }else {
            return;
        }
    }

    protected void dayCloudy(String description) {
        if(description.contains("d")){
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{Color.parseColor("#2f4f4f"), Color.parseColor("#EEEEEE")}
            );
            linearLayout.setBackground(gradientDrawable);
        }
    }
}
