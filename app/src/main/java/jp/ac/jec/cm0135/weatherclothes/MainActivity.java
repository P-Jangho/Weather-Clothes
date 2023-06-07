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
    ImageView img1;
    ImageView img2;
    ImageView img3;
    private String city;
    private String description;
    private String cityName = "";
    public static int celsius;
    private int celsiusMax;
    private int celsiusMin;
    public static int celsiusFeel;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;

    private static final String API_KEY = BuildConfig.serverKey;
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";
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

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        img1 = findViewById(R.id.icon1);
        img2 = findViewById(R.id.icon2);
        img3 = findViewById(R.id.icon3);

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
                fetchWeatherForecast();
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
                fetchWeatherForecast();
            }
        });
    }

    protected void weatherStatus(String description, int i) {
        if(description.contains("02")) {
            if(i == 0) {
                imageView.setImageResource(R.drawable.cloud);
            }else if(i == 1) {
                img1.setImageResource(R.drawable.cloud);
            }else if(i == 2) {
                img2.setImageResource(R.drawable.cloud);
            }else {
                img3.setImageResource(R.drawable.cloud);
            }
        }else if(description.contains("03") || description.contains("04")) {
            if(i == 0) {
                imageView.setImageResource(R.drawable.cloudy);
                dayCloudy(description);
            }else if(i == 1) {
                img1.setImageResource(R.drawable.cloudy);
            }else if(i == 2) {
                img2.setImageResource(R.drawable.cloudy);
            }else {
                img3.setImageResource(R.drawable.cloudy);
            }
        }else if(description.contains("09") || description.contains("10")) {
            if(i == 0) {
                imageView.setImageResource(R.drawable.rain);
                dayCloudy(description);
            }else if(i == 1) {
                img1.setImageResource(R.drawable.rain);
            }else if(i == 2) {
                img2.setImageResource(R.drawable.rain);
            }else {
                img3.setImageResource(R.drawable.rain);
            }
        }else if(description.contains("11")) {
            if(i == 0) {
                imageView.setImageResource(R.drawable.lightning);
                dayCloudy(description);
            }else if(i == 1) {
                img1.setImageResource(R.drawable.lightning);
            }else if(i == 2) {
                img2.setImageResource(R.drawable.lightning);
            }else {
                img3.setImageResource(R.drawable.lightning);
            }
        }else if(description.contains("13")) {
            if(i == 0) {
                imageView.setImageResource(R.drawable.snow);
            }else if(i == 1) {
                img1.setImageResource(R.drawable.snow);
            }else if(i == 2) {
                img2.setImageResource(R.drawable.snow);
            }else {
                img3.setImageResource(R.drawable.snow);
            }
        }else if(description.contains("50")){
            if(i == 0) {
                imageView.setImageResource(R.drawable.fog);
            }else if(i == 1) {
                img1.setImageResource(R.drawable.fog);
            }else if(i == 2) {
                img2.setImageResource(R.drawable.fog);
            }else {
                img3.setImageResource(R.drawable.fog);
            }
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
                    description = weatherObject.getString("icon");
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
                        weatherStatus(description, 0);
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
                        weatherStatus(description, 0);
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

    protected void fetchWeatherForecast() {
        String url = FORECAST_BASE_URL + "?q=" + city + "&appid=" + API_KEY;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // 3일간의 날씨 예보 정보를 가져오는 로직 추가
                    JSONArray forecastList = jsonObject.getJSONArray("list");

                    // 첫 번째 예보 정보 (현재 시간 기준으로 가까운 예보)
                    JSONObject forecast1 = forecastList.getJSONObject(8);
                    double temp1 = forecast1.getJSONObject("main").getDouble("temp");

                    JSONArray weatherArray1 = forecast1.getJSONArray("weather");
                    JSONObject weatherObject1 = weatherArray1.getJSONObject(0);
                    String iconCode1 = weatherObject1.getString("icon");
                    weatherStatus(iconCode1, 1);

                    // 날짜 정보
                    String dateTime1 = forecast1.getString("dt_txt");
                    String date1 = dateTime1.split(" ")[0];

                    // 두 번째 예보 정보
                    JSONObject forecast2 = forecastList.getJSONObject(16); // 3시간 단위로 예보 정보가 제공되므로 8번째 예보를 가져옴
                    double temp2 = forecast2.getJSONObject("main").getDouble("temp");

                    JSONArray weatherArray2 = forecast2.getJSONArray("weather");
                    JSONObject weatherObject2 = weatherArray2.getJSONObject(0);
                    String iconCode2 = weatherObject2.getString("icon");
                    weatherStatus(iconCode2, 2);

                    // 날짜 정보
                    String dateTime2 = forecast2.getString("dt_txt");
                    String date2 = dateTime2.split(" ")[0];

                    // 세 번째 예보 정보
                    JSONObject forecast3 = forecastList.getJSONObject(24); // 3시간 단위로 예보 정보가 제공되므로 16번째 예보를 가져옴
                    double temp3 = forecast3.getJSONObject("main").getDouble("temp");

                    JSONArray weatherArray3 = forecast3.getJSONArray("weather");
                    JSONObject weatherObject3 = weatherArray3.getJSONObject(0);
                    String iconCode3 = weatherObject3.getString("icon");
                    weatherStatus(iconCode3, 3);

                    // 날짜 정보
                    String dateTime3 = forecast3.getString("dt_txt");
                    String date3 = dateTime3.split(" ")[0];

                    // 추출한 정보를 활용하여 3일간의 날씨 예보를 표시하는 로직 추가
                    txt1.setText(date1 + "\n" + (int) (temp1 - 273.15) + " ℃");
                    txt2.setText(date2 + "\n" + (int) (temp2 - 273.15) + " ℃");
                    txt3.setText(date3 + "\n" + (int) (temp3 - 273.15) + " ℃");
                    int textColor = description.contains("d") ? Color.BLACK : Color.WHITE;
                    txt1.setTextColor(textColor);
                    txt2.setTextColor(textColor);
                    txt3.setTextColor(textColor);

                    img1.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
                    img2.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
                    img3.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);

                    Log.d("Forecast", "Day 1: " + temp1 + " K");
                    Log.d("Forecast", "Day 2: " + temp2 + " K");
                    Log.d("Forecast", "Day 3: " + temp3 + " K");

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
