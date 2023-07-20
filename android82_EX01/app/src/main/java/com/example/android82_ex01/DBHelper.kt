package com.example.android82_ex01

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "Memo.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        val categorySql = """create table CategoryTable
            (idx integer primary key autoincrement,
            categoryName text not null)
        """.trimIndent()

        db?.execSQL(categorySql)

        val memoSql = """create table MemoTable
            (idx integer primary key autoincrement,
            title text not null,
            date text not null,
            content text not null,
            categoryIdx integer not null)
        """.trimIndent()

        db?.execSQL(memoSql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}

