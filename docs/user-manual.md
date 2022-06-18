![JythonEngine](jythonengine.png)

# JythonEngine

## 简介
JythonEngine 通过对 Jython 的封装可以实现使用 python 脚本来直接控制服务器，通过交互式控制台实时获取输出，支持同步/异步两种运行方式，通过同步兼容Bukkit的线程不安全，通过异步实现更高效的运行。

得益于 Jython 的实现 ，使得 Python 与 Java 可以轻松实现互操作，并使得 Jython 脚本获得不亚于插件的操作能力，而 Jython 本身的脚本可以不经过编译器，直接运行则大大降低了调试编译打包所带来的额外时间开销。

交互式控制台，即刻反馈的操作方式，大大降低部署的时间开销

## 特性 
  - 完整的服务端 API 支持 -> 轻松操作任何内容
  - 交互式控制台 -> 便携而直观的服务器控制工具
  - 异步处理支持 -> 高效流畅 不卡主线程
  - 语法补全模拟 \[测试] -> 转 jar 包为 python 格式，便于语法补全
  - 脚本预储存 -> 保存脚本便于复用
  - 聊天捕获 -> 告别复杂的前缀 像在控制台一样执行命令
  - 灵活输出 -> 100%的消息格式自定义

## 使用效果
就不再搬运图片了
见 [mcbbs 发布帖](url=https://www.mcbbs.net/thread-1352510-1-1.html)

## 命令
注意：JE 的命令均以 '//' 起首，也就是执行时需要输入类似 '///frame reload' 的格式 <- 三斜杠

//
  - 开启/关闭控制台模式

//consoles
  - create 创建解释器
  - close 关闭当前解释器
  - clean 清空当前解释器变量
  - synch 切换同步执行/异步执行
  - get_output 重新获取当前解释器输出流

//script
  - list 列出当前已加载脚本
  - reload 重载脚本
  - run <path/#name> 执行脚本 参数为脚本相对服务器根目录路径 或 #脚本名

//frame
  - reload 重载框架到 JythonEngine/libs 文件夹

## 权限
全部权限格式为 jythonengine.<command>.use
如 //consoles 命令为 jythonengine.//consoles.use

// TODO emmm...很糟糕的权限机制，将优化

