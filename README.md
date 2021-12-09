# Fuzz-Auth-For-Burp

> 一个被动fuzz权限绕过的Burp插件

演示界面
![Uploading 截屏2021-12-09 下午12.25.48.png…](http://tva4.sinaimg.cn/large/005Js7eGgy1gx7h43ccs3j327w1b2n2d.jpg)

请求的Resquest会打印在Output,方便确认查看

![Uploading 截屏2021-12-09 下午12.25.48.png…](http://tva1.sinaimg.cn/large/005Js7eGgy1gx7h8vk637j327m0vq7ns.jpg)


#### 因为可能存在误报，所以加入了length 返回数据包大小

目前是全被动扫描，后续可能会加入主动扫描，并且设置被动扫描开关

以自身使用来看，误报率不算低，还在思考如何改善，并且把代码的耦合度降低

