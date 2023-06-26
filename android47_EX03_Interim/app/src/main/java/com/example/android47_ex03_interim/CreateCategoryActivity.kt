package com.example.android47_ex03_interim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.android47_ex03_interim.databinding.ActivityCreateCategoryBinding

// 카테고리 등록 액티비티
class CreateCategoryActivity : AppCompatActivity() {

    lateinit var activityCreateCategoryBinding: ActivityCreateCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCreateCategoryBinding = ActivityCreateCategoryBinding.inflate(layoutInflater)
        setContentView(activityCreateCategoryBinding.root)

        activityCreateCategoryBinding.run {
            buttonCreateCategory.setOnClickListener {
                val categoryName = editTextCategoryName.text.toString()

                // 카테고리 이름 유효성 검사
                if (editTextCategoryName.text.isEmpty()) {
                    val builder = AlertDialog.Builder(this@CreateCategoryActivity)
                    builder.setTitle("카테고리 이름 입력 오류")
                    builder.setMessage("카테고리 이름을 입력해주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                } else {
                    // 카테고리 등록
                    CategoryClass.categoryList.add(categoryName)
                    finish()
                }
            }

            buttonCancleCreateCategory.setOnClickListener {
                finish()
            }
        }
    }
}