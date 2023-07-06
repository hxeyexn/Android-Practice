package com.example.android81_ex01

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Memo.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """create table MemoTable
            (idx integer primary key autoincrement,
            date text not null,
            title text not null,
            content text not null)
        """.trimIndent()

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}