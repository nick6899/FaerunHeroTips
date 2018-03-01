package me.FaerunHeroes.Listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.FaerunHeros.Main.Main;


public class HealthListener  implements Listener
{
	  public static Main plugin;
	  
	  public HealthListener(Main m)
	  {
	    plugin = m;
	  }
	  
	  public static int getValueFromLore(ItemStack item, String value)
	  {
	    int returnVal = 0;
	    ItemMeta meta = item.getItemMeta();
	    try
	    {
	      List<?> lore = meta.getLore();
	      if (lore != null) {
	        for (int i = 0; i < lore.size(); i++) {
	          if (((String)lore.get(i)).contains(value))
	          {
	            String vals = ((String)lore.get(i)).split(": +")[1];
	            vals = ChatColor.stripColor(vals);
	            returnVal = Integer.parseInt(vals.trim());
	          }
	        }
	      }
	    }
	    catch (Exception localException) {}
	    return returnVal;
	  }
	  
	  public static int getVitFromLore(ItemStack item, String value)
	  {
	    int returnVal = 0;
	    ItemMeta meta = item.getItemMeta();
	    try
	    {
	      List<?> lore = meta.getLore();
	      if ((lore != null) && (((String)lore.get(2)).contains(value)))
	      {
	        String vals = ((String)lore.get(2)).split(": +")[1];
	        vals = ChatColor.stripColor(vals);
	        returnVal = Integer.parseInt(vals.trim());
	      }
	    }
	    catch (Exception localException) {}
	    return returnVal;
	  }
	  
	  public void hpCheck(Player p)
	  {
	    PlayerInventory i = p.getInventory();
	    double a = 50.0D;
	    double vital = 0.0D;
	    if ((i.getHelmet() != null) && (i.getHelmet().getItemMeta().hasLore()))
	    {
	      double health = getValueFromLore(i.getHelmet(), "HP");
	      int vit = getVitFromLore(i.getHelmet(), "VIT");
	      a += health;
	      vital += vit;
	    }
	    if ((i.getChestplate() != null) && 
	      (i.getChestplate().getItemMeta().hasLore()))
	    {
	      double health = getValueFromLore(i.getChestplate(), "HP");
	      int vit = getVitFromLore(i.getChestplate(), "VIT");
	      a += health;
	      vital += vit;
	    }
	    if ((i.getLeggings() != null) && 
	      (i.getLeggings().getItemMeta().hasLore()))
	    {
	      double health = getValueFromLore(i.getLeggings(), "HP");
	      int vit = getVitFromLore(i.getLeggings(), "VIT");
	      a += health;
	      vital += vit;
	    }
	    if ((i.getBoots() != null) && (i.getBoots().getItemMeta().hasLore()))
	    {
	      double health = getValueFromLore(i.getBoots(), "HP");
	      int vit = getVitFromLore(i.getBoots(), "VIT");
	      a += health;
	      vital += vit;
	    }
	    if (vital > 0.0D)
	    {
	      double divide = vital / 2000.0D;
	      double pre = a * divide;
	      int cleaned = (int)(a + pre);
	      if (p.getHealth() > cleaned) {
	        p.setHealth(cleaned);
	      }
	      p.setMaxHealth(cleaned);
	    }
	    else
	    {
	      p.setMaxHealth(a);
	    }
	    p.setHealthScale(20.0D);
	    p.setHealthScaled(true);
	  }
	  
	  @EventHandler
	  public void onInventoryClick(InventoryClickEvent e)
	  {
	    final Player p = (Player)e.getWhoClicked();
	    new BukkitRunnable()
	    {
	      public void run()
	      {
	        HealthListener.this.hpCheck(p);
	      }
	    }.runTaskLater(plugin, 1L);
	  }
	
	  @EventHandler
	  public void onJoin(PlayerJoinEvent e) {
		  Player p = e.getPlayer();
		    p.setHealthScale(20.0D);
		    p.setHealthScaled(true);
		  
	  }
	
}
