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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.Database.Repository;
import com.example.vacationplanner.Entities.Excursion;
import com.example.vacationplanner.Entities.Vacation;
import com.example.vacationplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    Repository repository;

    private int vacationID;
    private String title;
    private String hotel;
    private String startDate;
    private String endDate;

    EditText editTitle;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;

    String myFormat = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    private ExcursionAdapter excursionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTitle = findViewById(R.id.vacationtitle);
        editHotel = findViewById(R.id.vacationhotel);
        editStartDate = findViewById(R.id.vacationstartdate);
        editEndDate = findViewById(R.id.vacationenddate);

        vacationID = getIntent().getIntExtra("vacationID", -1);
        title = getIntent().getStringExtra("vacationtitle"); // referring to the name of the extra passed in the adapter
        hotel = getIntent().getStringExtra("vacationhotel");
        startDate = getIntent().getStringExtra("vacationstartdate");
        endDate = getIntent().getStringExtra("vacationenddate");
        editTitle.setText(title);
        editHotel.setText(hotel);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        // start date picker
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                if (startDate != "" && startDate != null) {
                    try {
                        Date date = sdf.parse(startDate);
                        c.setTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        VacationDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, day);

                                if (endDate != null && endDate != "") {
                                    try {
                                        Date endDateObj = sdf.parse(endDate);
                                        if (c.getTime().after(endDateObj)) {
                                            Toast.makeText(VacationDetails.this, "Start date can not be after end date", Toast.LENGTH_SHORT).show();
                                        } else {
                                            editStartDate.setText(sdf.format(c.getTime()));
                                            startDate = editStartDate.getText().toString();
                                        }
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    editStartDate.setText(sdf.format(c.getTime()));
                                    startDate = editStartDate.getText().toString();
                                }

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                if (endDate != "" && endDate != null) {
                    try {
                        Date date = sdf.parse(endDate);
                        c.setTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        VacationDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, day);

                                if (startDate != null && startDate != "") {
                                    try {
                                        Date startDateObj = sdf.parse(startDate);
                                        if (c.getTime().before(startDateObj)) {
                                            Toast.makeText(VacationDetails.this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                                        } else {
                                            editEndDate.setText(sdf.format(c.getTime()));
                                            endDate = editEndDate.getText().toString();
                                        }
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    Toast.makeText(VacationDetails.this, "A start date is required.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        excursionAdapter = new ExcursionAdapter(this, startDate, endDate);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter.setExcursions(repository.getAllAssociatedExcursions(vacationID));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.vacationsave) {
            if (editTitle.getText().toString().isEmpty() ||
                editHotel.getText().toString().isEmpty() ||
                editStartDate.getText().toString().isEmpty() ||
                editEndDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Missing fields. Please fill in the required information.", Toast.LENGTH_LONG).show();
                return false;
            }
            Vacation vacation;
            if (vacationID == -1) {
                vacation = new Vacation(
                        editTitle.getText().toString(),
                        editHotel.getText().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString()
                );
                repository.insert(vacation);
                this.finish();
            } else {
                vacation = new Vacation(
                        vacationID,
                        editTitle.getText().toString(),
                        editHotel.getText().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString()
                );
                repository.update(vacation);
                this.finish();
            }
        } else if (item.getItemId() == R.id.vacationdelete) {
            List<Excursion> assocExcursions = repository.getAllAssociatedExcursions(vacationID);
            if (!assocExcursions.isEmpty()) {
                Toast.makeText(this, "Cannot delete vacation with associated excursions.", Toast.LENGTH_LONG).show();
            } else {
                Vacation vacation = new Vacation(
                        vacationID,
                        editTitle.getText().toString(),
                        editHotel.getText().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString()
                );
                repository.delete(vacation);
                this.finish();
            }
        } else if (item.getItemId() == R.id.vacationalert) {
            Date myStartDate = new Date();
            Date myEndDate = new Date();
            try {
                myStartDate = sdf.parse(startDate);
                myEndDate = sdf.parse(endDate);
            } catch ( ParseException e) {
                e.printStackTrace();
            }

            String vacaTitle = editTitle.getText().toString();
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Long triggerStart = myStartDate.getTime();
            Long triggerEnd = myEndDate.getTime();

            // alarms trigger at 01:00
            setAlarm(alarmManager, triggerStart, String.format("Your vacation for %s is starting!", vacaTitle));
            setAlarm(alarmManager, triggerEnd, String.format("Your vacation for %s is ending!", vacaTitle));
        } else if (item.getItemId() == R.id.vacationshare) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE, String.format("Vacation: %s", title));

            StringBuilder message = new StringBuilder();
            message.append(title).append(System.lineSeparator())
                .append("Hotel: ").append(hotel).append(System.lineSeparator())
                .append("Date: ").append(startDate).append(" - ").append(endDate).append(System.lineSeparator());

            List<Excursion> excursionList = repository.getAllAssociatedExcursions(vacationID);
            if (!excursionList.isEmpty()) {
                message.append("Excursions: ").append(System.lineSeparator());
                for (int i = 0; i < excursionList.size(); i++) {
                    Excursion excursion = excursionList.get(i);
                    message.append(i + 1).append(") ").append(excursion.getExcursionTitle()).append(" to ").append(excursion.getDate());
                    if (i < excursionList.size() - 1) {
                        message.append(System.lineSeparator());
                    }
                }
            }
            sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
        return true;
    }

    private void setAlarm(AlarmManager alarmManager, Long trigger, String message) {
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", message);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    @Override
    protected void onResume() {
        super.onResume();
        excursionAdapter.setExcursions(repository.getAllAssociatedExcursions(vacationID));
    }
}