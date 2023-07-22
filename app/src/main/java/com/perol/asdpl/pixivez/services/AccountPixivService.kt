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

package com.perol.asdpl.pixivez.services

import com.perol.asdpl.pixivez.data.model.PixivAccountsEditResponse
import com.perol.asdpl.pixivez.data.model.PixivAccountsResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountPixivService {
    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    fun createProvisionalAccount(
        @Field("user_name") user_name: String,
        @Field("ref") ref: String,
        @Header("Authorization") paramString3: String
    ): Observable<PixivAccountsResponse>

    @FormUrlEncoded
    @POST("/api/login")
    fun login(
        @Field("pixiv_id") pixiv_id: String,
        @Field("password") password: String,
        @Field("captcha") captcha: String,
        @Field("g_recaptcha_response") g_recaptcha_response: String,
        @Field("ref") ref: String,
        @Field("source") source: String,
        @Field("return_to") return_to: String,
        @Field("recaptcha_enterprise_score_token") recaptcha_enterprise_score_token: String
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/api/account/edit")
    fun editAccount(
        @Field("new_mail_address") new_mail_address: String,
        @Field("new_user_account") new_user_account: String,
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String,
        @Header("Authorization") paramString5: String
    ): Observable<PixivAccountsEditResponse>
}
