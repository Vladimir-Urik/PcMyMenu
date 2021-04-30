package cz.pvpcraft.GGGEDR.PcMyMenus.Commands.Subcommands

import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.TextUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ReloadSCMD {

    fun execute(p: Player, cmd: Command?, label: String?, args: Array<out String>?) {
        val start:Long = System.currentTimeMillis()
        val menus = PcMyMenusPlugin.plugin?.loadMenus()
        PcMyMenusPlugin.plugin?.loadConfig()
        val stop:Long = System.currentTimeMillis()
        val time = stop - start
        p.sendMessage(TextUtils.colorize(PcMyMenusPlugin.cachedConfig.getString("commands.main.reload")
            .replace("%version%", PcMyMenusPlugin.plugin?.description?.version.toString())
            .replace("%menus%", menus.toString())
            .replace("%time%", time.toString()), p))
    }

}
