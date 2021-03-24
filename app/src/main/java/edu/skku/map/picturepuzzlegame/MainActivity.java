package edu.skku.map.picturepuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView sampleImage  = findViewById(R.id.sampleImage);

        //res폴더에 저장된 사진을 Bitmap으로 만들 때 사용한다.
        Bitmap _bit = BitmapFactory.decodeResource(getResources(), R.drawable.bear);

        //Bitmap을 ImageView의 Background로 저장하기
        BitmapDrawable bitDraw = new BitmapDrawable(getResources(), _bit);
        sampleImage.setBackground(bitDraw);
    }
}