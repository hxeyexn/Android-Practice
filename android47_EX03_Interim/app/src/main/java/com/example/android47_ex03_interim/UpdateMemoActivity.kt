package com.example.android47_ex03_interim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.android47_ex03_interim.databinding.ActivityUpdateMemoBinding

class UpdateMemoActivity : AppCompatActivity() {

    lateinit var activityUpdateMemoBinding: ActivityUpdateMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityUpdateMemoBinding = ActivityUpdateMemoBinding.inflate(layoutInflater)
        setContentView(activityUpdateMemoBinding.root)

        activityUpdateMemoBinding.run {

            val categoryPosition = intent.getIntExtra("categoryPosition", 0)
            val memoPosition = intent.getIntExtra("memoPosition", 0)

            // 현재 메모 제목, 내용 출력
            for ((categoryName, memoList) in MemoClass.memoDataList) {
                if (categoryName == CategoryClass.categoryList[categoryPosition]) {
                    for (memoDataClass in memoList) {
                        textViewCurrentMemoTitle.text = "제목 : ${memoList[memoPosition].title}"
                        textViewCurrentMemoContent.text = "내용 : ${memoList[memoPosition].content}"
                    }
                }
            }

            // 확인 버튼 눌렀을 때
            buttonUpdateMemo.setOnClickListener {
                val updateMemoTitle = editTextUpdateMemoTitle.text.toString()
                val updateMemoContent = editTextUpdateMemoContent.text.toString()

                // 새 메모 제목 유효성 검사
                if (editTextUpdateMemoTitle.text.isEmpty()) {
                    val builder = AlertDialog.Builder(this@UpdateMemoActivity)
                    builder.setTitle("새 메모 제목 입력 오류")
                    builder.setMessage("새 메모 제목을 입력해 주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                }
                // 새 메모 내용 유효성 검사
                else if(editTextUpdateMemoContent.text.isEmpty()) {
                    val builder = AlertDialog.Builder(this@UpdateMemoActivity)
                    builder.setTitle("새 메모 내용 입력 오류")
                    builder.setMessage("새 메모 내용을 입력해 주세요")
                    builder.setPositiveButton("확인", null)
                    builder.show()
                    return@setOnClickListener
                }
                else {
                    // 메모 제목, 내용 수정
                    for ((categoryName, memoList) in MemoClass.memoDataList) {
                        if (categoryName == CategoryClass.categoryList[categoryPosition]) {
                            for (memoDataClass in memoList) {
                                memoList[memoPosition].title = updateMemoTitle
                                memoList[memoPosition].content = updateMemoContent
                            }
                        }
                    }
                    // Log.d("memoList", MemoClass.memoDataList.toString())

                    finish()
                }
            }

            // 취소 버튼 눌렀을 때
            buttonCancleUpdateMemo.setOnClickListener {
                finish()
            }
        }
    }
}