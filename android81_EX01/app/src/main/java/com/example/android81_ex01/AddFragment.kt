package com.example.android81_ex01

import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
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
import com.example.android81_ex01.databinding.FragmentAddBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class AddFragment : Fragment() {

    lateinit var fragmentAddBinding: FragmentAddBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddBinding = FragmentAddBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentAddBinding.run {

            editTextAddTitle.requestFocus()

            thread {
                SystemClock.sleep(500)
                val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mainActivity.currentFocus, 0)
            }

            toolbarAdd.run {
                title = "메모 추가"
                setTitleTextColor(Color.WHITE)
                inflateMenu(R.menu.add_menu)

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    navigationIcon?.colorFilter =
                        BlendModeColorFilter(Color.WHITE, BlendMode.SRC_ATOP)
                } else {
                    navigationIcon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                }

                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.ADD_FRAGMENT)
                }

                setOnMenuItemClickListener {

                    if (editTextAddTitle.text.isEmpty()) {
                        val builder = AlertDialog.Builder(mainActivity)
                        builder.setTitle("제목 오류")
                        builder.setMessage("제목을 입력해주세요")

                        builder.setPositiveButton("확인", null)

                        builder.show()
                    } else if (editTextAddContent.text.isEmpty()) {
                        val builder = AlertDialog.Builder(mainActivity)
                        builder.setTitle("내용 오류")
                        builder.setMessage("내용을 입력해주세요")

                        builder.setPositiveButton("확인", null)

                        builder.show()
                    } else {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val idx = 0
                        val date = sdf.format(Date())
                        val title = editTextAddTitle.text.toString()
                        val content = editTextAddContent.text.toString()

                        val memoClass = MemoClass(idx, date, title, content)

                        DAO.insertData(mainActivity, memoClass)

                        mainActivity.removeFragment(MainActivity.ADD_FRAGMENT)
                    }

                    false
                }
            }

        }

        return fragmentAddBinding.root
    }

}