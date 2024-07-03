package com.example.tpo_da1.ui.data.dbLocal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DealDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDealDetails(dealDetails: DealDetailsEntity)

    @Query("SELECT * FROM deal_details WHERE dealID = :dealID")
    suspend fun getDealDetails(dealID: String): DealDetailsEntity?
}