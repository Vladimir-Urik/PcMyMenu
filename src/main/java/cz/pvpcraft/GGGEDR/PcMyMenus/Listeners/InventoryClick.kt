package cz.pvpcraft.GGGEDR.PcMyMenus.Listeners

import com.google.common.io.ByteStreams
import cz.pvpcraft.GGGEDR.PcMyMenus.Classes.MyItem
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.LogType
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.Logging
import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Scripts.ScriptBoss
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.TextUtils
import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.containsKey
import kotlin.collections.remove
import kotlin.collections.set


object InventoryClick : Listener {

    @EventHandler
    fun onClick(e: InventoryClickEvent){
        val player:Player = e.whoClicked as Player
        Logging.log("${player.name} clicked in inventory", LogType.DEBUG)
        if(PcMyMenusPlugin.items_cache.containsKey(player)){
            e.isCancelled = true
            Logging.log("${player.name} is in ItemCache", LogType.DEBUG)
            val item: ItemStack? = e.currentItem
            if(item != null) {
                if (NBTItem(item).hasKey("clickable_id")) {
                    Logging.log(
                        "${player.name} has clicked on item with clickable id: " + NBTItem(item).getString("clickable_id"), LogType.DEBUG
                    )
                    e.isCancelled = true
                    val clickID: String = NBTItem(item).getString("clickable_id")
                    val items: ArrayList<MyItem>? = PcMyMenusPlugin.items_cache[player]
                    val data: HashMap<String, MyItem> = HashMap()
                    for (itemMy in items!!) {
                        data[itemMy.clickableID] = itemMy
                    }
                    if (data.containsKey(clickID)) {
                        val myItem: MyItem? = data[clickID]
                        for(action in myItem!!.actions){
                            val parsedAction:String = ScriptBoss.parse(TextUtils.colorize(TextUtils.papiReplace(action, player), player))
                            if(parsedAction.startsWith("player: ")){
                                val cmd =  TextUtils.colorize(parsedAction.removePrefix("player: "), player)
                                player.chat("/$cmd")
                            }
                            if(parsedAction.startsWith("msg: ")){
                                val msg = TextUtils.colorize(parsedAction.removePrefix("msg: "), player)
                                player.sendMessage(msg)
                            }
                            if(parsedAction.startsWith("console: ")){
                                val cmd = TextUtils.colorize(parsedAction.removePrefix("console: "), player)
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
                            }
                            if(parsedAction.startsWith("server: ")){
                                val server = TextUtils.colorize(parsedAction.removePrefix("server: "), player)
                                val out = ByteStreams.newDataOutput()
                                out.writeUTF("Connect")
                                out.writeUTF(server)
                                player.sendPluginMessage(PcMyMenusPlugin.plugin, "BungeeCord", out.toByteArray())
                            }
                            if(parsedAction.startsWith("title: ")){
                                val title = TextUtils.colorize(parsedAction.removePrefix("title: "), player).split(";;;")
                                player.sendTitle(TextUtils.colorize(title[0], player), TextUtils.colorize(title[1], player))
                            }
                            if(parsedAction.startsWith("sound: ")){
                                val sound = parsedAction.removePrefix("sound: ").split(" ")
                                player.playSound(player.getLocation(), Sound.valueOf(sound[0]), sound[1].toFloat(), sound[2].toFloat())
                            }
                            if(parsedAction == "refresh"){
                                myItem.refresh(player, myItem.menu.getConfigurationSection("items.${myItem.char}"))
                                e.inventory.setItem(myItem.slot, myItem.item)
                            }
                            if(parsedAction == "close"){
                                player.closeInventory()
                            }
                        }
                    }
                }
            } else {
                Logging.log("Item is null", LogType.DEBUG)
            }
        }
    }

    @EventHandler
    fun onMove(e: InventoryMoveItemEvent) {
       if(e.item != null){
           if(NBTItem(e.item).hasKey("clickable_id")){
               e.isCancelled = true
           }
       }
    }

    @EventHandler
    fun onClose(e: InventoryCloseEvent) {
        if(PcMyMenusPlugin.items_cache.containsKey(e.player))
            e.player.itemOnCursor = ItemStack(Material.AIR)
            PcMyMenusPlugin.items_cache.remove(e.player)
    }

}
