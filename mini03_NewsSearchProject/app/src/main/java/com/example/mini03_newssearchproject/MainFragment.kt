package com.example.mini03_newssearchproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mini03_newssearchproject.databinding.FragmentMainBinding
import com.example.mini03_newssearchproject.databinding.RowMainBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentMainBinding.run {
            recyclerViewMain.run {
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val divider = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(divider)
            }
        }

        return fragmentMainBinding.root
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        inner class ViewHolder(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root) {
            var textViewRowMainTitle: TextView
            var textViewRowMainDescription: TextView

            init {
                textViewRowMainTitle = rowMainBinding.textViewRowMainTitle
                textViewRowMainDescription = rowMainBinding.textViewRowMainDescription

                rowMainBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.CONTENT_FRAGMENT, true, true, null)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowMainBinding)

            rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textViewRowMainTitle.text = "제목"
            holder.textViewRowMainDescription.text = "요약"
        }
    }
}