/*
 * MIT License
 *
 * Copyright (c) 2020 ultranity
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

package com.perol.asdpl.pixivez.manager

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.objects.FileInfo
import com.perol.asdpl.pixivez.objects.ThemeUtil
import com.perol.asdpl.pixivez.services.GlideApp


class ImgManagerAdapter(layoutResId: Int) : BaseQuickAdapter<FileInfo, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: FileInfo) {
        val icon = helper.getView<ImageView>(R.id.item_img)
        GlideApp.with(icon.context).load(item.icon.toIntOrNull()?:item.icon)
            .placeholder(ColorDrawable(ThemeUtil.halftrans)).into(icon)
        //helper.getView<ConstraintLayout>(R.id.layout).background
        helper.getView<TextView>(R.id.item_name).text = item.name
        helper.getView<TextView>(R.id.item_pid).text = item.pid.toString()
        helper.getView<TextView>(R.id.item_part).text = item.part
        helper.getView<TextView>(R.id.item_time).text = item.time
        helper.getView<TextView>(R.id.item_size).text = item.size
        helper.getView<TextView>(R.id.item_pixel).text = item.pixel
        helper.getView<TextView>(R.id.item_target).text = item.target?:""
        val check: CheckBox = helper.getView(R.id.item_check)
        check.isChecked = item.checked
        check.setOnCheckedChangeListener { buttonView, isChecked ->
            item.checked =isChecked
        }
        if (!item.isPic()){
            item.checked = false
            check.visibility = View.GONE
        }
        else{
            check.visibility = View.VISIBLE
        }
    }

}