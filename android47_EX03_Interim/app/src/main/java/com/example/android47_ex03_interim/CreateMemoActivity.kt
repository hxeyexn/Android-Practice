package com.example.android47_ex03_interim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.android47_ex03_interim.databinding.ActivityCreateMemoBinding

// 메모 등록 액티비티
class CreateMemoActivity : AppCompatActivity() {

    lateinit var activityCreateMemoBinding: ActivityCreateMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityCreateMemoBinding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(activityCreateMemoBinding.root)

        activityCreateMemoBinding.run {
            // 메모를 저장할 카테고리 위치를 불러옴
            val categoryPosition = intent.getIntExtra("categoryPosition", 0)
            val categoryName = CategoryClass.categoryList[categoryPosition]

            // 추가 버튼 눌렀을 때
            buttonCreateMemo.setOnClickListener {
                val title = editTextMemoTitle.text.toString()
                val content = editTextMemoContent.text.toString()

                // Log.d("categoryPosition", categoryPosition.toString())

                // 메모 제목 유효성 검사
                if (editTextMemoTitle.text.isEmpty()) {
                    val builder = AlertDialog.Builder(this@CreateMemoActivity)
                    builder.setTitle("메모 제목 입력 오류")
                    builder.setMessage("메모 제목을 입력해 주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                }
                // 메모 내용 유효성 검사
                else if (editTextMemoContent.text.isEmpty()) {
                    val builder = AlertDialog.Builder(this@CreateMemoActivity)
                    builder.setTitle("메모 내용 입력 오류")
                    builder.setMessage("메모 내용을 입력해 주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                } else {
                    // 메모 등록
                    val memoDataClass = MemoDataClass(title, content)
                    MemoClass.memoDataList.getOrPut(categoryName) { mutableListOf() }.add(memoDataClass)

                    // Log.d("memoList", MemoClass.memoDataList.toString())
                    finish()
                }
            }

            // 취소 버튼 눌렀을 때
            buttonCancleCreateMemo.setOnClickListener {
                finish()
            }

        }
    }
}
