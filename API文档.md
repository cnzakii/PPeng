<img src="/Users/zaki/IntelliJProjects/ppeng-backend-v1/assets/logo.png" style="zoom: 30%;" />

# ”烹烹“—PPeng API文档

[TOC]



> v1.0.0

Base URLs:

* <a href="https://ppeng.zhub.fun/v1/api">服务器测试环境: https://ppeng.zhub.fun/v1/api</a>

# 用户模块/用户操作

## GET 获取公钥

GET /user/rsa

获取RSA公钥

> 返回示例

> 成功

```json
{
  "timestamp": 1681540161289,
  "status": "200",
  "message": "success",
  "data": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvU5SYbb0F8kzMuGzmaHDxPkRENq/i9x4OV4CZzZZc2qY0ZIWmiqPt/VdaDo8bpRU8tAth1d5v74HIMIKZBQokOOW3EeTi4myvC59RO5u4Lft8dGpw7Gru6LlSqVSnSvIyfLvz/k9EFVUs6DyPzRtu6+Q4qc12LJQCYsfU/gX5qwIDAQAB"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|string|true|none|RSA公钥|none|

## POST 用户登出

POST /user/logout

用户登出

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|p-token|header|string| 是 |-|

> 返回示例

> 成功

```json
{
  "timestamp": 1680875009149,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681350846468,
  "status": "501",
  "message": "Token无效：693941cdaa034858a972efa657b25701",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|数据|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## POST 通过密码登录

POST /user/login/by/password

用户通过密码进行登录

> Body 请求参数

```json
{
  "email": "test@ppeng.com",
  "password": "dcJPxIKy8tInvxvFRNooEr8Zg+PAYxZo6dUjA5UPF2lhUvdSVEHOTvU5yH7Y8MHidWvXSc+i88gGTHW2WlF6//aQ8U7AfCLB50Q8jSCs8QOTtHtwtSBJlyywnfrKUy7oEDJuPYGEvUv9uHHA73xN2QzWctQRynTmbgzC9jZD6gI="
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» email|body|string| 是 | 邮箱|none|
|» password|body|string| 是 | 密码|需要RSA加密|

> 返回示例

> 成功

```json
{
  "timestamp": 1682607255514,
  "status": "200",
  "message": "success",
  "data": "p_wxbwzqm8pakkte_l4cw47fl6sdxbi_"
}
```

> 请求参数错误

```json
{
  "timestamp": 1681541266428,
  "status": "400",
  "message": "密码未加密",
  "data": null
}
```

```json
{
  "timestamp": 1681299695351,
  "status": "400",
  "message": "[邮箱格式错误]",
  "data": null
}
```

```json
{
  "timestamp": 1681299863247,
  "status": "400",
  "message": "[密码不能为空]",
  "data": null
}
```

```json
{
  "timestamp": 1681300109663,
  "status": "400",
  "message": "[邮箱不能为空]",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681349841776,
  "status": "501",
  "message": "密码错误",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求参数错误|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|string|true|none|p-token|none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|token|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|token|none|

## POST 获取当前用户基本信息

POST /user/current

获取用户当前信息

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681972939094,
  "status": "200",
  "message": "success",
  "data": {
    "id": "1648939539533795328",
    "email": "c******@gmail.com",
    "nickName": "PPeng_3vosljv7px",
    "icon": null,
    "address": null,
    "introduce": null,
    "gender": 0,
    "birthday": null,
    "fans": 0,
    "follows": 0,
    "createTime": "2023-04-20T14:39:57",
    "updateTime": null
  }
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681305083204,
  "status": "501",
  "message": "Token无效：3c275458494a4c7f9b3b031279794296",
  "data": null
}
```

```json
{
  "timestamp": 1681350643088,
  "status": "501",
  "message": "Redis command timed out",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|object|true|none|数据|none|
|»» id|string|true|none|用户id|none|
|»» email|string¦null|true|none|邮件|none|
|»» nickName|string¦null|true|none|昵称|none|
|»» icon|null|true|none|头像url|none|
|»» address|null|true|none|地址|none|
|»» introduce|null|true|none|简介|none|
|»» gender|integer|true|none|性别|none|
|»» birthday|null|true|none|生日|none|
|»» fans|integer|true|none|粉丝数|none|
|»» follows|integer|true|none|关注数|none|
|»» createTime|string|true|none|创建时间|none|
|»» updateTime|null|true|none|更新时间|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## PUT 更新用户信息

PUT /user/current

> Body 请求参数

```json
{
  "userId": "1651599705077059584",
  "nickName": "测试账号",
  "icon": null,
  "address": [
    "中国",
    "福建",
    "福州"
  ],
  "introduce": "PPeng，你好",
  "gender": 3,
  "birthday": "2000-01-12"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» userId|body|integer| 是 | 用户id|none|
|» nickName|body|string¦null| 否 | 昵称|none|
|» icon|body|string¦null| 否 | 头像url|none|
|» address|body|[string]¦null| 否 | 地址|none|
|» introduce|body|string¦null| 否 | 简介|none|
|» gender|body|integer¦null| 否 | 性别|0：未知 1：男  2：女|
|» birthday|body|string¦null| 否 | 生日|none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681541799598,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 请求参数错误

```json
{
  "timestamp": 1681544479303,
  "status": "400",
  "message": "参数过长",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681542052207,
  "status": "501",
  "message": "Token无效：62b6350b83f64e91a03797748c488f3c",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求参数错误|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||时间戳|
|» status|string|true|none||状态码|
|» message|string|true|none||状态信息|
|» data|null|true|none||null|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

## DELETE 删除当前用户

DELETE /user/current

删除当前用户，需要二级认证，请先调用/user/safe/verify接口

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681556686341,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 没有认证(token)

```json
{
  "timestamp": 1681556731749,
  "status": "401",
  "message": "未能读取到有效Token"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|没有认证(token)|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|数据|none|

状态码 **401**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|

## PUT 更新用户密码

PUT /user/update/password

修改用户密码，需要二级认证，请先调用/user/safe/verify接口

> Body 请求参数

```json
{
  "userId": "1648962918659788800",
  "newPassword": "Rzjkn+vyb4q02s/dc6aLjxVulofzS2ByHwvVjdd5FtzaB2UDbq0Xjphzx7V6kHwE3b+1M2f7vee2dMzqBPzVA0FDJCt9BaI6JLZNo7Ebh99I5BrbdWfvhsjJzyEfmeSo6AssFP1lQdbMeVaOPtB56fFWhhlxu0qaOrqu6aZnaD8="
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» userId|body|string| 是 | 用户id|none|
|» newPassword|body|string| 是 | 新密码|none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681371584006,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 请求参数有误

```json
{
  "timestamp": 1681373108149,
  "status": "400",
  "message": "[新密码不能为空]",
  "data": null
}
```

> 没有权限

> 服务器内部错误

```json
{
  "timestamp": 1681362586690,
  "status": "501",
  "message": "验证码错误",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求参数有误|Inline|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|没有权限|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## POST 用户注册

POST /user/register

> Body 请求参数

```json
{
  "email": "test@ppeng.com",
  "code": "123456",
  "password": "h9aHSzqlrdoojxKsLqrIyrMsUVWViYwXE4aKQU64X8SPSEO73Hmo0JkQZzYnC2ub0WB9NwXiW5rJmV12tfj0wjcRixKNxlpjkydz2kSyS/Ygg3OT3ooRlLC9Z4yJRU23o3bEgyytGAZLrrVtNhBOwsHYI/Nj/AKAldDk0W+CEDs="
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» email|body|string| 是 | 邮件|none|
|» code|body|string| 是 | 验证码|none|
|» password|body|string| 是 | 密码|需要RSA加密|

> 返回示例

> 200 Response

```json
{
  "timestamp": 0,
  "status": "string",
  "message": "string",
  "data": null
}
```

> 请求参数错误

```json
{
  "timestamp": 1681285273555,
  "status": "400",
  "message": "[邮箱格式错误, 验证码无效]",
  "data": null
}
```

> 服务器处理请求错误

```json
{
  "timestamp": 1681285348404,
  "status": "501",
  "message": "密码未加密",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求参数错误|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器处理请求错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|数据|none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|信息|none|
|» data|null|true|none|数据|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|信息|none|
|» data|null|true|none|数据|none|

## PUT 更新用户邮箱

PUT /user/update/email

修改用户邮箱，需要二级认证，请先调用/user/safe/verify接口

> Body 请求参数

```json
{
  "userId": 1647608968601800700,
  "email": "cnzakii@hotmail.com",
  "code": "333444"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» userId|body|integer| 是 | 用户id|none|
|» email|body|string| 是 | 新邮箱|none|
|» code|body|string| 是 | 新邮箱验证码|none|

> 返回示例

> 200 Response

```json
{
  "timestamp": 0,
  "status": "string",
  "message": "string",
  "data": null
}
```

> 请求参数错误

```json
{
  "timestamp": 1681546217583,
  "status": "400",
  "message": "[验证码无效]",
  "data": null
}
```

> 服务器错误

```json
{
  "timestamp": 1681556327516,
  "status": "501",
  "message": "2634376117@qq.com-验证码错误",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求参数错误|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## POST 微信一键登录

POST /user/login/by/wx/{code}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|code|path|string| 是 ||临时凭证code|

> 返回示例

> 成功

```json
{
  "timestamp": 1681974477691,
  "status": "200",
  "message": "success",
  "data": {
    "isFirst": false,
    "token": "3dcbf15f3e6d4684b92f51eaa7d4e66d"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|object|true|none|数据|none|
|»» isFirst|boolean|true|none|是否第一次登录|none|
|»» token|string|true|none|token|none|

## POST 用户身份安全验证

POST /user/safe/verify

请调用/mail/verify接口，获取邮箱验证码

> Body 请求参数

```json
{
  "userId": "1648962918659788800",
  "email": "cnzakii@gmail.com",
  "code": "943344",
  "type": 0
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» userId|body|string| 是 | 用户id|none|
|» email|body|null| 是 | 邮箱|none|
|» code|body|null| 是 | 验证码|none|
|» type|body|integer| 是 | 类型|0 更新密码 1 更新邮箱 2 删除账号|

> 返回示例

> 成功

```json
{
  "timestamp": 1681982491510,
  "status": "400",
  "message": "invalid parameter",
  "data": null
}
```

> 请求参数错误

```json
{
  "timestamp": 1681987469225,
  "status": "400",
  "message": "userId不能为空,email不能为空",
  "data": null
}
```

> 没有权限

```json
{
  "timestamp": 1681987615288,
  "status": "401",
  "message": "用户身份验证失败",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求参数错误|Inline|
|401|[Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1)|没有权限|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **401**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

## PUT 更新用户头像

PUT /user/update/icon/{userId}

> Body 请求参数

```yaml
icon: file:///Users/zaki/Desktop/client1.png

```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userId|path|string| 是 ||用户id|
|p-token|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» icon|body|string(binary)| 是 ||头像文件|

> 返回示例

> 成功

```json
{
  "timestamp": 1684333545712,
  "status": "200",
  "message": "success",
  "data": "https://ppeng.zhub.fun/resource/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|string|true|none|头像url|none|

# 用户模块/用户关注模块

## POST 添加关注

POST /user/follow/add/{followId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|followId|path|string| 是 ||关注的id|

> 返回示例

> 成功

```json
{
  "timestamp": 1681558780253,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681558888143,
  "status": "501",
  "message": "已经关注过该用户",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## GET 列出关注列表

GET /user/follow/list/follow

> 返回示例

> 成功

```json
{
  "timestamp": 1681558990046,
  "status": "200",
  "message": "success",
  "data": [
    "1646060411193266176"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|[string]|true|none|关注列表|none|

## GET 列出粉丝列表

GET /user/follow/list/fans

> 返回示例

> 成功

```json
{
  "timestamp": 1681559061343,
  "status": "200",
  "message": "success",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

## POST 查询是否关注过

POST /user/follow/{followId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|followId|path|integer| 是 ||关注的id|

> 返回示例

> 成功

```json
{
  "timestamp": 1681559123431,
  "status": "200",
  "message": "success",
  "data": true
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|boolean|true|none|是否关注|none|

## DELETE 取消关注

DELETE /user/follow/delete/{followId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|followId|path|integer| 是 ||关注的id|

> 返回示例

> 成功

```json
{
  "timestamp": 1681559206262,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681559316145,
  "status": "501",
  "message": "尚未关注该用户",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

# 用户模块/点赞菜谱模块

## POST 点赞喜欢的菜谱

POST /user/like/add/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||菜谱id|
|p-token|header|string| 否 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681788064874,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681788105030,
  "status": "501",
  "message": "已经点赞了",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

## GET 列出该用户点赞菜谱的id列表

GET /user/like/list

> 返回示例

> 成功

```json
{
  "timestamp": 1681788231786,
  "status": "200",
  "message": "success",
  "data": [
    "1"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|[string]|true|none|菜谱id|none|

## POST 查看该用户是否点赞该菜谱

POST /user/like/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||菜谱id|
|p-token|header|string| 否 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681788288274,
  "status": "200",
  "message": "success",
  "data": true
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|boolean|true|none|用户是否点赞该菜谱|none|

## DELETE 取消点赞菜谱

DELETE /user/like/delete/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||菜谱id|

> 返回示例

> 成功

```json
{
  "timestamp": 1681788649588,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681788534588,
  "status": "501",
  "message": "尚未点赞",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

# 用户模块/收藏菜谱模块

## POST 收藏喜欢的菜谱

POST /user/collect/add/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681787377116,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681787597127,
  "status": "501",
  "message": "已经收藏了",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

## GET 列出该用户收藏菜谱的id列表

GET /user/collect/list

> 返回示例

> 成功

```json
{
  "timestamp": 1681787381512,
  "status": "200",
  "message": "success",
  "data": [
    "1"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|[string]|true|none|菜谱id|none|

## POST 查看该用户是否收藏该菜谱

POST /user/collect/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681787751330,
  "status": "200",
  "message": "success",
  "data": true
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|boolean|true|none||none|

## DELETE 取消收藏菜谱

DELETE /user/collect/delete/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1681787944963,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681788596133,
  "status": "501",
  "message": "尚未收藏",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|null|true|none|null|none|

# 邮件模块

## POST 发送注册邮件

POST /mail/register/{mail}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|mail|path|string| 是 ||邮件|

> 返回示例

> 成功

```json
{
  "timestamp": 1684334095735,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 请求处理失败

```json
{
  "timestamp": 1681014465417,
  "status": "500",
  "message": "已向该邮箱发送验证，请勿频繁重试",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681559594434,
  "status": "501",
  "message": "邮件格式错误",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|请求处理失败|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **500**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## POST 发送用户身份认证邮件

POST /mail/verify

> Body 请求参数

```json
{
  "userId": "1648962918659788800",
  "email": "cnzakii@gmail.com",
  "type": 0
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» userId|body|string| 是 | 用户id|none|
|» email|body|string| 是 | 邮箱|none|
|» type|body|integer| 是 | 类型|0 更新密码      1 更新邮箱      2 删除账号|

> 返回示例

> 成功

```json
{
  "timestamp": 1681988354994,
  "status": "200",
  "message": "success",
  "data": null
}
```

> 服务器内部错误

```json
{
  "timestamp": 1681559476597,
  "status": "501",
  "message": "邮件格式错误",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|501|[Not Implemented](https://tools.ietf.org/html/rfc7231#section-6.6.2)|服务器内部错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

状态码 **501**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

# 文件模块

## POST 文件上传

POST /file/upload/{type}

> Body 请求参数

```yaml
file: file:///Users/zaki/Pictures/壁纸/IMG_1325.JPG

```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|type|path|string| 是 ||type 类型：icon-头像   recipe-images 菜谱图片集合  recipe-video 菜谱视频|
|p-token|header|string| 是 ||none|
|body|body|object| 否 ||none|
|» file|body|string(binary)| 否 ||文件，可以多个文件|

> 返回示例

> 成功

```json
{
  "timestamp": 1681724946303,
  "status": "200",
  "message": "success",
  "data": "https://ppeng.zhub.fun/icon/3e3d89a592744099a5dd741c999c1218.png"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|string|true|none|数据|none|

# 菜谱模块/菜谱

## POST 上传菜谱

POST /recipe/push

> Body 请求参数

```json
{
  "userId": "",
  "typeId": "",
  "title": "",
  "material": "",
  "content": "",
  "mediaUrl": [],
  "isVideo": 0
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» userId|body|string| 是 | 用户id|none|
|» typeId|body|string| 是 | 菜谱类型id|none|
|» title|body|string| 是 | 标题|200字以内|
|» material|body|string| 是 | 配料表|none|
|» content|body|string| 是 | 内容|none|
|» mediaUrl|body|[string]| 是 | 媒体资源路径|none|
|» isVideo|body|integer| 是 | 是否为视频|0代表图文，1代表视频|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## DELETE 删除菜谱

DELETE /recipe/delete/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||菜谱id|
|p-token|header|string| 否 ||token|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 根据用户id获取菜谱列表

GET /recipe/list/by/user

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userId|query|string| 否 ||用户id|
|timestampe|query|integer| 否 ||时间戳，默认为当前时间|
|size|query|string| 否 ||一页的菜谱数量，默认为5|

> 返回示例

> 成功

```json
{
  "timestamp": 1684769243805,
  "status": "200",
  "message": "success",
  "data": {
    "resultList": [
      {
        "id": "1651599705077059592",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
        "typeId": 20,
        "title": "【甜点】草莓慕斯",
        "material": "鲜草莓、吉利丁粉、饼干碎、黄油、糖粉、淡奶油",
        "content": "草莓慕斯是一道口感绵密、甜度适中的甜点，以鲜草莓为主要原料制作而成。\n\n制作步骤如下：\n1. 饼干碎与融化的黄油混合，铺在容器底部做为酥皮层。\n2. 鲜草莓洗净，去蒂切小块，用搅拌机打成泥状。\n3. 吉利丁粉加入适量冷水浸泡片刻，随后加热溶化。\n4. 打发淡奶油，加入糖粉，继续打发至充分蓬松。\n5. 将草莓泥和溶化的吉利丁粉混合均匀，然后加入打发的淡奶油，轻轻拌匀。\n6. 将混合好的慕斯倒入容器中，冷藏至凝固。\n7. 取出后撒上适量的鲜草莓作装饰即可。\n\n草莓慕斯色泽诱人，口感绵密香甜，是甜品爱好者的最爱。",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 95,
        "collections": 82,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059608",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
        "typeId": 22,
        "title": "🥬鲁菜葱烧海参🐙",
        "material": "海参、葱、生抽、老抽、料酒、糖、生姜、鸡精、水",
        "content": "介紹：鲁菜葱烧海参是一道鲁菜中的经典菜品。它色香味俱佳，口感鲜美，是中餐中的一道名菜。\n\n步驟1️⃣：海参提前泡发，去除内脏，切块。\n步驟2️⃣：锅中加入热水煮沸，放入海参焯水。\n步驟3️⃣：锅中加入适量油，放入葱段和生姜炒香。\n步驟4️⃣：加入海参，加入生抽、老抽、料酒、糖、鸡精和水，焖煮片刻。\n步驟5️⃣：待汤汁浓稠后，即可装盘。\n\n现在可以品尝美味的鲁菜葱烧海参啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 150,
        "collections": 120,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059607",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
        "typeId": 21,
        "title": "🍗卤鸡腿🍗",
        "material": "鸡腿、酱油、料酒、姜蒜、五香粉、糖、水",
        "content": "介紹：卤鸡腿是一道色香味俱佳的卤味菜品。它肉质鲜嫩多汁，香气扑鼻，是非常受欢迎的卤味之一。\n\n步驟1️⃣：鸡腿剁小块，用开水焯水。\n步驟2️⃣：锅中加入酱油、料酒、姜蒜、五香粉、糖和水，煮沸。\n步驟3️⃣：将焯水后的鸡腿放入锅中，煮制至鸡肉入味。\n步驟4️⃣：将煮熟的卤鸡腿取出，装盘即可。\n\n现在可以品尝香气扑鼻的卤鸡腿啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 120,
        "collections": 95,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059606",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
        "typeId": 20,
        "title": "🍥花生糖🥜",
        "material": "花生、糖、水",
        "content": "介紹：花生糖是一款香甜可口的中國傳統點心。它口感酥脆，香氣四溢，是非常受歡迎的小吃。\n\n步驟1️⃣：將花生放入鍋中炒熟。\n步驟2️⃣：在另一個鍋中加入糖和水，加熱並攪拌至糖溶化。\n步驟3️⃣：加入炒熟的花生，迅速攪拌均勻。\n步驟4️⃣：將混合物倒入平盤中，用刀切成小塊。\n\n現在可以品嚐香甜酥脆的花生糖啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 90,
        "collections": 75,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059605",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
        "typeId": 19,
        "title": "🥦麻辣素炒🌶️",
        "material": "青菜、豆腐、辣椒、蒜、生抽、麻辣醬",
        "content": "介紹：麻辣素炒是一道口感麻辣的素菜。它適合素食者，口味鮮辣，是中國菜中的經典素菜之一。\n\n步驟1️⃣：將青菜洗淨切段，豆腐切塊。\n步驟2️⃣：鍋中熱油，放入蒜和辣椒炒香。\n步驟3️⃣：加入豆腐和青菜翻炒。\n步驟4️⃣：加入適量的生抽和麻辣醬，繼續翻炒均勻。\n\n現在可以品嚐香辣可口的麻辣素炒啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 80,
        "collections": 70,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      }
    ],
    "timestamp": 1684668248000
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» resultList|[[菜谱对象](#schema%e8%8f%9c%e8%b0%b1%e5%af%b9%e8%b1%a1)]|true|none||none|
|»» timestamp|integer|true|none||none|

## GET 根据菜谱类型id获取菜谱列表

GET /recipe/list/by/type

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|typeId|query|integer| 否 ||类型id|
|timestampe|query|integer| 否 ||时间戳，默认为当前时间|
|size|query|string| 否 ||一页的菜谱数量,默认为5|

> 返回示例

> 成功

```json
{
  "timestamp": 1684684761099,
  "status": "200",
  "message": "success",
  "data": {
    "resultList": [
      {
        "id": "1651599705077059584",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
        "typeId": 1,
        "title": "简易早餐",
        "material": "面包、鸡蛋、牛奶",
        "content": "步骤1: 涂抹奶油在面包片上\n步骤2: 煎鸡蛋\n步骤3: 将煎鸡蛋夹在面包片中间\n享受美味早餐的时光！",
        "mediaUrl": "/recipe/test1.jpg,/recipe/test2.jpg,/recipe/test3.jpg,/recipe/test4.jpg,/recipe/test5.jpg",
        "isVideo": 0,
        "likes": 100,
        "collections": 50,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      }
    ],
    "timestamp": 1684667542000
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» resultList|[[菜谱对象](#schema%e8%8f%9c%e8%b0%b1%e5%af%b9%e8%b1%a1)]|true|none||none|
|»» timestamp|integer|true|none||none|

## GET 获取推荐列表--普通菜谱

GET /recipe/recommend/common

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|timestamp|query|number| 否 ||时间戳,默认为当前时间|
|size|query|string| 否 ||一页的菜谱数量,默认为5|

> 返回示例

> 成功

```json
{
  "timestamp": 1684860244984,
  "status": "200",
  "message": "success",
  "data": {
    "resultList": [
      {
        "id": "1651599705077059608",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "https://ppeng.zhub.fun/resource/icon/ppeng.png",
        "type": "鲁菜",
        "title": "🥬鲁菜葱烧海参🐙",
        "material": "海参、葱、生抽、老抽、料酒、糖、生姜、鸡精、水",
        "content": "介紹：鲁菜葱烧海参是一道鲁菜中的经典菜品。它色香味俱佳，口感鲜美，是中餐中的一道名菜。\n\n步驟1️⃣：海参提前泡发，去除内脏，切块。\n步驟2️⃣：锅中加入热水煮沸，放入海参焯水。\n步驟3️⃣：锅中加入适量油，放入葱段和生姜炒香。\n步驟4️⃣：加入海参，加入生抽、老抽、料酒、糖、鸡精和水，焖煮片刻。\n步驟5️⃣：待汤汁浓稠后，即可装盘。\n\n现在可以品尝美味的鲁菜葱烧海参啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 150,
        "collections": 120,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059607",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "https://ppeng.zhub.fun/resource/icon/ppeng.png",
        "type": "卤味",
        "title": "🍗卤鸡腿🍗",
        "material": "鸡腿、酱油、料酒、姜蒜、五香粉、糖、水",
        "content": "介紹：卤鸡腿是一道色香味俱佳的卤味菜品。它肉质鲜嫩多汁，香气扑鼻，是非常受欢迎的卤味之一。\n\n步驟1️⃣：鸡腿剁小块，用开水焯水。\n步驟2️⃣：锅中加入酱油、料酒、姜蒜、五香粉、糖和水，煮沸。\n步驟3️⃣：将焯水后的鸡腿放入锅中，煮制至鸡肉入味。\n步驟4️⃣：将煮熟的卤鸡腿取出，装盘即可。\n\n现在可以品尝香气扑鼻的卤鸡腿啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 120,
        "collections": 95,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059606",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "https://ppeng.zhub.fun/resource/icon/ppeng.png",
        "type": "点心",
        "title": "🍥花生糖🥜",
        "material": "花生、糖、水",
        "content": "介紹：花生糖是一款香甜可口的中國傳統點心。它口感酥脆，香氣四溢，是非常受歡迎的小吃。\n\n步驟1️⃣：將花生放入鍋中炒熟。\n步驟2️⃣：在另一個鍋中加入糖和水，加熱並攪拌至糖溶化。\n步驟3️⃣：加入炒熟的花生，迅速攪拌均勻。\n步驟4️⃣：將混合物倒入平盤中，用刀切成小塊。\n\n現在可以品嚐香甜酥脆的花生糖啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 90,
        "collections": 75,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059605",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "https://ppeng.zhub.fun/resource/icon/ppeng.png",
        "type": "素菜",
        "title": "🥦麻辣素炒🌶️",
        "material": "青菜、豆腐、辣椒、蒜、生抽、麻辣醬",
        "content": "介紹：麻辣素炒是一道口感麻辣的素菜。它適合素食者，口味鮮辣，是中國菜中的經典素菜之一。\n\n步驟1️⃣：將青菜洗淨切段，豆腐切塊。\n步驟2️⃣：鍋中熱油，放入蒜和辣椒炒香。\n步驟3️⃣：加入豆腐和青菜翻炒。\n步驟4️⃣：加入適量的生抽和麻辣醬，繼續翻炒均勻。\n\n現在可以品嚐香辣可口的麻辣素炒啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 80,
        "collections": 70,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      },
      {
        "id": "1651599705077059604",
        "userId": "1651599705077059584",
        "nickName": "测试账号",
        "icon": "https://ppeng.zhub.fun/resource/icon/ppeng.png",
        "type": "凉菜",
        "title": "🥗涼拌黃瓜🥒",
        "material": "黃瓜、醋、鹽、糖、大蒜",
        "content": "介紹：涼拌黃瓜是一道清涼解暑的傳統中國凉菜。它爽脆可口，口感酸甜，是夏季的熱門小吃。\n\n步驟1️⃣：將黃瓜切成薄片。\n步驟2️⃣：在碗中加入醋、鹽、糖和切碎的大蒜，攪拌均勻。\n步驟3️⃣：將黃瓜片放入碗中，輕輕攪拌使調料均勻沾附在黃瓜上。\n\n現在可以品嚐清涼爽口的涼拌黃瓜啦！",
        "mediaUrl": [
          "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
          "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
        ],
        "isVideo": 0,
        "likes": 100,
        "collections": 80,
        "createTime": "2023-05-21",
        "updateTime": "2023-05-21"
      }
    ],
    "timestamp": 1684668207000
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» resultList|[[菜谱对象](#schema%e8%8f%9c%e8%b0%b1%e5%af%b9%e8%b1%a1)]|true|none|菜谱列表|none|
|»» timestamp|integer|true|none|时间戳|none|

## GET 获取推荐列表--专业菜谱

GET /recipe/recommend/professional

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|timestamp|query|number| 否 ||最小时间戳|
|size|query|string| 否 ||一页的菜谱数量，默认为5|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## PUT 更新菜谱

PUT /recipe/update

> Body 请求参数

```json
{
  "userId": "",
  "recipeId": "",
  "title": "",
  "material": "",
  "content": "",
  "mediaUrl": ""
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 举报菜谱

POST /recipe/report/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||菜谱id|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 菜谱模块/菜谱评论

## POST 评价菜谱

POST /recipe/comment/push

> Body 请求参数

```json
{
  "recipeId": 1,
  "parentId": 0,
  "commenterId": 1647190430464479200,
  "content": "1"
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|
|» recipeId|body|integer| 是 ||所评价的菜谱id|
|» parentId|body|integer| 是 ||父评论id（0即为最高级评论）|
|» commenterId|body|integer| 是 ||评论者id|
|» content|body|string| 是 ||评论内容|

> 返回示例

> 成功

```json
{
  "timestamp": 1685112633686,
  "status": "200",
  "message": "success",
  "data": "评论成功"
}
```

> 请求有误

```json
{
  "timestamp": 1685112750756,
  "status": "400",
  "message": "菜谱不存在",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求有误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|状态信息|none|
|» data|string|true|none||none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## DELETE 通过菜谱id删除菜谱评价

DELETE /recipe/comment/delete/{id}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|id|path|string| 是 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1685112395131,
  "status": "200",
  "message": "success",
  "data": "删除成功"
}
```

> 服务器错误

```json
{
  "timestamp": 1685112303621,
  "status": "500",
  "message": "评价不存在",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|服务器错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|string|true|none||none|

状态码 **500**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## GET 通过菜谱id查看菜谱评价

GET /recipe/comment/list/{recipeId}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|recipeId|path|string| 是 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1685158137764,
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 30,
      "parentId": 0,
      "commenterId": 1647190430464479200,
      "content": "1",
      "createTime": "2023-05-26T23:02:24",
      "updateTime": null,
      "children": null,
      "icon": null,
      "nickName": "PPeng_ymtqtasbfi"
    },
    {
      "id": 23,
      "parentId": 0,
      "commenterId": 1637127723212214300,
      "content": "中国是一个餐饮文化大国，长期以来在某一地区由于地理环境、气候物产、文化传统以及民族习俗等因素的影响，形成有一定亲缘承袭关系、菜点风味相近，知名度较高，并为部分群众喜爱的地方风味著名流派称作菜系",
      "createTime": "2023-05-06T21:05:38",
      "updateTime": null,
      "children": null,
      "icon": null,
      "nickName": "违规昵称_96aj36mlld"
    },
    {
      "id": 13,
      "parentId": 0,
      "commenterId": 1647190430464479200,
      "content": "测试1",
      "createTime": "2023-05-01T23:52:46",
      "updateTime": null,
      "children": null,
      "icon": null,
      "nickName": "PPeng_ymtqtasbfi"
    },
    {
      "id": 12,
      "parentId": 0,
      "commenterId": 1647190430464479200,
      "content": "0",
      "createTime": "2023-05-01T23:52:09",
      "updateTime": null,
      "children": null,
      "icon": null,
      "nickName": "PPeng_ymtqtasbfi"
    },
    {
      "id": 11,
      "parentId": 0,
      "commenterId": 1647190430464479200,
      "content": "0",
      "createTime": "2023-05-01T23:50:16",
      "updateTime": null,
      "children": null,
      "icon": null,
      "nickName": "PPeng_ymtqtasbfi"
    },
    {
      "id": 5,
      "parentId": 0,
      "commenterId": 1647190430464479200,
      "content": "1",
      "createTime": "2023-04-30T23:10:29",
      "updateTime": null,
      "children": [
        {
          "id": 6,
          "parentId": 5,
          "commenterId": 1647190430464479200,
          "content": "1",
          "createTime": "2023-04-30T23:13:38",
          "updateTime": null,
          "children": [
            {
              "id": 29,
              "parentId": 6,
              "commenterId": 1647190430464479200,
              "content": "一条新评论",
              "createTime": "2023-05-26T22:50:34",
              "updateTime": null,
              "children": null,
              "icon": null,
              "nickName": "PPeng_ymtqtasbfi"
            },
            {
              "id": 28,
              "parentId": 6,
              "commenterId": 1647190430464479200,
              "content": "1",
              "createTime": "2023-05-16T18:43:42",
              "updateTime": null,
              "children": null,
              "icon": null,
              "nickName": "PPeng_ymtqtasbfi"
            },
            {
              "id": 10,
              "parentId": 6,
              "commenterId": 1647190430464479200,
              "content": "1",
              "createTime": "2023-05-01T23:49:29",
              "updateTime": null,
              "children": null,
              "icon": null,
              "nickName": "PPeng_ymtqtasbfi"
            },
            {
              "id": 9,
              "parentId": 6,
              "commenterId": 1647190430464479200,
              "content": "1",
              "createTime": "2023-04-30T23:40:40",
              "updateTime": null,
              "children": null,
              "icon": null,
              "nickName": "PPeng_ymtqtasbfi"
            }
          ],
          "icon": null,
          "nickName": "PPeng_ymtqtasbfi"
        }
      ],
      "icon": null,
      "nickName": "PPeng_ymtqtasbfi"
    }
  ]
}
```

> 请求有误

```json
{
  "timestamp": 1685111841312,
  "status": "400",
  "message": "菜谱不存在",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|400|[Bad Request](https://tools.ietf.org/html/rfc7231#section-6.5.1)|请求有误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||时间戳|
|» status|string|true|none||状态码|
|» message|string|true|none||状态信息|
|» data|[object]|true|none||评论结构体|
|»» id|integer|true|none||评论id|
|»» parentId|integer|true|none||父评论id（0即为最高级评论）|
|»» commenterId|integer|true|none||评论者id|
|»» content|string|true|none||评论内容|
|»» createTime|string|true|none||创建时间|
|»» updateTime|null|true|none||更新时间|
|»» children|[object]|true|none||子评论结构体|
|»»» id|integer|false|none||none|
|»»» parentId|integer|false|none||none|
|»»» commenterId|integer|false|none||none|
|»»» content|string|false|none||none|
|»»» createTime|string|false|none||none|
|»»» updateTime|null|false|none||none|
|»»» children|[object]|false|none||none|
|»»»» id|integer|true|none||none|
|»»»» parentId|integer|true|none||none|
|»»»» commenterId|integer|true|none||none|
|»»»» content|string|true|none||none|
|»»»» createTime|string|true|none||none|
|»»»» updateTime|null|true|none||none|
|»»»» children|null|true|none||none|

状态码 **400**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

# 菜谱模块/菜谱分类

## GET 获取所有菜谱类型 父类的集合

GET /recipe/type/list/parent

> 返回示例

> 成功

```json
{
  "timestamp": 1684860576749,
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "每日三餐",
      "parentId": 0
    },
    {
      "id": 2,
      "name": "主食",
      "parentId": 0
    },
    {
      "id": 3,
      "name": "家常菜谱",
      "parentId": 0
    },
    {
      "id": 4,
      "name": "中华菜系",
      "parentId": 0
    },
    {
      "id": 5,
      "name": "各地小吃",
      "parentId": 0
    },
    {
      "id": 6,
      "name": "外国菜谱",
      "parentId": 0
    },
    {
      "id": 7,
      "name": "烘培",
      "parentId": 0
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|[object]|true|none||none|
|»» id|integer|true|none|菜谱类型Id|none|
|»» name|string|true|none|菜谱类型|none|
|»» parentId|integer|true|none|菜谱类型父类Id|none|

# 菜谱搜索模块

## POST 菜品识别

POST /image/recognition/dish

> Body 请求参数

```yaml
dish: file:///Users/zaki/Desktop/test.jpeg

```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» dish|body|string(binary)| 否 ||菜品图片|

> 返回示例

> 成功

```json
{
  "timestamp": 1683807199587,
  "status": "200",
  "message": "success",
  "data": {
    "糖醋里脊": 0.0286858,
    "咕咾肉": 0.80132,
    "宫保鸡丁": 0.0498508
  }
}
```

> 服务器错误

```json
{
  "timestamp": 1683807677039,
  "status": "500",
  "message": "识别菜品失败",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|
|500|[Internal Server Error](https://tools.ietf.org/html/rfc7231#section-6.6.1)|服务器错误|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|object|true|none||none|
|»» 可能菜品名1|number|true|none||none|
|»» 可能菜品名2|number|true|none||none|
|»» 可能菜品名3|number|true|none||none|

状态码 **500**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## GET 菜谱搜索

GET /search

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|keyword|query|string| 是 ||搜索的关键词，也就是用户输入的内容|
|page|query|integer| 否 ||页数,默认为0|
|size|query|integer| 否 ||一页所呈现的信息数，默认为5|

> 返回示例

> 成功

```json
{
  "timestamp": 1684769601520,
  "status": "200",
  "message": "success",
  "data": [
    {
      "id": "1651599705078059593",
      "userId": "1651599705077059584",
      "nickName": "测试账号",
      "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
      "typeId": 11,
      "title": "🍜炸酱面🥢",
      "material": "面条、猪肉末、黄豆酱、葱姜蒜",
      "content": "步骤1: 炒猪肉末，加入葱姜蒜\n步骤2: 加入黄豆酱，炒香\n步骤3: 煮面条\n步骤4: 将炒好的猪肉末浇在面条上\n美味的炸酱面就做好了！",
      "mediaUrl": [
        "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
      ],
      "isVideo": 0,
      "likes": 95,
      "collections": 55,
      "createTime": "2023-05-21",
      "updateTime": "2023-05-21"
    },
    {
      "id": "1651599705077059586",
      "userId": "1651599705077059584",
      "nickName": "测试账号",
      "icon": "/icon/2023/5/17/243fe550c1cd4308994c5aa87af37246.png",
      "typeId": 8,
      "title": "🍔经典汉堡——美味快餐🍔",
      "material": "面包、牛肉饼、生菜、番茄、酱料",
      "content": "👉汉堡简介👈\n\n汉堡是一种非常受欢迎的快餐食品，起源于美国，被广泛传播和喜爱。\n\n👉制作步骤👈\n\n1. 准备食材：面包、牛肉饼、生菜、番茄、酱料。\n2. 烤面包：将面包片放入烤箱或烤盘中，烤至金黄色。\n3. 煎牛肉饼：在平底锅中加热一些油，将牛肉饼煎至两面金黄。\n4. 搭配生菜和番茄：将烤好的面包片上铺上生菜和番茄片。\n5. 加入牛肉饼：将煎好的牛肉饼放在生菜和番茄上。\n6. 涂抹酱料：在牛肉饼上涂抹适量的酱料。\n7. 盖上面包：将另一片烤好的面包片盖在酱料上。\n8. 完成：汉堡制作完成，可以享用啦！\n\n😋尝一口美味的汉堡，绝对让你欲罢不能！😋",
      "mediaUrl": [
        "https://ppeng.zhub.fun/resource/recipe/test1.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test2.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test3.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test4.jpg",
        "https://ppeng.zhub.fun/resource/recipe/test5.jpg"
      ],
      "isVideo": 0,
      "likes": 92,
      "collections": 68,
      "createTime": "2023-05-21",
      "updateTime": "2023-05-21"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|[[菜谱对象](#schema%e8%8f%9c%e8%b0%b1%e5%af%b9%e8%b1%a1)]|true|none||none|

# 消息模块

## GET 获取当前用户消息列表

GET /message/list

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|userId|query|string| 是 ||用户id|
|timestamp|query|string| 否 ||时间戳，默认为 当前时间戳|
|size|query|string| 否 ||一页所呈现的信息数，默认为5|
|p-token|header|string| 是 ||none|

> 返回示例

> 成功

```json
{
  "timestamp": 1684217262632,
  "status": "200",
  "message": "success",
  "data": {
    "resultList": [
      {
        "id": 224,
        "title": "标题21",
        "content": "内容3",
        "status": 0,
        "createTime": "2023-05-16 05:38"
      },
      {
        "id": 234,
        "title": "标题26",
        "content": "内容7",
        "status": 0,
        "createTime": "2023-05-16 05:35"
      },
      {
        "id": 210,
        "title": "标题48",
        "content": "内容50",
        "status": 0,
        "createTime": "2023-05-16 05:34"
      },
      {
        "id": 232,
        "title": "标题6",
        "content": "内容25",
        "status": 0,
        "createTime": "2023-05-16 05:34"
      },
      {
        "id": 205,
        "title": "标题34",
        "content": "内容42",
        "status": 0,
        "createTime": "2023-05-16 05:33"
      }
    ],
    "timestamp": 1684186411199
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none|时间戳|none|
|» status|string|true|none|状态码|none|
|» message|string|true|none|消息|none|
|» data|object|true|none|数据|none|
|»» resultList|[object]|true|none|消息列表|none|
|»»» id|integer|true|none|消息Id|none|
|»»» title|string|true|none|标题|none|
|»»» content|string|true|none|内容|none|
|»»» status|integer|true|none|状态|none|
|»»» createTime|string|true|none|创建时间|none|
|»» timestamp|integer|true|none|时间戳|none|

## PUT 已读消息

PUT /message/read

> Body 请求参数

```json
{
  "userId": "1651599705077059584",
  "messageIds": [
    1,
    2,
    3,
    4,
    5
  ]
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» userId|body|string| 是 | 用户id|none|
|» messageIds|body|[integer]| 是 | 消息id数组|none|

> 返回示例

> 成功

```json
{
  "timestamp": 1683993278562,
  "status": "200",
  "message": "success",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

## DELETE 删除用户消息

DELETE /message/delete

> Body 请求参数

```json
{
  "userId": "1651599705077059584",
  "messageIds": [
    1,
    2
  ]
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|p-token|header|string| 否 ||none|
|body|body|object| 否 ||none|
|» userId|body|string| 是 | 用户id|none|
|» messageIds|body|[integer]| 是 | 消息id数组|none|

> 返回示例

> 成功

```json
{
  "timestamp": 1683993317881,
  "status": "200",
  "message": "success",
  "data": null
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» timestamp|integer|true|none||none|
|» status|string|true|none||none|
|» message|string|true|none||none|
|» data|null|true|none||none|

# 管理员模块

## GET 获取RSA公钥

GET /hidden/admin/rsa

> 返回示例

> 成功

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 通过邮箱和密码登录

POST /hidden/admin/login/by/password

> Body 请求参数

```json
{
  "email": "",
  "password": ""
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|body|body|object| 否 ||none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 管理员登出

GET /hidden/admin/logout

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 获取当前用户信息

POST /hidden/admin/current

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 获取被举报/用户申述的菜谱列表

GET /hidden/admin/censor/list/{type}

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|type|path|string| 是 ||获取被举报/用户申述的菜谱列表     reported：被举报   appeal：申述|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 处理举报/申述请求

GET /hidden/admin/censor/handle/{type}

> Body 请求参数

```json
{
  "adminId": "",
  "recipeId": "",
  "result": 0,
  "reason": ""
}
```

### 请求参数

|名称|位置|类型|必选|中文名|说明|
|---|---|---|---|---|---|
|type|path|string| 是 ||处理举报/申述请求  reported：被举报   appeal：申述|
|body|body|object| 否 ||none|
|» adminId|body|string| 是 | 管理员id |none|
|» recipeId|body|string| 是 | 菜谱id|none|
|» result|body|integer| 是 | 审核结果|只能是0或者1|
|» reason|body|string| 是 | 判定理由|none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 数据模型

<h2 id="tocS_通用响应体">通用响应体</h2>

<a id="schema通用响应体"></a>
<a id="schema_通用响应体"></a>
<a id="tocS通用响应体"></a>
<a id="tocs通用响应体"></a>

```json
{
  "timestamp": 0,
  "status": "string",
  "message": "string",
  "data": {}
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|timestamp|integer|true|none|时间戳|none|
|status|string|true|none|状态码|none|
|message|string|true|none|状态信息|none|
|data|object|true|none|数据|none|

<h2 id="tocS_菜谱对象">菜谱对象</h2>

<a id="schema菜谱对象"></a>
<a id="schema_菜谱对象"></a>
<a id="tocS菜谱对象"></a>
<a id="tocs菜谱对象"></a>

```json
{
  "id": "string",
  "userId": "string",
  "nickName": "string",
  "icon": "string",
  "type": "string",
  "title": "string",
  "material": "string",
  "content": "string",
  "mediaUrl": [
    "string"
  ],
  "isVideo": 0,
  "likes": 0,
  "collections": 0,
  "createTime": "string",
  "updateTime": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|string|true|none|菜谱id|none|
|userId|string|true|none|用户id|none|
|nickName|string|true|none|昵称|none|
|icon|string|true|none|头像|none|
|type|string|true|none|菜谱类型|none|
|title|string|true|none|标题|none|
|material|string|true|none|配料表|none|
|content|string|true|none|内容|none|
|mediaUrl|[string]|true|none|媒体路径|none|
|isVideo|integer|true|none|是否为视频|0：图文 1：视频|
|likes|integer|true|none|点赞数|none|
|collections|integer|true|none|收藏数|none|
|createTime|string|true|none|创建时间|none|
|updateTime|string|true|none|更新时间|none|

