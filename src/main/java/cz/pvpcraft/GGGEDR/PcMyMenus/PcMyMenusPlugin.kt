package cz.pvpcraft.GGGEDR.PcMyMenus

import cz.pvpcraft.GGGEDR.PcMyMenus.Classes.MyItem
import cz.pvpcraft.GGGEDR.PcMyMenus.Classes.MyMenu
import cz.pvpcraft.GGGEDR.PcMyMenus.Commands.MainCMD
import cz.pvpcraft.GGGEDR.PcMyMenus.Listeners.CommandControl
import cz.pvpcraft.GGGEDR.PcMyMenus.Listeners.InventoryClick
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.ConsoleHider
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.LogType
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.Logging
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.Executors

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PcMyMenusPlugin: JavaPlugin() {
    companion object {
        var plugin: PcMyMenusPlugin? = null
        var cachedConfig: YamlConfiguration = YamlConfiguration()
        var menus: HashMap<String, MyMenu> = HashMap()
        var items_cache: HashMap<Player, ArrayList<MyItem>> = HashMap()
        var commands: HashMap<String, String> = HashMap()
        var editors:HashMap<Player, String> = HashMap()
    }

    var configFile: File = File(dataFolder, "config.yml")

    override fun onEnable() {
        plugin = this
        if(!File(dataFolder, "config.yml").exists()){
            this.config.options().copyDefaults(true)
            this.saveConfig()
        }
        this.loadConfig()
        Executors.newSingleThreadExecutor().submit(Runnable {
                this.loadMenus()
            })
        this.server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")
        Bukkit.getPluginManager().registerEvents(CommandControl, this)
        Bukkit.getPluginManager().registerEvents(InventoryClick, this)
        getCommand("pcmenu").executor = MainCMD

        ConsoleHider()
    }

    override fun onDisable() {
        plugin = null
        cachedConfig = YamlConfiguration()
        menus = HashMap()
        items_cache = HashMap()
        commands = HashMap()
    }


    fun loadConfig() {
        cachedConfig = YamlConfiguration()
        cachedConfig.load(this.configFile)
    }

    fun loadMenus(): Int{
        val folder = File("$dataFolder/menus/")
        if(!folder.exists()){
            folder.mkdir()
            plugin?.saveResource("menus/example.yml", false)
            plugin?.saveResource("menus/islandmenu.yml", false)
            /*val example:File = File("$folder/example.yml")
            plugin!!.getResource(example.name).use { input ->
                FileOutputStream(example).use { output ->
                    ByteStreams.copy(
                        input,
                        output
                    )
                }
            }*/
        }
        var i = 0
        for(file in folder.listFiles()){
            if(file.isFile && file.name.endsWith(".yml")){
                val data:YamlConfiguration = YamlConfiguration.loadConfiguration(file)
                val menu:MyMenu = MyMenu.buildFromFile(data, file.name)
                menus[file.name] = menu
                for(cmd in menu.commands){
                    commands[cmd] = file.name
                }
                Logging.log("Menu loaded: &b${menu.name}", LogType.INFO)
                i++
            }
        }
        return i
    }
}
