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

            val memo = DAO.selectMemo(mainActivity, DataClass.memoIdx)

            textViewShowMemoTitle.text = memo.title
            textViewShowMemoDate.text = memo.date
            textViewShowMemoContent.text = memo.content

            toolbarShowMemo.run {
                title = "메모 보기"
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.show_memo_menu)

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.showMemoMenuUpdate -> {
                            mainActivity.replaceFragment(DataClass.UPDATE_MEMO_FRAGMENT, true, false)
                        }
                        R.id.showMemoMenuDelete -> {
                            val builder = AlertDialog.Builder(mainActivity)
                            builder.run {
                                setTitle("메모 삭제")
                                setMessage("메모를 삭제 하겠습니까?")

                                setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                    DAO.deleteMemo(mainActivity, DataClass.memoIdx)
                                    mainActivity.removeFragment(DataClass.SHOW_MEMO_FRAGMENT)
                                }
                                setNegativeButton("취소", null)

                                show()
                            }
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
                    mainActivity.removeFragment(DataClass.SHOW_MEMO_FRAGMENT)
                }
            }
        }

        return fragmentShowMemoBinding.root
    }

}