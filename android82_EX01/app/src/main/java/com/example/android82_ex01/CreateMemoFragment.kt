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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.android82_ex01.databinding.FragmentCreateMemoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class CreateMemoFragment : Fragment() {

    lateinit var fragmentCreateMemoBinding: FragmentCreateMemoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCreateMemoBinding = FragmentCreateMemoBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentCreateMemoBinding.run {

            editTextCreateMemoTitle.requestFocus()

            thread {
                SystemClock.sleep(300)
                val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editTextCreateMemoTitle, 0)
            }

            toolbarCreateMemo.run {
                title = "메모 추가"
                isTitleCentered = true
                setTitleTextColor(Color.BLACK)
                inflateMenu(R.menu.create_memo_menu)

                setOnMenuItemClickListener {

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val idx = 0
                    val title = editTextCreateMemoTitle.text.toString()
                    val date = sdf.format(Date())
                    val content = editTextCreateMemoContent.text.toString()
                    val categoryIdx = DataClass.categoryIdx

                    val builder = MaterialAlertDialogBuilder(mainActivity)

                    if (title.isEmpty()) {
                        builder.run {
                            setTitle("제목 오류")
                            setMessage("제목을 입력하세요")
                            setPositiveButton("확인", null)
                            show()
                        }
                    } else if (content.isEmpty()) {
                        builder.run {
                            setTitle("내용 오류")
                            setMessage("내용을 입력하세요")
                            setPositiveButton("확인", null)
                            show()
                        }
                    } else {
                        val memoClass = MemoClass(idx, title, date, content, categoryIdx)
                        MemoDAO.insertMemo(mainActivity, memoClass)

                        mainActivity.removeFragment(DataClass.CREATE_MEMO_FRAGMENT)
                    }
                    false
                }

                setNavigationIcon(R.drawable.arrow_back_ios_new_24px)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter =
                        BlendModeColorFilter(Color.BLACK, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(DataClass.CREATE_MEMO_FRAGMENT)
                }

            }
        }

        return fragmentCreateMemoBinding.root
    }

}