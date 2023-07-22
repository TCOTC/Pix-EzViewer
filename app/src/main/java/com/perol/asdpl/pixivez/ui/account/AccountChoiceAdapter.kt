/*
 * MIT License
 *
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

package com.perol.asdpl.pixivez.ui.account

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.data.AppDataRepository
import com.perol.asdpl.pixivez.data.entity.UserEntity
import kotlinx.coroutines.runBlocking

class AccountChoiceAdapter(layoutResId: Int, data: List<UserEntity>) :
    BaseQuickAdapter<UserEntity, BaseViewHolder>(layoutResId, data.toMutableList()) {

    override fun convert(holder: BaseViewHolder, item: UserEntity) {
        val userImage = holder.getView<ImageView>(R.id.imageView4)
        Glide.with(context).load(item.userimage).circleCrop().into(userImage)
        holder.setImageResource(R.id.imageview_delete, R.drawable.ic_action_del)
            .setText(R.id.textView4, item.username)
            .setText(R.id.textview_email, item.useremail)
        val delete = holder.getView<ImageView>(R.id.imageview_delete)
        delete.setOnClickListener {
            runBlocking {
                AppDataRepository.deleteUser(item)
                this@AccountChoiceAdapter.remove(item)
            }
        }
        if (holder.layoutPosition == AppDataRepository.pre.getInt(
                "usernum",
                0
            )
        ) {
            (delete.parent as ViewGroup).isClickable = false
            delete.isClickable = false
            holder.setImageResource(R.id.imageview_delete, R.drawable.ic_check_black_24dp)
        } else {
            (delete.parent as ViewGroup).isClickable = true
            delete.isClickable = true
            holder.setImageResource(R.id.imageview_delete, R.drawable.ic_close_black_24dp)
        }
//        delete.colorFilter = LightingColorFilter(Color.BLACK, Color.BLACK)
    }
}
