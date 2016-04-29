# 使用gradle初始化多模块java-app项目


### 创建文件夹app,并在app文件夹下创建settings.gradle和build.gradle
### 修改settings.gradle和build.gradle

    settings.gradle
```java
rootProject.name = 'app'
```
    build.gradle
```java
group 'com.github'  // 类似maven的groupId
version '1.0-SNAPSHOT'

apply plugin: 'java'  // 这里使用gradle的java插件

sourceCompatibility = 1.8 // jdk版本

// 这里的subprojects 类似于在maven项目中将 pom.xml文件中packaging设置为pom
subprojects {

    apply plugin: 'idea'

    [compileJava, compileTestJava, javadoc]*.options*.encoding = 'UTF-8'

    repositories {
        maven {
        // os-china maven仓库
            url "http://maven.oschina.net/content/groups/public/"
        }
//        mavenCentral()
    }

    dependencies {

    }

}
```
    到这里父项目已经初始化完毕,可以用gradle 命令 gradle build

### 在app文件夹下创建多个文件夹如:api,core,web等,并分别在每个文件夹下创建build.gradle文件以及资源目录,如下

```java
app
├─api
│  └─src
│      ├─main
│      │  └─java
│      └─test
│          └─java
│  └─build.gradle
├─core
│  └─src
│      ├─main
│      │  └─java
│      └─test
│          └─java
│  └─build.gradle
└─web
    └─src
        ├─main
        │  └─java
        │  └─resources
        │  └─webapp
        └─test
            └─java
   └─build.gradle
```
### 修改app文件夹下settings.gradle,如下
```java
rootProject.name = 'app'
include 'api'
include 'core'
include 'web'
```
### 分别修改api,core,web等文件夹下的build.gradle文件,如下
```java
group 'com.github'
version '1.0-SNAPSHOT'

apply plugin: 'java'
// 其中 web项目需要添加下面的插件
// apply plugin: 'war'

```
### 基本的项目已经搭建完成,额外添加子模块如下


    1.  假设此时app项目下有个service模块,它又有2个子模块,搭建更多模块如下
    2.  在app目录下创建service,并在service目录中创建settings.gradle,build.gradle;以及a1-service,a2-service目录,并在这两个目录下新建build.gradle问津.其中a1-service,a2-service相当于service的子模块.
    3.  此时修改service目录下的settings.gradle和build.gradle文件

修改配置文件

    settings.gradle 修改如下
```java
rootProject.name = 'service'
include 'a1-service'
include 'a2-service'
```

``注意:使用idea等工具时,此时需要build一下项目,以方便idea的gradle插件识别多模块``

    build.gradle 修改如下
```java
group 'com.github' // 这里还是跟其他模块一样,因为他是app的子模块
version '1.0-SNAPSHOT'

// 想要包含多模块,则subprojects为必须
subprojects {
    // 内容与前面无差异
    apply plugin: 'java'
    apply plugin: 'maven'
    apply plugin: 'idea'

    sourceCompatibility = 1.8

    [compileJava, compileTestJava, javadoc]*.options*.encoding = 'UTF-8'

    repositories {
        mavenCentral()
    }

    dependencies {
        compile 'javax.servlet:javax.servlet-api:3.1.0'
        testCompile group: 'junit', name: 'junit', version: '4.11'
    }

}
```

    分别修改a1-service,a2-service文件夹下的build.gradle.`这里配置group 名字时需要注意`

```java
group 'com.github.service' // 注意,这里应该是app的group名字加上service的文件夹名
version '1.0-SNAPSHOT'

apply plugin: 'java'

```

    至此,我们的项目目录应该是如下

```java
app
├─settings.gradle  // 这里rootProject.name='app',include ('api'),('core'),('web'),('service')
├─build.gradle     // 这里group是com.github,并且需要有subprojects{}
├─api
│  └─src
│      ├─main
│      │  └─java
│      └─test
│          └─java
│  └─build.gradle // 这里group为com.github
├─core
│  └─src
│      ├─main
│      │  └─java
│      └─test
│          └─java
│  └─build.gradle // 这里group为com.github
├─web
│  └─src
│      ├─main
│      │  └─java
│      │  └─resources
│      │  └─webapp
│      └─test
│         └─java
│  └─build.gradle // 这里group为com.github,注意:这里需要添加war包插件 apply plugin: 'war'
├─service
│  └─settings.gradle  // 这里rootProject.name='service',include ('a1-service'),('a2-service')
│  └─build.gradle     // 这里group是com.github,并且需要有subprojects{}
│  └─a1-service
│      └─build.gradle // 注意:这里group是com.github.service
│      └─src
│          ├─main
│          │  └─java
│          └─test
│              └─java
│  └─a2-service
│      └─build.gradle // 注意:这里group是com.github.service
│      └─src
│          ├─main
│          │  └─java
│          └─test
│             └─java
```


多模块项目搭建完毕.