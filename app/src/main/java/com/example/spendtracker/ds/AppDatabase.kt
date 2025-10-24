package com.example.spendtracker.ds

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spendtracker.ds.dao.CalculationDao
import com.example.spendtracker.ds.dao.FundDepositDao
import com.example.spendtracker.ds.dao.InvestmentDao
import com.example.spendtracker.ds.dao.SpendingDao
import com.example.spendtracker.ds.entity.CalculationResult
import com.example.spendtracker.ds.entity.FundDepositResult
import com.example.spendtracker.ds.entity.Investment
import com.example.spendtracker.ds.entity.Spending

@Database(
    entities = [Investment::class, Spending::class, CalculationResult::class, FundDepositResult::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun calculationDao(): CalculationDao
    abstract fun investmentDao(): InvestmentDao
    abstract fun spendingDao(): SpendingDao
    abstract fun fundDepositDao(): FundDepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE calculations ADD COLUMN returnDate TEXT DEFAULT NULL")
//            }
//        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "investment_tracker_db"
                )
                    .fallbackToDestructiveMigration(true) // Use this for development/testing
//                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
