# QQRobotBaseOnMirai
1.此机器人遵循AGPL开源协议</br>
2.此机器人并不是独立的,它基于Mirai</br>
3.需要配置<a href="https://github.com/iTXTech/mirai-console-loader">MiraiConsoleLoader</a>
## 基本信息
运行环境: 机器人目前仅能运行在Linux(或者安卓设置配置proot环境的Linux)</br>
Java版本: 最低为Java8</br>
## 配置
机器人为开箱即用,仅需一个完整的jar即可运行,运行时会检查$HOME/Robot目录是否存在,若不存在则会创建并要求输入基本信息(Mirai的IP,端口,登陆的机器人)
## 构建
此项目为Maven项目,使用mvn assembly:assembly命令构建
