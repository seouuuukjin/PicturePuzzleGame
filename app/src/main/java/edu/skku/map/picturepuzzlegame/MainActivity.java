package edu.skku.map.picturepuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] itemArr = new int[]{
            R.drawable.bear
    };
    //3*3에서 사용할 리스트 - 섞기도 하고 실제로 작업이 일어나는 리스트
    ArrayList<Bitmap> lastArr33 = new ArrayList<Bitmap>(9);
    //3*3에서 사용할 원본 배열 - 수정이 전혀 일어나지 않는, 견본으로 쓰일 배열임
    Bitmap[] lastArrOrigin33 = new Bitmap[9];
    ArrayList<Bitmap> lastArr44 = new ArrayList<Bitmap>(16);
    Bitmap[] lastArrOrigin44 = new Bitmap[16];

    //3*3모드인지 4*4모드인지 체크해줄 플래그
    int modeFlag = 0;

    GridView gridView;
    Button btn3, btn4, shuffleBtn;
    Context context = this;
    gridAdapter customAdapter;
    //원본사진
    Bitmap bearPic;

    //px값을 dp값으로 변환해주는 함수
    private float pxToDp(Context context, float px){
        float density = context.getResources().getDisplayMetrics().density;

        if(density == 1.0){
            density *= 4.0;
        }
        else if(density == 1.5){
            density *= (8 / 3);
        }
        else if(density == 2.0){
            density *= 2.0;
        }
        return px / density;
    }
    //dp값을 px값으로 변환해주는 함수
    private int dpToPx(Context context, float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    //Bitmap 2차원 배열을 랜덤하게 섞어주는 함수
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
    //Bitmap 2차원 배열을 서로 복사하는 함수
    private void copyBitmapArr(Bitmap[][] src, Bitmap[][] dest, int mode){
        for(int i=0; i<mode; i++) {
            for(int j =0; j<mode; j++) {
                dest[i][j] = src[i][j].copy(src[i][j].getConfig(), true);
            }
        }
    }
    //주변 4칸중에 어느곳에 하얀색이 있는지 알려준다.
    //있다면 해당 칸의 pos값을 반환
    //없다면 -1을 반환한다.
    private int checkingNearWhite(int cur){
        if(modeFlag == 0){
            if((cur-3)>0 && lastArr33.get(cur-3) == lastArrOrigin33[8]){
                return cur-3;
            }
            else if((cur-1)>0 && lastArr33.get(cur-1) == lastArrOrigin33[8]){
                return cur-1;
            }
            else if((cur+1)<9 && lastArr33.get(cur+1) == lastArrOrigin33[8]){
                return cur+1;
            }
            else if((cur+3)<9 && lastArr33.get(cur+3) == lastArrOrigin33[8]){
                return cur+3;
            }
        }
        else if(modeFlag == 1){
            if((cur-4)>0 && lastArr44.get(cur-4) == lastArrOrigin44[15]){
                return cur-4;
            }
            else if((cur-1)>0 && lastArr44.get(cur-1) == lastArrOrigin44[15]){
                return cur-1;
            }
            else if((cur+1)<16 && lastArr44.get(cur+1) == lastArrOrigin44[15]){
                return cur+1;
            }
            else if((cur+4)>0 && lastArr44.get(cur+4) == lastArrOrigin44[15]){
                return cur+4;
            }
        }
        return -1;
    }

    //checkingNearWhite 함수에서 받은 결과 값을 토대로 현재 클릭한 bitmap과 흰색타일이 존재하는 pos의 bitmap을 swap 한다.
    private void checkAndSwap(int cur){
        if(checkingNearWhite(cur) != -1){
            if(modeFlag == 0){
                Collections.swap(lastArr33,cur, checkingNearWhite(cur));
            }
            else if(modeFlag == 1){
                Collections.swap(lastArr44,cur,checkingNearWhite(cur));
            }
        }
    }
    private int answerCheck(){
        if(modeFlag == 0){
            int i=0;
            for(; i<9; i++){
                if(lastArr33.indexOf(lastArrOrigin33[i]) != i){
                    break;
                }
                if(i == 9){
                    return 1;
                }
                else
                    return 0;
            }
        }
        else if(modeFlag == 1){
            int i=0;
            for(; i<16; i++){
                if(lastArr44.indexOf(lastArrOrigin44[i]) != i){
                    break;
                }
                if(i == 16){
                    return 1;
                }
                else
                    return 0;
            }
        }
        return 0;
    }
    public class gridAdapter extends BaseAdapter {

        private Context c;
        int mode; //메인함수에서의 modeFlag 숫자를 넘겨받는다.

        public gridAdapter(Context c, int modeNum){
            this.c = c;
            mode = modeNum;
        }
        public final int getCount(){
            if(mode == 3){
                return lastArr33.size();
            }
            else if(mode == 4){
                return lastArr44.size();
            }
            return 0;
        }
        public final Object getItem(int index){
            if(mode == 3){
                return lastArr33.get(index);
            }
            else if(mode == 4){
                return lastArr44.get(index);
            }
            return 0;
        }
        public final long getItemId(int index){
            return 0;
        }

        //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소드. 각 칸 하나하나가 이미지뷰 하나씩이다.
        public View getView(int index, View targetView, ViewGroup parent) {

            ImageView imageView;
            if(targetView == null){
                imageView = new ImageView(c);
            }
            else{
                imageView = (ImageView)targetView;
            }
            Display display = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            imageView.setLayoutParams(new ViewGroup.LayoutParams(size.x / mode,size.x / mode));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if(mode == 3){
                imageView.setImageBitmap(lastArr33.get(index));
            }
            else if(mode == 4){
                imageView.setImageBitmap(lastArr44.get(index));
            }

            return imageView;
//        ImageButton imageBtn;
//        if(targetView == null){
//            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            targetView = inflater.inflate(R.layout.item, parent, false);
//            imageBtn = targetView.findViewById(R.id.imageButton);
//        }
//        else{
//            imageBtn = (ImageButton) targetView;
//        }
//        Display display = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        imageBtn.setLayoutParams(new ViewGroup.LayoutParams(size.x / mode,size.x / mode));
//        imageBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        imageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("~~~~~~~~~~~~~~~~");
//                //여기다가 클릭시 이벤트 처리해주면 됨.
//            }
//        });
//        if(mode == 3){
//            imageBtn.setImageBitmap(items33[i]);
//        }
//        else if(mode == 4){
//            imageBtn.setImageBitmap(items44[i]);
//        }
//
//        return imageBtn;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LinearLayout itemView = (LinearLayout)findViewById(R.id.)
        ImageView sampleImage  = findViewById(R.id.sampleImage);
        gridView = findViewById(R.id.gridView);
        btn3 = findViewById(R.id.button3x3);
        btn4 = findViewById(R.id.button4x4);
        shuffleBtn = findViewById(R.id.button_shuffle);

        //디스플레이 화면 크기 구하기
        Display display = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //샘플 이미지 띄우기
        bearPic = BitmapFactory.decodeResource(getResources(), R.drawable.bear);
        bearPic = Bitmap.createScaledBitmap(bearPic, size.x - size.x / 10, size.x- size.x / 10, true);

        //원본 비트맵 이미지 넓이, 높이
        int bearWidth = bearPic.getWidth();
        int bearHeight = bearPic.getHeight();
        //3*3 일때의 slice 된 비트맵들의 각 너비와 높이
        int imgSliceWidth33 = bearWidth / 3;
        int imgSliceHeight33 = bearHeight / 3;
        //4*4 일때의 slice된 비트맵들의 각 너비와 높이
        int imgSliceWidth44 = bearWidth / 4;
        int imgSliceHeight44 = bearHeight / 4;

        //사이즈에 맞게 비트맵 이미지 2차원 배열에 각각 생성
        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                lastArrOrigin33[3 * i + j] = Bitmap.createBitmap(bearPic, j*imgSliceWidth33, i*imgSliceHeight33, imgSliceWidth33, imgSliceHeight33);
                lastArr33.add(lastArrOrigin33[3 * i + j]);
//                System.out.println(lastArr33.get(3 * i + j));
//                System.out.println(lastArrOrigin33[3 * i + j]);
            }
        }
        for (int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                lastArrOrigin44[i * 4 + j] = Bitmap.createBitmap(bearPic, j*imgSliceWidth44, i*imgSliceHeight44, imgSliceWidth44, imgSliceHeight44);
                lastArr44.add(lastArrOrigin44[4 * i + j]);
            }
        }
        //마지막 칸을 하얀색으로 칠한다
        lastArr33.get(8).eraseColor(Color.WHITE);
        lastArrOrigin33[8].eraseColor(Color.WHITE);
        lastArr44.get(15).eraseColor(Color.WHITE);
        lastArrOrigin44[15].eraseColor(Color.WHITE);
        System.out.println(lastArr33.size());
