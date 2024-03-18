# 项目框架

## 项目搭建

- 父模块创建: MyBlog
- 子模块创建: dawn-framework
- 后台模块创建: dawn-admin
- 前台模块创建: dawn-blog

IDEA构建Maven尽量选择自己本地下载好的，并且重写repository和setting文件

创建父模块的时候最好不要直接建立spring initializr，而是用最基础的maven，这样好配置pom.xml文件

如果飘红，就点击右侧Maven导航栏或用命令行进行clean然后install

## 前台模块

### 文章列表接口/需求分析

- 需要查询浏览量最高的前10篇文章的信息。
- 要求展示文章标题和浏览量。
- 能让用户自己点击跳转到具体的文章详情进行浏览。
- 注意：不能把草稿展示出来，不能把删除了的文章查询出来。
- 要按照浏览量进行降序排序

### 分类列表接口/需求分析

- 页面上需要展示分类列表
- 用户可以点击具体的分类查看该分类下的文章列表。
- 要求必须是正常(非禁用)状态的分类

### 分页列表接口/需求分析

- 首页需要查询所有的文章列表。
- 分类页面需要查询对应分类下的文章列表。
- 只能查询正式发布的文章。
- 置顶的文章要显示在最前面。

### 文章详情接口/需求分析

- 要求在文章列表点击阅读全文时能够跳转到文章详情页面。
- 可以让用户阅读文章正文。
- 要在文章详情中展示其分类名。

```json
{
  "code": 200,
  "data": {
    "categoryId": "1",
    "categoryName": "java",
    "content": "文章详情的具体文章内容",
    "createTime": "2022-01-23 23:20:11",
    "id": "1",
    "isComment": "0",
    "title": "SpringSecurity从入门到精通",
    "viewCount": "114"
  },
  "msg": "操作成功"
}
```

### 友链功能接口/需求分析

- 友链要进行审核
- 友链页面要查询出所有的审核通过的友链

```json
{
  "code": 200,
  "data": [
    {
      "address": "https://www.baidu.com",
      "description": "sda",
      "id": "1",
      "logo": "图片url1",
      "name": "sda"
    },
    {
      "address": "https://www.qq.com",
      "description": "dada",
      "id": "2",
      "logo": "图片url2",
      "name": "sda"
    }
  ],
  "msg": "操作成功"
}
```

### 登录功能

#### 接口/需求分析

- 博客的前台、后台的登录功能，需要使用认证授权，所以统一都使用SpringSecurity安全框架来实现
- 需要实现登录功能，有些功能必须登录后才能使用，未登录状态是不能使用的。
- 请求体：

```json
{
  "userName": "sg",
  "password": "1234"
}
```

- 响应格式

```json
{
  "code": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0ODBmOThmYmJkNmI0NjM0OWUyZjY2NTM0NGNjZWY2NSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY0Mzg3NDMxNiwiZXhwIjoxNjQzOTYwNzE2fQ.ldLBUvNIxQCGemkCoMgT_0YsjsWndTg5tqfJb77pabk",
    "userInfo": {
      "avatar": "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F3bf9c263bc0f2ac5c3a7feb9e218d07475573ec8.gi",
      "email": "23412332@qq.com",
      "id": 1,
      "nickName": "sg333",
      "sex": "1"
    }
  },
  "msg": "操作成功"
}
```

#### 思路分析

**登录**

[1] 自定义登录接口

- 调用ProviderManager的方法进行认证 如果认证通过生成jwt
- 把用户信息存入redis中

[2] 自定义UserDetailsService

- 在这个实现类中去查询数据库

注意配置passwordEncoder为BCryptPasswordEncoder

**校验**

[1] 定义Jwt认证过滤器

- 获取token
- 解析token获取其中的userid
- 从redis中获取用户信息
- 存入SecurityContextHolder

### 异常处理功能

#### 认证授权异常处理

目前我们的项目在认证出错或者权限不足的时候响应回来的Json，默认是使用Security官方提供的响应的格式，如密码错误或者不带token进行友链访问时：

![img.png](img.png)

但是这种响应的格式肯定是不符合我们项目的接口规范的。所以需要自定义异常处理。

