package com.gmail.sharpcastle33.listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.sharpcastle33.Nobility;
import com.gmail.sharpcastle33.group.Group;

import net.md_5.bungee.api.ChatColor;

public class CommandListener implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(ChatColor.BLUE + "-== Nobility Commands ==- \n"
		+ ChatColor.GOLD + "/nobility create <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Creates a Nobility group. \n"
		+ ChatColor.GOLD + "/nobility info" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Lists information about your Nobility group. \n"
		+ ChatColor.GOLD + "/nobility info <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Lists information about a Nobility group. \n"
		+ ChatColor.GOLD + "/nobility join <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Joins a Nobility group, if you have an invite. \n"
		+ ChatColor.GOLD + "/nobility estate" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Access estate-related commands. Nobility groups without an estate are considered Nomads. \n"
		+ ChatColor.GOLD + "/nobility invite <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Invites a player to your Nobility group.\n"
		+ ChatColor.GOLD + "/nobility kick <name>" + ChatColor.GRAY + "| " + ChatColor.AQUA + "Kicks a player from your Nobility group. \n");
			
		}else if(args.length == 1) {
			//nobility create
			if(args[0] == "create") {
			  sender.sendMessage(ChatColor.GOLD + "This command creates a Nobility group, which can be used later to allow a group of players to claim land by founding an Estate. \n" + ChatColor.RED + "Correct usage is /nobility create <name>");
			}
		}else if(args.length == 2) {
			//nobility create <NAME>
			if(args[0] == "create" && args[1].length() >= 2) {
				
				for(int i = 0; i < Nobility.groupMan.groups.size(); i++) {
					Group g = Nobility.groupMan.groups.get(i);
					if(g.name.equals(args[1])) {
						sender.sendMessage(ChatColor.RED + "That group already exists. Try another name");
						return false;
					}
				}
				
				Group temp = new Group(args[1], sender.getName());
				Nobility.groupMan.groups.add(temp);
				
				return true;
			}
			
			//nobility invite <name>
			if(args[0] == "invite") {
				String inviter = sender.getName();
				String reciever = args[1];
				
				boolean inGroup = false;
				Group temp = null;
				for(int i = 0; i < Nobility.groupMan.groups.size(); i++) {
					Group g = Nobility.groupMan.groups.get(i);
					for(String s : g.members) {
						if(s.equals(reciever)) {
							sender.sendMessage(ChatColor.RED + "That player is already part of an Estate.");
							return false;
						}
						
						if(s.equals(inviter)) {
							inGroup = true;
							temp = g;
						}
					}
				}
				
				if(inGroup) {
					temp.pendingInvites.add(reciever);
				}else {
					sender.sendMessage(ChatColor.RED + "You are not part of a Nobility Group.");
					return false;
				}
				
			}
			
			//nobility join <name>
			if(args[0] == "join") {
				Group temp = null;
				for(int i = 0; i < Nobility.groupMan.groups.size(); i++) {
					Group g = Nobility.groupMan.groups.get(i);
					for(String s : g.members) {
						if(sender.getName().equals(s)) {
							sender.sendMessage(ChatColor.RED + "You are already part of a Nobility group.");
							return false;
						}
					}
					if(g.name == args[1]) {
						temp = g;
					}
				}
				
				if(temp != null) {
					if(temp.pendingInvites.contains(sender.getName())) {
						temp.pendingInvites.remove(sender.getName());
						temp.members.add(sender.getName());
						sender.sendMessage(ChatColor.GREEN + "You have been added to " + temp.name);
					}
				}
			}
		}
		return false;
	}

}
