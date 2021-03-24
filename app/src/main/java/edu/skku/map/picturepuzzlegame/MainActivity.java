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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //밑의 정의한 클래스에서 사용하기 위해 전역변수로 설정.

    Activity activity = this; // == Main Activity
    GridView gridView;
    private List<ResolveInfo> apps; // 앱정보를 기록할 List
    private PackageManager pm; //앱을 제어할 변수를 생성

    public class gridAdapter extends BaseAdapter{
        LayoutInflater inflater;

        public  gridAdapter(){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        };
        public final int getCount(){
            //출력할 목록 수 반환
            return apps.size();
        }
        public final Object getItem(int index){
            //apps 배열에서 아이템 호출 해줌
            return apps.get(index);
        }
        public final long getItemId(int index){
            //apps 배열 인덱스 구하기 위한 메소드
            return index;
        }
        //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소
        public View getView(int index, View targetView, ViewGroup parent) {

            if(targetView == null){
                targetView = inflater.inflate(R.layout.item, parent, false);
            }
            //앱 목록에서 지금 이 메소드에 mapping 할 한개의 app 정보만 얻어온다.
            final ResolveInfo info = apps.get(index);
            ImageView imageView = (ImageView)targetView.findViewById(R.id.imagePuzzle);
            imageView.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));

            return targetView;
        }
    }
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