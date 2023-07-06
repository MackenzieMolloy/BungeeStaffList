package net.mackenziemolloy.BungeeStaffList.core;

import net.mackenziemolloy.BungeeStaffList.util.FileUtils;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class PluginConfig {

  private Plugin plugin;
  private String name;
  private String inJarPath;
  private Configuration configuration;
  private File file;

  public PluginConfig(Plugin plugin, String name) {
    this(plugin, name, name);
  }

  public PluginConfig(Plugin plugin, String name, String inJarPath) {
    this.plugin = plugin;
    this.name = name;
    this.inJarPath = inJarPath;
    load();
  }

  private void load() {
    if (!plugin.getDataFolder().exists()) {
      FileUtils.mkdir(plugin.getDataFolder());
    }

    File f = new File(plugin.getDataFolder(), name);
    if (!f.exists()) {
      FileUtils.copy(plugin.getResourceAsStream(inJarPath), f);
    }

    file = f;

    try {
      configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), name));
    } catch(IOException exception) {
      exception.printStackTrace();
    }

    //configuration.options().copyDefaults(true);
  }

  /*public void save() {
    try {
      configuration.
      configuration.options().copyDefaults(true);
      configuration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/

  public Configuration getConfig() {
    return configuration;
  }

  /*public void makeConfigAlternative() throws IOException {
    if (!plugin.getDataFolder().exists()) {
      plugin.getDataFolder().mkdir();
    }

    File file = new File(plugin.getDataFolder(), name);


    if (!file.exists()) {
      try (InputStream in = plugin.getResourceAsStream(name)) {
        Files.copy(in, file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }*/

}
