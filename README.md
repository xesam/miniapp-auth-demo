# 微信小程序敏感信息接口

1. wx.login 获得 openid，session_key，unionid
2. getUserInfo 获取用户昵称等
3. getPhoneNumber 获取用户手机号，需要使用企业主体小程序

### 工程结构

    --app # demo 小程序，默认端口 8081
    --server # demo 服务端，默认端口 8081

server 需要配置 appid 与 appsecret：

    <web-app>
        <display-name>Miniapp Auth</display-name>
    
        <context-param>
            <param-name>appid</param-name>
            <param-value>wxappid</param-value>
        </context-param>
    
        <context-param>
            <param-name>appsecret</param-name>
            <param-value>wxappsecret</param-value>
        </context-param>
   
    </web-app>


