package edu.skku.map.picturepuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] itemArr = new int[]{
            R.drawable.bear
    };

    Bitmap imgSlice33[][] = new Bitmap[3][3];
    Bitmap imgSlice44[][] = new Bitmap[4][4];
    Bitmap copy33[][] = new Bitmap[3][3];
    Bitmap copy44[][] = new Bitmap[4][4];

    //3*3모드인지 4*4모드인지 체크해줄 플래그
    int modeFlag = 0;

    GridView gridView;
    Button btn3, btn4, shuffleBtn;
    Context context = this;
    private float pxToDp(Context context, float px){
        float density = context.getResources().getDisplayMetrics().density;

        if(density == 1.0){
            density *= 4.0;
        }
        else if(density == 1.5){
            density *= (8/3);
        }
        else if(density == 2.0){
            density *= 2.0;
        }
        return px/density;
    }
    private int dpToPx(Context context, float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    private void shuffleImgSlice(Bitmap[][] arr, int mode){
        Bitmap tmp1, tmp2;
        for(int i=0; i<mode; i++) {
            for(int j =0; j<mode; j++) {
                int randNum1 = (int) (Math.random() * arr.length);
                int randNum2 = (int) (Math.random() * arr.length);
                int randNum3 = (int) (Math.random() * arr.length);
                int randNum4 = (int) (Math.random() * arr.length);
                tmp1 = arr[randNum1][randNum2];
                tmp2 = arr[randNum3][randNum4];
                arr[randNum1][randNum2] = tmp2;
                arr[randNum3][randNum4] = tmp1;
            }
        }
    }
    private void copyBitmapArr(Bitmap[][] src, Bitmap[][] dest, int mode){
        for(int i=0; i<mode; i++) {
            for(int j =0; j<mode; j++) {
                dest[i][j] = src[i][j].copy(src[i][j].getConfig(), true);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView sampleImage  = findViewById(R.id.sampleImage);
        gridView = findViewById(R.id.gridView);
        btn3 = findViewById(R.id.button3x3);
        btn4 = findViewById(R.id.button4x4);
        shuffleBtn = findViewById(R.id.button_shuffle);

        //디스플레이 화면 크기 구하기
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //res폴더에 저장된 사진을 Bitmap으로 만들 때 사용한다. <- 샘플 이미지 띄우기
        Bitmap bearPic = BitmapFactory.decodeResource(getResources(), R.drawable.bear);
        bearPic = Bitmap.createScaledBitmap(bearPic, size.x - size.x / 10, size.x- size.x / 10, true);

        System.out.println(size.y);

        //원본 비트맵 이미지 넓이, 높이
        int bearWidth = bearPic.getWidth();
        int bearHeight = bearPic.getHeight();

        Log.d("bitmap size!!!!", Integer.toString(bearWidth));
        Log.d("bitmap size!!!!", Integer.toString(bearHeight));

        int imgSliceWidth33 = bearWidth / 3;
        int imgSliceHeight33 = bearHeight / 3;

        int imgSliceWidth44 = bearWidth / 4;
        int imgSliceHeight44 = bearHeight / 4;

        System.out.println(imgSliceHeight33);
        System.out.println(imgSliceWidth33);

        //사이즈에 맞게 비트맵 이미지 2차원 배열에 각각 생성
        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                imgSlice33[i][j] = Bitmap.createBitmap(bearPic, j*imgSliceWidth33, i*imgSliceHeight33, imgSliceWidth33, imgSliceHeight33);
                //imgSlice33[i][j] = Bitmap.createScaledBitmap(imgSlice33[i][j], size.x - size.x / 100, size.x- size.x / 100, true);
            }
        }
        for (int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                imgSlice44[i][j] = Bitmap.createBitmap(bearPic, j*imgSliceWidth44, i*imgSliceHeight44, imgSliceWidth44, imgSliceHeight44);
            }
        }
        //마지막 사진은 빈 배열로 만들
        imgSlice33[2][2] = Bitmap.createBitmap(bearPic, 0,0,1,1);
        imgSlice44[3][3] = Bitmap.createBitmap(bearPic, 0,0,1,1);
        //위 과정만 거치면 빈 사진이 하늘색이어서, 하얀색으로 해당 칸 설정
        imgSlice33[2][2].eraseColor(Color.WHITE);
        imgSlice44[3][3].eraseColor(Color.WHITE);

        copyBitmapArr(imgSlice33, copy33,3);
        copyBitmapArr(imgSlice44, copy44,4);

        //Bitmap을 ImageView의 Background로 저장하기. 샘플이미지 설정하는 것임
        BitmapDrawable bitDraw = new BitmapDrawable(getResources(), bearPic);
        sampleImage.setBackground(bitDraw);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setColumnWidth(dpToPx(context, (float)110));
                gridAdapter customAdapter = new gridAdapter(context, copy33, 3);
                gridView.setAdapter(customAdapter);
                modeFlag = 0;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setColumnWidth(dpToPx(context, (float)90));
                gridAdapter customAdapter = new gridAdapter(context, copy44, 4);
                gridView.setAdapter(customAdapter);
                modeFlag = 1;
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modeFlag == 0){
                    shuffleImgSlice(imgSlice33, 3);
                    gridAdapter customAdapter = new gridAdapter(context, imgSlice33, 3);
                    gridView.setAdapter(customAdapter);
                }
                else if(modeFlag == 1){
                    shuffleImgSlice(imgSlice44, 4);
                    gridAdapter customAdapter = new gridAdapter(context, imgSlice44, 4);
                    gridView.setAdapter(customAdapter);
                }
            }
        });
        //어댑터 생성 및 붙여주기
        gridAdapter customAdapter = new gridAdapter(context, imgSlice33, 3);
        gridView.setAdapter(customAdapter);
    }
}