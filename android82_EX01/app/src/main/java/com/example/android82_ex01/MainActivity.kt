package com.example.android82_ex01

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.android82_ex01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val databaseName = "Password.db"
        val exists = isDatabaseExists(this, databaseName)
        if (exists) {
            // 회원가입된 상태 : Password.db가 이미 존재하는 경우에 대한 처리
            replaceFragment(DataClass.LOGIN_FRAGMENT, false, false)
        } else {
            //  회원가입되지 않은 상태 : Password.db가 존재하지 않는 경우에 대한 처리
            replaceFragment(DataClass.SIGNUP_FRAGMENT, false, false)
        }
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: String, addToBackStack:Boolean, animate:Boolean) {
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // 새로운 Fragment를 담을 변수
        var newFragment = when(name) {

            DataClass.SIGNUP_FRAGMENT -> {
                SignupFragment()
            }
            DataClass.LOGIN_FRAGMENT -> {
                LoginFragment()
            }
            DataClass.CATEGORY_FRAGMENT -> {
                CategoryFragment()
            }
            DataClass.MEMO_LIST_FRAGMENT -> {
                MemoListFragment()
            }
            DataClass.CREATE_MEMO_FRAGMENT -> {
                CreateMemoFragment()
            }
            DataClass.SHOW_MEMO_FRAGMENT -> {
                ShowMemoFragment()
            }
            DataClass.UPDATE_MEMO_FRAGMENT -> {
                UpdateMemoFragment()
            }
            else -> {
                Fragment()
            }
        }

        if (newFragment != null) {
            // Fragment를 교채한다.
            fragmentTransaction.replace(R.id.mainContainer, newFragment)

            if (animate == true) {
                // 애니메이션을 설정한다.
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

            if (addToBackStack == true) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name: String) {
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    // DB 파일이 있는지 확인
    fun isDatabaseExists(context: Context, databaseName: String): Boolean {
        val databaseFile = context.getDatabasePath(databaseName)
        return databaseFile.exists()
    }
}