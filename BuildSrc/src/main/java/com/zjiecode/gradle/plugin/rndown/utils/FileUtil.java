package com.zjiecode.gradle.plugin.rndown.utils;

import javax.imageio.stream.FileImageOutputStream;
import java.io.*;

public class FileUtil {

    /**
     * 读取文件内容
     *
     * @param file 文件路径
     * @return
     */
    public static String read(String file) {
        if (file == null || "".equals(file)) {
            return null;
        }
        File f = new File(file);
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            return new String(outputStream.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入文件
     *
     * @param fileName 文件地址
     * @param content  内容
     */
    public static void write(String fileName, String content) {
        try {
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
            FileImageOutputStream outputStream = new FileImageOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 格式化文件大小
     *
     * @param size b
     * @return 方便阅读的大小
     */
    public static String convertFileSizeFormat(int size) {
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB"};
        double mod = 1024.0;
        int i = 0;
        for (i = 0; size >= mod; i++) {
            size /= mod;
        }
        return Math.round(size) + units[i];
    }
}
