package com.example.android47_ex03_interim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.android47_ex03_interim.databinding.ActivityUpdateCategoryBinding

class UpdateCategoryActivity : AppCompatActivity() {

    lateinit var activityUpdateCategoryBinding: ActivityUpdateCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityUpdateCategoryBinding = ActivityUpdateCategoryBinding.inflate(layoutInflater)
        setContentView(activityUpdateCategoryBinding.root)

        activityUpdateCategoryBinding.run {
            val categoryPosition = intent.getIntExtra("categoryPosition", 0)
            val currentCategoryName = CategoryClass.categoryList[categoryPosition]
            textViewCategoryName.text = " : ${currentCategoryName}"

            // 확인 버튼 눌렀을 때
            buttonUpdateCategory.setOnClickListener {
                val updateCategoryName = editTextUpdateCategoryName.text.toString()

                // 새 카테고리 이름 유효성 검사
                if (editTextUpdateCategoryName.text.isEmpty()) {
                    val builder = AlertDialog.Builder(this@UpdateCategoryActivity)
                    builder.setTitle("새 카테고리 이름 입력 오류")
                    builder.setMessage("새 카테고리 이름을 입력해주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                } else {
                    // 카테고리 이름 수정
                    CategoryClass.categoryList[categoryPosition] = updateCategoryName

                    // 메모를 저장하기 위해 memoDataList에 저장한 카테고리 이름 변경
                    for ((categoryName, memoList) in MemoClass.memoDataList) {
                        if (categoryName == currentCategoryName) {
                            // 현재 카테고리 제거
                            MemoClass.memoDataList.remove(currentCategoryName)
                            // 수정된 카테고리 이름으로 메모 리스트 다시 추가
                            MemoClass.memoDataList[updateCategoryName] = memoList

                            // Log.d("memoList", MemoClass.memoDataList.toString())
                        }
                        break
                    }

                    finish()
                }
            }

            // 취소 버튼 눌렀을 때
            buttonCancleUpdateCategory.setOnClickListener {
                finish()
            }
        }

    }
}
