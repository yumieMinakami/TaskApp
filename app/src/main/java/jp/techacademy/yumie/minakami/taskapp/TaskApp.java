package jp.techacademy.yumie.minakami.taskapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by user on 2016/11/22.
 */

public class TaskApp extends Application {
    @Override                   // 継承した親のクラスのメソッドを置換
    public void onCreate(){
        super.onCreate();       // 親クラスのonCreate()
        Realm.init(this);       // Realm 初期化、デフォルト設定使用
    }
}