**我们需要去实现AuthenticationEntryPoint(官方提供的认证失败处理器)类、AccessDeniedHandler(官方提供的授权失败处理器)
类，然后配置给Security**

#### 统一异常处理

实际开发过程中可能需要做很多的判断校验，如果出现了非法情况我们是期望响应对应的提示的。

但是如果我们每次都自己手动去处理就会非常麻烦。可以选择**直接抛出异常**的方式，然后对异常进行统一处理。把异常中的信息封装成ResponseResult响应给前端

### 退出登录功能

响应格式

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 评论列表功能

把评论的模块展示出来。从数据库查询到数据，然后展示在评论模块里面

评论响应格式如下，包含了根评论+子评论:

```json
{
  "code": 200,
  "data": {
    "rows": [
      {
        "articleId": "1",
        "children": [
          {
            "articleId": "1",
            "content": "评论内容(子评论)",
            "createBy": "1",
            "createTime": "2022-01-30 10:06:21",
            "id": "20",
            "rootId": "1",
            "toCommentId": "1",
            "toCommentUserId": "1",
            "toCommentUserName": "这条评论(子评论)回复的是哪个人",
            "username": "发这条评论(子评论)的人"
          }
        ],
        "content": "评论内容(根评论)",
        "createBy": "1",
        "createTime": "2022-01-29 07:59:22",
        "id": "1",
        "rootId": "-1",
        "toCommentId": "-1",
        "toCommentUserId": "-1",
        "username": "发这条评论(根评论)的人"
      }
    ],
    "total": "15"
  },
  "msg": "操作成功"
}
```

### 发送评论

#### 1. 发送文章评论

用户登录后可以对文章发表评论，也可以对已有的评论进行回复

- 回复了文章。0表示文章评论，如果是友链评论，type应该为1：

```json
{
  "articleId": 1,
  "type": 0,
  "rootId": -1,
  "toCommentId": -1,
  "toCommentUserId": -1,
  "content": "评论了文章"
}
```

- 回复了某条评论。0表示文章评论，如果是友链评论，type应该为1：

```json
{
  "articleId": 1,
  "type": 0,
  "rootId": "3",
  "toCommentId": "3",
  "toCommentUserId": "1",
  "content": "回复了某条评论"
}
```

- 响应格式

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

#### 2. 发送友链评论

在友链的评论区发送评论

### 个人信息功能

进入个人中心的时候需要能够查看当前用户信息。请求不需要参数

### 文件上传功能

#### 基础-为什么使用OSS

因为如果把图片视频等文件上传到自己的应用的Web服务器的某个目录下，在读取图片的时候会占用比较多的资源。影响应用服务器的性能。所以我们一般使用OSS(
Object Storage Service对象存储服务)存储图片或视频。

#### 基础-使用七牛云

pom.xml中添加了相关坐标

```xml
<!--七牛云OOS-->
<dependency>
    <groupId>com.qiniu</groupId>
    <artifactId>qiniu-java-sdk</artifactId>
    <version>[7.7.0, 7.7.99]</version>
</dependency>
```

七牛云官方文档直接把 '数据流'代码拷贝过来，进行相应修改:

- 更改自己的secret Key（密钥管理获取）
- 更改自己的access Key（密钥管理获取）
- 更改自己的bucket （自定义的空间名称）
- 更改key值为要取的文件名

#### 上传头像

在个人中心点击编辑的时候可以上传头像图片。上传完头像后，可以用于更新个人信息接口，参数img，值为要上传的文件

#### 更新个人信息

在编辑完个人资料后点击保存会对个人资料进行更新。使用mybatisplus提供的updateById的方法

### 用户注册功能

要求用户能够在注册界面完成用户的注册。

要求用户名，昵称，邮箱不能和数据库中原有的数据重复。

如果某项重复了注册失败并且要有对应的提示。

并且要求用户名，密码，昵称，邮箱都不能为空。注意:密码必须密文存储到数据库中

注意这里还要让主键id使用自增策略，原因是我们不使用mybatisplus提供的雪花算法。执行之后重启mysql

```sql
use
sg_blog;
alter table sys_user modify column id int auto_increment;
```

### 日志记录

