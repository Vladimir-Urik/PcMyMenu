package cz.pvpcraft.GGGEDR.PcMyMenus.Framework

import cz.pvpcraft.GGGEDR.PcMyMenus.Framework.Annotations.CommandName
import cz.pvpcraft.GGGEDR.PcMyMenus.Framework.Annotations.DefaultCommand
import cz.pvpcraft.GGGEDR.PcMyMenus.Framework.Annotations.Permissions
import cz.pvpcraft.GGGEDR.PcMyMenus.Framework.Annotations.SubCommand

class CommandManager {


    fun registerCommand(cmd: PcCommand) {
        var name = (cmd.javaClass.annotations.find { it is CommandName } as? CommandName)?.name
        var permissions = (cmd.javaClass.annotations.find { it is Permissions } as? Permissions)?.permissions
        val default = cmd.javaClass.annotations.find { it is DefaultCommand } as? DefaultCommand
        

        val subCommands = cmd.javaClass.annotations.filterIsInstance<SubCommand>()


    }


}
