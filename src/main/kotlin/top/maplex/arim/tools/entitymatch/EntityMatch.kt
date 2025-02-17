package top.maplex.arim.tools.entitymatch

import org.bukkit.entity.LivingEntity
import taboolib.module.chat.colored
import top.maplex.arim.tools.entitymatch.handler.*
import top.maplex.arim.tools.entitymatch.util.ParserUtils
import java.util.concurrent.ConcurrentHashMap

/**
 * 实体匹配公式
 * @Author WhiteSoul
 */
class EntityMatch {

    private val handlers = ConcurrentHashMap<String, EntityHandler>()


    fun registerHandler(name: String, handler: EntityHandler) {
        handlers[name] = handler
    }

    fun unregisterHandler(name: String) {
        handlers.remove(name)
    }

    init {
        registerHandler("name", NameHandler())
        registerHandler("type", TypeHandler())
        registerHandler("meta", MetaHandler())
        registerHandler("health", HealthHandler())
    }

    fun match(entity: LivingEntity, match: String): Boolean {
        return ParserUtils.splitConditions(match).all { condition ->
            val (key, value) = ParserUtils.parseKeyValue(condition) ?: return@all false
            handlers[key]?.check(entity, value.colored()) ?: false
        }
    }

    fun matchTry(entity: LivingEntity, match: String): Boolean {
        return try {
            match(entity, match)
        } catch (e: Exception) {
            false
        }
    }
}
