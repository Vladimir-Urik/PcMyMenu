package cz.pvpcraft.GGGEDR.PcMyMenus.Utils

import com.iridium.iridiumcolorapi.IridiumColorAPI
import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Scripts.ScriptBoss
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.entity.Player


class TextUtils {

    companion object {

        fun colorize(text: String, p: Player?): String {
            return if(p == null){
                ScriptBoss.parse(IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', text.replace("%version%", PcMyMenusPlugin.plugin?.description?.version.toString()))))
            } else {
                ScriptBoss.parse(IridiumColorAPI.process(papiReplace(ChatColor.translateAlternateColorCodes('&', text.replace("%version%", PcMyMenusPlugin.plugin?.description?.version.toString())), p)))
            }
        }

        fun colorize(lines: List<String>, p: Player?): List<String> {
            val list: ArrayList<String> = ArrayList()
            if(p == null){
                for(line in lines){
                    list.add(ScriptBoss.parse(IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', line.replace("%version%", PcMyMenusPlugin.plugin?.description?.version.toString())))))
                }
            } else {
                for(line in lines){
                    list.add(ScriptBoss.parse(IridiumColorAPI.process(papiReplace(ChatColor.translateAlternateColorCodes('&', line.replace("%version%", PcMyMenusPlugin.plugin?.description?.version.toString())), p))))
                }
            }
            return list
        }

        fun papiReplace(msg: String, p: Player): String {
            return PlaceholderAPI.setPlaceholders(p, msg)
        }
    }
}
