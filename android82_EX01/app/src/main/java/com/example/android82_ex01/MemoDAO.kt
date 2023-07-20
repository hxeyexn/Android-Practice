package com.example.android82_ex01

import android.content.Context

class MemoDAO {

    companion object {

        // MemoTable
        fun insertMemo(context: Context, memoClass: MemoClass) {
            val sql = """insert into MemoTable
                | (title, date, content, categoryIdx)
                | values(?, ?, ?, ?) 
            """.trimMargin()

            val memo = arrayOf(
                memoClass.title, memoClass.date, memoClass.content, memoClass.categoryIdx
            )

            val sqliteDatabase = DBHelper(context)
            sqliteDatabase.writableDatabase.execSQL(sql, memo)
            sqliteDatabase.close()

        }

        fun selectMemo(context: Context, idx : Int) : MemoClass {
            val sql = "select * from MemoTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, arg1)
            cursor.moveToNext()

            val idx1 = cursor.getColumnIndex("idx")
            val idx2 = cursor.getColumnIndex("title")
            val idx3 = cursor.getColumnIndex("date")
            val idx4 = cursor.getColumnIndex("content")
            val idx5 = cursor.getColumnIndex("categoryIdx")

            val idx = cursor.getInt(idx1)
            val title = cursor.getString(idx2)
            val date = cursor.getString(idx3)
            val content = cursor.getString(idx4)
            val categoryIdx = cursor.getInt(idx5)

            val memoClass = MemoClass(idx, title, date, content, categoryIdx)

            dbHelper.close()

            return memoClass
        }

        fun selectAllMemo(context: Context) : MutableList<MemoClass> {
            val sql = "select * from MemoTable ORDER BY idx DESC"

            val dbHelperMemo = DBHelper(context)
            val cursor = dbHelperMemo.writableDatabase.rawQuery(sql, null)

            val memoList = mutableListOf<MemoClass>()

            while (cursor.moveToNext()) {
                val idx1 = cursor.getColumnIndex("idx")
                val idx2 = cursor.getColumnIndex("title")
                val idx3 = cursor.getColumnIndex("date")
                val idx4 = cursor.getColumnIndex("content")
                val idx5 = cursor.getColumnIndex("categoryIdx")

                val idx = cursor.getInt(idx1)
                val title = cursor.getString(idx2)
                val date = cursor.getString(idx3)
                val content = cursor.getString(idx4)
                val categoryIdx = cursor.getInt(idx5)

                val memoClass = MemoClass(idx, title, date, content, categoryIdx)
                memoList.add(memoClass)
            }
            dbHelperMemo.close()

            return memoList
        }

        fun updateMemo(context: Context, memoClass: MemoClass) {
            val sql = """update MemoTable
                | set title = ?, date = ?, content = ?, categoryIdx = ?
                | where idx = ?
            """.trimMargin()

            val memo = arrayOf(memoClass.title, memoClass.date, memoClass.content, memoClass.categoryIdx, memoClass.idx)
            val dbHelper = DBHelper(context)
            dbHelper.writableDatabase.execSQL(sql, memo)
            dbHelper.close()
        }

        fun deleteMemo(context: Context, idx: Int) {
            val sql = "delete from MemoTable where idx = ?"
            val args = arrayOf(idx)
            val dbHelper = DBHelper(context)

            dbHelper.writableDatabase.execSQL(sql, args)
            dbHelper.close()
        }

    }

}