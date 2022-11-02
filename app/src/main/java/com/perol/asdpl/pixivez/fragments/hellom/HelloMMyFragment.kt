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

package com.perol.asdpl.pixivez.fragments.hellom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.tabs.TabLayout
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.adapters.PicListAdapter
import com.perol.asdpl.pixivez.adapters.PicListBtnAdapter
import com.perol.asdpl.pixivez.databinding.FragmentHelloMmyBinding
import com.perol.asdpl.pixivez.fragments.BaseFragment
import com.perol.asdpl.pixivez.objects.AdapterRefreshEvent
import com.perol.asdpl.pixivez.objects.IllustFilter
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.viewmodel.HelloMMyViewModel
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HelloMMyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HelloMMyFragment : BaseFragment() {
    override fun loadData() {
        viewmodel.onRefresh(restrict)
    }

    override fun onResume() {
        isLoaded = picListAdapter.data.isNotEmpty()
        super.onResume()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: AdapterRefreshEvent) {
        runBlocking {
            picListAdapter.notifyDataSetChanged()
        }
    }

    private lateinit var picListAdapter: PicListAdapter
    lateinit var viewmodel: HelloMMyViewModel
    var restrict = "all"
    private fun initViewModel() {
        viewmodel.illusts.observe(viewLifecycleOwner) {
            binding.swiperefreshLayout.isRefreshing = false
            if (it == null) {
                picListAdapter.loadMoreFail()
            }
            else {
                picListAdapter.setNewInstance(it)
                binding.recyclerview.scrollToPosition(0)
            }
        }
        viewmodel.addillusts.observe(viewLifecycleOwner) {
            if (it != null) {
                picListAdapter.addData(it)
            }
            else {
                picListAdapter.loadMoreFail()
            }
        }
        viewmodel.nextUrl.observe(viewLifecycleOwner) {
            if (it == null) {
                picListAdapter.loadMoreEnd()
            }
            else {
                picListAdapter.loadMoreComplete()
            }
        }
        viewmodel.hideBookmarked.observe(viewLifecycleOwner) {
            if (it != null) {
                PxEZApp.instance.pre.edit().putBoolean(
                    "hide_bookmark_item_in_mmy",
                    it
                ).apply()
                picListAdapter.illustFilter.hideBookmarked = if (it) 1 else 0
            }
        }
        viewmodel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swiperefreshLayout.isRefreshing = it
        }
    }

    private var param1: String? = null
    private var param2: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
        viewmodel = ViewModelProvider(this)[HelloMMyViewModel::class.java]
    }

    private lateinit var binding: FragmentHelloMmyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelloMmyBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var exitTime = 0L
    lateinit var filter: IllustFilter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewmodel.hideBookmarked.value = PxEZApp.instance.pre
            .getBoolean("hide_bookmark_item_in_mmy", false)
        filter = IllustFilter(isR18on, blockTags, if (viewmodel.hideBookmarked.value!!) 1 else 0)
        picListAdapter = PicListBtnAdapter(
            R.layout.view_recommand_item,
            null,
            filter
        )
        binding.recyclerview.apply {
            layoutManager = StaggeredGridLayoutManager(
                2 * context.resources.configuration.orientation,
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = picListAdapter
        }
        binding.swiperefreshLayout.setOnRefreshListener {
            viewmodel.onRefresh(restrict)
        }
        picListAdapter.loadMoreModule.setOnLoadMoreListener {
            viewmodel.onLoadMore()
        }
        val headerView = layoutInflater.inflate(R.layout.header_mmy, null)
        picListAdapter.addHeaderView(headerView)
        headerView.findViewById<SwitchMaterial>(R.id.swith_hidebookmarked).apply {
            isChecked = viewmodel.hideBookmarked.value!!
            setOnCheckedChangeListener { _, state ->
                viewmodel.hideBookmarked.value = state
            }
        }
        headerView.findViewById<Spinner>(R.id.spinner_mmy).apply {
            setSelection(0, false)
            }
            .onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                restrict = when (position) {
                    0 -> "all"
                    1 -> "public"
                    2 -> "private"
                    else -> "all"
                }
                viewmodel.onRefresh(restrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        // parentFragment?.view?.findViewById<TabLayout>(R.id.tablayout)? 重复ID问题导致只有单个有用
        ((parentFragment?.view as ViewGroup?)?.getChildAt(0) as TabLayout?)?.getTabAt(0)
            ?.view?.setOnClickListener {
                if ((System.currentTimeMillis() - exitTime) > 3000) {
                    Toast.makeText(
                        PxEZApp.instance,
                        getString(R.string.back_to_the_top),
                        Toast.LENGTH_SHORT
                    ).show()
                    exitTime = System.currentTimeMillis()
                }
                else {
                    binding.recyclerview.scrollToPosition(0)
                }
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HelloMMyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            HelloMMyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}
