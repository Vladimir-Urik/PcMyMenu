package cz.pvpcraft.GGGEDR.PcMyMenus.Listeners

import cz.pvpcraft.GGGEDR.PcMyMenus.Classes.MyMenu
import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object CommandControl: Listener{

    @EventHandler(priority = EventPriority.MONITOR)
    fun onCommand(e: PlayerCommandPreprocessEvent){
        val cmd: String = e.message.split("/")[1]
        if(PcMyMenusPlugin.commands.containsKey(e.message.split("/")[1])){
            e.isCancelled = true
            val menu: MyMenu? = PcMyMenusPlugin.menus[PcMyMenusPlugin.commands[cmd]]
            menu?.openMenu(e.player)
        }
    }

}
