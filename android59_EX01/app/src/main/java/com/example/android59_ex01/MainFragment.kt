package com.example.android59_ex01

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.android59_ex01.databinding.FragmentMainBinding
import com.example.android59_ex01.databinding.RowBinding

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)

        fragmentMainBinding.run {
            buttonToInput.setOnClickListener {
                mainActivity.replaceFragment(FragmentName.FRAGMENT_INPUT, true, true)
            }

            recyclerViewMain.run {
                adapter = MainRecyclerAdapterClass()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }
        return fragmentMainBinding.root
    }

    inner class MainRecyclerAdapterClass : RecyclerView.Adapter<MainRecyclerAdapterClass.MainViewHolderClass>() {
        inner class MainViewHolderClass(rowBinding: RowBinding) : RecyclerView.ViewHolder(rowBinding.root) {
            val textViewRowType: TextView
            val textViewRowName: TextView

            init {
                textViewRowType = rowBinding.textViewRowType
                textViewRowName = rowBinding.textViewRowName

                rowBinding.root.setOnClickListener {
                    mainActivity.position = adapterPosition

                    mainActivity.replaceFragment(FragmentName.FRAGMENT_RESULT, true, true)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolderClass {
            val rowBinding = RowBinding.inflate(layoutInflater)
            val mainViewHolderClass = MainViewHolderClass(rowBinding)

            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            rowBinding.root.layoutParams = params

            return mainViewHolderClass
        }

        override fun getItemCount(): Int {
            return mainActivity.animalList.size
        }

        override fun onBindViewHolder(holder: MainViewHolderClass, position: Int) {
            holder.textViewRowType.text = "종류 : ${mainActivity.animalList[position].type}"
            holder.textViewRowName.text = "이름 : ${mainActivity.animalList[position].name}"
        }
    }

}