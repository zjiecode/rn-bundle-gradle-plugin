package com.zjiecode.gradle.plugin.rndown.utils;

import com.zjiecode.gradle.plugin.rndown.DownException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载工具
 */
public class DownloadUtil {
    public static void down(String in, String out) throws Exception {
        down(in, out, null);
    }

    public static void down(String in, String out, ProgressListener progressListener) throws Exception {
        URL url = new URL(in);
        //得到connection对象。
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //设置请求方式
        connection.setRequestMethod("GET");
        //连接
        connection.connect();
        //得到响应码
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            File outFile = new File(out);
            if (outFile.exists()) {
                outFile.delete();
            }
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            outFile.createNewFile();

            //得到响应流
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(out);
            int total = connection.getContentLength();
            int progress = 0;
            int showProgress = 0;
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                progress += len;
                if (progressListener != null && ((float) (progress - showProgress) / total) > 0.01f) {
                    progressListener.onProgress(progress, total);
                    showProgress = progress;
                }
            }
            //最后发送一次进度，表示完成
            if (progressListener != null) {
                progressListener.onProgress(total, total);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } else {
            throw new DownException("下载出错,错误码[" + responseCode + "]");
        }
    }

    /**
     * 下载进度接口
     */
    public interface ProgressListener {
        void onProgress(int progress, int total);
    }
}
