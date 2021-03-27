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
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

//public class gridAdapter extends BaseAdapter {
//
//    private Context c;
//    //비트맵 배열을 미리 생성해두지 않고, 선언만 해두면 밑에 생성자에서 오류가 난다.
//    Bitmap[] items33 = new Bitmap[9];
//    Bitmap[] items44 = new Bitmap[16];
//    int mode;
//
//    public  gridAdapter(Context c, Bitmap arr[][], int modeNum){
//        mode = modeNum;
//        this.c = c;
//
//        //3*3버튼 클릭시 작동
//        if(mode == 3){
//            int i = 0, j=0, m=0;
//
//            for(m=0; m<9; m++){
//                if(j == 3){
//                    j = j / 3 - 1;
//                    i = i + 1;
//                }
//                //비트맵 변수 -> 비트맵 변수로 변수 복사하는 방법
//                items33[m] = arr[i][j].copy(arr[i][j].getConfig(), true);
//                j++;
//            }
//        }//4*4버튼 클릭시 작동
//        else if(mode == 4){
//            int i = 0, j=0, m=0;
//
//            for(m=0; m<16; m++){
//                if(j == 4){
//                    j = j / 4 - 1;
//                    i = i + 1;
//                }
//                //비트맵 변수 -> 비트맵 변수로 변수 복사하는 방법
//                items44[m] = arr[i][j].copy(arr[i][j].getConfig(), true);
//                j++;
//            }
//        }
//
//    };
//    public final int getCount(){
//        if(mode == 3){
//            return items33.length;
//        }
//        else if(mode == 4){
//            return items44.length;
//        }
//        return 0;
//    }
//    public final Object getItem(int index){
//        if(mode == 3){
//            return items33[index];
//        }
//        else if(mode == 4){
//            return items44[index];
//        }
//        return 0;
//    }
//    public final long getItemId(int index){
//        return 0;
//    }
//
//    //grid에 앱 정보와 새로 따로 정의한 아이템 레이아웃을 매핑 하는 메소드. 각 칸 하나하나가 이미지뷰 하나씩이다.
//    public View getView(int i, View targetView, ViewGroup parent) {
//
//        ImageView imageView;
//        if(targetView == null){
//            imageView = new ImageView(c);
//        }
//        else{
//            imageView = (ImageView)targetView;
//        }
//        Display display = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(size.x / mode,size.x / mode));
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        if(mode == 3){
//            imageView.setImageBitmap(items33[i]);
//        }
//        else if(mode == 4){
//            imageView.setImageBitmap(items44[i]);
//        }
//
//        return imageView;
////        ImageButton imageBtn;
////        if(targetView == null){
////            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////            targetView = inflater.inflate(R.layout.item, parent, false);
////            imageBtn = targetView.findViewById(R.id.imageButton);
////        }
////        else{
////            imageBtn = (ImageButton) targetView;
////        }
////        Display display = ((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
////        Point size = new Point();
////        display.getSize(size);
////
////        imageBtn.setLayoutParams(new ViewGroup.LayoutParams(size.x / mode,size.x / mode));
////        imageBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
////        imageBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                System.out.println("~~~~~~~~~~~~~~~~");
////                //여기다가 클릭시 이벤트 처리해주면 됨.
////            }
////        });
////        if(mode == 3){
////            imageBtn.setImageBitmap(items33[i]);
////        }
////        else if(mode == 4){
////            imageBtn.setImageBitmap(items44[i]);
////        }
////
////        return imageBtn;
//    }
//}
