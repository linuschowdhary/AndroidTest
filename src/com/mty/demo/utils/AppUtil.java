package com.mty.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AppUtil {
    public static void startOtherApp(Context context) {
        // 打开wap活动页（目前活动基本是html页）,url为活动地址。
        String uriWap = "jhs://wap?url=http://taobao.com";

        // 打开商品详情，id为商品ID。
        String uriItem = "jhs://item?id=10000000555095";

        // 打开聚划算（仅打开首页可不填写tab参数，或者tab=1）
        // tab = 1：今日团，2：生活团，3：品牌团，4：聚宝盆， 5：我的
        String uriHome = "jhs://home?tab=1";
        try {
            // 带有复杂参数或者汉字的URL可以使用URLEncoder encode
            String hanzi = "http://www.baidu.com/s?wd=逆战";
            uriWap = "jhs://wap?url=" + URLEncoder.encode(hanzi, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 打开方式：
        Uri uri = Uri.parse(uriWap);
        // action为android平台默认的ACIONT_VIEW：android.intent.action.VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void startAppByPkg(Context context, String packageName) {
        try {

            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            intent.putExtra("alipay_user_id", "007");
            intent.putExtra("auth_code", "code999");

            intent.putExtra("app_id", "2013020213456");
            intent.putExtra("version", "1.0");
            intent.putExtra("alipay_client_version", "7.0.0.0627");

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
