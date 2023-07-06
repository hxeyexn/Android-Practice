package com.example.android80_ex01

import android.content.Context

class DAO {

    companion object {
        fun insertData(context: Context, data: StudentClass) {
            val sql = """insert into StudentTable
                | (name, age, korean)
                | values(?, ?, ?)
            """.trimMargin()

            val arg1 = arrayOf(
                data.name, data.age, data.korean
            )
            val sqliteDatabase = DBHelper(context)
            sqliteDatabase.writableDatabase.execSQL(sql, arg1)
            sqliteDatabase.close()
        }

        fun selectData(context: Context, idx: Int) : StudentClass {
            val sql = "select * from StudentTable where idx = ?"
            val arg1 = arrayOf("$idx")

            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, arg1)
            cursor.moveToNext()

            // 컬럼의 이름을 지정하여 컬럼의 순서값을 가져온다.
            val idx1 = cursor.getColumnIndex("idx")
            val idx2 = cursor.getColumnIndex("name")
            val idx3 = cursor.getColumnIndex("age")
            val idx4 = cursor.getColumnIndex("korean")

            // 데이터를 가져온다.
            val idx = cursor.getInt(idx1)
            val name = cursor.getString(idx2)
            val age = cursor.getInt(idx3)
            val korean = cursor.getInt(idx4)

            val studentClass = StudentClass(idx, name, age, korean)

            dbHelper.close()

            return studentClass
        }

        fun selectAllData(context: Context) : MutableList<StudentClass> {
            val sql = "select * from StudentTable"
            val dbHelper = DBHelper(context)
            val cursor = dbHelper.writableDatabase.rawQuery(sql, null)

            val studentList = mutableListOf<StudentClass>()

            while (cursor.moveToNext()) {
                // 컬럼의 이름을 지정하여 컬럼의 순서값을 가져온다.
                val idx1 = cursor.getColumnIndex("idx")
                val idx2 = cursor.getColumnIndex("name")
                val idx3 = cursor.getColumnIndex("age")
                val idx4 = cursor.getColumnIndex("korean")

                // 데이터를 가져온다.
                val idx = cursor.getInt(idx1)
                val name = cursor.getString(idx2)
                val age = cursor.getInt(idx3)
                val korean = cursor.getInt(idx4)

                val studentClass = StudentClass(idx, name, age, korean)
                studentList.add(studentClass)
            }

            dbHelper.close()

            return studentList
        }

        fun updateData(context: Context, obj: StudentClass) {
            val sql = """update StudentTable
                | set name = ?, age = ?, korean = ?
                | where idx = ?
            """.trimMargin()

            val args = arrayOf(obj.name, obj.age, obj.korean, obj.idx)
            val dbHelper = DBHelper(context)
            dbHelper.writableDatabase.execSQL(sql, args)
            dbHelper.close()
        }

        fun deleteData(context: Context, idx: Int) {
            val sql = "delete from StudentTable where idx = ?"
            val args = arrayOf(idx)
            val dbHelper = DBHelper(context)
            dbHelper.writableDatabase.execSQL(sql, args)
            dbHelper.close()
        }

    }
}