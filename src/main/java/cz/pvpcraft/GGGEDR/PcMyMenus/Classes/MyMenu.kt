package cz.pvpcraft.GGGEDR.PcMyMenus.Classes


import com.google.common.io.ByteStreams
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.LogType
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.Logging
import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Scripts.ScriptBoss
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.TextUtils
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class MyMenu {

    var rows:Int = 1;

    var title:String = "Moje PvPCraft menu"

    var items:HashMap<Int, MyItem> = HashMap()

    var commands:List<String> = ArrayList()

    var name:String = ""

    private var yamlConfiguration:YamlConfiguration = YamlConfiguration()

    private fun addItem(item: MyItem){
        for(i in 1..(rows*9)){
            if(!items.containsKey(i)){
                items[i] = item
                return
            }
        }
    }

    private fun setItem(slot: Int, item: MyItem){
        items[slot] = item
    }

    companion object {

        fun buildFromFile(c: YamlConfiguration, name: String): MyMenu {
            val menu:MyMenu = MyMenu()
            menu.commands = c.getStringList("info.commands")
            menu.yamlConfiguration = c
            menu.name = name
            return menu
        }
    }

    fun openMenu(p: Player){
        var open:Boolean = true
        val c: YamlConfiguration = yamlConfiguration
        val lines:List<String> = c.getStringList("syntax")
        title = c.getString("info.title")
        Logging.log("loading menu with title: $title", LogType.DEBUG)
        rows = lines.size
        Logging.log("loading menu with $rows rows", LogType.DEBUG)
        commands = c.getStringList("info.commands")
        Logging.log("loading menu with commands:", LogType.DEBUG)
        for(cmd in commands){
            Logging.log(cmd, LogType.DEBUG)
        }
        var i = 0
        for(line in lines){
            if(line.length > 9){
                Logging.log("Syntax line: &c\"$line\" &7in menu: \"&c$title\" &7It has more than 9 characters!", LogType.ERROR)
                return
            }
            if(line.length < 9){
                Logging.log("Syntax line: &c\"$line\" &7in menu: &c\"$title\" &7It has less than 9 characters!", LogType.ERROR)
                return
            }

            for(char in line.toCharArray()){
                if(char.toString() != " "){
                    val character:String = char.toString()
                    Logging.log("loading menu item with indicator: \"$character\" on slot: $i", LogType.DEBUG)
                    val item:MyItem = MyItem.parseMyItem(character, c.getConfigurationSection("items.$character"), p)
                    items[i] = item
                }
                i++
            }

        }

        var itemses:ArrayList<MyItem> = ArrayList()

        if(PcMyMenusPlugin.items_cache.containsKey(p)) {
            itemses = PcMyMenusPlugin.items_cache[p]!!
            PcMyMenusPlugin.items_cache.remove(p)
        }

        val inv: Inventory = Bukkit.createInventory(null, rows*9, TextUtils.colorize(title, p))
        for(slot in items.keys){
            items[slot]?.slot = slot
            items[slot]?.menu = c
            inv.setItem(slot, items[slot]?.item)
            items[slot]?.let { itemses.add(it) }
        }

        if(c.getStringList("info.actions.open") != null){
            for(action in c.getStringList("info.actions.open")){
                val parsedAction:String = ScriptBoss.parse(TextUtils.papiReplace(TextUtils.colorize(action, p), p))
                Logging.log("Parsed action | $action | $parsedAction", LogType.DEBUG)
                if(parsedAction.startsWith("player: ")){
                    val cmd =  TextUtils.colorize(parsedAction.removePrefix("player: "), p)
                    p.chat("/$cmd")
                }
                if(parsedAction.startsWith("msg: ")){
                    val msg = TextUtils.colorize(parsedAction.removePrefix("msg: "), p)
                    p.sendMessage(msg)
                }
                if(parsedAction.startsWith("console: ")){
                    val cmd = TextUtils.colorize(parsedAction.removePrefix("console: "), p)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
                }
                if(parsedAction.startsWith("server: ")){
                    val server = TextUtils.colorize(parsedAction.removePrefix("server: "), p)
                    val out = ByteStreams.newDataOutput()
                    out.writeUTF("Connect")
                    out.writeUTF(server)
                    p.sendPluginMessage(PcMyMenusPlugin.plugin, "BungeeCord", out.toByteArray())
                }
                if(parsedAction.startsWith("sound: ")){
                    val sound = parsedAction.removePrefix("sound: ").split(" ")
                    p.playSound(p.getLocation(), Sound.valueOf(sound[0]), sound[1].toFloat(), sound[2].toFloat())
                }
                if(parsedAction == "close"){
                    open = false
                }
            }
        }

        if(open){
            PcMyMenusPlugin.items_cache[p] = itemses
            p.openInventory(inv)
        }
    }

}
