package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface ReviewsDao {

    @Insert
    void addReviewsData(MovieReviews reviews);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReviewsInfo(MovieReviews reviews);

    @Query("DELETE FROM movieReviews")
    void emptyReviewsCache();

    @Query("SELECT EXISTS(SELECT 1 FROM movieReviews WHERE movie_id = :movieId AND reviews_author = :author)")
    boolean checkIfReviewsExists(int movieId, String author);

}