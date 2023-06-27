package com.example.android59_ex01

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android59_ex01.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    lateinit var fragmentResultBinding: FragmentResultBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity
        fragmentResultBinding = FragmentResultBinding.inflate(layoutInflater)

        fragmentResultBinding.run {
            textViewResultType.text = "종류 : ${mainActivity.animalList[mainActivity.position].type}"
            textViewResultName.text = "이름 : ${mainActivity.animalList[mainActivity.position].name}"
            textViewResultAge.text = "나이 : ${mainActivity.animalList[mainActivity.position].age}"
            textViewResultWeight.text = "몸무게 : ${mainActivity.animalList[mainActivity.position].weight}"

            buttonToMain.setOnClickListener {
                mainActivity.removeFragment(FragmentName.FRAGMENT_RESULT)
            }

            buttonDelete.setOnClickListener {
                mainActivity.animalList.removeAt(mainActivity.position)
                Log.d("animlaList", mainActivity.animalList.toString())
                mainActivity.removeFragment(FragmentName.FRAGMENT_RESULT)

            }
        }

        return fragmentResultBinding.root
    }

}