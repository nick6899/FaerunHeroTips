package me.FaerunHeroes.Listeners;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.FaerunHeros.Main.Main;

public class DamageListener  implements Listener
{
	  public static Main plugin;
	  
	  public DamageListener(Main m)
	  {
	    plugin = m;
	  }
	  
	  @EventHandler(priority=EventPriority.LOWEST)
	  public void onPlayerDamage(EntityDamageByEntityEvent e)
	  {
	    if (((e.getDamager() instanceof Player)) && 
	      ((e.getEntity() instanceof LivingEntity)))
	    {
	      Player p = (Player)e.getDamager();
	      if (e.getDamage() <= 0.0D) {
	        return;
	      }
	      if ((p.getItemInHand() != null) && 
	        (p.getItemInHand().getType() != Material.AIR) && 
	        (p.getItemInHand().getItemMeta().hasLore()))
	      {
	        int damageMin = getMinValueFromLore(p.getItemInHand(), "DMG");
	        int damageMax = getMaxValueFromLore(p.getItemInHand(), "DMG");
	        Random random = new Random();
	        double dmg = random.nextInt(damageMax - damageMin + 1) + 
	          damageMin;
	        e.setDamage(dmg);
	      }
	    }
	  }
	  
	  
	  @EventHandler
	  public void onWeaponStats(EntityDamageByEntityEvent e)
	  {
	    if (((e.getDamager() instanceof Player)) && 
	      ((e.getEntity() instanceof LivingEntity)))
	    {
	      double dmg = e.getDamage();
	      Player p = (Player)e.getDamager();
	      LivingEntity li = (LivingEntity)e.getEntity();
	      if (e.getDamage() <= 0.0D) {
	        return;
	      }
	      if ((p.getItemInHand() != null) && 
	        (p.getItemInHand().getType() != Material.AIR) && 
	        (p.getItemInHand().getItemMeta().hasLore()))
	      {
	        List<?> lore = p.getItemInHand().getItemMeta().getLore();
	        for (int i = 0; i < lore.size(); i++)
	        {
	          if (((String)lore.get(i)).contains("ICE DMG"))
	          {
	            li.getWorld().playEffect(li.getEyeLocation(), 
	              Effect.POTION_BREAK, 8194);
	            li.addPotionEffect(new PotionEffect(
	              PotionEffectType.SLOW, 20, 0));
	            double eldmg = getElemFromLore(p.getItemInHand(), 
	              "ICE DMG");
	            dmg += eldmg;
	          }
	          if (((String)lore.get(i)).contains("POISON DMG"))
	          {
	            li.getWorld().playEffect(li.getEyeLocation(), 
	              Effect.POTION_BREAK, 8196);
	            li.addPotionEffect(new PotionEffect(
	              PotionEffectType.POISON, 10, 1));
	            double eldmg = getElemFromLore(p.getItemInHand(), 
	              "POISON DMG");
	            dmg += eldmg;
	          }
	          if (((String)lore.get(i)).contains("FIRE DMG"))
	          {
	            li.setFireTicks(40);
	            double eldmg = getElemFromLore(p.getItemInHand(), 
	              "FIRE DMG");
	            dmg += eldmg;
	          }
	          if (((String)lore.get(i)).contains("PURE DMG"))
	          {
	            double eldmg = getElemFromLore(p.getItemInHand(), 
	              "PURE DMG");
	            dmg += eldmg;
	          }
	          if (((String)lore.get(i)).contains("CRITICAL HIT"))
	          {
	            int crit = getLifestealFromLore(p.getItemInHand(), 
	              "CRITICAL HIT");
	            Random random = new Random();
	            int drop = random.nextInt(25) + 1;
	            if (drop <= crit) {
	              dmg *= 2.0D;
	            }
	          }
					if (((String) lore.get(i)).contains("LIFE STEAL"))
	          {
	            li.getWorld().playEffect(li.getEyeLocation(), 
	              Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
	            double base = getLifestealFromLore(p.getItemInHand(), 
	              "LIFE STEAL");
	            double pcnt = base / 100.0D;
	            int life = 0;
	            if ((int)(pcnt * dmg) > 0) {
	              life = (int)(pcnt * dmg);
	            } else {
	              life = 1;
	            }
	            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + 
	              "            +" + ChatColor.GREEN + life + 
	              ChatColor.GREEN + ChatColor.BOLD + " HP " + 
	              ChatColor.GRAY + "[" + (int)p.getHealth() + 
	              "/" + (int)p.getMaxHealth() + "HP]");
	            if (p.getHealth() < p.getMaxHealth() - life) {
	              p.setHealth(p.getHealth() + life);
	            } else if (p.getHealth() >= p.getMaxHealth() - life) {
	              p.setHealth(p.getMaxHealth());
	            }
	          }
	        }
	      }
	      e.setDamage(dmg);
	    }
	  }
	  
	  public static int getElemFromLore(ItemStack item, String value)
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
	  
	  public static int getLifestealFromLore(ItemStack item, String value)
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
	            String vals = ((String)lore.get(i)).split(": ")[1];
	            vals = ChatColor.stripColor(vals);
	            vals = vals.replace("%", "").trim().toString();
	            returnVal = Integer.parseInt(vals.trim());
	          }
	        }
	      }
	    }
	    catch (Exception localException) {}
	    return returnVal;
	  }
	  
	  public static int getMinValueFromLore(ItemStack item, String value)
	  {
	    int returnVal = 1;
	    ItemMeta meta = item.getItemMeta();
	    try
	    {
	      List<?> lore = meta.getLore();
	      if ((lore != null) && (((String)lore.get(0)).contains(value)))
	      {
	        String vals = ((String)lore.get(0)).split(": ")[1];
	        vals = ChatColor.stripColor(vals);
	        vals = vals.split(" - ")[0];
	        returnVal = Integer.parseInt(vals.trim());
	      }
	    }
	    catch (Exception localException) {}
	    return returnVal;
	  }
	  
	  public static int getMaxValueFromLore(ItemStack item, String value)
	  {
	    int returnVal = 1;
	    ItemMeta meta = item.getItemMeta();
	    try
	    {
	      List<?> lore = meta.getLore();
	      if ((lore != null) && (((String)lore.get(0)).contains(value)))
	      {
	        String vals = ((String)lore.get(0)).split(": ")[1];
	        vals = ChatColor.stripColor(vals);
	        vals = vals.split(" - ")[1];
	        returnVal = Integer.parseInt(vals.trim());
	      }
	    }
	    catch (Exception localException) {}
	    return returnVal;
	  }
	  
}
