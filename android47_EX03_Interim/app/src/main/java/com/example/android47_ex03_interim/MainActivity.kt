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
import com.example.android47_ex03_interim.databinding.ActivityMainBinding
import com.example.android47_ex03_interim.databinding.RowCategoryBinding


class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            recyclerViewCategoryList.run {
                adapter = RecyclerViewCategoryAdapterClass()
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }

    }

    // 카테고리 옵션 메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.category_option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    // 카테고리 옵션 메뉴 선택 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 카테고리 등록 메뉴 선택 시
            R.id.categoryOptionCreate -> {
                // 카테고리 등록 액티비티로 이동
                val createCategoryIntent = Intent(this@MainActivity, CreateCategoryActivity::class.java)
                startActivity(createCategoryIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class RecyclerViewCategoryAdapterClass : RecyclerView.Adapter<RecyclerViewCategoryAdapterClass.ViewHolderClass>() {
        inner class ViewHolderClass(rowCategoryBinding: RowCategoryBinding) : RecyclerView.ViewHolder(rowCategoryBinding.root) {
            val textViewRowCategoryName: TextView

            init {
                textViewRowCategoryName = rowCategoryBinding.textViewRowCategoryName

                rowCategoryBinding.run {
                    // 카테고리 항목을 눌렀을 때
                    root.setOnClickListener {
                        val memoListIntent = Intent(this@MainActivity, MemoListActivity::class.java)
                        // 선택한 카테고리 위치 넘겨줌
                        memoListIntent.putExtra("categoryPosition", adapterPosition)
                        // 메모 리스트 액티비티로 이동
                        startActivity(memoListIntent)
                    }

                    // 카테고리 항목을 길게 눌렀을 때 보여줄 Context Menu
                    root.setOnCreateContextMenuListener { menu, v, menuInfo ->
                        // 카테고리 Context Menu 헤더 제목 설정
                        menu.setHeaderTitle("${CategoryClass.categoryList[adapterPosition]}")
                        // 카테고리 Context Menu 목록
                        menuInflater.inflate(R.menu.category_context_menu, menu)

                        // 카테고리 수정 선택 시
                        menu[0].setOnMenuItemClickListener {
                            val updateCategoryIntent = Intent(this@MainActivity, UpdateCategoryActivity::class.java)
                            // 선택한 카테고리 위치 넘겨줌
                            updateCategoryIntent.putExtra("categoryPosition", adapterPosition)
                            // 카테고리 수정 액티비티로 이동
                            startActivity(updateCategoryIntent)

                            false
                        }

                        // 카테고리 삭제 선택 시
                        menu[1].setOnMenuItemClickListener {

                            // 선택한 카테고리 이름
                            val currentCategoryName = CategoryClass.categoryList[adapterPosition]

                            // 카테고리에 있는 메모 삭제
                            for ((categoryName, _) in MemoClass.memoDataList) {
                                // memoDataList에서 선택한 카테고리 이름과 동일한 이름을 가진 memoDataList 삭제
                                if (categoryName == currentCategoryName) {
                                    MemoClass.memoDataList.remove(currentCategoryName)
                                    break
                                }
                            }

                            // 선택한 카테고리 삭제
                            CategoryClass.categoryList.removeAt(adapterPosition)

                            // 카테고리 recylerView 갱신
                            this@RecyclerViewCategoryAdapterClass.notifyDataSetChanged()
                            false
                        }
                    }
                }


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowCategoryBinding = RowCategoryBinding.inflate(layoutInflater)
            val viewHolderClass = ViewHolderClass(rowCategoryBinding)

            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            rowCategoryBinding.root.layoutParams = params

            return viewHolderClass
        }

        override fun getItemCount(): Int {
            return CategoryClass.categoryList.size
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.textViewRowCategoryName.text = CategoryClass.categoryList[position]
        }
    }

    override fun onResume() {
        super.onResume()

        val adapter = activityMainBinding.recyclerViewCategoryList.adapter as RecyclerViewCategoryAdapterClass
        adapter.notifyDataSetChanged()
    }
}



