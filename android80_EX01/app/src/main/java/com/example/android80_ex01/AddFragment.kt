package com.example.android80_ex01

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
import com.example.android80_ex01.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    lateinit var fragmentAddBinding: FragmentAddBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddBinding = FragmentAddBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentAddBinding.run {
            toolbarAdd.run {
                title = "정보입력"
                setTitleTextColor(Color.WHITE)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.ADD_FRAGMENT)
                }
            }

            editTextAddKorean.setOnEditorActionListener { v, actionId, event ->
                val name = editTextAddName.text.toString()
                val age = editTextAddAge.text.toString().toInt()
                val korean = editTextAddKorean.text.toString().toInt()

                val studentClass = StudentClass(name, age, korean)

                DAO.insertData(mainActivity, studentClass)

                mainActivity.removeFragment(MainActivity.ADD_FRAGMENT)

                false
            }
        }

        return fragmentAddBinding.root
    }

}