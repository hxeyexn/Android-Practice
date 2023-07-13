package com.example.android81_ex01

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android81_ex01.databinding.FragmentMainBinding
import com.example.android81_ex01.databinding.RowMainBinding

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentMainBinding.run {
            toolbarMain.run {
                title = "메모앱"
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.main_menu)

                setOnMenuItemClickListener {
                    mainActivity.replaceFragment(MainActivity.ADD_FRAGMENT, true, false)

                    false
                }
            }

            recyclerViewMain.run {
                adapter = MainRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))
            }
        }

        return fragmentMainBinding.root
    }

    inner class MainRecyclerViewAdapter : RecyclerView.Adapter<MainRecyclerViewAdapter.MainViewHolderClass>() {

        inner class MainViewHolderClass(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root) {
            val textViewRowMainDate: TextView
            val textViewRowMainTitle: TextView

            init {
                textViewRowMainDate = rowMainBinding.textViewRowMainDate
                textViewRowMainTitle = rowMainBinding.textViewRowMainTitle

                rowMainBinding.root.setOnClickListener {
                    MainActivity.memoIdx = MainActivity.memoList[adapterPosition].idx
                    mainActivity.replaceFragment(MainActivity.READ_FRAGMENT, true, false)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolderClass {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val mainViewHolderClass = MainViewHolderClass(rowMainBinding)

            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            rowMainBinding.root.layoutParams = params

            return mainViewHolderClass
        }

        override fun getItemCount(): Int {
            return MainActivity.memoList.size
        }

        override fun onBindViewHolder(holder: MainViewHolderClass, position: Int) {
            holder.textViewRowMainDate.text = MainActivity.memoList[position].date
            holder.textViewRowMainTitle.text = MainActivity.memoList[position].title
        }
    }

    override fun onResume() {
        super.onResume()

        MainActivity.memoList = DAO.selectAllData(mainActivity)
        fragmentMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
    }

}