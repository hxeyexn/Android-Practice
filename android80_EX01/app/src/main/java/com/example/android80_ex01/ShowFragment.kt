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
import com.example.android80_ex01.databinding.FragmentShowBinding
import java.sql.DataTruncation

class ShowFragment : Fragment() {

    lateinit var fragmentShowBinding: FragmentShowBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentShowBinding = FragmentShowBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentShowBinding.run {
            toolbarShow.run {
                title = "상세정보"
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.show_menu)

                setOnMenuItemClickListener {
                    when(it?.itemId) {
                        R.id.showMenuUpdate -> {
                            mainActivity.replaceFragment(MainActivity.UPDATE_FRAGMENT, true, false)
                        }
                        R.id.showMenuDelete -> {
                            DAO.deleteData(mainActivity, MainActivity.studentIdx)
                            mainActivity.removeFragment(MainActivity.SHOW_FRAGMENT)
                        }
                    }
                    false
                }

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.SHOW_FRAGMENT)
                }
            }

            val student = DAO.selectData(mainActivity, MainActivity.studentIdx)
            textViewShowName.text = "이름 : ${student.name}"
            textViewShowAge.text = "나이 : ${student.age}"
            textViewShowKorean.text = "국어점수 : ${student.korean}"

        }
        return fragmentShowBinding.root
    }

}