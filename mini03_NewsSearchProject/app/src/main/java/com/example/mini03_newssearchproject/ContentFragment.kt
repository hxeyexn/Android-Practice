package com.example.mini03_newssearchproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mini03_newssearchproject.databinding.FragmentContentBinding

class ContentFragment : Fragment() {

    lateinit var fragmentContentBinding: FragmentContentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentContentBinding = FragmentContentBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentContentBinding.run {
            toolbarContent.run {
                title = "내용"
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CONTENT_FRAGMENT)
                }
            }
        }

        return fragmentContentBinding.root
    }

}