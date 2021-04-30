package cz.pvpcraft.GGGEDR.PcMyMenus.Commands

import cz.pvpcraft.GGGEDR.PcMyMenus.Commands.Subcommands.ReloadSCMD
import cz.pvpcraft.GGGEDR.PcMyMenus.Commands.Subcommands.TemplateSCMD
import cz.pvpcraft.GGGEDR.PcMyMenus.PcMyMenusPlugin
import cz.pvpcraft.GGGEDR.PcMyMenus.Utils.TextUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object MainCMD : CommandExecutor{

    override fun onCommand(sender: CommandSender?, cmd: Command?, label: String?, args: Array<out String>?): Boolean {
        if(sender is Player){
            if(sender.hasPermission("pc.mymenus.cmd")){
                when(args?.size){
                    0 -> {
                        for(line in TextUtils.colorize(PcMyMenusPlugin.cachedConfig.getStringList("commands.main.help"), null)){
                            sender.sendMessage(line)
                        }
                    }

                    1 -> {
                        val player:Player = sender
                        when(args[0]){
                            "reload" -> {
                                ReloadSCMD.execute(player, cmd, label, args)
                            }

                            /*"template" -> {
                                TemplateSCMD.execute(player, cmd, label, args)
                            }*/

                            else -> {
                                for(line in TextUtils.colorize(PcMyMenusPlugin.cachedConfig.getStringList("commands.main.help"), null)){
                                    sender.sendMessage(line)
                                }
                            }

                        }
                    }

                    else -> {
                        for(line in TextUtils.colorize(PcMyMenusPlugin.cachedConfig.getStringList("commands.main.help"), null)){
                            sender.sendMessage(line)
                        }
                    }
                }
            }
        } else {
            sender?.sendMessage(TextUtils.colorize(PcMyMenusPlugin.cachedConfig.getString("commands.errors.player-only"), null))
        }
        return false
    }

}
