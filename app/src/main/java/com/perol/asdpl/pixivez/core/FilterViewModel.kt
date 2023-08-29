/*
 * MIT License
 *
 * Copyright (c) 2022 ultranity
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

package com.perol.asdpl.pixivez.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.base.DMutableLiveData
import com.perol.asdpl.pixivez.base.KotlinUtil.plus
import com.perol.asdpl.pixivez.base.KotlinUtil.times
import com.perol.asdpl.pixivez.data.model.Illust
import com.perol.asdpl.pixivez.databinding.DialogPicListFilterBinding
import com.perol.asdpl.pixivez.objects.FileUtil
import com.perol.asdpl.pixivez.objects.KVData
import com.perol.asdpl.pixivez.services.PxEZApp
import kotlin.reflect.KMutableProperty0

enum class ADAPTER_TYPE {
    PIC_LIKE,
    PIC_USER_LIKE,
    PIC_BTN,
    PIC_USER_BTN,
}

//PicListFilterKV(TAG)

open class PicListFilterKV(tag: String) : KVData(tag) {
    init {
        kv.disableAutoCommit()
    }

    fun applyConfig() {
        kv.commit()
    }

    var max_sanity by int("max_sanity", 8)

    var minLike by int("minLike", 0)
    var minViewed by int("minViewed", 0)
    var minLikeRate by int("minLikeRate", 0)

    var showBlocked by boolean("showBlocked", false)

    var showBookmarked by boolean("showBookmarked", true)
    var showNotBookmarked by boolean("showNotBookmarked", true)
    var showDownloaded by boolean("showDownloaded", true)
    var showNotDownloaded by boolean("showNotDownloaded", true)

    var showIllust by boolean("showIllust", true)
    var showManga by boolean("showManga", true)
    var showUgoira by boolean("showUgoira", true)

    var showFollowed by boolean("showFollowed", true)
    var showNotFollowed by boolean("showNotFollowed", true)

    var showAINone by boolean("showAINone", true)
    var showAIHalf by boolean("showAIHalf", true)
    var showAIFull by boolean("showAIFull", true)

    var showPrivate by boolean("showPrivate", true)
    var showPublic by boolean("showPublic", true)

    var startTime by long("startTime", 0)
    var endTime by long("endTime", Long.MAX_VALUE)

    var sortTime by boolean("sortTime", false)
    var sortLike by boolean("sortLike", false)
    var sortLikeRate by boolean("sortLikeRate", false)
}

fun checkTrilean(showTrue: Boolean, showFalse: Boolean, bool: Boolean): Boolean {
    //(!showBookmarked && item.is_bookmarked) || (!showNotBookmarked && !item.is_bookmarked)
    return (!(showTrue and showFalse)) && (showTrue xor bool)
}

class PicsFilter(tag: String) : PicListFilterKV(tag) {
    var blockTags: List<String>? = null
    var blockUser: List<Int>? = null
    fun needHide(item: Illust): Boolean =
        checkTrilean(showPrivate, showPublic, item.x_restrict == 1) ||
                checkTrilean(showBookmarked, showNotBookmarked, item.is_bookmarked) ||
                checkTrilean(showFollowed, showNotFollowed, item.user.is_followed) ||
                checkTrilean(showDownloaded, showNotDownloaded, FileUtil.isDownloaded(item)) ||
                (!showAINone && item.illust_ai_type == 0) || (!showAIHalf && item.illust_ai_type == 1) || (!showAIFull && item.illust_ai_type == 2) ||
                (!showIllust && (item.type == "illust")) || (!showManga && (item.type == "manga")) || (!showUgoira && (item.type == "ugoira"))
    //|| (minLike > item.total_bookmarks) || (minViewed > item.total_view) || (minLikeRate*item.total_view> item.total_bookmarks)

    fun needBlock(item: Illust): Boolean {
        if (blockTags.isNullOrEmpty() and blockUser.isNullOrEmpty()) {
            return false
        }
        val tags = item.tags.map{ it.name }
        if (tags.isNotEmpty()) {
            // if (blockTags.intersect(tags).isNotEmpty())
            for (i in blockTags!!) {
                if (tags.contains(i)) {
                    return true
                }
            }
        }
        return blockUser?.contains(item.user.id) == true
    }

    fun blockSanity(item: Illust): Boolean {
        return ((max_sanity != 8) && (item.sanity_level > max_sanity))
    }
}

class FilterViewModel : ViewModel() {
    var TAG = "FilterViewModel"
    val spanNum = DMutableLiveData(2)
    lateinit var filter: PicsFilter
    var adapterType = DMutableLiveData(ADAPTER_TYPE.PIC_USER_LIKE)
    var modeCollect = false
    fun init(tag: String) {
        TAG = tag
        filter = PicsFilter(TAG)
        if (!PxEZApp.instance.pre.getBoolean("r18on", false))
            filter.showPrivate = false
    }

    fun applyConfig() = filter.applyConfig()
    fun getAdapter() = if (modeCollect) {
        DownPicListAdapter(R.layout.view_ranking_item_s, null, filter)
    } else when (adapterType.value) {
        ADAPTER_TYPE.PIC_BTN -> PicListBtnAdapter(R.layout.view_recommand_item, null, filter)
        ADAPTER_TYPE.PIC_USER_BTN -> PicListBtnUserAdapter(R.layout.view_ranking_item, null, filter)
        ADAPTER_TYPE.PIC_LIKE -> PicListXAdapter(R.layout.view_recommand_item_s, null, filter)
        ADAPTER_TYPE.PIC_USER_LIKE -> PicListXUserAdapter(
            R.layout.view_ranking_item_s,
            null,
            filter
        )

        else -> PicListXUserAdapter(R.layout.view_ranking_item_s, null, filter)
    }
}

fun showFilterDialog(
    context: Context,
    filterModel: FilterViewModel,
    picListAdapter: PicListAdapter,
    layoutInflater: LayoutInflater,
    layoutManager: StaggeredGridLayoutManager,
    configAdapter: () -> Unit
): MaterialDialog {
    val propsMap = hashSetOf<Pair<Checkable, KMutableProperty0<Boolean>>>()
    fun pairBtnFilter(view: Checkable, props: KMutableProperty0<Boolean>) {
        propsMap.add(view to props)
        view.isChecked = props.get()
    }

    val dialog = DialogPicListFilterBinding.inflate(layoutInflater)
    dialog.apply {
        if (!PxEZApp.instance.pre.getBoolean("init_download_filter", false))
            toggleDownload.setOnClickListener {
                showFilterDownloadDialog(context)
            }
        if (PxEZApp.instance.pre.getBoolean("r18on", false)) {
            dialog.toggleRestrict.visibility = View.VISIBLE
        }
        val filter = filterModel.filter
        sliderSanity.value = filter.max_sanity.toFloat()
        sliderSpan.value = layoutManager.spanCount.toFloat()
        //filterModel.spanNum.value!!.toFloat()
        pairBtnFilter(showBlocked, filter::showBlocked)

        pairBtnFilter(showBookmarked, filter::showBookmarked)
        pairBtnFilter(showNotBookmarked, filter::showNotBookmarked)

        pairBtnFilter(showDownloaded, filter::showDownloaded)
        pairBtnFilter(showNotDownloaded, filter::showNotDownloaded)

        pairBtnFilter(showFollowed, filter::showFollowed)
        pairBtnFilter(showNotFollowed, filter::showNotFollowed)

        pairBtnFilter(showIllust, filter::showIllust)
        pairBtnFilter(showManga, filter::showManga)
        pairBtnFilter(showUgoira, filter::showUgoira)

        pairBtnFilter(showAINone, filter::showAINone)
        pairBtnFilter(showAIHalf, filter::showAIHalf)
        pairBtnFilter(showAIFull, filter::showAIFull)

        pairBtnFilter(showPrivate, filter::showPrivate)
        pairBtnFilter(showPublic, filter::showPublic)
        listOf(
            showHideUserImg,
            showShowUserImg
        )[filterModel.adapterType.value!!.ordinal % 2].isChecked = true
        listOf(
            showHideSave,
            showShowSave
        )[filterModel.adapterType.value!!.ordinal / 2].isChecked = true
    }
    return MaterialDialog(context).show {
        customView(view = dialog.root, scrollable = true)
        positiveButton {
            propsMap.forEach {
                it.second.set(it.first.isChecked)
            }
            filterModel.filter.max_sanity = dialog.sliderSanity.value.toInt()
            filterModel.applyConfig()
            picListAdapter.resetFilterFlag()
            picListAdapter.notifyFilterChanged()
            val span = dialog.sliderSpan.value.toInt()
            layoutManager.spanCount = if (span == 0) filterModel.spanNum.value!! else span
            val adapterVersion = ADAPTER_TYPE.values()[
                dialog.showShowSave.isChecked * 2 + dialog.showShowUserImg.isChecked]
            if (filterModel.adapterType.checkUpdate(adapterVersion)) {
                val data = picListAdapter.mData
                configAdapter()
                picListAdapter.initData(data)
            } else {
                //TODO: check //picListAdapter.notifyDataSetChanged()
            }
        }
        negativeButton { }
    }
}

fun showFilterDownloadDialog(context: Context) = MaterialDialog(context).show {
    PxEZApp.instance.pre.edit {
        putBoolean("init_download_filter", true)
    }
    title(R.string.hide_downloaded)
    message(R.string.hide_downloaded_detail) {
        html()
    }
    positiveButton(R.string.I_know) { }
    neutralButton(R.string.download) {
        val uri = Uri.parse(context.getString(R.string.plink))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}