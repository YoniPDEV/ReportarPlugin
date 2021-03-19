package me.yonatan.reportar;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComandoReport implements CommandExecutor {

	private net.dv8tion.jda.api.entities.Guild discordServer;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("§cVocê não pode utilizar este comando pelo console.");
			return true;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			p.sendMessage("§cUtilize: /reportar <nick>.");
			return true;
		}
		Player j = Bukkit.getPlayer(args[0]);
		if (args.length == 1) {

			if (j == null) {
				p.sendMessage("§cO jogador que você tentou reportar está offline.");
				return true;
			} else {

				TextComponent hack = new TextComponent("§7'Uso de hack.'");
				TextComponent abuso = new TextComponent("§7'Abuso de bugs.'");
				TextComponent anti = new TextComponent("§7'Anti-jogo'");
				TextComponent outra = new TextComponent("§7Outra opção...");

				hack.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reportar " + j.getName() + " Uso de hack."));
				hack.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("§eReportar o jogador " + j.getName() + " §epor uso de hack.").create()));

				abuso.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reportar " + j.getName() + " Abuso de bugs."));
				abuso.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("§eReportar o jogador " + j.getName() + " §epor abuso de bugs.")
								.create()));

				anti.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/reportar " + j.getName() + " Anti-jogo."));
				anti.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("§eReportar o jogador " + j.getName() + " §epor anti-jogo.").create()));

				outra.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/reportar " + j.getName() + " <motivo>"));
				outra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("§eReportar o jogador " + j.getName() + " §epor um motivo diferente.")
								.create()));

				p.sendMessage("");
				p.sendMessage("§6Para reportar o jogador, clique em alguma das seguintes opções:");
				p.spigot().sendMessage(hack);
				p.spigot().sendMessage(abuso);
				p.spigot().sendMessage(anti);
				p.spigot().sendMessage(outra);
				p.sendMessage("");

				return true;

			}
		}

		if (args.length > 1) {
			StringBuilder juntador = new StringBuilder();
			int id = 0;
			for (String arg : args) {
				if (id == 0) {
					id++;
					continue;
				}
				juntador.append(arg);
				juntador.append(" ");
				id++;
			}
			String textojuntado = juntador.toString();

			Report novoReport = new Report();
			novoReport.setReportedPlayer(j.getName());
			novoReport.setReporterPlayer(p.getName());
			novoReport.setCause(textojuntado);
			novoReport.setTime(System.currentTimeMillis());
			Report.getReports().add(novoReport);
			p.sendMessage("§aO jogador foi reportado, por: " + textojuntado + "§a.");
			Report.saveReports();

			discordServer = Main.getJDA().getGuildById("795692200678785055");

			// discordServer.getTextChannelById("795809555376111624").sendMessage("**NOVO REPORT!**\n" + "Reportado: "+ j.getName() + "\n" + "Reportador: " + p.getName() + "\n" + "Motivo: " + textojuntado).queue();
			
			EmbedBuilder builder = new EmbedBuilder()
					.setColor(Color.RED)
					.setTitle(":pencil: ** Novo report | Maze Mc**")
					.addField("Reportado:", j.getName(), true)
					.addField("Quem reportou:", p.getName(), true)
					.addField("Motivo:", textojuntado, false);
			
			discordServer.getTextChannelById("813762407440056361").sendMessage(builder.build()).queue();
			
			for (Player jogadores : Bukkit.getOnlinePlayers()) {
				if (jogadores.hasPermission("staff-reports")) {

					TextComponent JsonN = new TextComponent("§aChegou um novo report, clique ");
					TextComponent JsonClique = new TextComponent("§a§lAQUI ");
					TextComponent JsonN2 = new TextComponent("§apara abrir o menu.");
					JsonN.addExtra(JsonClique);
					JsonN.addExtra(JsonN2);

					JsonClique.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports"));
					JsonClique.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("§aCLIQUE AQUI PRA ABRIR O MENU").create()));
					jogadores.sendMessage("");
					jogadores.spigot().sendMessage(JsonN);
					jogadores.sendMessage("");
				}
			}

		}

		return false;
	}

}
