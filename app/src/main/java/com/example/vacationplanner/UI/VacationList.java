package com.example.vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.sample) {
//            repository = new Repository(getApplication());
//            Vacation vacation1 = new Vacation("Sydney", "Sydney Harbour Hotel", "10/15/2024", "10/22/2024");
//            Vacation vacation2 = new Vacation("London", "The Savoy", "11/05/2024", "11/12/2024");
//            Vacation vacation3 = new Vacation("Dubai", "Burj Al Arab", "12/01/2024", "12/08/2024");
//            Vacation vacation4 = new Vacation("Cancun", "Grand Fiesta Americana", "12/20/2024", "12/27/2024");
//            Vacation vacation5 = new Vacation("Cape Town", "One&Only Cape Town", "01/10/2025", "01/17/2025");
//            repository.insert(vacation1);
//            repository.insert(vacation2);
//            repository.insert(vacation3);
//            repository.insert(vacation4);
//            repository.insert(vacation5);
//
//            // Excursions for Vacation 1: Sydney
//            Excursion excursion1_1 = new Excursion("Sydney Opera House Tour", "10/16/2024", 1);
//            Excursion excursion1_2 = new Excursion("Bondi Beach Visit", "10/17/2024", 1);
//            Excursion excursion1_3 = new Excursion("Blue Mountains Day Trip", "10/18/2024", 1);
//
//            repository.insert(excursion1_1);
//            repository.insert(excursion1_2);
//            repository.insert(excursion1_3);
//
//// Excursions for Vacation 2: London
//            Excursion excursion2_1 = new Excursion("Thames River Cruise", "11/06/2024", 2);
//            Excursion excursion2_2 = new Excursion("London Eye Experience", "11/07/2024", 2);
//            Excursion excursion2_3 = new Excursion("Westminster Abbey Tour", "11/08/2024", 2);
//
//            repository.insert(excursion2_1);
//            repository.insert(excursion2_2);
//            repository.insert(excursion2_3);
//
//// Excursions for Vacation 3: Dubai
//            Excursion excursion3_1 = new Excursion("Desert Safari", "12/02/2024", 3);
//            Excursion excursion3_2 = new Excursion("Burj Khalifa Observation Deck", "12/03/2024", 3);
//            Excursion excursion3_3 = new Excursion("Dubai Mall Shopping Spree", "12/04/2024", 3);
//
//            repository.insert(excursion3_1);
//            repository.insert(excursion3_2);
//            repository.insert(excursion3_3);
//
//// Excursions for Vacation 4: Cancun
//            Excursion excursion4_1 = new Excursion("Chichen Itza Visit", "12/21/2024", 4);
//            Excursion excursion4_2 = new Excursion("Xcaret Park Adventure", "12/22/2024", 4);
//            Excursion excursion4_3 = new Excursion("Snorkeling in Cozumel", "12/23/2024", 4);
//
//            repository.insert(excursion4_1);
//            repository.insert(excursion4_2);
//            repository.insert(excursion4_3);
//
//// Excursions for Vacation 5: Cape Town
//            Excursion excursion5_1 = new Excursion("Table Mountain Hike", "01/11/2025", 5);
//            Excursion excursion5_2 = new Excursion("Cape Winelands Tour", "01/12/2025", 5);
//            Excursion excursion5_3 = new Excursion("Robben Island Visit", "01/13/2025", 5);
//
//            repository.insert(excursion5_1);
//            repository.insert(excursion5_2);
//            repository.insert(excursion5_3);
//
//            return true;
//        }
//        if (item.getItemId() == android.R.id.home) {
//            this.finish();
//            return true;
//        }
//        return true;
//    }
    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }
}