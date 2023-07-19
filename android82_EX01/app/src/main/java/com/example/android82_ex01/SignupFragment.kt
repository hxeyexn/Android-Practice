package com.example.android82_ex01

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

            signupPw.requestFocus()

            thread {
                SystemClock.sleep(500)
                val imm = mainActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mainActivity.currentFocus, 0)
            }

            // toolbar -> materialToolbar
            toolbarSignup.run {
                title = "비밀번호 설정"
                isTitleCentered = true
                setTitleTextColor(Color.BLACK)
            }

            buttonSignupSet.setOnClickListener {
                // editText -> textInputLayout
                val pw = signupPw.editText?.text.toString()
                val pwCheck = signupCheckPw.editText?.text.toString()

                if (pw.isEmpty()) {
                    signupPw.run {
                        error = "비밀번호를 입력하세요"
                        setErrorIconDrawable(R.drawable.error_24px)
                        signupPw.requestFocus()
                    }
                } else if (pw != pwCheck || pwCheck.isEmpty()) {
                    signupCheckPw.run {
                        error = "비밀번호가 일치하지 않습니다"
                        setErrorIconDrawable(R.drawable.error_24px)
                    }
                } else {
                    signupPw.error = null
                    signupCheckPw.error = null

                    val idx = 0
                    val passwordClass = PasswordClass(idx, pw.toInt())

                    DAO.insertPassword(mainActivity, passwordClass)

                    mainActivity.replaceFragment(DataClass.LOGIN_FRAGMENT, false, true)
                }
            }
        }

        return fragmentSignupBinding.root
    }

}

