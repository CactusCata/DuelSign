package com.gmail.cactuscata.duelsign;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.cactuscata.duelsign.listeners.ClickSignEvent;
import com.gmail.cactuscata.duelsign.listeners.DeathEvent;
import com.gmail.cactuscata.duelsign.listeners.LeaveGame;
import com.gmail.cactuscata.duelsign.objects.Dual;
import com.gmail.cactuscata.duelsign.objects.SignObject;

public class DuelSign extends JavaPlugin {

	public void onEnable(){
		
		PluginManager pm = this.getServer().getPluginManager();
		
		SignObject signObject = new SignObject(this);
		new Dual(signObject, this);
		
		pm.registerEvents(new ClickSignEvent(signObject), this);
		pm.registerEvents(new LeaveGame(signObject, this), this);
		pm.registerEvents(new DeathEvent(signObject, this), this);
		
	}
}
