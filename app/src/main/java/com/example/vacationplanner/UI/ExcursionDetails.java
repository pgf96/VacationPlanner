package com.example.vacationplanner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacationplanner.Database.Repository;
import com.example.vacationplanner.Entities.Excursion;
import com.example.vacationplanner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    Repository repository;

    int excursionID;
    int vacationID;
    String excursionTitle;
    String excursionDate;
    String vacationStartDate;
    String vacationEndDate;

    EditText editTitle;
    EditText editDate;

    String myFormat = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.excursiontitle);
        editDate = findViewById(R.id.excursiondate);

        vacationID = getIntent().getIntExtra("vacationID", -1);
        excursionID = getIntent().getIntExtra("excursionID", -1);
        excursionTitle = getIntent().getStringExtra("excursionTitle");
        excursionDate = getIntent().getStringExtra("excursionDate");
        vacationStartDate = getIntent().getStringExtra("vacationStartDate");
        vacationEndDate = getIntent().getStringExtra("vacationEndDate");

        editTitle.setText(excursionTitle);
        editDate.setText(excursionDate);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("start date: " + vacationStartDate + ", end date: " + vacationEndDate);
                final Calendar c = Calendar.getInstance();
                if (excursionDate != "" && excursionDate != null) {
                    try {
                        Date date = sdf.parse(excursionDate);
                        c.setTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExcursionDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, day);

                                Date startDate;
                                Date endDate;

                                try {
                                    startDate = sdf.parse(vacationStartDate);
                                    endDate = sdf.parse(vacationEndDate);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                                if (startDate.getTime() <= c.getTimeInMillis() && c.getTimeInMillis() <= endDate.getTime()) {
                                    editDate.setText(sdf.format(c.getTime()));
                                    excursionDate = editDate.getText().toString();
                                } else {
                                    String msg = String.format("Selected date must be between %s - %s", vacationStartDate, vacationEndDate);
                                    Toast.makeText(ExcursionDetails.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {
            if (editTitle.getText().toString().isEmpty() || editDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Missing fields. Please fill in the required information.", Toast.LENGTH_LONG).show();
                return false;
            }
            Excursion excursion;
            // if id is -1, then it is invalid
            if (this.vacationID == -1) {
                return false;
            }

            if (excursionID == -1) { // new excursion
                excursion = new Excursion(editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                repository.insert(excursion);
                this.finish();
            } else { // update
                excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                repository.update(excursion);
                this.finish();
            }
        } else if (item.getItemId() == R.id.excursiondelete) {
            if (excursionID == -1) { // deleting an excursion that does not exist
                this.finish();
            } else {
                Excursion excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                repository.delete(excursion);
                this.finish();
            }
        } else if (item.getItemId() == R.id.excursionalert) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Date myStartDate = new Date();
            try {
                myStartDate = sdf.parse(excursionDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Long trigger = myStartDate.getTime();
            System.out.println(trigger);
            setAlarm(alarmManager, trigger, String.format("Your excursion for %s is starting!", excursionTitle));
            String msg = String.format("Alert set on %s.", excursionDate);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void setAlarm(AlarmManager alarmManager, Long trigger, String message) {
        Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
        intent.putExtra("key", message);
        PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlertExcursion, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }
}
