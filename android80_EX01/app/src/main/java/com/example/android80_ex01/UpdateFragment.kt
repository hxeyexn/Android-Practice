package com.example.android80_ex01

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android80_ex01.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    lateinit var fragmentUpdateBinding: FragmentUpdateBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUpdateBinding = FragmentUpdateBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentUpdateBinding.run {

            val student = DAO.selectData(mainActivity, MainActivity.studentIdx)
            editTextUpdateName.setText(student.name)
            editTextUpdateAge.setText(student.age.toString())
            editTextUpdateKorean.setText(student.korean.toString())

            toolbarUpdate.run {
                title = "정보 수정"
                setTitleTextColor(Color.WHITE)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.UPDATE_FRAGMENT)
                }
            }

            editTextUpdateKorean.setOnEditorActionListener { v, actionId, event ->
                student.name = editTextUpdateName.text.toString()
                student.age = editTextUpdateAge.text.toString().toInt()
                student.korean = editTextUpdateKorean.text.toString().toInt()

                DAO.updateData(mainActivity, student)

                mainActivity.removeFragment(MainActivity.UPDATE_FRAGMENT)

                false
            }
        }

        return fragmentUpdateBinding.root
    }

}