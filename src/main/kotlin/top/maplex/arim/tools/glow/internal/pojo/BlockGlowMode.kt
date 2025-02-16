package top.maplex.arim.tools.glow.internal.pojo

/**
 * 方块发光模式
 * CLASSIC_11200_11605_UNIVERSAL: 使用潜影贝，不影响方块交互，但边框只能为正方形，1.12.2，1.16.5，以及1.17以上
 * STYLE_11200_11605_ONLY: 使用掉落方块实体，方块交互将不可用(直到取消发光)，但边框可以完全贴合原方块，目前仅1.12.2和1.16.5可用
 */
enum class BlockGlowMode {
    CLASSIC_11200_11605_UNIVERSAL, STYLE_11200_11605_ONLY
}
