package DAO

import Model.Job
import androidx.room.*

@Dao
interface JobDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertJob(job: MutableList<Job>)

    @Query("select * from job")
    suspend fun getJobs() : MutableList<Job>

    @Query("DELETE FROM job")
    suspend fun deleteJobs()
}