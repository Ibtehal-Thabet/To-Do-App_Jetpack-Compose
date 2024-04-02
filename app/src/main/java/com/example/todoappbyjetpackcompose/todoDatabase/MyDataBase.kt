package com.example.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo.dao.TasksDao
import com.example.todo.model.Task

@Database(
    entities = [Task::class], version = 1,
    exportSchema = true
)
abstract class MyDataBase : RoomDatabase() {
    abstract fun tasksDao(): TasksDao

    companion object {
        //static
        private var instance: MyDataBase? = null

        fun getInstance(context: Context): MyDataBase {
            synchronized(MyDataBase::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, MyDataBase::class.java,
                        "tasksDB"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance!!
            }
        }
    }
}