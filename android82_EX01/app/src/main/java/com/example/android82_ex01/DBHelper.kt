package com.example.android82_ex01

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperPassword(context: Context) : SQLiteOpenHelper(context, "Password.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """create table PasswordTable
            (idx integer primary key autoincrement,
            password integer not null)
        """.trimIndent()

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}

class DBHelperCategory(context: Context) : SQLiteOpenHelper(context, "Category.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """create table CategoryTable
            (idx integer primary key autoincrement,
            categoryName text not null)
        """.trimIndent()

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}

class DBHelperMemo(context: Context) : SQLiteOpenHelper(context, "Memo.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """create table MemoTable
            (idx integer primary key autoincrement,
            title text not null,
            date text not null,
            content text not null,
            categoryIdx integer not null)
        """.trimIndent()

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}

