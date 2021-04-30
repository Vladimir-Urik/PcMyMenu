package cz.pvpcraft.GGGEDR.PcMyMenus.Classes

import com.sun.xml.internal.fastinfoset.util.StringArray
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.LogType
import cz.pvpcraft.GGGEDR.PcMyMenus.Logs.Logging
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.*
import de.tr7zw.changeme.nbtapi.NBTItem
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.*
import java.util.function.Consumer
import kotlin.collections.ArrayList


class MyItem {

    var action: Consumer<InventoryClickEvent> = Consumer {  }

    var actions:List<String> = ArrayList()

    var item:ItemStack = ItemStack(Material.BARRIER)

    var clickableID:String = UUID.randomUUID().toString()

    var char:String = ""

    var slot:Int = 0

    var menu:YamlConfiguration = YamlConfiguration()

    companion object {

        fun parseMyItem(char: String, config: ConfigurationSection, p: Player): MyItem{
            val result: MyItem = MyItem()
            val displayName: String? = config.getString("name")?.let { TextUtils.colorize(it, p) }
            Logging.log("loading item with displayname: $displayName", LogType.DEBUG)

            val lore: List<String> = config.getStringList("lore").let { TextUtils.colorize(it, p) }
            Logging.log("loading item with lore:", LogType.DEBUG)
            for(line in lore){
                Logging.log(line, LogType.DEBUG);
            }
                val materialString: String = config.getString("material")!!
                Logging.log("loading item with Material String: $materialString", LogType.DEBUG)

                val actions: List<String> = config.getStringList("actions")
                Logging.log("loading item with actions:", LogType.DEBUG)
                for (line in actions) {
                    Logging.log(line, LogType.DEBUG);
                }

                val amount: Int = config.getInt("amount")
                Logging.log("loading item with amount: $amount", LogType.DEBUG)

                val material: ItemStack? = if (materialString.startsWith("<") && materialString.endsWith(">")) {
                    if (materialString.startsWith("<skull:")) {
                        val base64Texture: String = materialString
                            .replace("<skull:", "")
                            .replace(">", "")
                        SkullManager.itemFromBase64(base64Texture, UUID.randomUUID().toString())
                    } else if (materialString.startsWith("<head:")) {
                        var playerName: String = materialString
                            .replace("<head:", "")
                            .replace(">", "")
                        playerName = PlaceholderAPI.setPlaceholders(p, playerName)
                        val item: ItemStack? = XMaterial.PLAYER_HEAD.parseItem()
                        val meta: SkullMeta = item?.itemMeta as SkullMeta
                        meta.setOwner(playerName)
                        item.itemMeta = meta
                        item
                    } else if (materialString.startsWith("<pcpack:")) {
                        val pack: List<String> = materialString
                            .replace("<pcpack:", "")
                            .replace(">", "")
                            .split(":");
                        val item: ItemStack? = XMaterial.valueOf(pack[0]).parseItem()
                        val nbt: NBTItem = NBTItem(item)
                        nbt.setInteger("CustomModelData",pack[1].toInt())
                        nbt.item
                    } else {
                        ItemStack(Material.BARRIER)
                    }
                } else {
                    XMaterial.valueOf(materialString).parseItem()
                }
                if (material != null) {
                    Logging.log("loading item with Material: ${material.type.name}", LogType.DEBUG)
                }

                val item: ItemStack? = material
                item?.amount = amount
                val meta: ItemMeta = item!!.itemMeta
                meta.lore = lore
                meta.setDisplayName(displayName)
                item.itemMeta = meta

                result.actions = actions
                result.char = char
                result.clickableID =
                    UUID.randomUUID().toString() + ">" + System.currentTimeMillis().toString() + ">" + p.name;
                result.item = item
                val nbt: NBTItem = NBTItem(item)
                nbt.setString("clickable_id", result.clickableID)
                result.item = nbt.item
                val clickID: String = NBTItem(result.item).getString("clickable_id")
                Logging.log("NBT TAG click id: $clickID", LogType.DEBUG)
                Logging.log("loading item with Click ID: ${result.clickableID}", LogType.DEBUG)
            return result
        }
    }


    fun refresh(p: Player, config: ConfigurationSection){
        val meta:ItemMeta = item.itemMeta
        val displayName: String? = config.getString("name")?.let { TextUtils.colorize(it, p) }
        val lore: List<String> = config.getStringList("lore").let { TextUtils.colorize(it, p) }
        meta.lore = lore
        meta.displayName = displayName
        item.itemMeta = meta
    }
}
