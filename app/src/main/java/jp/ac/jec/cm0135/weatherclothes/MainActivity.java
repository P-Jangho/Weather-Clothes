package jp.ac.jec.cm0135.weatherclothes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    private TextView txtCityName;
    private TextView temperatureMax;
    private TextView temperatureMin;
    private Button btnSearch;
    private Button btnClothes;
    private Button btnSave;
    private LinearLayout linearLayout;
    ImageView imageView;
    private String city;
    private String cityName = "";
    public static int celsius;
    private int celsiusMax;
    private int celsiusMin;
    public static int celsiusFeel;

    private static final String API_KEY = BuildConfig.serverKey;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCity = findViewById(R.id.editTextCity);
        txtTemperature = findViewById(R.id.textViewTemperature);
        btnSearch = findViewById(R.id.buttonSearch);
        linearLayout = findViewById(R.id.relativeLayout);
        btnSave = findViewById(R.id.textCitySave);
        txtCityName = findViewById(R.id.cityName);
        temperatureMax = findViewById(R.id.temperatureMax);
        temperatureMin = findViewById(R.id.temperatureMin);
        imageView = findViewById(R.id.sunIcon);
        btnClothes = findViewById(R.id.buttonClothes);

        SharedPreferences sp = getSharedPreferences("WeatherClothes", Context.MODE_PRIVATE);
        SharedPreferences.Editor edtr = sp.edit();
        String y = sp.getString("NAME", "");
        btnSave.setText(y);

        btnClothesStatus();
        btnSaveStatus(y);

        Drawable drawable = imageView.getDrawable();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼클릭시 자판다운
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);

                city = editCity.getText().toString();

                weatherStatus();
            }
        });

        btnClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtr.putString("NAME", cityName);
                edtr.apply();

                String x = sp.getString("NAME", "");
                btnSave.setText(x);
                btnSaveStatus(x);

                Intent intent = new Intent(MainActivity.this, ClothesActivity.class);
                intent.putExtra("temperature", celsius);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = btnSave.getText().toString();

                weatherStatus();
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
    protected void btnClothesStatus() {
        if (cityName.equals("")) {
            btnClothes.setEnabled(false);
        } else {
            btnClothes.setEnabled(true);
        }
    }
    protected void btnSaveStatus(String citySave) {
        if (citySave.equals("")) {
            btnSave.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
        }
    }
    protected void weatherStatus() {
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
                    double tempFeel = main.getDouble("feels_like");

                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("icon");
                    cityName = jsonObject.getString("name");

                    btnClothesStatus();

                    celsius = (int) (temperature - 273.15);
                    celsiusMax = (int) (tempMax - 273.15);
                    celsiusMin = (int) (tempMin - 273.15);
                    celsiusFeel = (int) (tempFeel - 273.15);
                    txtTemperature.setText(celsius + " ℃");
                    temperatureMax.setText("↑" + celsiusMax);
                    temperatureMin.setText("↓" + celsiusMin);
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
            }
        });
        queue.add(stringRequest);
    }
}
