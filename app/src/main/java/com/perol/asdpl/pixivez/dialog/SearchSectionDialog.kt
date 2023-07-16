/*
 * MIT License
 *
 * Copyright (c) 2020 ultranity
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.dialog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.databinding.DialogSearchSectionBinding
import com.perol.asdpl.pixivez.viewmodel.IllustfragmentViewModel
import com.perol.asdpl.pixivez.viewmodel.generateDateString
import java.util.*

class SearchSectionDialog : BaseVBDialogFragment<DialogSearchSectionBinding>() {

    private val tms: Calendar = Calendar.getInstance()
    val thisMonth01 = "${tms.get(Calendar.YEAR)}-${tms.get(Calendar.MONTH) + 1}-01"
    val halfYear01 = "${tms.get(Calendar.YEAR)}-${tms.get(Calendar.MONTH) + 1}-01"

    @SuppressLint("SetTextI18n")
    override fun onCreateDialogBinding(builder: MaterialAlertDialogBuilder) {
        val word = arguments?.getString("word", "")
        val viewModel =
            ViewModelProvider(requireParentFragment())[IllustfragmentViewModel::class.java]
        var searchTargeti = viewModel.searchTarget.value
        binding.tablayoutSearchTarget.apply {
            clearOnTabSelectedListeners()
            searchTargeti?.let { getTabAt(it)?.select() }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    searchTargeti = tab.position
                }
            })
        }
        var sorti = viewModel.sort.value
        binding.tablayoutSort.apply {
                clearOnTabSelectedListeners()
                sorti?.let { getTabAt(it)?.select() }
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabReselected(tab: TabLayout.Tab?) {}

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabSelected(tab: TabLayout.Tab) {
                        sorti = tab.position
                    }
                })
            }

        binding.toggle.apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                binding.pickDateLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
            isChecked = viewModel.endDate.value != null && viewModel.startDate.value != null
        }
        var hideBookmarked = viewModel.hideBookmarked.value!!
        val toggleShowTitle = binding.toggleShowTitle
        binding.toggleShow.apply {
            isChecked = hideBookmarked % 2 != 0
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (viewModel.pre.getBoolean("enableonlybookmarked", false)) {
                    when (hideBookmarked) {
                        3 -> {
                            toggleShowTitle.text = getString(R.string.hide_bookmarked)
                        }
                        1 -> {
                            toggleShowTitle.text = getString(R.string.only_bookmarked)
                        }
                    }
                    hideBookmarked = (hideBookmarked + 1) % 4
                }
                else {
                    hideBookmarked = (hideBookmarked + 1) % 2
                }
            }
        }
        binding.pickButton.apply {
            var calendar = Calendar.getInstance()
            if (viewModel.startDate.value != null) {
                calendar = viewModel.startDate.value!!
                this.text = viewModel.startDate.value.generateDateString()
            }
            setOnClickListener {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val dateDialog = DatePickerDialog(
                    requireActivity(),
                    { p0, year1, month1, day1 ->
                        val monthR = month1 + 1
                        text = "$year1-$monthR-$day1"
                        val calendar1 = Calendar.getInstance()
                        calendar1.set(year1, month1, day1)
                        viewModel.startDate.value = calendar1
                    },
                    year,
                    month,
                    day
                )
                dateDialog.datePicker.maxDate = System.currentTimeMillis()
                dateDialog.show()
            }
        }
        binding.pickEndButton.apply {
            var calendar = Calendar.getInstance()
            if (viewModel.endDate.value != null) {
                calendar = viewModel.endDate.value!!
                this.text = viewModel.endDate.value.generateDateString()
            }
            setOnClickListener {
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val dateDialog = DatePickerDialog(
                    requireActivity(),
                    { p0, year1, month1, day1 ->
                        val monthR = month1 + 1
                        val calendar1 = Calendar.getInstance()
                        calendar1.set(year1, month1, day1)
                        text = "$year1-$monthR-$day1"
                        viewModel.endDate.value = calendar1
                    },
                    year,
                    month,
                    day
                )
                dateDialog.datePicker.maxDate = System.currentTimeMillis()
                dateDialog.show()
            }
        }

        builder
        .setNegativeButton(android.R.string.cancel) { p0, p1 -> }
        .setPositiveButton(android.R.string.ok) { p0, p1 ->
            viewModel.hideBookmarked.value = hideBookmarked
            viewModel.sort.value = sorti
            viewModel.searchTarget.value = searchTargeti
            if (word != null) {
                viewModel.firstSetData(word)
            }
        }
    }
}