需要通过日志记录接口调用信息。便于后期调试排查。并且可能有很多接口都需要进行日志的记录。接口被调用时日志打印格式如下:

```json
log.info("======================Start======================");
// 打印请求 URL
log.info("请求URL   : {}", );
// 打印描述信息
log.info("接口描述   : {}", );
// 打印 Http method
log.info("请求方式   : {}", );
// 打印调用 controller 的全路径以及执行方法
log.info("请求类名   : {}.{}", );
// 打印请求的 IP
log.info("访问IP    : {}", );
// 打印请求入参
log.info("传入参数   : {}", );
// 打印出参
log.info("返回参数   : {}", );
// 结束后换行
log.info("=======================end=======================" + System.lineSeparator());

```

相当于是对原有的功能进行增强。并且是批量的增强，这个时候就非常适合用AOP来进行实现，不对业务代码进行侵入，完全解耦

#### AOP实现日志记录

需要通过日志记录接口调用信息。便于后期调试排查。并且可能有很多接口都需要进行日志的记录。接口被调用时日志打印格式如下:

```java
log.info("======================Start======================");
// 打印请求 URL
log.

info("请求URL   : {}",);
// 打印描述信息
log.

info("接口描述   : {}",);
// 打印 Http method
log.

info("请求方式   : {}",);
// 打印调用 controller 的全路径以及执行方法
log.

info("请求类名   : {}.{}",);
// 打印请求的 IP
log.

info("访问IP    : {}",);
// 打印请求入参
log.

info("传入参数   : {}",);
// 打印出参
log.info("返回参数   : {}",);
// 结束后换行
log.

info("=======================end======================="+System.lineSeparator());
```

相当于是对原有的功能进行增强。并且是批量的增强，这个时候就非常适合用AOP来进行实现，不对业务代码进行侵入，完全解耦.

#### AOP实现日志记录的分析

定义切面类，在切面类通过 '切点表达式' 或 '自定义注解'，来指定切点

切面类: 指定要增强哪个切点，里面写通知的方法，通知的方法里面写具体的增强代码
AOP中的通知方法有五种，如下:

- 前置通知 在一个方法执行之前的阶段，执行通知。可以在目标方法执行前做一些预处理操作
- 后置通知 在一个方法执行之后的阶段，执行通知。通常用于执行一些清理操作或日志记录
- 异常通知 在方法抛出异常退出时执行的通知。用于处理目标方法抛出的异常情况
- 最终通知 无论目标方法是否成功执行，最终通知总会被执行，常用于释放资源
- 环绕通知 环绕通知是AOP中最灵活的通知类型。能在目标方法前后完全控制连接点，决定是否执行目标方法并进行额外处理

### 更新博客浏览次数

在用户浏览博文时要实现对应博客浏览量的增加。我们只需要在每次用户浏览博客时更新对应的浏览数即可

但是如果直接操作博客表的浏览量的话，在并发量大的情况下会出现什么问题呢？

如何去优化呢？如下四点

1. 在应用启动时把博客的浏览量存储到redis中 - 项目启动的预处理功能
2. 更新浏览量时去更新redis中的数据
3. 每隔3分钟把Redis中的浏览量更新到数据库中 - 定时任务功能
4. 读取文章浏览量时从redis读取

#### 基础任务--启动预处理

在SpringBoot应用启动时进行一些初始化操作可以选择使用CommandLineRunner接口来进行处理。
只需要实现CommandLineRunner接口，并且把对应的bean注入容器。把相关初始化的代码重新到需要重新的方法中。
这样就会在应用启动的时候执行对应的代码。

```java

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class myCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("程序初始化。。。");
    }
}
```

#### 基础任务--定时任务
定时任务的实现方式有很多，比如XXL-Job等。但是其实核心功能和概念都是类似的，很多情况下只是调用的API不同而已
这里用SpringBoot为我们提供的定时任务的API来实现一个简单的定时任务，先对定时任务里面的一些核心概念有个大致的了解

1. 添加@EnableScheduling注解，就能开启定时任务功能

cron表达式是用来设置定时任务执行时间的表达式。cron表达式生成器网站 -> https://www.bejson.com/othertools/cron/