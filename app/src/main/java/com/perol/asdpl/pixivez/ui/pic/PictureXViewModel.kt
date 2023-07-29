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

package com.perol.asdpl.pixivez.ui.pic

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.perol.asdpl.pixivez.base.BaseViewModel
import com.perol.asdpl.pixivez.data.AppDatabase
import com.perol.asdpl.pixivez.data.entity.IllustBeanEntity
import com.perol.asdpl.pixivez.data.model.BookmarkDetailBean
import com.perol.asdpl.pixivez.data.model.Illust
import com.perol.asdpl.pixivez.objects.InteractionUtil.visRestrictTag
import com.perol.asdpl.pixivez.objects.Toasty
import com.perol.asdpl.pixivez.services.PxEZApp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.lingala.zip4j.ZipFile
import java.io.File

class PictureXViewModel : BaseViewModel() {
    val illustDetail = MutableLiveData<Illust?>()
    val relatedPics = MutableLiveData<MutableList<Illust>?>()
    val relatedPicsAdded = MutableLiveData<MutableList<Illust>?>()
    val nextRelatedPics = MutableLiveData<String?>()
    val likeIllust = MutableLiveData<Boolean>()
    val followUser = MutableLiveData<Boolean>()
    var tags = MutableLiveData<BookmarkDetailBean>()
    val progress = MutableLiveData<Int>()
    val downloadGifSuccess = MutableLiveData<Boolean>()
    private val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
    fun downloadZip(medium: String) {
        val zipPath =
            "${PxEZApp.instance.cacheDir.path}/${illustDetail.value!!.id}.zip"
        val file = File(zipPath)
        if (file.exists()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    ZipFile(file).extractAll(
                        PxEZApp.instance.cacheDir.path + File.separatorChar + illustDetail.value!!.id
                    )
                    withContext(Dispatchers.Main) {
                        downloadGifSuccess.value = true
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                    Toasty.shortToast("Unzip Failed")
                    }
                    File(PxEZApp.instance.cacheDir.path + File.separatorChar + illustDetail.value!!.id).deleteRecursively()
                    file.delete()
                    reDownLoadGif(medium)
                }
            }
        } else {
            reDownLoadGif(medium)
        }
    }

    fun loadGif(id: Long) = retrofit.getUgoiraMetadata(id)

    private fun reDownLoadGif(medium: String) {
        val zipPath = "${PxEZApp.instance.cacheDir}/${illustDetail.value!!.id}.zip"
        val file = File(zipPath)
        progress.value = 0
        retrofit.getGIFFile(medium).subscribe({ response ->
            val inputStream = response.byteStream()
            Observable.create<Int> { ob ->
                val output = file.outputStream()
                Log.d("GIF", "----------")
                val totalLen = response.contentLength()
                var bytesCopied: Long = 0
                val buffer = ByteArray(8 * 1024)
                var bytes = inputStream.read(buffer)
                Log.d("GIF", Thread.currentThread().toString())
                while (bytes >= 0) {
                    output.write(buffer, 0, bytes)
                    bytesCopied += bytes
                    bytes = inputStream.read(buffer)
                    launchUI {
                        progress.value = (100 * bytesCopied / totalLen).toInt()
                    }
                }
                inputStream.close()
                output.close()
                Log.d("GIF", "++++${progress.value}++++")
                ZipFile(file).extractAll(
                    PxEZApp.instance.cacheDir.path + File.separatorChar + illustDetail.value!!.id
                )
                ob.onNext(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
                downloadGifSuccess.value = true
                Log.d("GIF", "wwwwwwwwwwwwwwwwwwwwww")
            }, {
                it.printStackTrace()
            }).add()
        }, {}, {}).add()
    }

    fun firstGet(illust: Illust) {
        illustDetail.value = illust
        likeIllust.value = illust.is_bookmarked
        CoroutineScope(Dispatchers.IO).launch {
            val ee = appDatabase.illusthistoryDao().getHistoryOne(illust.id)
            if (ee.isNotEmpty()) {
                appDatabase.illusthistoryDao().deleteOne(ee[0])
            }
            appDatabase.illusthistoryDao().insert(
                IllustBeanEntity(
                    illust.id,
                    illust.image_urls.square_medium
                )
            )
        }
    }

    fun firstGet(toLong: Long) {
        retrofit.getIllust(toLong).subscribe({
            firstGet(it.illust)
        }, {
            Toasty.warning(
                PxEZApp.instance,
                "PID 404: $toLong",
                Toast.LENGTH_SHORT
            ).show()
            illustDetail.value = null
        }, {}).add()
    }

    fun getRelated(pid: Long) {
        retrofit.getIllustRelated(pid).subscribeNext(relatedPics, nextRelatedPics)
    }

    fun fabClick() {
        val id = illustDetail.value!!.id
        val x_restrict = visRestrictTag(illustDetail.value!!)
        if (illustDetail.value!!.is_bookmarked) {
            retrofit.postUnlikeIllust(id).subscribe({
                likeIllust.value = false
                illustDetail.value!!.is_bookmarked = false
            }, {
            }, {}, {}).add()
        } else {
            retrofit.postLikeIllustWithTags(id, x_restrict, null).subscribe({
                likeIllust.value = true
                illustDetail.value!!.is_bookmarked = true
            }, {}, {}).add()
        }
    }

    fun fabOnLongClick() {
        if (illustDetail.value != null) {
            retrofit
                .getBookmarkDetail(illustDetail.value!!.id)
                .subscribe(
                    { tags.value = it.bookmark_detail },
                    {},
                    {}
                ).add()
        } else {
            val a = illustDetail.value
            print(a)
        }
    }

    fun onDialogClick(private: Boolean) {
        val toLong = illustDetail.value!!.id
        if (!illustDetail.value!!.is_bookmarked or private) {
            //TODO: default tag to add?
            val tagList = tags.value?.tags?.mapNotNull { if (it.is_registered) it.name else null }
            retrofit.postLikeIllustWithTags(toLong, visRestrictTag(private), tagList).subscribe({
                likeIllust.value = true
                illustDetail.value!!.is_bookmarked = true
            }, {}, {}).add()
        } else {
            retrofit.postUnlikeIllust(toLong)
                .subscribe({
                    likeIllust.value = false
                    illustDetail.value!!.is_bookmarked = false
                }, {}, {}).add()
        }
    }

    fun likeUser() {
        val id = illustDetail.value!!.user.id
        if (!illustDetail.value!!.user.is_followed) {
            retrofit.postFollowUser(id, "public").subscribe({
                followUser.value = true
                illustDetail.value!!.user.is_followed = true
            }, {}, {}).add()
        } else {
            retrofit.postUnfollowUser(id).subscribe(
                {
                    followUser.value = false
                    illustDetail.value!!.user.is_followed = false
                }, {}, {}).add()
        }
    }
}

//data class ProgressInfo(var now: Long, var all: Long)
