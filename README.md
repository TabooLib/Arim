# Arim （城市）

[![](https://jitpack.io/v/FxRayHughes/Arim.svg)](https://jitpack.io/#FxRayHughes/Arim)

## 本工具库集合:
- 双栈计算器 - Saukiya
- 符号树变量计算器 - Saukiya
- 条件表达式求值器 - 枫溪
- 物品匹配工具 - 枫溪
- 生物&方块发光工具 - Gei
- 文件夹读取工具 - 枫溪

## 使用方法

1. 设置仓库

```kts
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```

2. 添加依赖

```kts
dependencies {
    taboo("com.github.FxRayHughes:Arim:VERSION") // 替换为上方标签中的版本
}
```

3. 设置重定向

```kts
taboolib{
    relocate("top.maplex.arim","xxx.xxx.arim")
}
```
## 模块文档
https://taboolib.feishu.cn/wiki/PisKwWgRHirVeRkAz9ncDY7Cn2d?from=from_copylink
