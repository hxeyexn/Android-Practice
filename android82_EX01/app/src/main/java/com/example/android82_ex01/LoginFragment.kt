package com.example.android82_ex01

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.android82_ex01.databinding.FragmentLoginBinding
import kotlin.concurrent.thread

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentLoginBinding.run {

            loginPw.requestFocus()

            thread {
                SystemClock.sleep(500)
                val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(loginPw.editText, 0)
            }

            toolbarLogin.run {
                title = "로그인"
                isTitleCentered = true
                setTitleTextColor(Color.BLACK)
            }

            buttonLogin.setOnClickListener {
                val pwClass = DAO.selectPassword(mainActivity, 1)

                loginPw.run {
                    if (loginPw.editText?.text.toString() != pwClass.password.toString()) {
                        error = "잘못된 비밀번호입니다"
                        setErrorIconDrawable(R.drawable.error_24px)
                    } else {
                        error = null
                        mainActivity.replaceFragment(DataClass.CATEGORY_FRAGMENT, false, true)
                    }
                }
            }

        }

        return fragmentLoginBinding.root
    }

}