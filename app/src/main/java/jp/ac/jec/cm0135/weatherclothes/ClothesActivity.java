package jp.ac.jec.cm0135.weatherclothes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClothesActivity extends AppCompatActivity {
    private LinearLayout layout;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private ImageView image6;
    private String[] arrayStr = new String[]{"#F00", "#FF8C00", "#FFFF00", "#7CFC00", "#00BFFF", "#000080"};;
    ImageView[] images = {image1, image2, image3, image4, image5, image6};
    int[][] resources = {{R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.g1, R.drawable.g2, R.drawable.g3},
                    {R.drawable.m4, R.drawable.m5, R.drawable.m6, R.drawable.g4, R.drawable.g5, R.drawable.g6},
                    {R.drawable.m7, R.drawable.m8, R.drawable.m9, R.drawable.g7, R.drawable.g8, R.drawable.g9},
                    {R.drawable.m10, R.drawable.m11, R.drawable.m12, R.drawable.g10, R.drawable.g11, R.drawable.g12},
                    {R.drawable.m13, R.drawable.m14, R.drawable.m15, R.drawable.g13, R.drawable.g14, R.drawable.g15},
                    {R.drawable.m16, R.drawable.m17, R.drawable.m18, R.drawable.g16, R.drawable.g17, R.drawable.g18}};

    private TextView temp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        MainActivity mainActivity = new MainActivity();
        temp = findViewById(R.id.temp);
        temp.setText(mainActivity.celsius + " ℃");
        int temperature = getIntent().getIntExtra("temperature", 0);
        layout = findViewById(R.id.layout);
        int[] imageViews =
                {R.id.imageView1, R.id.imageView2, R.id.imageView3,
                R.id.imageView4, R.id.imageView5, R.id.imageView6};

        for (int i = 0; i < images.length; i++) {
            images[i] = findViewById(imageViews[i]);
        }

        if(temperature > 28) {
            background(0);
            clothes(0);
        }else if(temperature > 23) {
            background(1);
            clothes(1);
        }else if(temperature > 15) {
            background(2);
            clothes(2);
        }else if(temperature > 10) {
            background(3);
            clothes(3);
        }else if(temperature > 6) {
            background(4);
            clothes(4);
        }else {
            background(5);
            clothes(5);
        }
    }
    protected void background(int index) {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(arrayStr[index]), Color.parseColor("#FFFFFF")}
        );
        layout.setBackground(gradientDrawable);
    }
    protected void clothes(int index) {
        for (int i = 0; i < images.length; i++) {
            images[i].setImageResource(resources[index][i]);
        }
    }
}