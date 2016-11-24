package jp.techacademy.yumie.minakami.taskapp;

/**
 * Created by user on 2016/11/21.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TaskAdapter extends BaseAdapter{
    private LayoutInflater mLayoutInflater;     // 他xmlリソースのViewを取り扱うための仕組み、メンバ変数として定義
    private ArrayList<Task> mTaskArrayList;   // アイテムを保持するArrayList(Taskkクラス)

    public TaskAdapter(Context context){
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 他xmlリソースのViewを取り扱うための仕組み、コンストラクタを新規追加取得
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList){
        mTaskArrayList = taskArrayList;
    }

    @Override
    public int getCount() {                     // mTaskArrayListサイズを返す
        return mTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {       // mTaskArrayList要素を返す
        return mTaskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTaskArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, null);
                // LayoutInflaterよりsimple_list_item_2からViewを取得
                // simple_list_item_2：タイトルとサブタイトルがあるセル
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        //　後でTaskクラスから情報を取得するよう変更
        textView1.setText(mTaskArrayList.get(position).getTitle());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskArrayList.get(position).getDate();
        textView2.setText(simpleDateFormat.format(date));

        return convertView;
    }
}
