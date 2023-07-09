package com.example.android82_ex01

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android82_ex01.databinding.FragmentMemoListBinding
import com.example.android82_ex01.databinding.RowMemoBinding

class MemoListFragment : Fragment() {

    lateinit var fragmentMemoListBinding: FragmentMemoListBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMemoListBinding = FragmentMemoListBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentMemoListBinding.run {

            DataClass.memoList = DAO.selectAllMemo(mainActivity)
            DataClass.filteredMemoList = DataClass.memoList.filter { it.categoryIdx == DataClass.categoryIdx } as MutableList<MemoClass>

            toolbarMemoList.run {
                val category = DAO.selectCategory(mainActivity, DataClass.categoryIdx)
                title = category.categoryName
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.memo_menu)

                setOnMenuItemClickListener {
                    mainActivity.replaceFragment(DataClass.CREATE_MEMO_FRAGMENT, true, false)
                    false
                }

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(DataClass.MEMO_LIST_FRAGMENT)
                }
            }

            recyclerViewMemo.run {
                adapter = MemoRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))
            }
        }
        return fragmentMemoListBinding.root
    }

    inner class MemoRecyclerViewAdapter : RecyclerView.Adapter<MemoRecyclerViewAdapter.MemoViewHolderClass>() {

        inner class MemoViewHolderClass(rowMemoBinding: RowMemoBinding) :
            RecyclerView.ViewHolder(rowMemoBinding.root) {
            val textViewRowMemoTitle: TextView

            init {
                textViewRowMemoTitle = rowMemoBinding.textViewRowMemoTitle

                rowMemoBinding.root.setOnClickListener {
                    DataClass.memoIdx = DataClass.filteredMemoList[adapterPosition].idx
                    Log.d("memoIdx", DataClass.memoIdx.toString())
                    mainActivity.replaceFragment(DataClass.SHOW_MEMO_FRAGMENT, true, false)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolderClass {
            val rowMemoBinding = RowMemoBinding.inflate(layoutInflater)
            val memoViewHolderClass = MemoViewHolderClass(rowMemoBinding)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            rowMemoBinding.root.layoutParams = params

            return memoViewHolderClass
        }

        override fun getItemCount(): Int {
            return DataClass.memoList.count { it.categoryIdx == DataClass.categoryIdx }
        }

        override fun onBindViewHolder(holder: MemoViewHolderClass, position: Int) {
            holder.textViewRowMemoTitle.text = DataClass.filteredMemoList[position].title
        }
    }

    override fun onResume() {
        super.onResume()

        fragmentMemoListBinding.recyclerViewMemo.adapter?.notifyDataSetChanged()
    }

}

