package me.yonatan.reportar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoReports implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cVocê não pode utilizar este comando pelo console.");
			return true;
		}
		Player p = (Player) sender;
		
		if(!p.hasPermission("report.ver")) {
			p.sendMessage("§cVocê não tem permissão para utilizar este comando.");
			return true;
		}
		
		MenuReport.abrirMenu(p);
		
		return false;
	}

}
