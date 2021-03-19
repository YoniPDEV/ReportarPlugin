package me.yonatan.reportar;

import me.yonatan.reportar.BukkitConfigs;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Report {

	private static SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public ItemStack getIcon() {

		String horarioformatado = formatador.format(time);
		String horarioformatado2 = formatador.format(verifiedTime);

		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

		// ItemMeta meta = item.getItemMeta();
		
		SkullMeta meta =  (SkullMeta) item.getItemMeta();

		meta.setDisplayName("§a" + reportedPlayer);

		List<String> lore = new ArrayList<>();
		
		meta.setOwner(reportedPlayer);

		lore.add("");
		lore.add("§eReportador: §f" + reportedPlayer);
		lore.add("§eMotivo: §f"+cause);
		lore.add("§eHorário: §f" + horarioformatado);
		if(verified == false) {
			lore.add("");
			lore.add("§cReport não verificado!");
		} else {
			lore.add("§eVerificado por: §a"+ verifierPlayer +" §aàs " +horarioformatado2);
		}
		
		lore.add("");
		lore.add("§7* Para remover o report, clique com o botão direito!");
		lore.add("§7* Para verificar o report, clique com o botão esquerdo!");
		lore.add("");
		lore.add("§cObservação: Apenas §5[COD] §cpodem remover reports!");
		lore.add("");
		meta.setLore(lore);
		
		item.setItemMeta(meta);

		return item;

	}

	private static BukkitConfigs config = new BukkitConfigs("reports.yml");

	private static final List<Report> reports = new ArrayList<>();

	public static void saveReports() {
		config.remove("reports");
		int id = 1;
		for (Report report : reports) {
			ConfigurationSection secao = config.create("reports.report-" + id);
			report.save(secao);
			id++;
		}
		config.saveConfig();
	}

	public static void reloadReports() {
		config.reloadConfig();
		reports.clear();
		for (String chave : config.getSection("reports").getKeys(false)) {
			ConfigurationSection secao = config.getSection("reports." + chave);
			Report reportNovo = new Report();
			reportNovo.reload(secao);
			reports.add(reportNovo);

		}

	}

	public void save(ConfigurationSection section) {
		section.set("reporterPlayer", reporterPlayer);
		section.set("reportedPlayer", reportedPlayer);
		section.set("cause", cause);
		section.set("time", time);
		section.set("verified", verified);
		section.set("verifiedTime", verifiedTime);
		section.set("verifierPlayer", verifierPlayer);
	}

	public void reload(ConfigurationSection section) {
		this.reportedPlayer = section.getString("reporterPlayer");
		this.reportedPlayer = section.getString("reportedPlayer");
		this.cause = section.getString("cause");
		this.time = section.getLong("time");
		this.verified = section.getBoolean("verified");
		this.verified = section.getBoolean("verifiedTime");
		this.verifierPlayer = section.getString("verifierPlayer");
	}

	private String reporterPlayer;
	private String reportedPlayer;
	private String cause;
	private long time;
	private boolean verified;
	private long verifiedTime;
	private String verifierPlayer;
	

	public static BukkitConfigs getConfig() {
		return config;
	}

	public static void setConfig(BukkitConfigs config) {
		Report.config = config;
	}

	public String getReporterPlayer() {
		return reporterPlayer;
	}

	public void setReporterPlayer(String reporterPlayer) {
		this.reporterPlayer = reporterPlayer;
	}

	public String getReportedPlayer() {
		return reportedPlayer;
	}

	public void setReportedPlayer(String reportedPlayer) {
		this.reportedPlayer = reportedPlayer;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public long getVerifiedTime() {
		return verifiedTime;
	}

	public void setverifiedTime(long time) {
		this.verifiedTime = time;
	}

	public String getVerifierPlayer() {
		return verifierPlayer;
	}

	public void setVerifierPlayer(String verifierPlayer) {
		this.verifierPlayer = verifierPlayer;
	}

	public static List<Report> getReports() {
		return reports;
	}

}
