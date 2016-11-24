package jp.techacademy.yumie.minakami.taskapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class InputActivity extends AppCompatActivity {

    // Member vars
    private int         mYear, mMonth, mDay, mHour, mMinute;    // vars for Task's date & time
    private Button      mDateButton, mTimeButton;   // Buttons for setting Date & Time
    private EditText    mTitleEdit, mContentEdit;   // EditTexts for setting Date & Content
    private Task        mTask;  // Object of Task-class
    private View.OnClickListener    mOnDateClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            DatePickerDialog    datePickerDialog = new DatePickerDialog(InputActivity.this,
                    new DatePickerDialog.OnDateSetListener(){   // User Input Date
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){  // update the input date
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
                            mDateButton.setText(dateString);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };          // Listeners for Button as setting date

    private View.OnClickListener mOnTimeClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            TimePickerDialog timePickerDialog = new TimePickerDialog(InputActivity.this,    // User Input Time
                    new TimePickerDialog.OnTimeSetListener(){
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute){  // Update the input Time
                            mHour = hourOfDay;
                            mMinute = minute;
                            String timeString = String.format("%02d",mHour) + ":" + String.format("%02d", mMinute);
                            mTimeButton.setText(timeString);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    };         // Listeners for Button as setting time

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {    // Save/Update on Realm
        @Override
        public void onClick(View v) {
            addTask();
            finish();   // Close InputActivity and Back to MainActivity
        }
    };         // Listeners for Button as setting decision

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // Set ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Set UI parts
        mDateButton = (Button) findViewById(R.id.date_button);
        mDateButton.setOnClickListener(mOnDateClickListener);

        mTimeButton = (Button) findViewById(R.id.times_button);
        mTimeButton.setOnClickListener(mOnTimeClickListener);

        findViewById(R.id.done_button).setOnClickListener(mOnDoneClickListener);

        mTitleEdit = (EditText) findViewById(R.id.title_edit_text);
        mContentEdit = (EditText) findViewById(R.id.content_edit_text);

        Intent intent = getIntent();
        mTask = (Task) intent.getSerializableExtra(MainActivity.EXTRA_TASK);    // get Task from Intent's Extra

        if(mTask == null){  // when it creats new, it doesn't receive Task (= null) from MainActivity as transition source
            // case New, set current date & time
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
        } else {
            // case Change, update title, content and the updated time
            mTitleEdit.setText(mTask.getTitle());
            mContentEdit.setText(mTask.getContents());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mTask.getDate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
            mDateButton.setText(dateString);
            mTimeButton.setText(timeString);
        }
    }

    private void addTask(){
        Realm realm = Realm.getDefaultInstance();        // get Realm obj

        if(mTask == null){
            // case New
            mTask = new Task();

            RealmResults<Task> taskRealmResults = realm.where(Task.class).findAll();

            int identifier;     // var as unique id
            if(taskRealmResults.max("id") != null){
                identifier = taskRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mTask.setId(identifier);
        }

        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();

        mTask.setTitle(title);
        mTask.setContents(content);
        GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        Date date = calendar.getTime();
        mTask.setDate(date);

        realm.beginTransaction();   // call beginTransaction() for data addition/deletion etc; from here
        realm.copyToRealmOrUpdate(mTask);    // call copyToRealmOrUpdate() for data saving/updating; if obj exists, it is updated. if not, it is added.
        realm.commitTransaction();  // call commitTransaction() for data addition/deletion etc; to here

        realm.close();  // call CLOSE

        Intent resultIntent = new Intent(getApplicationContext(), TaskAlarmReceiver.class); // Create Intent which wakes TaskAlarmReceiver
        resultIntent.putExtra(MainActivity.EXTRA_TASK, mTask);  // Set Task the intent's Extra to send notification
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast( // Create PendingIntent
                this,
                mTask.getId(),  // Set Task ID, Alarm needs to delete when its Task deleted
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT   // Update only Extra if current PendingIntent exists
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // get AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), resultPendingIntent); // RTC_WAKEUP; set UTC time and release alarms during screen sleep
    }

}
