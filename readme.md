## 安卓RN bundle下载插件
本插件使用IDEA开发，在Android studio上面使用.

### 使用方法

1、添加依赖

```groovy
classpath "com.zjiecode:rn-bundle-download:0.0.1"
```

2、应用插件

```groovy
apply plugin: 'mt-rn-bundle-download'
```

3、配置下载地址

```groovy
//Bundle下载配置信息，会在根目录/build下面生成一个版本缓存文件(.react-native-bundle.version)
RNDownloadConfig {
    //远程文件目录,因为有可能有多个目录，下载的时候，会去多个目录下面查找。
    paths = [
            'http://s3plus.sankuai.com/v1/mss_xxx==/rn-bundle-dev/xxx/',
            'http://s3plus-corp.sankuai.com/v1/mss_xxx==/rn-bundle-prod/xxx/'
            ]
    version = '1' //远程文件版本,每次修改版本，修改这里，就可以了。
    fileName = 'xxx.android.bundle-%s.zip' //远程文件的文件名,%s会用上面的version来填充
    outFile = 'xxx/src/main/assets/JsBundle/xxx.android.bundle.zip' //下载到本地的保存路径，相对应项目根目录
}
```

4、下载时机

你可以手动运行`RNBundleDownLoad`这个task，或者直接打包，他会在打包之前，自动下载


### 开发相关

1、请使用IDEA开发；

2、插件会新建一个RNBundleDownLoad的task，插入到安卓打包的task之前。

3、TestGradlePlugin模块可以用于开发的时候测试。

