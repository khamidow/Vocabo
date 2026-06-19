package uz.mobiler.gita.gitadictionary.data.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uz.mobiler.gita.gitadictionary.data.source.dao.DictionaryDao
import uz.mobiler.gita.gitadictionary.data.source.entity.DictionaryEntity

@Database(entities = [DictionaryEntity::class], version = 2)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun getDictionaryDao(): DictionaryDao

    companion object {
        private lateinit var instance: MyAppDatabase

        fun init(context: Context) {
            if (!(::instance.isInitialized)) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAppDatabase::class.java,
                    "MyDictionary.db"
                )
                    .createFromAsset("dictionary_3.db")
                    .allowMainThreadQueries()
                    .build()
            }
        }

        fun getInstance(): MyAppDatabase = instance
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // SQL commands to perform the necessary schema changes
            // For example, adding a new column:
            database.execSQL("ALTER TABLE users ADD COLUMN age INTEGER NOT NULL DEFAULT 0")
            // Or creating a new table:
            // database.execSQL("CREATE TABLE new_table (...)")
        }
    }
}