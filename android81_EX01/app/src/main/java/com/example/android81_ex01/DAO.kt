package com.example.android81_ex01

import android.content.Context
import android.util.Log

class DAO {

    companion object {

        fun insertData(context: Context, memoClass: MemoClass) {
            val sql = """insert into MemoTable
                | (date, title, content)
                | values(?, ?, ?)
            """.trimMargin()

            val memo = arrayOf(
                memoClass.date, memoClass.title, memoClass.content
            )
            val sqliteDatabase = DBHelper(context)
            sqliteDatabase.writableDatabase.execSQL(sql, memo)
            sqliteDatabase.close()
        }

        fun selectData(context: Context, idx: Int): MemoClass {
            val sql = "select * from MemoTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, arg1)
            cursor.moveToNext()

            val idx1 = cursor.getColumnIndex("idx")
            val idx2 = cursor.getColumnIndex("date")
            val idx3 = cursor.getColumnIndex("title")
            val idx4 = cursor.getColumnIndex("content")

            val idx = cursor.getInt(idx1)
            val date = cursor.getString(idx2)
            val title = cursor.getString(idx3)
            val content = cursor.getString(idx4)

            val memoClass = MemoClass(idx, date, title, content)

            dbHelper.close()

            return memoClass
        }

        fun selectAllData(context: Context): MutableList<MemoClass> {
            val sql = "select * from MemoTable"
            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, null)

            val memoList = mutableListOf<MemoClass>()

            while (cursor.moveToNext()) {
                val idx1 = cursor.getColumnIndex("idx")
                val idx2 = cursor.getColumnIndex("date")
                val idx3 = cursor.getColumnIndex("title")
                val idx4 = cursor.getColumnIndex("content")

                val idx = cursor.getInt(idx1)
                val date = cursor.getString(idx2)
                val title = cursor.getString(idx3)
                val content = cursor.getString(idx4)

                val memoClass = MemoClass(idx, date, title, content)
                memoList.add(memoClass)
            }
            dbHelper.close()

            return memoList
        }

        fun updateData(context: Context, memoClass: MemoClass) {
            val sql = """update MemoTable
                | set date = ?, title = ?, content = ?
                | where idx = ?
            """.trimMargin()

            val memo = arrayOf(memoClass.date, memoClass.title, memoClass.content, memoClass.idx)
            val dbHelper = DBHelper(context)
            dbHelper.writableDatabase.execSQL(sql, memo)
            dbHelper.close()
        }

        fun deleteData(context: Context, idx: Int) {
            val sql = "delete from MemoTable where idx = ?"
            val args = arrayOf(idx)
            val dbHelper = DBHelper(context)

            dbHelper.writableDatabase.execSQL(sql, args)
            dbHelper.close()
        }
    }
}