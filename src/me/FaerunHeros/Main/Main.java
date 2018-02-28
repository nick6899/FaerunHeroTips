package me.FaerunHeros.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.FaerunHeroes.Utils.Files;



public class Main extends JavaPlugin implements Listener {
	
	private static Main plugin;
	public Files tipFile;
	List<String> Tips = new ArrayList();
	int tipId = 0; 
	
	
	@SuppressWarnings("deprecation")
	public void onEnable() {
		plugin = this;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getLogger().info("===--Faerun Heroes--===");
		Bukkit.getServer().getLogger().info("===--Version: 1.0--===");
		
		
	//Tip File Saving
		tipFile = new Files(new File(getDataFolder() + "/Data"), "Tips");
		
		if (!tipFile.fileExists()) {
			tipFile.createFile();
			tipFile.loadFile();
			tipFile.saveFile();
		}
		
		tipFile.loadFile();
		int totalTips = tipFile.GetStringList("Tips").size();
		System.out.println("" + totalTips);
		
		if (tipId == totalTips || tipId >= totalTips) {
			tipId = 0;
		}
		Tips = tipFile.GetStringList("Tips");
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				String Tip = Tips.get(tipId);
				Bukkit.getServer().broadcastMessage("Tip>> " + Tip);
				tipId++;
				
			}
		}, 1, 300*20);
	//
		
}

	public void onDisable() {
		
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	
	
	
}

