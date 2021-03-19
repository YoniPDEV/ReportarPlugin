package me.yonatan.reportar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuReport implements Listener {

	private static String nome = "§cReportados";
	// private static HashMap<Integer, Report> reportes = new HashMap<>();

	private static HashMap<Player, HashMap<Integer, Report>> playersReportes = new HashMap<>();

	private static HashMap<Player, Integer> paginaAberta = new HashMap<>();

	private static final int slotVoltar = 0;
	private static final int slotAvancar = 8;
	private final static ItemStack teia;
	
	
	static {

		teia = new ItemStack(Material.WEB, 1, (short) 3);
		{
			ItemMeta meta = teia.getItemMeta();
			meta.setDisplayName("§cSem reports.");
			teia.setItemMeta(meta);
		}	
	}
	

	@EventHandler
	public void controle(InventoryClickEvent evento) {
		Player player = (Player) evento.getWhoClicked();
		if (!evento.getInventory().getName().equals(nome))
			return;
		evento.setCancelled(true);
		if (evento.getCurrentItem() == null)
			return;
		int slotClicado = evento.getRawSlot();
		int paginaAtual = paginaAberta.get(player);

		if (paginaAtual > 1 && slotClicado == slotVoltar) {
			abrirMenu(player, paginaAtual - 1);
			return;
		}
		if (slotClicado == slotAvancar) {
			abrirMenu(player, paginaAtual + 1);
			return;
		}
		HashMap<Integer, Report> reportes = playersReportes.get(player);
		if (reportes == null)
			return;
		Report reporte = reportes.get(slotClicado);
		if (reporte == null)
			return;

		// player.sendMessage("Â§aVocÃª clicou no report do " +
		// reporte.getReportedPlayer());

		if (evento.getClick() == ClickType.RIGHT) {

			if (!player.hasPermission("cod.reports")) {
				player.sendMessage("§cApenas §5§l[COD] §cpodem remover reports");
				return;
			}

			Report.getReports().remove(reporte);
			reportes.remove(slotClicado);
			player.sendMessage("§AReport removido com sucesso.");
			abrirMenu(player);
		} else if (evento.getClick() == ClickType.LEFT) {
			Player alvo = Bukkit.getPlayer(reporte.getReportedPlayer());
			if (alvo == null) {
				return;
			}
			if (reporte.isVerified()) {
				player.sendMessage("§aO report já foi verificado!");
				return;
			}
			player.teleport(alvo);
			player.sendMessage("§aVocê verificou o reporte com sucesso.");
			reporte.setVerified(true);
			reporte.setVerifierPlayer(player.getName());
			reporte.setverifiedTime(System.currentTimeMillis());

		}
	}

	public static void abrirMenu(Player player) {
		abrirMenu(player, 1);
	}

	public static void abrirMenu(Player player, int pagina) {
		Inventory menu = Bukkit.createInventory(null, 6 * 9, nome);
		// argumentos [0]
		paginaAberta.put(player, pagina);
		int slotUsado = 10;
		int porPagina = 28;
		int fim = (pagina * porPagina);
		HashMap<Integer, Report> reports = new HashMap<>();
		int size = Report.getReports().size();

		if (pagina > 1) {
			ItemStack item = new ItemStack(Material.ARROW);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§aVoltar para a página " + (pagina - 1) + "§a.");
			item.setItemMeta(meta);
			menu.setItem(slotVoltar, item);
		}
		{
			ItemStack item = new ItemStack(Material.ARROW);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§aAvançar para página " + (pagina + 1) + "§a.");
			item.setItemMeta(meta);
			menu.setItem(slotAvancar, item);
		}

		for (int atual = (pagina - 1) * porPagina; atual < fim; atual++) {

			if (slotUsado == 17 || slotUsado == 26 || slotUsado == 35 || slotUsado == 44) {
				slotUsado++;
			}
			if (slotUsado == 9 || slotUsado == 18 || slotUsado == 36 || slotUsado == 27) {
				slotUsado++;
			}

			if (atual >= size) {
				break;
			}
			Report reporte = Report.getReports().get(atual);
			if (reporte.isVerified()) {
				
			}
			menu.setItem(slotUsado, reporte.getIcon());
			reports.put(slotUsado, reporte);
			slotUsado++;
			
		}

		if(Report.getReports().isEmpty()) {
			menu.setItem(22, teia);
		}
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add("§e* §fEste menu está listado todos reports feitos.");
		lore.add("§e* §fApenas o cargo §c[Admin] §facima pode remover reports.");
		lore.add("§e* §fÉ proibido você reportar um jogador para ir até à ele.");
		
		ItemStack ca = Main.newHeadSkin("http://textures.minecraft.net/texture/2e3f50ba62cbda3ecf5479b62fedebd61d76589771cc19286bf2745cd71e47c6", 
				"§aInformações", 1, lore);
		menu.setItem(53, ca);
		playersReportes.put(player, reports);
		player.openInventory(menu);
	}

}
