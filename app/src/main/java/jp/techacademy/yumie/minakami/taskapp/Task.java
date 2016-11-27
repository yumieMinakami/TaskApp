package jp.techacademy.yumie.minakami.taskapp;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 2016/11/22.
 */

// measures for close/down app
@Parcel     // add annotation
public class Task extends RealmObject implements Serializable {
// measures for close/down app
//public class Task extends RealmObject implements Serializable {
    private String  title;           // Title of Task
    private String  contents;       // Contents of Task
    private Date    date;           // Date of Task

    // Set id as primary key
    @PrimaryKey
    private int id;

    public String getTitle(){
        return  title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContents(){
        return contents;
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
