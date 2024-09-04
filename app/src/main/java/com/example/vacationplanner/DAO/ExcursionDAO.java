package com.example.vacationplanner.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationplanner.Entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert (Excursion excursion);

    @Update
    void update (Excursion excursion);

    @Delete
    void delete (Excursion excursion);

    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM EXCURSIONS WHERE vacationID=:vacaID ORDER BY excursionID ASC")
    List<Excursion> getAllAssociatedExcursions(int vacaID);


}
