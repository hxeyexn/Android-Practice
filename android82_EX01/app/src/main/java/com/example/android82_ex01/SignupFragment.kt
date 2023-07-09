package com.example.android82_ex01

import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.android82_ex01.databinding.FragmentSignupBinding
import kotlin.concurrent.thread

class SignupFragment : Fragment() {

    lateinit var fragmentSignupBinding: FragmentSignupBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSignupBinding = FragmentSignupBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentSignupBinding.run {

            editTextSignupPw.requestFocus()

            thread {
                SystemClock.sleep(500)
                val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mainActivity.currentFocus, 0)
            }

            toolbarSignup.run {
                title = "비밀번호 설정"
                setTitleTextColor(Color.WHITE)
            }

            buttonSignupSet.setOnClickListener {
                val pw = editTextSignupPw.text.toString()
                val pwCheck = editTextSignupCheckPw.text.toString()

                val builder = AlertDialog.Builder(mainActivity)

                if (pw != pwCheck || pwCheck.isEmpty()) {
                    builder.run {
                        setTitle("비밀번호 설정 오류")
                        setMessage("비밀번호가 일치하지 않습니다")
                        setPositiveButton("확인", null)
                        show()
                    }
                } else if (pw.isEmpty()) {
                    builder.run {
                        setTitle("비밀번호 설정 오류")
                        setMessage("비밀번호를 입력하세요")
                        setPositiveButton("확인", null)
                        show()
                    }
                } else {
                    val idx = 0
                    val passwordClass = PasswordClass(idx, pw.toInt())

                    DAO.insertPassword(mainActivity, passwordClass)

                    mainActivity.replaceFragment(DataClass.LOGIN_FRAGMENT, true, false)
                }
            }
        }

        return fragmentSignupBinding.root
    }

}