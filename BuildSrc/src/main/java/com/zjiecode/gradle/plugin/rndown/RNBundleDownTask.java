package com.zjiecode.gradle.plugin.rndown;

import com.zjiecode.gradle.plugin.rndown.utils.DownloadUtil;
import com.zjiecode.gradle.plugin.rndown.utils.FileUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * 用于下载bundle的插件
 * 下载后，会在build/.react-native-bundle.version留下一个文件，标记本地bundle的版本。
 * clean的时候会清理掉build目录，但是不会清理本地bundle，
 * 在bundle的时候，因为缓存记录文件（build/.react-native-bundle.version）不存在，会重新去下载。
 */
public class RNBundleDownTask extends DefaultTask {
    @Input
    private ConfigExtension config;
    private String projectRootDir;//项目根目录

    //记录本地下载的bundle版本，用于判断缓存
    private String saveVersion;
    private String saveFile;//下载后存放的文件

    public void setProjectRootDir(String projectRootDir) {
        this.projectRootDir = projectRootDir;
        saveVersion = projectRootDir + File.separator + "build" + File.separator + ".react-native-bundle.version";
        saveFile = projectRootDir + File.separator + config.getOutFile();
    }

    public void setConfig(ConfigExtension config) {
        this.config = config;
    }

    @TaskAction
    private void action() {
        checkArguments();
        if (localIsTarget()) {
            System.out.printf("本地bundle版本匹配(verson=%s)，跳过下载bundle", config.getVersion());
            return;
        }
        String[] paths = config.getPaths();
        File outFile = new File(saveFile);
        //如果存在，就先删除再下载
        if (outFile.exists()) {
            outFile.delete();
        }
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            try {
                String file = String.format("%s" + config.getFileName(), path, config.getVersion());
                System.out.println("开始下载bundle:" + file);
                DownloadUtil.down(file, saveFile, (int progress, int total) -> {
                    float proF = ((float) progress / total * 100f);
                    String down = FileUtil.convertFileSizeFormat(progress);
                    String all = FileUtil.convertFileSizeFormat(total);
                    System.out.printf("bundle(version:%s)-[%.2f%%]%s/%s\n", config.getVersion(), proF, down, all);
                });
                System.out.println("bundle下载完成.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(path + "->" + e.getMessage());
                System.out.println("尝试下一个下载路径 ...");
            }
        }

        if (!outFile.exists()) {
            throw new DownException("下载bundle文件失败");
        } else {
            System.out.printf("本地保存位置:%s\n",saveFile);
            FileUtil.write(saveVersion, config.getVersion());
        }
    }

    /**
     * 检查参数是否都合法
     *
     * @return
     */
    private void checkArguments() {
        ifArgumentError(config.getFileName(), "请在build.gradle配置outFile，指明下载文件的名字");
        ifArgumentError(config.getOutFile(), "请在build.gradle配置outFile，指明下载文件保存位置");
        ifArgumentError(config.getVersion(), "请在build.gradle配置version，指明下载文件版本");
        if (config.getPaths() == null || config.getPaths().length == 0) {
            throw new DownException("请在build.gradle配置paths,指明下载文件远程地址");
        }
    }

    /**
     * 当参数不合法的时候，抛出异常，中断过程
     *
     * @param arg 参数
     * @param msg 出错信息
     */
    private void ifArgumentError(String arg, String msg) {
        if (arg == null || "".equals(arg)) {
            throw new DownException(msg);
        }
    }

    /**
     * 判断本地版本，是不是目标本版本
     *
     * @return true  表示，本地存在目标版本，不用去下载了
     */
    private boolean localIsTarget() {
        File outFile = new File(saveFile);
        if (outFile.exists() && outFile.isFile()) {
            String read = FileUtil.read(saveVersion);
            if (config.getVersion().equals(read)) {
                return true;
            }
        }
        return false;
    }
}
