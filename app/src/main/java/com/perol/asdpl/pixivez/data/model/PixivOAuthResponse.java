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

package com.perol.asdpl.pixivez.data.model;


/**
 * response : {"access_token":"SrsL1Z7tGhM6yArRKeGkfaZ-ID3TTiTdFWtLmJtvWBA","expires_in":3600,"token_type":"bearer","scope":"","refresh_token":"YeysYu5dgbu0tV1yckzLhJmUCMOyDmqTeriZfFh-UTw","user":{"profile_image_urls":{"px_16x16":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_16.jpg","px_50x50":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_50.jpg","px_170x170":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_170.jpg"},"id":"14713395","name":"Notsfsssf","account":"912756674","mail_address":"912756674@qq.com","is_premium":false,"x_restrict":2,"is_mail_authorized":true},"device_token":"eb3adb884046935fe8952c977b949209"}
 */
public class PixivOAuthResponse {

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    /**
     * access_token : SrsL1Z7tGhM6yArRKeGkfaZ-ID3TTiTdFWtLmJtvWBA
     * expires_in : 3600
     * token_type : bearer
     * scope :
     * refresh_token : YeysYu5dgbu0tV1yckzLhJmUCMOyDmqTeriZfFh-UTw
     * user : {"profile_image_urls":{"px_16x16":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_16.jpg","px_50x50":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_50.jpg","px_170x170":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_170.jpg"},"id":"14713395","name":"Notsfsssf","account":"912756674","mail_address":"912756674@qq.com","is_premium":false,"x_restrict":2,"is_mail_authorized":true}
     * device_token : DEPRECATED
     */
    public static class ResponseBean {

        private String access_token;
        private int expires_in;
        private String token_type;
        private String scope;
        private String refresh_token;
        private UserBean user;
        private String device_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

//        public String getDevice_token() {
//            return device_token;
//        }
//
//        public void setDevice_token(String device_token) {
//            this.device_token = device_token;
//        }

        /**
         * profile_image_urls : {"px_16x16":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_16.jpg","px_50x50":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_50.jpg","px_170x170":"https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_170.jpg"}
         * id : 14713395
         * name : Notsfsssf
         * account : 912756674
         * mail_address : 912756674@qq.com
         * is_premium : false
         * x_restrict : 2
         * is_mail_authorized : true
         */
        public static class UserBean {

            private ProfileImageUrlsBean profile_image_urls;
            private Long id;
            private String name;
            private String account;
            private String mail_address;
            private boolean is_premium;
            private int x_restrict;
            private boolean is_mail_authorized;

            public ProfileImageUrlsBean getProfile_image_urls() {
                return profile_image_urls;
            }

            public void setProfile_image_urls(ProfileImageUrlsBean profile_image_urls) {
                this.profile_image_urls = profile_image_urls;
            }

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getMail_address() {
                return mail_address;
            }

            public void setMail_address(String mail_address) {
                this.mail_address = mail_address;
            }

            public boolean getIs_premium() {
                return is_premium;
            }

            public void setIs_premium(boolean is_premium) {
                this.is_premium = is_premium;
            }

            public int getX_restrict() {
                return x_restrict;
            }

            public void setX_restrict(int x_restrict) {
                this.x_restrict = x_restrict;
            }

            public boolean isIs_mail_authorized() {
                return is_mail_authorized;
            }

            public void setIs_mail_authorized(boolean is_mail_authorized) {
                this.is_mail_authorized = is_mail_authorized;
            }

            /**
             * px_16x16 : https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_16.jpg
             * px_50x50 : https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_50.jpg
             * px_170x170 : https://i.pximg.net/user-profile/img/2018/06/11/22/00/29/14348260_c1f2b130248005062b7c6c358812160a_170.jpg
             */
            public static class ProfileImageUrlsBean {

                private String px_16x16;
                private String px_50x50;
                private String px_170x170;

                public String getPx_16x16() {
                    return px_16x16;
                }

                public void setPx_16x16(String px_16x16) {
                    this.px_16x16 = px_16x16;
                }

                public String getPx_50x50() {
                    return px_50x50;
                }

                public void setPx_50x50(String px_50x50) {
                    this.px_50x50 = px_50x50;
                }

                public String getPx_170x170() {
                    return px_170x170;
                }

                public void setPx_170x170(String px_170x170) {
                    this.px_170x170 = px_170x170;
                }
            }
        }
    }
}
