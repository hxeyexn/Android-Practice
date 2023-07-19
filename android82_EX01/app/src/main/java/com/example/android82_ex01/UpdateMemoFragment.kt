package com.example.android82_ex01

import android.app.AlertDialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.android82_ex01.databinding.FragmentUpdateMemoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.concurrent.thread

class UpdateMemoFragment : Fragment() {

    lateinit var fragmentUpdateMemoBinding: FragmentUpdateMemoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentUpdateMemoBinding = FragmentUpdateMemoBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentUpdateMemoBinding.run {

            val memo = DAO.selectMemo(mainActivity, DataClass.memoIdx)
            editTextUpdateMemoTitle.setText(memo.title)
            editTextUpdateMemoContent.setText(memo.content)

            toolbarUpdateMemo.run {
                title = "메모 수정"
                isTitleCentered = true
                setTitleTextColor(Color.BLACK)
                inflateMenu(R.menu.update_memo_menu)

                setOnMenuItemClickListener {
                    val builder = MaterialAlertDialogBuilder(mainActivity)
                    if (editTextUpdateMemoTitle.text.isEmpty()) {
                        builder.run {
                            setTitle("제목 수정 오류")
                            setMessage("제목을 입력하세요")
                            setPositiveButton("확인", null)
                            show()
                        }
                    } else if (editTextUpdateMemoContent.text.isEmpty()) {
                        builder.run {
                            setTitle("내용 수정 오류")
                            setMessage("내용을 입력하세요")
                            setPositiveButton("확인", null)
                            show()
                        }
                    } else {
                        memo.title = editTextUpdateMemoTitle.text.toString()
                        memo.content = editTextUpdateMemoContent.text.toString()
                        DAO.updateMemo(mainActivity, memo)

                        mainActivity.removeFragment(DataClass.UPDATE_MEMO_FRAGMENT)
                    }

                    false
                }

                setNavigationIcon(R.drawable.arrow_back_ios_new_24px)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter = BlendModeColorFilter(Color.BLACK, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(DataClass.SHOW_MEMO_FRAGMENT)
                }
            }

        }

        return fragmentUpdateMemoBinding.root
    }

}