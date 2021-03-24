package edu.skku.map.picturepuzzlegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class gridAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context c;
    int items[];

    public  gridAdapter(Context c, int arr[]){
        this.c = c;
        items = arr;
    };
    public final int getCount(){
        //출력할 목록 수 반환
        //return apps.size();
        return items.length;
    }
    public final Object getItem(int index){
        //apps 배열에서 아이템 호출 해줌
        return null;
    }
    public final long getItemId(int index){
        //apps 배열 인덱스 구하기 위한 메소드
        return 0;
    }

    //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소
    public View getView(int index, View targetView, ViewGroup parent) {

        if(targetView == null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            targetView = inflater.inflate(R.layout.item, parent, false);
        }
        ImageView imageView = (ImageView)targetView.findViewById(R.id.imagePuzzle);
        imageView.setImageResource(items[index]);

        return targetView;
    }
}
