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

package com.perol.asdpl.pixivez.services

import com.perol.asdpl.pixivez.data.model.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AppApiPixivService {

    @GET("v1/walkthrough/illusts")
    fun walkthroughIllusts(): Observable<IllustNext>

    //    @FormUrlEncoded
    //    @POST("/v1/mute/edit")
    //    public abstract l<PixivResponse> postMuteSetting(@Header("Authorization") String paramString, @Field("add_user_ids[]") List<Long> paramList1, @Field("delete_user_ids[]") List<Long> paramList2, @Field("add_tags[]") List<String> paramList3, @Field("delete_tags[]") List<String> paramList4);
    @GET("/v2/illust/bookmark/detail")
    fun getLikeIllustDetail(
        // @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long
    ): Observable<BookMarkDetailResponse>

    @GET("v1/user/bookmark-tags/illust")
    fun getIllustBookmarkTags(
        // @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: Long,
        @Query("restrict") restrict: String
    ): Observable<BookMarkTagsResponse>

    @GET("/v1/spotlight/articles?filter=for_android")
    fun getPixivisionArticles(
        // @Header("Authorization") paramString1: String,
        @Query("category") category: String
    ): Observable<SpotlightResponse>

    @GET("/v1/user/recommended?filter=for_android")
    fun getUserRecommended(
        // @Header("Authorization") paramString: String
        @Query("offset") offset: Int?=0
    ): Observable<SearchUserResponse>

    @Streaming
    @GET
    fun getGIFFile(@Url fileUrl: String): Observable<ResponseBody>

    @Multipart
    @POST("/v1/user/profile/edit")
    fun postUserProfileEdit(
        // @Header("Authorization") paramString: String,
        @Part paramRequestBody: MultipartBody.Part
    ): Observable<ResponseBody>

    @GET("/v1/ugoira/metadata")
    fun getUgoiraMetadata(
        // @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long
    ): Observable<UgoiraMetadataResponse>

    @GET("/v1/user/browsing-history/illusts")
    fun getIllustBrowsingHistory(
        // @Header("Authorization") paramString: String
    ): Observable<IllustNext>

    @GET("/v1/user/bookmarks/illust")
    fun getLikeIllust(
        // @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: Long,
        @Query("restrict") restrict: String,
        @Query("tag") tag: String?
    ): Observable<IllustNext>

    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    fun postUnfollowUser(
        // @Header("Authorization") paramString: String,
        @Field("user_id") user_id: Long
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    fun postFollowUser(
        // @Header("Authorization") paramString1: String,
        @Field("user_id") user_id: Long,
        @Field("restrict") restrict: String
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    fun postUnlikeIllust(
        // @Header("Authorization") paramString: String,
        @Field("illust_id") illust_id: Long
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    fun postLikeIllust(
        // @Header("Authorization") paramString1: String,
        @Field("illust_id") illust_id: Long,
        @Field("restrict") restrict: String,
        @Field("tags[]") tagList: List<String>?
    ): Observable<ResponseBody>

    @GET("/v2/search/autocomplete?merge_plain_keyword_results=true")
    fun getSearchAutoCompleteKeywords(
        // @Header("Authorization") paramString1: String,
        @Query("word") word: String?
    ): Observable<PixivResponse>

    @GET("/v1/trending-tags/illust?filter=for_android")
    fun getIllustTrendTags(
        // @Header("Authorization") paramString: String
    ): Observable<TrendingtagResponse>

    //    &start_date=2019-11-24&end_date=2019-12-01
    @GET("/v1/search/illust?filter=for_android&merge_plain_keyword_results=true")
    fun getSearchIllust(
        // @Header("Authorization") paramString5: String,
        @Query("word") word: String,
        @Query("sort") sort: String,
        @Query("search_target") search_target: String?,
        @Query("start_date") start_date: String?,
        @Query("end_date") end_date: String?,
        @Query("bookmark_num") paramInteger: Int?
    ): Observable<SearchIllustResponse>

    @GET("/v1/search/popular-preview/illust?filter=for_android&merge_plain_keyword_results=true")
    fun getSearchIllustPreview(
        // @Header("Authorization") paramString5: String,
        @Query("word") word: String,
        @Query("sort") sort: String,
        @Query("search_target") search_target: String?,
        @Query("bookmark_num") paramInteger: Int?,
        @Query("duration") duration: String?
    ): Observable<SearchIllustResponse>

    @GET("/v1/search/popular-preview/illust?filter=for_android")
    fun getPopularPreviewIllust(
        // @Header("Authorization") paramString1: String,
        @Query("word") word: String,
        @Query("search_target") search_target: String,
        @Query("duration") duration: String
    ): Call<PixivResponse>

    @GET("/v1/search/novel")
    fun getSearchNovel(
        // @Header("Authorization") paramString1: String,
        @Query("word") word: String,
        @Query("sort") sort: String,
        @Query("search_target") search_target: String,
        @Query("bookmark_num") paramInteger: Int?,
        @Query("duration") duration: String
    ): Call<PixivResponse>

    @GET("/v1/search/user?filter=for_android")
    fun getSearchUser(
        // @Header("Authorization") paramString1: String,
        @Query("word") word: String
    ): Observable<SearchUserResponse>

    @GET("/v1/user/follower?filter=for_android")
    fun getUserFollower(
        // @Header("Authorization") paramString: String,
        @Query("user_id") paramLong: Long
    ): Observable<SearchUserResponse>

    @GET("/v1/user/following?filter=for_android")
    fun getUserFollowing(
        // @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: Long,
        @Query("restrict") restrict: String
    ): Observable<SearchUserResponse>

    @GET("/v1/illust/recommended?filter=for_android&include_ranking_label=true")
    fun getRecommend(
        // @Header("Authorization") Authorization: String
    ): Observable<RecommendResponse>

    @GET("/v1/illust/detail?filter=for_android")
    fun getIllust(
        // @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long
    ): Observable<IllustDetailResponse>

    @GET("/v1/illust/detail?filter=for_android")
    suspend fun getIllustCor(
        // @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long
    ): IllustDetailResponse

    @GET("/v2/illust/related?filter=for_android")
    fun getIllustRecommended(
        // @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long
    ): Observable<RecommendResponse>

    @FormUrlEncoded
    @POST("/v2/user/browsing-history/illust/add")
    fun postAddIllustBrowsingHistory(
        // @Header("Authorization") paramString: String,
        @Field("illust_ids[]") illust_idList: List<Long>
    ): Observable<ResponseBody>

    @GET("/v1/illust/ranking?filter=for_android")
    fun getIllustRanking(
        // @Header("Authorization") paramString1: String,
        @Query("mode") mode: String,
        @Query("date") date: String?
    ): Observable<IllustNext>

    @GET("/v1/illust/ranking?filter=for_android")
    fun getIllustRanking1(
        // @Header("Authorization") paramString1: String,
        @Query("mode") mode: String,
        @Query("date") date: String
    ): Observable<ResponseBody>

    @GET("/v2/illust/follow")
    fun getFollowIllusts(
        // @Header("Authorization") paramString1: String,
        @Query("restrict") restrict: String
    ): Observable<IllustNext>

    @GET("/v1/illust/bookmark/users")
    fun getIllustBookmarkUsers(
        // @Header("Authorization") token: String?,
        @Query("illust_id") illust_id: Long,
        @Query("offset") offset: Int? = 0
    ): Observable<ListUserResponse>

    @GET("/v1/illust/comments")
    fun getIllustComments(
        // @Header("Authorization") paramString: String,
        @Query("illust_id") paramLong: Long,
        @Query("offset") offset: Int? = 0,
        @Query("include_total_comments") include_total_comments: Boolean? = false
    ): Observable<IllustCommentsResponse>

    @FormUrlEncoded
    @POST("/v1/illust/comment/add")
    fun postIllustComment(
        // @Header("Authorization") paramString1: String,
        @Field("illust_id") illust_id: Long,
        @Field("comment") comment: String,
        @Field("parent_comment_id") parent_comment_id: Int?
    ): Observable<ResponseBody>

    @GET("/v1/user/detail?filter=for_android")
    fun getUserDetail(
        // @Header("Authorization") paramString: String,
        @Query("user_id") id: Long
    ): Observable<UserDetailResponse>

    @GET("/v1/user/illusts?filter=for_android")
    fun getUserIllusts(
        // @Header("Authorization") paramString1: String,
        @Query("user_id") paramLong: Long,
        @Query("type") type: String
    ): Observable<IllustNext>

    @GET
    fun getUrl(
        // @Header("Authorization") paramString1: String,
        @Url url: String
    ): Observable<ResponseBody>
}