//        if(lastArr33.indexOf(lastArrOrigin33[0]) != -1){
//            System.out.println("!~!~");
//        }

        //Bitmap을 ImageView의 Background로 저장하기. 샘플이미지 설정하는 것임
        BitmapDrawable bitDraw = new BitmapDrawable(getResources(), bearPic);
        sampleImage.setBackground(bitDraw);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setColumnWidth(dpToPx(context, (float)110));
                lastArr33.clear();
                for(int i=0; i<9; i++){
                    lastArr33.add(lastArrOrigin33[i]);
                }
                customAdapter = new gridAdapter(context, 3);
                gridView.setAdapter(customAdapter);
                modeFlag = 0;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setColumnWidth(dpToPx(context, (float)90));
                lastArr44.clear();
                for(int i=0; i<16; i++){
                    lastArr44.add(lastArrOrigin44[i]);
                }
                customAdapter = new gridAdapter(context, 4);
                gridView.setAdapter(customAdapter);
                modeFlag = 1;
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modeFlag == 0){
                    //shuffleImgSlice(imgSlice33, 3);
                    //gridAdapter customAdapter = new gridAdapter(context, imgSlice33, 3);
                    Collections.shuffle(lastArr33);
                    customAdapter = new gridAdapter(context,3);
                    gridView.setAdapter(customAdapter);
                    for (int i=0; i<3; i++){
                        for(int j=0; j<3; j++){
                            System.out.println(lastArr33.get(3 * i + j));
                            System.out.println(lastArrOrigin33[3 * i + j]);
                        }
                    }
                }
                else if(modeFlag == 1){
                    //shuffleImgSlice(imgSlice44, 4);
                    //gridAdapter customAdapter = new gridAdapter(context, imgSlice44, 4);
                    Collections.shuffle(lastArr44);
                    customAdapter = new gridAdapter(context,4);
                    gridView.setAdapter(customAdapter);
                }
            }
        });

        //어댑터 생성 및 붙여주기
        customAdapter = new gridAdapter(context,3);
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println(position);
//                System.out.println("!@!!@!@!@");
                if(modeFlag == 0) {
                    checkAndSwap(position);
                    gridView.setAdapter(customAdapter);
                    if(answerCheck() == 1){
                        //정답완성
                        Toast answerAlert = Toast.makeText(getApplicationContext(), "FINISH!", Toast.LENGTH_SHORT);
                        answerAlert.show();
                    }
                }
                else if(modeFlag == 1){
                    checkAndSwap(position);
                    gridView.setAdapter(customAdapter);
                    if(answerCheck() == 1){
                        //정답완성
                        Toast answerAlert = Toast.makeText(getApplicationContext(), "FINISH!", Toast.LENGTH_SHORT);
                        answerAlert.show();
                    }
                }
            }
        });
    }

}
