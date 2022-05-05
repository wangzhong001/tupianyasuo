package com.qihe.imagecompression.util;


public class TimerUtils {


    /**
     * 将秒转换为00:00
     *
     * @param seconds
     * @return
     */
    public static String intToTime(int seconds) {
        if (seconds <= 0) {
            return "00:00";
        }
        String time = "";

        int min = seconds / 60;
        if (min < 10) {
            time = time + "0" + min + ":";
        } else {
            time = time + min + ":";
        }

        int sec = seconds - min * 60;
        if (sec < 10) {
            time = time + "0" + sec;
        } else {
            time = time + sec;
        }

        return time;
    }


}
