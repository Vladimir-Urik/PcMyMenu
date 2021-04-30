package cz.pvpcraft.GGGEDR.PcMyMenus.Scripts;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptBoss {

    public static String parse(String line){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        Pattern pattern = Pattern.compile("<js>(.*?)</js>");
        Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String js = matcher.group(1);
                try {
                    line = line.replace("<js>" + js + "</js>", (CharSequence) engine.eval(js));
                } catch (ScriptException e){
                    Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&4[&lERROR&c]: &7Javascript code is invalid | "+ js));
                }
            }
        return line;
    }

}
