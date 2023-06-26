package com.example.android47_ex03_interim

class MemoClass {

    // 카테고리별 메모를 담을 리스트
    companion object {
        val memoDataList: MutableMap<String, MutableList<MemoDataClass>> = mutableMapOf()
    }
}

// 메모 데이터 클래스
data class MemoDataClass(var title: String, var content: String)

