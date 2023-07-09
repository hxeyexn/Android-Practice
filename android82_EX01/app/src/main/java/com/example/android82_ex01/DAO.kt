package com.example.android82_ex01

import android.content.Context
import android.util.Log

class DAO {

    companion object {

        // PasswordTable
        fun insertPassword(context: Context, passwordClass: PasswordClass) {
            val sql = """insert into PasswordTable
                | (password)
                | values(?)
            """.trimMargin()

            val password = arrayOf(
                passwordClass.password
            )

            val sqliteDatabase = DBHelperPassword(context)
            sqliteDatabase.writableDatabase.execSQL(sql, password)
            sqliteDatabase.close()
        }

        fun selectPassword(context: Context, idx: Int) : PasswordClass {
            val sql = "select * from PasswordTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelperPassword = DBHelperPassword(context)
            val cursor = dbHelperPassword.writableDatabase.rawQuery(sql, arg1)
            cursor.moveToNext()

            val idx1 = cursor.getColumnIndex("idx")
            val idx2 = cursor.getColumnIndex("password")

            val idx = cursor.getInt(idx1)
            val password = cursor.getInt(idx2)

            val passwordClass = PasswordClass(idx, password)

            dbHelperPassword.close()

            return passwordClass
        }


        // CategoryTable
        fun insertCategory(context: Context, categoryClass: CategoryClass) {
            val sql = """insert into CategoryTable
                | (categoryName)
                | values(?)
            """.trimMargin()

            val category = arrayOf(
                categoryClass.categoryName
            )

            val sqliteDatabase = DBHelperCategory(context)
            sqliteDatabase.writableDatabase.execSQL(sql, category)
            sqliteDatabase.close()
        }

        fun selectCategory(context: Context, idx: Int) : CategoryClass {
            val sql = "select * from CategoryTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelperCategory = DBHelperCategory(context)
            val cursor = dbHelperCategory.writableDatabase.rawQuery(sql, arg1)
            cursor.moveToNext()

            val idx1 = cursor.getColumnIndex("idx")
            val idx2 = cursor.getColumnIndex("categoryName")

            val idx = cursor.getInt(idx1)
            val categoryName = cursor.getString(idx2)

            val categoryClass = CategoryClass(idx, categoryName)

            dbHelperCategory.close()

            return categoryClass
        }

        fun selectAllCategory(context: Context) : MutableList<CategoryClass> {
            val sql = "select * from CategoryTable ORDER BY idx DESC"
            val dbHelperCategory = DBHelperCategory(context)
            val cursor = dbHelperCategory.writableDatabase.rawQuery(sql, null)

            val categoryList = mutableListOf<CategoryClass>()

            while (cursor.moveToNext()) {
                val idx1 = cursor.getColumnIndex("idx")
                val idx2 = cursor.getColumnIndex("categoryName")

                val idx = cursor.getInt(idx1)
                val categoryName = cursor.getString(idx2)

                val categoryClass = CategoryClass(idx, categoryName)
                categoryList.add(categoryClass)
            }
            dbHelperCategory.close()

            return categoryList
        }

        fun updateCategory(context: Context, categoryClass: CategoryClass) {
            val sql = """update CategoryTable
                | set categoryName = ?
                | where idx = ?
            """.trimMargin()

            val category = arrayOf(categoryClass.categoryName, categoryClass.idx)
            val dbHelperCategory = DBHelperCategory(context)
            dbHelperCategory.writableDatabase.execSQL(sql, category)
            dbHelperCategory.close()
        }

        fun deleteCategory(context: Context, idx: Int) {
            val sql = "delete from CategoryTable where idx = ?"
            val args = arrayOf(idx)
            val dbHelperCategory = DBHelperCategory(context)

            dbHelperCategory.writableDatabase.execSQL(sql, args)
            dbHelperCategory.close()
        }


        // MemoTable
        fun insertMemo(context: Context, memoClass: MemoClass) {
            val sql = """insert into MemoTable
                | (title, date, content, categoryIdx)
                | values(?, ?, ?, ?) 
            """.trimMargin()

            val memo = arrayOf(
                memoClass.title, memoClass.date, memoClass.content, memoClass.categoryIdx
            )

            val sqliteDatabase = DBHelperMemo(context)
            sqliteDatabase.writableDatabase.execSQL(sql, memo)
            sqliteDatabase.close()

        }

        fun selectMemo(context: Context, idx : Int) : MemoClass {
            val sql = "select * from MemoTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelperMemo = DBHelperMemo(context)
            val cursor = dbHelperMemo.writableDatabase.rawQuery(sql, arg1)
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

            dbHelperMemo.close()

            return memoClass
        }

        fun selectAllMemo(context: Context) : MutableList<MemoClass> {
            val sql = "select * from MemoTable ORDER BY idx DESC"

            val dbHelperMemo = DBHelperMemo(context)
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
            val dbHelperMemo = DBHelperMemo(context)
            dbHelperMemo.writableDatabase.execSQL(sql, memo)
            dbHelperMemo.close()
        }

        fun deleteMemo(context: Context, idx: Int) {
            val sql = "delete from MemoTable where idx = ?"
            val args = arrayOf(idx)
            val dbHelperMemo = DBHelperMemo(context)

            dbHelperMemo.writableDatabase.execSQL(sql, args)
            dbHelperMemo.close()
        }

    }

}