package Database

import DAO.JobDAO
import DAO.UserDAO
import Model.Job
import Model.User
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [(User::class),(Job::class)],
        version = 2,
    exportSchema = false
)

abstract class UsersDB : RoomDatabase() {

    abstract fun getUserDAO() : UserDAO
    abstract fun getJobDAO() : JobDAO

    companion object{
        @Volatile
        private var instance: UsersDB? = null
        fun getInstance(context: Context): UsersDB{
            if(instance==null){
                synchronized(UsersDB::class){
                    instance= buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context): UsersDB? {
            return  Room.databaseBuilder(
                    context.applicationContext,
                    UsersDB::class.java,
                    "UsersDatabase"
            ).fallbackToDestructiveMigration().build()
        }
    }
}