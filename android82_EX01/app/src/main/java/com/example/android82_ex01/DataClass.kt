package com.example.android82_ex01

class DataClass {
    companion object {
        val SIGNUP_FRAGMENT = "SignupFragment"
        val LOGIN_FRAGMENT = "LoginFragment"

        val CATEGORY_FRAGMENT = "CategoryFragment"

        val MEMO_LIST_FRAGMENT = "MemoListFragment"
        val CREATE_MEMO_FRAGMENT = "CreateMemoFragment"
        val SHOW_MEMO_FRAGMENT = "ShowMemoFragment"
        val UPDATE_MEMO_FRAGMENT = "UpdateMemoFragment"

        var categoryList = mutableListOf<CategoryClass>()
        var categoryIdx = 0

        var memoList = mutableListOf<MemoClass>()
        var filteredMemoList = mutableListOf<MemoClass>()
        var memoIdx = 0
    }
}

data class PasswordClass(var idx: Int, var password: Int)
data class CategoryClass(var idx: Int, var categoryName: String)
data class MemoClass(var idx: Int, var title: String, var date: String, var content: String, var categoryIdx: Int)