package com.example.android82_ex01

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
import com.example.android82_ex01.databinding.FragmentShowMemoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ShowMemoFragment : Fragment() {

    lateinit var fragmentShowMemoBinding: FragmentShowMemoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentShowMemoBinding = FragmentShowMemoBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentShowMemoBinding.run {

            val memo = MemoDAO.selectMemo(mainActivity, DataClass.memoIdx)

            textViewShowMemoTitle.text = memo.title
            textViewShowMemoDate.text = memo.date
            textViewShowMemoContent.text = memo.content

            toolbarShowMemo.run {
                title = "메모 보기"
                isTitleCentered = true
                setTitleTextColor(Color.BLACK)
                inflateMenu(R.menu.show_memo_menu)

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.showMemoMenuUpdate -> {
                            mainActivity.replaceFragment(DataClass.UPDATE_MEMO_FRAGMENT, true, true)
                        }
                        R.id.showMemoMenuDelete -> {
                            val builder = MaterialAlertDialogBuilder(mainActivity)
                            builder.run {
                                setTitle("메모 삭제")
                                setMessage("메모를 삭제 하겠습니까?")

                                setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    MemoDAO.deleteMemo(mainActivity, DataClass.memoIdx)
                                    mainActivity.removeFragment(DataClass.SHOW_MEMO_FRAGMENT)
                                }
                                setNegativeButton("취소", null)

                                show()
                            }
                        }
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

        return fragmentShowMemoBinding.root
    }

}