# Fuzz-AuthBypass-For-Burp

> 一个被动fuzz权限绕过的Burp插件


**在渗透过程中一些对目录扫描、权限绕过的fuzz都是主动扫描，我更希望把这些动作形成一个被动的动作，精简字典，快速fuzz出漏洞**



演示界面

![Uploading 截屏2021-12-09 下午12.25.48.png…](http://tva4.sinaimg.cn/large/005Js7eGgy1gx7h43ccs3j327w1b2n2d.jpg)

请求的Resquest会打印在Output,方便确认查看

![Uploading 截屏2021-12-09 下午12.25.48.png…](http://tva1.sinaimg.cn/large/005Js7eGgy1gx7h8vk637j327m0vq7ns.jpg)


#### 因为可能存在误报，所以加入了length 返回数据包大小

目前是全被动扫描，后续可能会加入主动扫描，并且设置被动扫描开关

> 一些待完善的点

* 代码耦合度还需降低
* 实现主动扫描
* 加入被动扫描开关
* host去重


