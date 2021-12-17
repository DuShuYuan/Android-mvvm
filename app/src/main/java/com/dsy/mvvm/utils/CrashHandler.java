package com.dsy.mvvm.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 *
 * @author user
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    //CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    //日志路径
    private String path;
    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //程序的Context对象
    private Context mContext;

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        path = context.getExternalCacheDir() + "/log/";
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        //保存日志文件
        String fileName = saveCrashInfo2File(ex);
        //上传文件
        uploadExceptionToServer(fileName);

//        if (BuildConfig.DEBUG) {
//            //让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, ex);
//        }else {
            try {
                //延迟3秒上传日志
                Thread.sleep(3000);
                //退出App
//                AppUtils.exitApp();
            } catch (InterruptedException e) {
            }
//        }

    }


    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {


        final StringBuilder sb = new StringBuilder();
        final String head = "************* Log Head ****************" +
                "\nTime Of Crash      : " + new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())) +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +
                "\nDevice Model       : " + Build.MODEL +
                "\nAndroid Version    : " + Build.VERSION.RELEASE +
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
//                "\nApp VersionName    : " + BuildConfig.VERSION_NAME +
//                "\nApp VersionCode    : " + BuildConfig.VERSION_CODE +
                "\n************* Log Head ****************\n\n";
        sb.append(head);

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = new SimpleDateFormat("MM-dd").format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 将错误信息上传至服务器
     */
    private void uploadExceptionToServer(String fileName) {

//                String filePath = path + fileName;
//                if (new File(filePath).exists()) {
//                    UploadUtil.getInstance().uploadFile(filePath, "file", url, parms);
//                }


    }
}
