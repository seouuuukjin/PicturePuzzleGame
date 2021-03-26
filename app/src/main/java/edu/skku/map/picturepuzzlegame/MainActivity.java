package edu.skku.map.picturepuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] itemArr = new int[]{
            R.drawable.bear
    };

    Bitmap imgSlice33[][] = new Bitmap[3][3];
    Bitmap imgSlice44[][] = new Bitmap[4][4];

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView sampleImage  = findViewById(R.id.sampleImage);
        gridView = findViewById(R.id.gridView);

        //디스플레이 화면 크기 구하기
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //res폴더에 저장된 사진을 Bitmap으로 만들 때 사용한다.것 <- 샘플 이미지 띄우기
        Bitmap bearPic = BitmapFactory.decodeResource(getResources(), R.drawable.bear);
        bearPic = Bitmap.createScaledBitmap(bearPic, size.x, size.x, true);

        //원본 비트맵 이미지 넓이, 높이
        int bearWidth = bearPic.getWidth();
        int bearHeight = bearPic.getHeight();
        Log.d("bitmap size!!!!", Integer.toString(bearWidth));

        int imgSliceWidth33 = bearWidth / 3;
        int imgSliceHeight33 = bearHeight / 3;

        int imgSliceWidth44 = bearWidth / 4;
        int imgSliceHeight44 = bearHeight / 4;

        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                imgSlice33[i][j] = Bitmap.createBitmap(bearPic, j*imgSliceWidth33, i*imgSliceHeight33, imgSliceWidth33, imgSliceHeight33);
            }
        }
        for (int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                imgSlice44[i][j] = Bitmap.createBitmap(bearPic, j*imgSliceWidth44, i*imgSliceHeight44, imgSliceWidth44, imgSliceHeight44);
            }
        }
        imgSlice33[2][2] = Bitmap.createBitmap(bearPic, 0,0,1,1);
        imgSlice44[3][3] = Bitmap.createBitmap(bearPic, 0,0,1,1);

        //Bitmap을 ImageView의 Background로 저장하기 <- 샘플이미지 만지는
        BitmapDrawable bitDraw = new BitmapDrawable(getResources(), bearPic);
        sampleImage.setBackground(bitDraw);


        gridAdapter customAdapter = new gridAdapter(this, imgSlice33);
        gridView.setAdapter(customAdapter);
    }
}