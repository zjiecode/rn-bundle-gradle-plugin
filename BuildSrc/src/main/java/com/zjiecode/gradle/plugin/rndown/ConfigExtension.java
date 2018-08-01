package com.zjiecode.gradle.plugin.rndown;

/**
 * 下载bundle文件的配置
 */
class ConfigExtension {

    private String[] paths;//下载路径，可以是多个环境的
    private String version;//版本
    private String fileName;//远程文件名
    private String outFile;//下载以后，存放的位置

    public ConfigExtension() {
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOutFile() {
        return outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }
}