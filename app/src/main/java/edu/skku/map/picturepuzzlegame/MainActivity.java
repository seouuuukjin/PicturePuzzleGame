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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] itemArr = new int[]{
            R.drawable.bear
    };

    ArrayList<Bitmap> lastArr33 = new ArrayList<Bitmap>(9);
    ArrayList<Bitmap> lastArrOrigin33 = new ArrayList<Bitmap>(9);
    ArrayList<Bitmap> lastArr44 = new ArrayList<Bitmap>(16);
    ArrayList<Bitmap> lastArrOrigin44 = new ArrayList<Bitmap>(16);

    //3*3모드인지 4*4모드인지 체크해줄 플래그
    int modeFlag = 0;

    GridView gridView;
    Button btn3, btn4, shuffleBtn;
    Context context = this;
    gridAdapter customAdapter;
    Bitmap bearPic;

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
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public class gridAdapter extends BaseAdapter {

        private Context c;
        //비트맵 배열을 미리 생성해두지 않고, 선언만 해두면 밑에 생성자에서 오류가 난다.
        Bitmap[] items33 = new Bitmap[9];
        Bitmap[] items44 = new Bitmap[16];
        int mode;

        //        public  gridAdapter(Context c, Bitmap arr[][], int modeNum){
//            mode = modeNum;
//            this.c = c;
//
//            //3*3버튼 클릭시 작동
//            if(mode == 3){
//                int i = 0, j=0, m=0;
//
//                for(m=0; m<9; m++){
//                    if(j == 3){
//                        j = j / 3 - 1;
//                        i = i + 1;
//                    }
//                    //비트맵 변수 -> 비트맵 변수로 변수 복사하는 방법
//                    items33[m] = arr[i][j].copy(arr[i][j].getConfig(), true);
//                    j++;
//                }
//            }//4*4버튼 클릭시 작동
//            else if(mode == 4){
//                int i = 0, j=0, m=0;
//
//                for(m=0; m<16; m++){
//                    if(j == 4){
//                        j = j / 4 - 1;
//                        i = i + 1;
//                    }
//                    //비트맵 변수 -> 비트맵 변수로 변수 복사하는 방법
//                    items44[m] = arr[i][j].copy(arr[i][j].getConfig(), true);
//                    j++;
//                }
//            }
//
//        };
        public gridAdapter(Context c, int modeNum){
            this.c = c;
            mode = modeNum;
        }
        public final int getCount(){
            if(mode == 3){
                return lastArr33.size();
            }
            else if(mode == 4){
                return items44.length;
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

        //res폴더에 저장된 사진을 Bitmap으로 만들 때 사용한다. <- 샘플 이미지 띄우기
        bearPic = BitmapFactory.decodeResource(getResources(), R.drawable.bear);
        bearPic = Bitmap.createScaledBitmap(bearPic, size.x - size.x / 10, size.x- size.x / 10, true);

        //원본 비트맵 이미지 넓이, 높이
        int bearWidth = bearPic.getWidth();
        int bearHeight = bearPic.getHeight();

        int imgSliceWidth33 = bearWidth / 3;
        int imgSliceHeight33 = bearHeight / 3;

        int imgSliceWidth44 = bearWidth / 4;
        int imgSliceHeight44 = bearHeight / 4;

        //사이즈에 맞게 비트맵 이미지 2차원 배열에 각각 생성
        for (int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                lastArr33.add(Bitmap.createBitmap(bearPic, j*imgSliceWidth33, i*imgSliceHeight33, imgSliceWidth33, imgSliceHeight33));
                lastArrOrigin33.add(Bitmap.createBitmap(bearPic, j*imgSliceWidth33, i*imgSliceHeight33, imgSliceWidth33, imgSliceHeight33));
//                System.out.println(lastArr33.size());
//                System.out.println(lastArr33.get(3 * i + j));
            }
        }
        for (int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                lastArr44.add(Bitmap.createBitmap(bearPic, j*imgSliceWidth44, i*imgSliceHeight44, imgSliceWidth44, imgSliceHeight44));
                lastArrOrigin44.add(Bitmap.createBitmap(bearPic, j*imgSliceWidth44, i*imgSliceHeight44, imgSliceWidth44, imgSliceHeight44));
            }
        }

        lastArr33.get(8).eraseColor(Color.WHITE);
        lastArrOrigin33.get(8).eraseColor(Color.WHITE);
        lastArr44.get(15).eraseColor(Color.WHITE);
        lastArrOrigin44.get(15).eraseColor(Color.WHITE);
        System.out.println(lastArr33.size());

        //Bitmap을 ImageView의 Background로 저장하기. 샘플이미지 설정하는 것임
        BitmapDrawable bitDraw = new BitmapDrawable(getResources(), bearPic);
        sampleImage.setBackground(bitDraw);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setColumnWidth(dpToPx(context, (float)110));
                customAdapter = new gridAdapter(context, 3);
                gridView.setAdapter(customAdapter);
                modeFlag = 0;
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gridView.setColumnWidth(dpToPx(context, (float)90));
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
        //customAdapter = new gridAdapter(context, imgSlice33, 3);
        customAdapter = new gridAdapter(context,3);
        gridView.setAdapter(customAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("!@!!@!@!@");
//                if(view instanceof ImageView){
//                    Drawable d = ((ImageView) view).getDrawable();
//                    Bitmap cur = drawableToBitmap(d);
////                    if(copy33[2][2].sameAs(item)){
////                        System.out.println("#################################################");
////                    }
////                    System.out.println(d);
//
//                    if(customAdapter.getCount() == 9){
//                        System.out.println("#################################################");
//
//                        if(cur.sameAs(imgSlice33[0][0])){
//                            System.out.println("##########");
//                            System.out.println(imgSlice33[0][0]);
//                            System.out.println(imgSlice33[0][1]);
//                            Bitmap tmp1 = imgSlice33[0][1];
////                            imgSlice33[0][1].copy(imgSlice33[0][0].getConfig(), true);
////                            imgSlice33[0][0].copy(tmp1.getConfig(), true);
//                            imgSlice33[0][1] = imgSlice33[0][0];
//                            imgSlice33[0][0] = tmp1;
//                            customAdapter.notifyDataSetChanged();
//                            System.out.println(imgSlice33[0][0]);
//                            System.out.println(imgSlice33[0][1]);
//                            gridView.setAdapter(customAdapter);
//                        }
//                        else if(cur.sameAs(copy33[0][1])){
//
//                        }
//                        else if(cur.sameAs(copy33[0][2])){
//
//                        }
//                        else if(cur.sameAs(copy33[1][0])){
//
//                        }
//                        else if(cur.sameAs(copy33[1][1])){
//
//                        }
//                        else if(cur.sameAs(copy33[1][2])){
//
//                        }
//                        else if(cur.sameAs(copy33[2][0])){
//
//                        }
//                        else if(cur.sameAs(copy33[2][1])){
//
//                        }
//                        else if(cur.sameAs(copy33[2][2])){
//
//                        }
//
//                    }
//                    else if(customAdapter.getCount() == 16){
//                        if(cur.sameAs(copy44[0][0])){
//
//                        }
//                        else if(cur.sameAs(copy44[0][1])){
//
//                        }
//                        else if(cur.sameAs(copy44[0][2])){
//
//                        }
//                        else if(cur.sameAs(copy44[0][3])){
//
//                        }
//                        else if(cur.sameAs(copy44[1][0])){
//
//                        }
//                        else if(cur.sameAs(copy44[1][1])){
//
//                        }
//                        else if(cur.sameAs(copy44[1][2])){
//
//                        }
//                        else if(cur.sameAs(copy44[1][3])){
//
//                        }
//                        else if(cur.sameAs(copy44[2][0])){
//
//                        }
//                        else if(cur.sameAs(copy44[2][1])){
//
//                        }
//                        else if(cur.sameAs(copy44[2][2])){
//
//                        }
//                        else if(cur.sameAs(copy44[2][3])){
//
//                        }
//                        else if(cur.sameAs(copy44[3][0])){
//
//                        }
//                        else if(cur.sameAs(copy44[3][1])){
//
//                        }
//                        else if(cur.sameAs(copy44[3][2])){
//
//                        }
//                        else if(cur.sameAs(copy44[3][3])){
//
//                        }
//                    }
//                }
////                for(int i =0 ; i < gridView.getChildCount(); i++){
////                    View gridChild = (View) gridView.getChildAt(i);
////                    System.out.println(gridChild);
////                }
//                //Bitmap item = (Bitmap) customAdapter.getItem(position);
////                if(copy33[2][2].sameAs(item)){
////                    System.out.println("#################################################");
////                }
//                //System.out.println(item);
//            }
//        });
    }

}
