package dev.amal.chessclock.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.amal.chessclock.utils.DataUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ChessClock::class], version = 1, exportSchema = false)
abstract class ChessClockDatabase : RoomDatabase() {
    abstract val chessClockDao: ChessClockDao

    private class ChessDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val chessClockDao = database.chessClockDao
                    if (chessClockDao.count() == 0) {
                        prePopulateDatabase(chessClockDao)
                    }
                }
            }
        }

        private suspend fun prePopulateDatabase(chessClockDao: ChessClockDao) {
            chessClockDao.insertAll(DataUtil.getDefaultClocks())
        }
    }

    companion object {
        private var INSTANCE: ChessClockDatabase? = null

        fun getInstance(context: Context, coroutineScope: CoroutineScope): ChessClockDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChessClockDatabase::class.java, "chess_clock_list"
                    )
                        .addCallback(ChessDatabaseCallback(coroutineScope))
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}