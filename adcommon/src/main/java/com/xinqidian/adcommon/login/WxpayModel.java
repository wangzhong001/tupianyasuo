package com.xinqidian.adcommon.login;

/**
 * Created by lipei on 2020/6/9.
 */

public class WxpayModel {

    /**
     "msg": "操作成功",
     "code": 200,
     "data": {
     "appid": "wx7f3060cd7200e2e0",
     "noncestr": "wODZdttB7KND88sOYk0z1t6xznufC4r0",
     "partnerid": "1606984314",
     "prepayid": "wx17192129192803ae64c181d21c90610000",
     "sign": "E48C515203E2734F7C0DE251714DFB12",
     "timestamp": "1615980089"
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
         * "appid": "wx7f3060cd7200e2e0",
         *      "noncestr": "wODZdttB7KND88sOYk0z1t6xznufC4r0",
         *      "partnerid": "1606984314",
         *      "prepayid": "wx17192129192803ae64c181d21c90610000",
         *      "sign": "E48C515203E2734F7C0DE251714DFB12",
         *      "timestamp": "1615980089"
         */

        private String appid;
        private String noncestr;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;


        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "appid='" + appid + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", sign='" + sign + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
