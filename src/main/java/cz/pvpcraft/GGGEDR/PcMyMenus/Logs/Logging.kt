package cz.pvpcraft.GGGEDR.PcMyMenus.Logs

import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.TextUtils
import org.bukkit.Bukkit

class Logging {

    companion object {

         fun log(message: String, type: LogType){
            if(type == LogType.DEBUG){
                val debug = PcMyMenusPlugin.cachedConfig.getBoolean("dev-settings.debug")
                if(debug){
                    Bukkit.getLogger().info(TextUtils.colorize("[PcMyMenu] &9[&lDEBUG&9]: &7${TextUtils.colorize(message, null)}", null))
                }

            } else if(type == LogType.WARNING){
                Bukkit.getLogger().info(TextUtils.colorize("[PcMyMenu] &e[PcMyMenu][&lWARNING&c]: &7${TextUtils.colorize(message, null)}", null))

            } else if(type == LogType.ERROR){
                Bukkit.getLogger().info(TextUtils.colorize("[PcMyMenu] &4[&lERROR&c]: &7${TextUtils.colorize(message, null)}", null))

            } else if(type == LogType.INFO){
                Bukkit.getLogger().info(TextUtils.colorize("[PcMyMenu] &b[&lINFO&b]: &7${TextUtils.colorize(message, null)}", null))
            }
         }

    }
}
