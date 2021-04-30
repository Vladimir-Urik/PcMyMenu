package cz.pvpcraft.GGGEDR.PcMyMenus.Commands.Subcommands

import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.TextUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

object TemplateSCMD {

    fun execute(p: Player, cmd: Command?, label: String?, args: Array<out String>?) {
        val id = "${System.currentTimeMillis()}-${p.name}"
        val inv:Inventory = Bukkit.createInventory(null, 54, id)
        PcMyMenusPlugin.editors[p] = id
        p.openInventory(inv)
        p.sendMessage(
            TextUtils.colorize(PcMyMenusPlugin.cachedConfig.getString("commands.template.open"), p))
    }

}
