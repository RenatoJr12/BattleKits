package com.lavacraftserver.BattleKits;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class BattleKits extends JavaPlugin {

	public HashSet<String> death = new HashSet<String>();
	public static net.milkbowl.vault.economy.Economy economy = null;
	
	@Override
	public void onEnable() {
		getLogger().info("BattleKits has been enabled!");
		getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
		getServer().getPluginManager().registerEvents(new RespawnKit(this), this);
		getServer().getPluginManager().registerEvents(new RestrictionEvents(this), this);
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		getCommand("soup").setExecutor(new CommandSoup(this));
		getCommand("battlekits").setExecutor(new CommandBattleKits(this));

		if (getConfig().getBoolean("settings.auto-update")) {
			Updater updater = new Updater(this, "battlekits", this.getFile(), Updater.UpdateType.DEFAULT, true); //New slug
		}
		if (Bukkit.getPluginManager().getPlugin("Vault") != null && setupEconomy()) {
			getLogger().info("Found vault successfully!");
		} else {
			getLogger().info("Couldn't find vault. Economy disabled for now.");
		}
		
		saveConfig();
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Saved config! Use /pvp reload if you wish to modify it"); 
		this.saveConfig();
		getLogger().info("BattleKits has been disabled."); 
		
	}
	
	private boolean setupEconomy() {

        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	public ItemStack setColor(ItemStack item, int color) {
		CraftItemStack craftStack = null;
		net.minecraft.server.ItemStack itemStack = null;
		if (item instanceof CraftItemStack) {
			craftStack = (CraftItemStack) item;
			itemStack = craftStack.getHandle();
		} else if (item instanceof ItemStack) {
			craftStack = new CraftItemStack(item);
			itemStack = craftStack.getHandle();
		}
		NBTTagCompound tag = itemStack.tag;
		if (tag == null) {
			tag = new NBTTagCompound();
			tag.setCompound("display", new NBTTagCompound());
			itemStack.tag = tag;
		}

		tag = itemStack.tag.getCompound("display");
		tag.setInt("color", color);
		itemStack.tag.setCompound("display", tag);
		return craftStack;
	}
	
}