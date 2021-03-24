package edu.skku.map.picturepuzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] itemArr = new int[]{
            R.drawable.bear
    };

    GridView gridView;

//    public class gridAdapter extends BaseAdapter{
//        LayoutInflater inflater;
//        Context c;
//        int items[];
//
//        public  gridAdapter(Context c, int arr[]){
//            this.c = c;
//            items = arr;
//        };
//        public final int getCount(){
//            //출력할 목록 수 반환
//            //return apps.size();
//            return items.length;
//        }
//        public final Object getItem(int index){
//            //apps 배열에서 아이템 호출 해줌
//            return null;
//        }
//        public final long getItemId(int index){
//            //apps 배열 인덱스 구하기 위한 메소드
//            return 0;
//        }
//        //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소
//        public View getView(int index, View targetView, ViewGroup parent) {
//
//            if(targetView == null){
//                LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                targetView = inflater.inflate(R.layout.item, parent, false);
//            }
//            ImageView imageView = (ImageView)targetView.findViewById(R.id.imagePuzzle);
//            imageView.setImageResource(items[index]);
//
//            return targetView;
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView sampleImage  = findViewById(R.id.sampleImage);
        gridView = findViewById(R.id.gridView);

        //res폴더에 저장된 사진을 Bitmap으로 만들 때 사용한다.것 <- 샘플 이미지 띄우기
        Bitmap _bit = BitmapFactory.decodeResource(getResources(), R.drawable.bear);
        _bit = Bitmap.createScaledBitmap(_bit, 500, 500, true);

        //원본 비트맵 이미지 넓이, 높이
//        int bitmapWidth = _bit.getWidth();
//        int bitmapHeight = _bit.getHeight();
//        Log.d("bitmap size!!!!", Integer.toString(bitmapHeight));
//        Log.d("bitmap size@@@@", Integer.toString(bitmapWidth));

        //Bitmap을 ImageView의 Background로 저장하기 <- 샘플이미지 만지는
        BitmapDrawable bitDraw = new BitmapDrawable(getResources(), _bit);
        sampleImage.setBackground(bitDraw);

        gridAdapter customAdapter = new gridAdapter(this, itemArr);
        gridView.setAdapter(customAdapter);
    }
}