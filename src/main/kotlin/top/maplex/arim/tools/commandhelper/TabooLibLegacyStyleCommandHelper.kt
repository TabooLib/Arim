/*
 *  Copyright (C) 2022-2024 PolarAstrumLab
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package top.maplex.arim.tools.commandhelper

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.common.platform.function.pluginId
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Strings
import taboolib.common.util.VariableReader
import taboolib.library.reflex.Reflex.Companion.getProperty
import taboolib.module.chat.component
import taboolib.module.lang.asLangText
import taboolib.module.lang.asLangTextList
import taboolib.module.lang.asLangTextOrNull
import taboolib.module.lang.sendLang
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ingame.command.CommandUtils
 *
 * @author mical
 * @since 2024/10/6 01:02
 */

/**
command-helper:
- '&7'
- '  &f&l{pluginId} &7v{pluginVersion}'
- '&7'
- '  &7命令: &f/{command} &8\[...\]'
- '  &7参数:'
- '{subCommands}'
- '&7'
command-sub:
- '    &8- [&f{name}](h=/{command} {name} {usage}&8- &7{description};suggest=/{command} {name})'
- '      &7{description}'
command-argument-missing:
- '&8[&3&l{pluginId}&8] &7指令 &f{name} &7参数不足'
- '&8[&3&l{pluginId}&8] &7正确用法:'
- '&8[&3&l{pluginId}&8] &7/{command} {name} {usage}&8- &7{description}'
command-argument-wrong:
- '&8[&3&l{pluginId}&8] &7指令 &f{name} &7参数有误'
- '&8[&3&l{pluginId}&8] &7正确用法:'
- '&8[&3&l{pluginId}&8] &7/{command} {name} {usage}&8- &7{description}'
command-argument-unknown:
- '&8[&3&l{pluginId}&8] &7指令 &f{name} &7不存在'
- '&8[&3&l{pluginId}&8] &7你可能想要:'
- '&8[&3&l{pluginId}&8] &7/{command} {similar} {usage}&8- &7{description}'
command-incorrect-sender: '&8[&3&l{pluginId}&8] &7指令 &f{name} &7只能由 &f玩家 &7执行'

command-subCommands-test1-description: '测试命令1'
command-subCommands-test1-usage: '&7[&8必填参数&7] &7<&8选填参数&7>'

command-subCommands-test2-description: '测试命令2'
command-subCommands-test2-usage: ''
 */
@Suppress("DuplicatedCode")
fun CommandComponent.createTabooLegacyStyleCommandHelper(commandType: String = "main", main: Boolean = commandType == "main") {
    val prefix = "command" + if (main) "" else "-$commandType"
    execute<ProxyCommandSender> { sender, ctx, _ ->
        val text = mutableListOf<String>()

        for (command in children.filterIsInstance<CommandComponentLiteral>()) {
            if (!sender.isOp) {
                if (!sender.hasPermission(command.permission)) continue
                else if (command.hidden) continue
            }
            val name = command.aliases[0]
            var usage = sender.asLangTextOrNull("$prefix-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangTextOrNull("$prefix-subCommands-$name-description") ?: sender.asLangText("$prefix-no-desc")
            text += sender.asLangTextList("$prefix-sub", name to "name", description to "description", usage to "usage", ctx.command.name to "command")
        }

        sender.asLangTextList(
            "$prefix-helper",
            pluginId to "pluginId",
            pluginVersion to "pluginVersion",
            ctx.command.name to "command"
        ).variable("subCommands", text).forEach { it.component().buildColored().sendTo(sender) }
    }

    if (this is CommandBase) {
        incorrectCommand { sender, ctx, _, state ->
            val input = ctx.getProperty<Array<String>>("rawArgs")!!.first()
            val name = children.filterIsInstance<CommandComponentLiteral>()
                .firstOrNull { it.aliases.contains(input) }?.aliases?.get(0) ?: input
            var usage = sender.asLangTextOrNull("$prefix-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            var description = sender.asLangTextOrNull("$prefix-subCommands-$name-description") ?: sender.asLangText("$prefix-no-desc")

            when (state) {
                // 缺参数
                1 -> {
                    sender.sendLang("$prefix-argument-missing", name to "name", usage to "usage", description to "description", ctx.command.name to "command", pluginId to "pluginId")
                }

                // 参数错误
                2 -> {
                    if (ctx.args().size > 1) {
                        sender.sendLang("$prefix-argument-wrong", name to "name", usage to "usage", description to "description", ctx.command.name to "command", pluginId to "pluginId")
                    } else {
                        val similar = children.filterIsInstance<CommandComponentLiteral>()
                            .filterNot { it.hidden }
                            .filter { sender.hasPermission(it.permission) }
                            .maxByOrNull { Strings.similarDegree(name, it.aliases[0]) }
                            ?.aliases?.get(0)
                            ?: ""
                        if (similar.isEmpty()) return@incorrectCommand
                        usage = sender.asLangTextOrNull("$prefix-subCommands-$similar-usage") ?: ""
                        if (usage.isNotEmpty()) usage += " "
                        description = sender.asLangTextOrNull("$prefix-subCommands-$similar-description") ?: sender.asLangText("$prefix-no-desc")
                        sender.sendLang("$prefix-argument-unknown", name to "name", similar to "similar", ctx.command.name to "command", pluginId to "pluginId", usage to "usage", description to "description")
                    }
                }
            }
        }

        incorrectSender { sender, ctx ->
            sender.sendLang("$prefix-incorrect-sender", ctx.args().first() to "name", pluginId to "pluginId")
        }
    }
}

/**
 * NOTICE 也可以让下面的工具成为一个单独的工具
 * 工具 by Polar-Pumpkin
 * https://github.com/Polar-Pumpkin/Module-ParrotX/blob/dev/src/main/kotlin/org/serverct/parrot/parrotx/function/Variable.kt
 */
object VariableReaders {
    val BRACES by lazy { VariableReader("{", "}") }
    val DOUBLE_BRACES by lazy { VariableReader("{{", "}}") }
    val PERCENT by lazy { VariableReader("%", "%") }

    internal val AREA_START by lazy { "^#area (?<area>.+)$".toRegex() }
    internal val AREA_END by lazy { "^#end(?: (?<area>.+))?$".toRegex() }
}

fun interface VariableFunction {
    fun transfer(name: String): Collection<String>?
}

fun Collection<String>.variables(reader: VariableReader = VariableReaders.BRACES, func: VariableFunction): List<String> {
    return flatMap { context ->
        val result = ArrayList<String>()
        val queued = HashMap<String, Queue<String>>()
        reader.replaceNested(context) scan@{
            queued[this] = LinkedList(func.transfer(this) ?: return@scan this)
            this
        }
        if (queued.isEmpty()) {
            return@flatMap listOf(context)
        }

        while (queued.any { (_, queue) -> queue.isNotEmpty() }) {
            result += reader.replaceNested(context) {
                if (this in queued) {
                    queued[this]!!.poll() ?: ""
                } else {
                    this
                }
            }
        }
        result
    }
}

fun Collection<String>.variable(key: String, value: Collection<String>, reader: VariableReader = VariableReaders.BRACES): List<String> {
    return variables(reader) { if (it == key) value else null }
}