package com.mydear.haru;

import static android.content.ContentValues.TAG;

import android.util.Log;

public class JSONParser {
    public JSONParser() {

    }

    public static String getJsonObject(String str, String key) {
        int length = str.length();

        str = str.substring(0, 1).equals("{") ? str.substring(1, length) : str;
        str = str.substring(length - 2, length - 1).equals("}") ?
                str.substring(0, length - 2) : str;
        str = "," + str.replace(" ", "") + ",";   // 앞뒤에 "," 추가 및 공백 제거
        Log.e(TAG, "str 확인용 " + str);

        length = str.length();
        boolean find = true;
        int index = 0;
        while(find) {  // find == false 일때까지
            index = str.indexOf("," + key + "=", index) + 1;

            find = index > 0;  // index > 0 ? true : false (즉, 1 이상이면 true, 아니라면 false)

            if(find) {
                String res = str.substring((index + 1) + key.length(), length);
                if(res.substring(0, 1).equals("{")) {
                    //res = res.substring(0, res.indexOf("}") + 1);
                    int countBeginSymbol = 0;
                    for(int i = 0; i < length - 1; i++) {
                        String first = res.substring(i, i + 1);
                        if(first.equals("}")) {
                            countBeginSymbol = countBeginSymbol - 1;
                        }
                        else if(first.equals("{")) {
                            countBeginSymbol = countBeginSymbol + 1;
                        }

                        if(countBeginSymbol <= 0) {
                            res = res.substring(0, i + 1).replaceAll("nbsp;", " ");
                            res = res.replace("*", "nbsp;");
                            res = res.replace("&middot;", "·");
                            break;
                        }
                    }
                }
                else {
                    res = res.substring(0, res.indexOf(",")).replaceAll("nbsp;", " ");
                    res = res.replace("*", "nbsp;");
                    res = res.replace("&middot;", "·");
                    Log.e(TAG, "res확인용 " + res);
                }

                return res;
            }
        }

        return null;
    }

    String JSONParse(String str, String key) {
        str = str.replace("{", "");
        str = str.replace("}", "");
        str = str.replace(" ", "");

        String[] objs = str.split(",");

        for(int i = 0; i < objs.length; i++) {
            if(objs[i].contains(key + "=")) {
                try {
                    return objs[i].substring(objs[i].indexOf("=") + 1, objs[i].length());
                }
                catch(Exception e) {
                    return null;
                }
            }
        }

        return null;
    }
}
