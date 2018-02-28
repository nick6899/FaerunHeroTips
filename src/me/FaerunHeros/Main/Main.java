package me.FaerunHeros.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.FaerunHeroes.Utils.Files;



public class Main extends JavaPlugin implements Listener {
	
	private static Main plugin;
	public Files tipFile;
	List<String> Tips = new ArrayList();
	int tipId = 0;
	int totalTips = 0;
	int sec = 300;
	
	
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
		sec = tipFile.getInt("Delay In Sconds");
		
		totalTips = tipFile.GetStringList("Tips").size();
		
		Tips = tipFile.GetStringList("Tips");
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (tipId == totalTips || tipId >= totalTips) {
					tipId = 0;
				}
				
				String Tip = Tips.get(tipId);
				Bukkit.getServer().broadcastMessage("" + Tip.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
				tipId++;
				
			}
		}, 1, sec*20);
	//
		
		
		
}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player)) {
			final Player p = (Player) sender;
			if (p.isOp()) {
			if (cmd.getName().equalsIgnoreCase("TipReload")) {
				tipFile.loadFile();
				totalTips = tipFile.GetStringList("Tips").size();
				Tips = tipFile.GetStringList("Tips");
				
				p.sendMessage(ChatColor.GREEN + "You have successfully reloaded the Tip Data file!");
				p.sendMessage(ChatColor.AQUA + "The current tips are " + Tips + "!");
				return true;
				}
			}else {
				return false;
			}
		}
		return false;
		
		
	}

	public void onDisable() {
		
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	
	
	
}

