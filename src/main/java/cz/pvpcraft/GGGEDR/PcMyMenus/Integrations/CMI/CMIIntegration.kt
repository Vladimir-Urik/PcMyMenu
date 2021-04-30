package cz.pvpcraft.GGGEDR.PcMyMenus.Integrations.CMI

import com.Zrips.CMI.CMI
import com.Zrips.CMI.Containers.CMIUser


class CMIIntegration {

    var cmi:CMI = CMI.getInstance()

    fun getPlayer(name: String): CMIUser {
        return CMI.getInstance().playerManager.getUser(name)
    }
}
