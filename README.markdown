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
    "userName":"sg",
    "password":"1234"
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

**我们需要去实现AuthenticationEntryPoint(官方提供的认证失败处理器)类、AccessDeniedHandler(官方提供的授权失败处理器)类，然后配置给Security**

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