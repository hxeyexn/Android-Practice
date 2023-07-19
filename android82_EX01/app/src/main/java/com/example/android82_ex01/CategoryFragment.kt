package com.example.android82_ex01

import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android82_ex01.databinding.DialogCategoryBinding
import com.example.android82_ex01.databinding.FragmentCategoryBinding
import com.example.android82_ex01.databinding.RowCategoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.concurrent.thread

class CategoryFragment : Fragment() {

    lateinit var fragmentCategoryBinding: FragmentCategoryBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentCategoryBinding.run {

            toolbarCategory.run {
                title = "카테고리 목록"
                isTitleCentered = true
                setTitleTextColor(Color.BLACK)
                inflateMenu(R.menu.category_menu)

                setOnMenuItemClickListener {

                    val dialogCategoryBinding = DialogCategoryBinding.inflate(layoutInflater)
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    builder.setTitle("카테고리 추가")

                    builder.setView(dialogCategoryBinding.root)

                    dialogCategoryBinding.run {

                        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->

                            textInputLayoutCategoryName.run {
                                val categoryName = textInputLayoutCategoryName.editText!!.text.toString()
                                editText.run {
                                    if (categoryName.isEmpty()) {
                                        error = "카테고리 이름을 입력해주세요"
                                        return@setPositiveButton
                                    } else {
                                        error = null
                                        val idx = 0

                                        val categoryClass = CategoryClass(idx, categoryName)
                                        DAO.insertCategory(mainActivity, categoryClass)

                                        DataClass.categoryList = DAO.selectAllCategory(mainActivity)
                                        fragmentCategoryBinding.recyclerViewCategory.adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                        }

                        builder.setNegativeButton("취소", null)
                        builder.show()

                        textInputLayoutCategoryName.requestFocus()

                    }

                    thread {
                        SystemClock.sleep(300)
                        val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(dialogCategoryBinding.textInputEditText, InputMethodManager.SHOW_IMPLICIT)
                    }

                    false
                }
            }

            recyclerViewCategory.run {
                adapter = CategoryRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                addItemDecoration(
                    DividerItemDecoration(
                        mainActivity,
                        DividerItemDecoration.VERTICAL
                    )
                )

                DataClass.categoryList = DAO.selectAllCategory(mainActivity)
                fragmentCategoryBinding.recyclerViewCategory.adapter?.notifyDataSetChanged()
            }

        }

        return fragmentCategoryBinding.root
    }

    inner class CategoryRecyclerViewAdapter :
        RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolderClass>() {

        inner class CategoryViewHolderClass(rowCategoryBinding: RowCategoryBinding) : RecyclerView.ViewHolder(rowCategoryBinding.root) {
            val textViewRowCategoryName: TextView

            init {
                textViewRowCategoryName = rowCategoryBinding.textViewRowCategoryName

                rowCategoryBinding.run {
                    root.setOnCreateContextMenuListener { menu, v, menuInfo ->
                        DataClass.categoryIdx = DataClass.categoryList[adapterPosition].idx

                        menu.setHeaderTitle(DataClass.categoryList[adapterPosition].categoryName)
                        mainActivity.menuInflater.inflate(R.menu.category_context_menu, menu)

                        // 카테고리 수정 선택 시
                        menu[0].setOnMenuItemClickListener {

                            val dialogCategoryBinding = DialogCategoryBinding.inflate(layoutInflater)
                            val builder = MaterialAlertDialogBuilder(mainActivity)
                            builder.setTitle("카테고리 수정")

                            builder.setView(dialogCategoryBinding.root)

                            dialogCategoryBinding.run {

                                thread {
                                    SystemClock.sleep(300)
                                    val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.showSoftInput(textInputLayoutCategoryName.editText!!, 0)
                                }

                                val category = DAO.selectCategory(mainActivity, DataClass.categoryIdx)

                                textInputLayoutCategoryName.run {
                                    editText!!.setText(category.categoryName)
                                    requestFocus()
                                }

                                builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    category.categoryName = textInputLayoutCategoryName.editText!!.text.toString()
                                    if (category.categoryName.isEmpty()) {
                                        textInputLayoutCategoryName.error = "카테고리 이름 없음"
                                    } else {
                                        DAO.updateCategory(mainActivity, category)

                                        DataClass.categoryList = DAO.selectAllCategory(mainActivity)
                                        fragmentCategoryBinding.recyclerViewCategory.adapter?.notifyDataSetChanged()
                                    }
                                }

                                builder.setNegativeButton("취소", null)
                                builder.show()
                            }
                            false
                        }

                        // 카테고리 삭제 선택 시
                        menu[1].setOnMenuItemClickListener {

                            val filteredMemo = DataClass.memoList.filter { it.categoryIdx == DataClass.categoryIdx }

                            //Log.d("filterMemo", filteredMemo.toString())

                            for (memoClass in filteredMemo) {
                                DAO.deleteMemo(mainActivity, memoClass.idx)
                            }

                            DAO.deleteCategory(mainActivity, DataClass.categoryIdx)

                            DataClass.categoryList = DAO.selectAllCategory(mainActivity)
                            fragmentCategoryBinding.recyclerViewCategory.adapter?.notifyDataSetChanged()
                            false
                        }
                    }

                    root.setOnClickListener {
                        DataClass.categoryIdx = DataClass.categoryList[adapterPosition].idx
                        mainActivity.replaceFragment(DataClass.MEMO_LIST_FRAGMENT, true, true)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolderClass {
            val rowCategoryBinding = RowCategoryBinding.inflate(layoutInflater)
            val categoryViewHolderClass = CategoryViewHolderClass(rowCategoryBinding)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            rowCategoryBinding.root.layoutParams = params

            return categoryViewHolderClass
        }

        override fun getItemCount(): Int {
            return DataClass.categoryList.size
        }

        override fun onBindViewHolder(holder: CategoryViewHolderClass, position: Int) {
            holder.textViewRowCategoryName.text = DataClass.categoryList[position].categoryName
        }

    }

}


