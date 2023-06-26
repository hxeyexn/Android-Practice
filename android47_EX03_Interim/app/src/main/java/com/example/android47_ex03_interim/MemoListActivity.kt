package com.example.android47_ex03_interim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android47_ex03_interim.databinding.ActivityMemoListBinding
import com.example.android47_ex03_interim.databinding.RowMemoBinding

class MemoListActivity : AppCompatActivity() {

    lateinit var activityMemoListBinding: ActivityMemoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMemoListBinding = ActivityMemoListBinding.inflate(layoutInflater)
        setContentView(activityMemoListBinding.root)

        activityMemoListBinding.run {
            // 선택한 카테고리 이름
            val categoryPosition = intent.getIntExtra("categoryPosition", 0)
            textViewMemoListCategoryName.text = "${CategoryClass.categoryList[categoryPosition]}의 메모 목록"

            // 카테고리 목록 돌아가기 버튼 눌렀을 때
            buttonToCategoryList.setOnClickListener {
                finish()
            }

            // 메모 recyclerView
            recyclerViewMemoList.run {
                adapter = RecyclerViewMemoAdapterClass()
                layoutManager = LinearLayoutManager(this@MemoListActivity)
            }
        }
    }

    // 메모 옵션 메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    // 메모 옵션 메뉴 선택 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            // 메모 등록 메뉴 선택 시
            R.id.memoOptionCreate -> {
                // 선택한 카테고리 위치를 메모 등록 액티비티로 넘겨줌
                val categoryPosition = intent.getIntExtra("categoryPosition", 0)
                val memoCreateIntent = Intent(this@MemoListActivity, CreateMemoActivity::class.java)
                memoCreateIntent.putExtra("categoryPosition", categoryPosition)
                // 메모 등록 액티비티로 이동
                startActivity(memoCreateIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class RecyclerViewMemoAdapterClass : RecyclerView.Adapter<RecyclerViewMemoAdapterClass.ViewHolderClass>() {
        inner class ViewHolderClass(rowMemoBinding: RowMemoBinding) : RecyclerView.ViewHolder(rowMemoBinding.root) {
            val textViewRowMemoTitle: TextView

            init {
                textViewRowMemoTitle = rowMemoBinding.textViewRowMemoTitle

                rowMemoBinding.run {
                    val categoryPosition = intent.getIntExtra("categoryPosition", 0)

                    // 메모 항목을 길게 눌렀을 때 보여줄 Context Menu
                    root.setOnCreateContextMenuListener { menu, v, menuInfo ->

                        val currentCategoryName = CategoryClass.categoryList[categoryPosition]

                        Log.d("currentCategory", currentCategoryName)


                        // 메모 Context Menu 헤더 제목 설정
                        for ((categoryName, memoList) in MemoClass.memoDataList) {
                            if (categoryName == currentCategoryName) {
                                for (memoDataClass in memoList) {
                                    menu.setHeaderTitle(memoList[adapterPosition].title)
                                }
                            }
                        }

                        // 메모 Context Menu 목록
                        menuInflater.inflate(R.menu.memo_context_menu, menu)

                        // 메모 수정 선택 시
                        menu[0].setOnMenuItemClickListener {

                            val updateMemoIntent = Intent(this@MemoListActivity, UpdateMemoActivity::class.java)
                            // 선택된 카테고리 위치 넘겨줌
                            updateMemoIntent.putExtra("categoryPosition", categoryPosition)
                            // 선택한 메모 위치 넘겨줌
                            updateMemoIntent.putExtra("memoPosition", adapterPosition)
                            // 메모 수정 액티비티로 이동
                            startActivity(updateMemoIntent)

                            false
                        }

                        // 메모 삭제 선택 시
                        menu[1].setOnMenuItemClickListener {

                            // 선택한 메모 삭제
                            for ((categoryName, memoList) in MemoClass.memoDataList) {
                                if (categoryName == CategoryClass.categoryList[categoryPosition]) {
                                    // for (memoDataClass in memoList) {
                                    for (i in 0..memoList.size) {
                                        if (i == adapterPosition) {
                                            memoList.removeAt(i)
                                        }
                                    }
                                }
                            }

                            // 메모 recycler 갱신
                            this@RecyclerViewMemoAdapterClass.notifyDataSetChanged()
                            false
                        }

                    }

                    // 메모 항목을 눌렀을 때
                    root.setOnClickListener {
                        val detailMemoIntent = Intent(this@MemoListActivity, DetailMemoActivity::class.java)
                        // 선택된 카테고리 위치 넘겨줌
                        detailMemoIntent.putExtra("categoryPosition", categoryPosition)
                        // 선택한 메모 위치 넘겨줌
                        detailMemoIntent.putExtra("memoPosition", adapterPosition)
//                        Log.d("Position", "카테고리 : ${categoryPosition}")
//                        Log.d("Position", "메모 : ${adapterPosition}")
                        // 상세 메모 액티비티로 이동
                        startActivity(detailMemoIntent)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowMemoBinding = RowMemoBinding.inflate(layoutInflater)
            val viewHolderClass = ViewHolderClass(rowMemoBinding)

            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            rowMemoBinding.root.layoutParams = params

            return viewHolderClass
        }

        override fun getItemCount(): Int {
            // 선택된 카테고리 위치를 받아와 해당 카테고리의 메모 크기 알아냄
            val categoryPosition = intent.getIntExtra("categoryPosition", 0)
            var memoListSize = 0
            for ((categoryName, memoList) in MemoClass.memoDataList) {
                if (categoryName == CategoryClass.categoryList[categoryPosition]) {
                    memoListSize = memoList.size
                }
            }
            return memoListSize
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            val categoryPosition = intent.getIntExtra("categoryPosition", 0)

            // 선택된 카테고리에 담긴 메모 리스트를 불러와 메모 제목 출력
            for ((categoryName, memoList) in MemoClass.memoDataList) {
                if (categoryName == CategoryClass.categoryList[categoryPosition]) {
                    for (memoDataClass in memoList) {
                        holder.textViewRowMemoTitle.text = memoList[position].title
                    }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

        val adapter = activityMemoListBinding.recyclerViewMemoList.adapter as RecyclerViewMemoAdapterClass
        adapter.notifyDataSetChanged()
    }
}


