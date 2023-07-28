package com.example.mini03_newssearchproject

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mini03_newssearchproject.databinding.ActivityMainBinding
import com.google.android.material.transition.MaterialSharedAxis

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    var newFragment: Fragment? = null
    var oldFragment: Fragment? = null

    companion object {
        val MAIN_FRAGMENT = "MainFragment"
        val CONTENT_FRAGMENT = "ContentFragment"
        val linkList = mutableListOf<String>()
        var position = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(1000)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            replaceFragment(MAIN_FRAGMENT, false, false, null)
        }

    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: String, addToBackStack: Boolean, animate: Boolean, bundle: Bundle?) {
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (newFragment != null) {
            oldFragment = newFragment
        }

        // 새로운 Fragment를 담을 변수
        var newFragment = when (name) {
            MAIN_FRAGMENT -> MainFragment()
            CONTENT_FRAGMENT -> ContentFragment()
            else -> Fragment()
        }

        newFragment.arguments = bundle

        // 애니메이션 설정
        if (oldFragment != null) {
            oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            oldFragment?.enterTransition = null
            oldFragment?.returnTransition = null
        }

        newFragment.exitTransition = null
        newFragment.reenterTransition = null
        newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)


        // Fragment를 교채한다.
        fragmentTransaction.replace(R.id.mainContainer, newFragment)

        if (addToBackStack == true) {
            // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
            fragmentTransaction.addToBackStack(name)
        }

        // 교체 명령이 동작하도록 한다.
        fragmentTransaction.commit()
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name: String) {
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

}