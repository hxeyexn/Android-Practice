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

            editTextLoginPw.requestFocus()

            thread {
                SystemClock.sleep(300)
                val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editTextLoginPw, 0)
            }

            toolbarLogin.run {
                title = "로그인"
                setTitleTextColor(Color.WHITE)
            }

            buttonLogin.setOnClickListener {
                val pwClass = DAO.selectPassword(mainActivity, 1)
                val builder = AlertDialog.Builder(mainActivity)

                if (editTextLoginPw.text.toString() != pwClass.password.toString()) {
                    builder.run {
                        setTitle("비밀번호 오류")
                        setMessage("비밀번호가 틀렸습니다")
                        setPositiveButton("확인", null)
                        show()
                    }
                } else {
                    mainActivity.replaceFragment(DataClass.CATEGORY_FRAGMENT, true, false)
                }
            }

        }

        return fragmentLoginBinding.root
    }

}