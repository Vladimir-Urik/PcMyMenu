package cz.pvpcraft.GGGEDR.PcMyMenus;

import cz.pvpcraft.GGGEDR.PcMyMenus.Scripts.ScriptBoss;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Debug {

    public static void main(String[] args) throws ScriptException {
        System.out.println(ScriptBoss.parse(" ahoj ty <js>\"gggedr\" === \"bublif\" ? \"bublif\" : \"GGGEDR\"</js> dsdsdsds <js>\"gggedr\" === \"bublif\" ? \"bublif\" : \"nob\"</js>"));
        //System.out.println(engine.eval("\"kkz\" == \"kkkz\" ? \"hjaha\" : \"etc\""));
    }

}
