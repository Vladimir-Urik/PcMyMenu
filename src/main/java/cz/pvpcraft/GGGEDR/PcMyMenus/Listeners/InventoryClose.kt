package cz.pvpcraft.GGGEDR.PcMyMenus.Listeners

import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.XMaterial
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.File
import java.util.concurrent.Executors

object InventoryClose : Listener {

    @EventHandler
    fun onClose(e: InventoryCloseEvent){
        if(PcMyMenusPlugin.editors.keys.contains(e.player)){
            if(PcMyMenusPlugin.editors[e.player] == e.inventory.title){
                val id = PcMyMenusPlugin.editors[e.player]
                PcMyMenusPlugin.editors.remove(e.player)
                val inv:Inventory = e.inventory
                Executors.newSingleThreadExecutor().submit(Runnable {
                    val file:File = File("${PcMyMenusPlugin.plugin?.dataFolder}/menus/$id.yml")
                    if(!file.exists()){
                        file.createNewFile()
                        val editor:YamlConfiguration = YamlConfiguration.loadConfiguration(file)
                        editor.set("info.title", id)
                        val commands:ArrayList<String> = ArrayList()
                        commands.add(id!!)
                        commands.add("menu $id")
                        editor.set("info.commands", commands)
                        val items:HashMap<String, ItemStack> = HashMap()
                        val itemsPositions: HashMap<ItemStack, ArrayList<Int>> = HashMap()
                        var lines:ArrayList<String> = ArrayList()
                        val list:String = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpRrSsTtUuVvXxYyZz#123456789"
                        for(r in 1..6){
                            for(i in 0..8){
                                if(inv.getItem((i + ((9 * r) - 9))) != null){
                                    val item:ItemStack = inv.getItem((i + ((9 * r) - 9)))
                                    if(item.type != Material.AIR){
                                        if(!items.values.contains(item)){
                                            items[list.toCharArray()[items.size + 1].toString()] = item
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }

}
