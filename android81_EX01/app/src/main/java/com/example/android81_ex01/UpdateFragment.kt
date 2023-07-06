package com.example.android81_ex01

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.android81_ex01.databinding.FragmentUpdateBinding
import kotlin.concurrent.thread

class UpdateFragment : Fragment() {

    lateinit var fragmentUpdateBiding: FragmentUpdateBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUpdateBiding = FragmentUpdateBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentUpdateBiding.run {

            editTextUpdateTitle.requestFocus()

            thread {
                SystemClock.sleep(500)
                val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mainActivity.currentFocus, 0)
            }

            val memo = DAO.selectData(mainActivity, MainActivity.memoIdx)
            editTextUpdateTitle.setText(memo.title)
            editTextUpdateContent.setText(memo.content)

            toolbarUpdate.run {
                title = "메모 수정"
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.update_menu)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.UPDATE_FRAGMENT)
                }

                setOnMenuItemClickListener {

                    if (editTextUpdateTitle.text.isEmpty()) {
                        val builder = AlertDialog.Builder(mainActivity)
                        builder.setTitle("제목 수정 오류")
                        builder.setMessage("제목을 입력해주세요")

                        builder.setPositiveButton("확인", null)

                        builder.show()
                    } else if (editTextUpdateContent.text.isEmpty()) {
                        val builder = AlertDialog.Builder(mainActivity)
                        builder.setTitle("내용 수정 오류")
                        builder.setMessage("내용을 입력해주세요")

                        builder.setPositiveButton("확인", null)

                        builder.show()
                    } else {
                        val memo = DAO.selectData(mainActivity, MainActivity.memoIdx)
                        memo.title = editTextUpdateTitle.text.toString()
                        memo.content = editTextUpdateContent.text.toString()

                        DAO.updateData(mainActivity, memo)

                        mainActivity.removeFragment(MainActivity.UPDATE_FRAGMENT)
                    }

                    false
                }
            }

        }
        return fragmentUpdateBiding.root
    }

}