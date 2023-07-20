package com.example.android82_ex01

import android.content.Context

class CategoryDAO {

    companion object {
        // CategoryTable
        fun insertCategory(context: Context, categoryClass: CategoryClass) {
            val sql = """insert into CategoryTable
                | (categoryName)
                | values(?)
            """.trimMargin()

            val category = arrayOf(
                categoryClass.categoryName
            )

            val sqliteDatabase = DBHelper(context)
            sqliteDatabase.writableDatabase.execSQL(sql, category)
            sqliteDatabase.close()
        }

        fun selectCategory(context: Context, idx: Int) : CategoryClass {
            val sql = "select * from CategoryTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, arg1)
            cursor.moveToNext()

            val idx1 = cursor.getColumnIndex("idx")
            val idx2 = cursor.getColumnIndex("categoryName")

            val idx = cursor.getInt(idx1)
            val categoryName = cursor.getString(idx2)

            val categoryClass = CategoryClass(idx, categoryName)

            dbHelper.close()

            return categoryClass
        }

        fun selectAllCategory(context: Context) : MutableList<CategoryClass> {
            val sql = "select * from CategoryTable ORDER BY idx DESC"
            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, null)

            val categoryList = mutableListOf<CategoryClass>()

            while (cursor.moveToNext()) {
                val idx1 = cursor.getColumnIndex("idx")
                val idx2 = cursor.getColumnIndex("categoryName")

                val idx = cursor.getInt(idx1)
                val categoryName = cursor.getString(idx2)

                val categoryClass = CategoryClass(idx, categoryName)
                categoryList.add(categoryClass)
            }
            dbHelper.close()

            return categoryList
        }

        fun updateCategory(context: Context, categoryClass: CategoryClass) {
            val sql = """update CategoryTable
                | set categoryName = ?
                | where idx = ?
            """.trimMargin()

            val category = arrayOf(categoryClass.categoryName, categoryClass.idx)
            val dbHelper = DBHelper(context)
            dbHelper.writableDatabase.execSQL(sql, category)
            dbHelper.close()
        }

        fun deleteCategory(context: Context, idx: Int) {
            val sql = "delete from CategoryTable where idx = ?"
            val args = arrayOf(idx)
            val dbHelperCategory = DBHelper(context)

            dbHelperCategory.writableDatabase.execSQL(sql, args)
            dbHelperCategory.close()
        }
    }
}