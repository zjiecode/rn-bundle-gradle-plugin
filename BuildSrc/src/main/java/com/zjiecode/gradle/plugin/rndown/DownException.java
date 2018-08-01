package com.zjiecode.gradle.plugin.rndown;

/**
 * 下载出错的时候提示
 */
public class DownException extends RuntimeException {
    public DownException(String message) {
        super(message);
    }
}
