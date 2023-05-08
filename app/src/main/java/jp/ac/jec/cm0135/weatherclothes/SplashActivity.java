package jp.ac.jec.cm0135.weatherclothes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {
    Calendar calendar = Calendar.getInstance();
    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // 24시간 기준 시간을 가져옵니다.
    private ImageView timeSplash;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        timeSplash = findViewById(R.id.imgSplash);

        if(hourOfDay >= 18 || hourOfDay < 6) {
            timeSplash.setImageResource(R.drawable.night);
        }else {
            timeSplash.setImageResource(R.drawable.morning);
        }

        moveMain(1);	//1초 후 main activity 로 넘어감
    }

    private void moveMain(int sec) {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);

                finish();	//현재 액티비티 종료
            }
        }, 1000 * sec); // sec초 정도 딜레이를 준 후 시작
    }
}
