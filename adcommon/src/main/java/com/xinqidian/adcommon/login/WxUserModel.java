package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class WxUserModel {

    /**
     * msg : 操作成功
     * code : 200
     * data : {"uname":"","iconImg":"","mobile":"15171418960","expireDate":""}
     */



    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         *  "country": "CN",
         *     "userLevel": null,
         *   "province": "Hubei",
         *     "city": "Wuhan",
         *   "openid": "oLlgF6qNBpPxOrMpOtfC9Gspo4aw",
         *     "sex": "男",
         *     "nickname": "吴红强",
         *   "headimgurl": "https:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/Q0j4TwGTfTJH3DIrHZmWswKwyQMlZluuGvohbGkWxWSYKmvOYNPO0NiaZPB7F1P7rI0GpAic5G2kc4B4ibCI5njpw\/132",
         *     "expireDate": null,
         *    "freeCount": 0
         *
         *
         */

        private String country;
        private int userLevel;
        private String province;
        private String city;
        private String openid;
        private String sex;
        private String nickname;
        private String headimgurl;
        private String expireDate;
        private int freeCount;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public int getFreeCount() {
            return freeCount;
        }

        public void setFreeCount(int freeCount) {
            this.freeCount = freeCount;
        }

     /*   private String uname;
        private String iconImg;
        private String mobile;
        private String expireDate;
        private int userLevel;*/


    }
}
