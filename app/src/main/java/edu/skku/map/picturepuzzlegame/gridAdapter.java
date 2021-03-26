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
    Bitmap[] items = new Bitmap[9];
//    int[] items = new int[]{
//            R.drawable.bear
//    };
    public  gridAdapter(Context c, Bitmap arr[][]){
        this.c = c;
        //items = arr;
        int i = 0, j=0, m=0;

        for(m=0; m<9; m++){
            if(j == 3){
                j = j / 3 - 1;
                i = i + 1;
            }
            //items[m] = arr[i][j];
            items[m] = arr[i][j].copy(arr[i][j].getConfig(), true);
            j++;
        }
    };
    public final int getCount(){
        //출력할 목록 수 반환
        //return apps.size();
        return items.length;
        //return 0;
    }
    public final Object getItem(int index){
        //apps 배열에서 아이템 호출 해줌
        //return null;
        return items[index];
    }
    public final long getItemId(int index){
        //apps 배열 인덱스 구하기 위한 메소드
        return 0;
    }

    //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소
    public View getView(int i, View targetView, ViewGroup parent) {

//        if(targetView == null){
//            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            targetView = inflater.inflate(R.layout.item, null);
//        }
//        ImageView imageView = (ImageView)targetView.findViewById(R.id.imagePuzzle);
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

        imageView.setLayoutParams(new ViewGroup.LayoutParams(size.x,size.x));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(items[i]);

        return imageView;
    }
}
