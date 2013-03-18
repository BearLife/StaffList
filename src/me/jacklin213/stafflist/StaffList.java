package me.jacklin213.stafflist;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffList extends JavaPlugin {
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static StaffList plugin;
	PluginDescriptionFile pdfFile;
	protected StaffData sd;
	
	public void onDisable()  {
		sd.save();
		logger.info(String.format("[%s] Disabled Version %s", getDescription()
				.getName(), getDescription().getVersion()));
	}

	public void onEnable() {
		
		String pluginFolder = this.getDataFolder().getAbsolutePath();
		(new File(pluginFolder)).mkdirs();
		this.sd = new StaffData(new File(pluginFolder + File.separator + "staff-names.txt"));
		
		logger.info(String.format("[%s] Enabled Version %s by jacklin213",
				getDescription().getName(), getDescription().getVersion()));
		
		sd.load();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
		if (commandLabel.equalsIgnoreCase("stafflist")){
			if (args.length == 1){
				if (args[0].equalsIgnoreCase("all")){
					if (sender.hasPermission("stafflist.all") || sender.isOp()){
						String staffNames = sd.getStaffNames();
						sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] " + staffNames);
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "You do not have the permission to use this command");
						return true;
					}
				} 
				if (args[0].equalsIgnoreCase("help")) {
					if (sender.hasPermission("stafflist.help") || sender.isOp()){
						sender.sendMessage("Type in /help stafflist to see the help");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "You do not have the permission to use this command");
						return true;
					}
				} 
				
				else {
					sender.sendMessage(ChatColor.RED + "Invalid command, Type /help stafflist for help");
					return true;
				}
			}
			if(sender.hasPermission("stafflist.use")){
				sd.load();		
					try {
						String allOnlineStaff = sd.staffOnline();
						int staffCount = sd.staffCount();
						int numStaffOnline = sd.numStaffOnline();
						sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] The number of Staff online is: " + numStaffOnline+ "/" + staffCount);
						sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] Staff Online: " + allOnlineStaff);
					} catch (IOException e) {
						sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] The Staff List is empty!");
						return true;
					}
			} else {
				sender.sendMessage(ChatColor.RED + "You do not have the permission to use this command!");
				return true;
			}
		} else if (commandLabel.equalsIgnoreCase("sl") && args.length == 2){
			if (args[0].equalsIgnoreCase("add")){
				if (sender.hasPermission("stafflist.add") || sender.isOp()){
					Player player = Bukkit.getServer().getPlayer(args[1]);
					if (player != null){
						String playerName = Bukkit.getServer().getPlayer(args[1]).getName();
						if (sd.contains(playerName) == true){
							sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] " + playerName + " is already on the list");
							return true;
						} else {
							sd.add(playerName);
							sd.save();
							sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] " +playerName + " has been added to the StaffList"); 
							return true;
						}
					} else {
						sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] This player is either not online or doesnt exist");
						return true;
					} 
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have the permission to use this command!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("remove")){
				if (sender.hasPermission("stafflist.remove") || sender.isOp()){
					String playerName = Bukkit.getServer().getPlayer(args[1]).getName();
					sender.sendMessage("[" +ChatColor.DARK_GREEN + "StaffList" + ChatColor.RESET + "] " + playerName + " has been removed from the StaffList");
					sd.remove(playerName);
					sd.save();
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "You do not have the permission to use this command!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Not a valid command! Type /help stafflist for help");
			}
			return true;
		} else if (commandLabel.equalsIgnoreCase("sl") && args.length > 2 || args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Not a valid command! Type /help stafflist for help");
			return true;
		}
		
		return false;
	}
}
