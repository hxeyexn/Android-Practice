package com.example.mini03_newssearchproject

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mini03_newssearchproject.databinding.FragmentMainBinding
import com.example.mini03_newssearchproject.databinding.RowMainBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.search.SearchView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    val clientId = BuildConfig.NAVER_CLIENT_ID
    val clientSecret = BuildConfig.NAVER_CLIENT_SECRET

    lateinit var searchWord: String
    lateinit var word: String

    val titleList = mutableListOf<String>()
    val descriptionList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentMainBinding.run {
            searchView.editText.setOnEditorActionListener { v, actionId, event ->
                searchBar.text = searchView.text
                word = searchBar.text.toString()
                searchView.hide()

                // Log.d("word", word)

                if (word != null) {
                    getNewsData()
                } else {
                    Log.e("검색어 오류", "검색어를 입력하세요")
                }

                true
            }

            recyclerViewMain.run {
                adapter = RecyclerViewAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
                val divider = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(divider)
            }

        }

        return fragmentMainBinding.root
    }

    fun getNewsData() {
        thread {
            try {
                // searchWord = URLEncoder.encode(word, "UTF-8")
                searchWord = URLEncoder.encode(word, "UTF-8")
                Log.d("검색어 인코딩 성공", searchWord)
            } catch (e: Exception) {
                Log.d("검색어 인코딩 실패", e.toString())
            }

            val apiURL =
                "https://openapi.naver.com/v1/search/news.json?query=$searchWord&display=30&sort=sim"
            val url = URL(apiURL)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.setRequestProperty("X-Naver-Client-Id", clientId)
            httpURLConnection.setRequestProperty("X-Naver-Client-Secret", clientSecret)

            val responseCode = httpURLConnection.responseCode

            Log.d("response", responseCode.toString())

            if (responseCode == 200) {

                val inputStreamReader = InputStreamReader(httpURLConnection.inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                var str: String? = null
                val stringBuffer = StringBuffer()

                do {
                    str = bufferedReader.readLine()
                    if (str != null) {
                        stringBuffer.append(str)
                    }
                } while (str != null)

                val data = stringBuffer.toString()

                val root = JSONObject(data)

                titleList.clear()
                descriptionList.clear()
                MainActivity.linkList.clear()

                val itemArray = root.getJSONArray("items")

                for (idx in 0 until itemArray.length()) {

                    val itemObject = itemArray.getJSONObject(idx)
                    val title = itemObject.getString("title")
                    val description = itemObject.getString("description")
                    val link = itemObject.getString("link")

                    // "title":"챗GPT <b>안드로이드<\/b>용 앱 4개국서 출시…한국은 내주 전망"
                    // b 태그 없애줌
                    val regex = Regex("<b>(.*?)</b>")
                    val newTitle =
                        title.replace(regex, "$1").replace("&quot;", "\"").replace("&apos;", "\'")
                    val newDescription = description.replace(regex, "$1").replace("&quot;", "\"")
                        .replace("&apos;", "\'")

                    titleList.add(newTitle)
                    descriptionList.add(newDescription)
                    MainActivity.linkList.add(link)

                    Log.d("news", newTitle)
                    Log.d("link", link)

                    mainActivity.runOnUiThread {
                        fragmentMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
                    }

                }

                Log.d("data", data)


                // val reader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                // val response = StringBuffer()

                // var line: String?
                // while (reader.readLine().also { line = it } != null) {
                //     response.append(line)
                // }
                // reader.close()

            } else {
                Log.e("News", "Error: $responseCode")
            }
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        inner class ViewHolder(rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root) {
            var textViewRowMainTitle: TextView
            var textViewRowMainDescription: TextView

            init {
                textViewRowMainTitle = rowMainBinding.textViewRowMainTitle
                textViewRowMainDescription = rowMainBinding.textViewRowMainDescription

                rowMainBinding.root.setOnClickListener {
                    MainActivity.position = adapterPosition
                    mainActivity.replaceFragment(MainActivity.CONTENT_FRAGMENT, true, true, null)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater)
            val viewHolder = ViewHolder(rowMainBinding)

            rowMainBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return viewHolder
        }

        override fun getItemCount(): Int {
            return titleList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textViewRowMainTitle.text = titleList[position]
            holder.textViewRowMainDescription.text = descriptionList[position]
        }
    }
}