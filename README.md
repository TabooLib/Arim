# Arim （城市）

## 本工具库集合:
- 双栈计算器 - Saukiya
- 符号树变量计算器 - Saukiya
- 条件表达式求值器 - 枫溪
- 物品匹配工具 - 枫溪
- 实体匹配工具 - WhiteSoul
- 生物&方块发光工具 - Gei
- 文件夹读取工具 - 枫溪
- TabooLib 5.X FLAT 风格命令帮助 - 坏黑、Mical
- 基于权重的快随随机工具 - 枫溪
- 多物品库插件挂钩工具 - 嘿鹰

## 使用方法

1. 添加依赖

```kts
repositories {
    maven {
        // 枫溪的仓库
        url = uri("https://nexus.maplex.top/repository/maven-public/")
        isAllowInsecureProtocol = true
    }
}
dependencies {
    taboo("top.maplex.arim:Arim:VERSION") // 替换为最新版本
}
```

2. 设置重定向

```kts
taboolib{
    relocate("top.maplex.arim","xxx.xxx.arim")
}
```
## 模块文档
https://taboolib.feishu.cn/wiki/PisKwWgRHirVeRkAz9ncDY7Cn2d?from=from_copylink
