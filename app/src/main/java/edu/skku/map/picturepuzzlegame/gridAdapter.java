package edu.skku.map.picturepuzzlegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class gridAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private Context c;
    //비트맵 배열을 미리 생성해두지 않고, 선언만 해두면 밑에 생성자에서 오류가 난다.
    Bitmap[] items = new Bitmap[9];

    public  gridAdapter(Context c, Bitmap arr[][]){
        this.c = c;
        //items = arr;
        int i = 0, j=0, m=0;

        for(m=0; m<9; m++){
            if(j == 3){
                j = j / 3 - 1;
                i = i + 1;
            }
            //비트맵 변수 -> 비트맵 변수로 변수 복사하는 방법
            items[m] = arr[i][j].copy(arr[i][j].getConfig(), true);
            j++;
        }
    };
    public final int getCount(){
        return items.length;
    }
    public final Object getItem(int index){
        return items[index];
    }
    public final long getItemId(int index){
        //apps 배열 인덱스 구하기 위한 메소드
        return 0;
    }

    //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소
    public View getView(int i, View targetView, ViewGroup parent) {

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

        imageView.setLayoutParams(new ViewGroup.LayoutParams(size.x / 3,size.x / 3));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(items[i]);

        return imageView;
    }
}
