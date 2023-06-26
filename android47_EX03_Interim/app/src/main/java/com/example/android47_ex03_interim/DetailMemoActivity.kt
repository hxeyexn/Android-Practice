package com.example.android47_ex03_interim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.android47_ex03_interim.databinding.ActivityDetailMemoBinding

// 메모 내용 액티비티
class DetailMemoActivity : AppCompatActivity() {

    lateinit var activityDetailMemoBinding: ActivityDetailMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDetailMemoBinding = ActivityDetailMemoBinding.inflate(layoutInflater)
        setContentView(activityDetailMemoBinding.root)

        activityDetailMemoBinding.run {

            // 메모가 저장된 카테고리 위치 불러옴
            val categoryPosition = intent.getIntExtra("categoryPosition", 0)
            // 메모 위치 불러옴
            val memoPosition = intent.getIntExtra("memoPosition", 0)

            // Log.d("categoryPosition", "받은 카테고리 : ${categoryPosition}")
            // Log.d("memoPosition", "받은 메모 : ${memoPosition}")

            // 메모 제목, 내용 출력
            for ((categoryName, memoList) in MemoClass.memoDataList) {
                if (categoryName == CategoryClass.categoryList[categoryPosition]) {
                    for (memoDataClass in memoList) {
                        textViewMemoTitle.text = "제목 : ${memoList[memoPosition].title}"
                        textViewMemoContent.text = "내용 : ${memoList[memoPosition].content}"
                    }
                }
            }

            // 메모 목록 돌아가기 버튼 눌렀을 때
            buttonToMemoList.setOnClickListener {
                finish()
            }
        }
    }
}