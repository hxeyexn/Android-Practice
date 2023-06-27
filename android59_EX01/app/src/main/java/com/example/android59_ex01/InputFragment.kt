package com.example.android59_ex01

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.android59_ex01.databinding.FragmentInputBinding
import kotlin.concurrent.thread

class InputFragment : Fragment() {

    lateinit var fragmentInputBinding: FragmentInputBinding
    lateinit var mainActivity: MainActivity

    val typeList = arrayOf(
        "코끼리", "기린", "토끼"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentInputBinding = FragmentInputBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentInputBinding.run {

            fragmentInputBinding.editTextName.requestFocus()

            thread {
                SystemClock.sleep(500)
                val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(fragmentInputBinding.editTextName, 0)
            }

            spinnerType.run {
                val a1 = ArrayAdapter<String> (
                    mainActivity,
                    android.R.layout.simple_spinner_item,
                    typeList
                )
                a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = a1

                setSelection(0)

            }

            buttonSave.setOnClickListener {
                val type = typeList[spinnerType.selectedItemPosition]
                val name = editTextName.text.toString()
                val ageText = editTextAge.text.toString()
                val weightText = editTextWeight.text.toString()
                var age = 0
                var weight = 0

                if (name.isEmpty()) {
                    val builder = AlertDialog.Builder(mainActivity)
                    builder.setTitle("이름 입력 오류")
                    builder.setMessage("이름을 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        editTextName.requestFocus()
                    }
                    builder.show()
                    return@setOnClickListener
                }

                if (ageText.isEmpty()) {

                    val builder = AlertDialog.Builder(mainActivity)
                    builder.setTitle("나이 입력 오류")
                    builder.setMessage("0이상만 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        editTextAge.requestFocus()
                    }
                    builder.show()
                    return@setOnClickListener
                }

                if (weightText.isEmpty()) {
                    val builder = AlertDialog.Builder(mainActivity)
                    builder.setTitle("몸무게 입력 오류")
                    builder.setMessage("0이상만 입력해주세요")
                    builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        editTextWeight.requestFocus()
                    }
                    builder.show()
                    return@setOnClickListener
                }

                age = ageText.toInt()
                weight = weightText.toInt()

                val animalInfo = AnimalInfo(type, name, age, weight)
                mainActivity.animalList.add(animalInfo)

                // Log.d("animalList", mainActivity.animalList.toString())

                mainActivity.removeFragment(FragmentName.FRAGMENT_INPUT)
            }
            buttonCancel.setOnClickListener {
                mainActivity.removeFragment(FragmentName.FRAGMENT_INPUT)
            }
        }

        return fragmentInputBinding.root
    }

}