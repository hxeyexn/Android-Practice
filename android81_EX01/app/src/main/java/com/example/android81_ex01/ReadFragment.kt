package com.example.android81_ex01

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.android81_ex01.databinding.FragmentReadBinding

class ReadFragment : Fragment() {

    lateinit var fragmentReadBinding: FragmentReadBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReadBinding = FragmentReadBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentReadBinding.run {
            toolbarRead.run {
                title = "메모 읽기"
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.read_menu)

                setOnMenuItemClickListener {
                    when(it?.itemId) {
                        R.id.readMenuUpdate -> {
                            mainActivity.replaceFragment(MainActivity.UPDATE_FRAGMENT, true, false)
                        }
                        R.id.readMenuDelete -> {
                            val builder = AlertDialog.Builder(mainActivity)
                            builder.setTitle("메모 삭제")
                            builder.setMessage("메모를 삭제 하겠습니까?")

                            builder.setPositiveButton("삭제") { dialogInterface: DialogInterface, i: Int ->
                                DAO.deleteData(mainActivity, MainActivity.memoIdx)

                                mainActivity.removeFragment(MainActivity.READ_FRAGMENT)
                            }
                            builder.setNegativeButton("취소", null)

                            builder.show()
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
                    mainActivity.removeFragment(MainActivity.READ_FRAGMENT)
                }
            }
        }

        return fragmentReadBinding.root
    }

    override fun onResume() {
        super.onResume()

        val memo = DAO.selectData(mainActivity, MainActivity.memoIdx)

        fragmentReadBinding.run {
            textViewReadTitle.text = memo.title
            textViewReadDate.text= memo.date
            textViewReadContent.text = memo.content
        }
    }
}