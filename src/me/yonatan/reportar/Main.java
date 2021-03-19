package me.yonatan.reportar;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main extends JavaPlugin {

	private static Main plugin;

	public static Main getInstance() {
		return plugin;
	}

	private static JDA jda;

	public static JDA getJDA() {
		return jda;
	}
	
	public static ItemStack newHeadSkin(String url, String nome, int amount, List<String> lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(nome);
        meta.setLore(lore);
        item.setItemMeta(meta);
        if (url.isEmpty())
            return item;

        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder()
                .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        item.setItemMeta(itemMeta);
        return item;
    }
	
	@SuppressWarnings("static-access")
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§fPlugin ativado: §aReportar");
		Bukkit.getPluginCommand("reportar").setExecutor(new ComandoReport());
		getDataFolder().mkdirs();
		Report.reloadReports();
		Bukkit.getPluginManager().registerEvents(new MenuReport(), this);

		Bukkit.getPluginCommand("reports").setExecutor(new ComandoReports());

		// bot
		try {

			JDABuilder builder = net.dv8tion.jda.api.JDABuilder
					.createDefault("ODA3ODkxMzU0NDQ4ODIyMjky.YB-ldA.qHvLt0JHbv7LCeK7yA7HJSuoT8Q");

			builder.setAutoReconnect(false);
            this.jda = builder.build();			
			jda.awaitReady();

		} catch (Error | Exception err) {
			err.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("Falha ao ligar o bot");
			Bukkit.getPluginManager().disablePlugin(this);

		}
	}
	@Override
	public void onDisable() {
	}

}
