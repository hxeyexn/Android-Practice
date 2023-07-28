package com.example.mini03_newssearchproject

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
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
                title = "기사 본문"
                setNavigationIcon(R.drawable.arrow_back_ios_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.CONTENT_FRAGMENT)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webViewContent.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            val webSettings = webViewContent.settings
            webSettings.javaScriptEnabled = true // JavaScript 허용
            webSettings.loadsImagesAutomatically = true // 이미지 로드 허용

            webSettings.allowContentAccess = true // 컨텐츠 접근 허용
            webSettings.allowFileAccess = true // 파일 접근 허용

            // 안드로이드 웹뷰가 따로 크롬으로 열리지 않고 기기에 설치된 웹뷰 엔진을 사용하여 웹 페이지를 표시
            webViewContent.webViewClient = WebViewClient() // 크롬 Custom Tabs 사용하지 않도록 설정
            webViewContent.webChromeClient = WebChromeClient() // 크롬 WebView 사용하지 않도록 설정

            webViewContent.loadUrl(MainActivity.linkList[MainActivity.position])

        }

        return fragmentContentBinding.root
    }

}