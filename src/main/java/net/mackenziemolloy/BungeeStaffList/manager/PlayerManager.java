package net.mackenziemolloy.BungeeStaffList.manager;

import com.google.common.io.Files;
import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.exception.group.GroupProviderAlreadyRegistered;
import net.mackenziemolloy.BungeeStaffList.util.FileUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerManager {

  private VanishManager vanishManager;

  public PlayerManager() {
    vanishManager = new VanishManager(this);
  }

  public VanishManager getVanishManager() {
    return vanishManager;
  }

  /*

    PlayerManager#getPlayer ???

    Do I need a player manager to easily fetch staff?


    --- different idea

    X PlayerManager#createFile(Player)
    PlayerManager#deleteFile(Player)
    PlayerManager#fetch(Player, Path, value)
    X PlayerManager#set(Player, Path, value)
    PlayerManager#delete(Player, Path)

   */

  private File createFile(UUID uuid) {
    File playerFile = new File(BungeeStaffList.getInstance().getDataFolder() + "/playerdata", uuid.toString() + ".yml");

    try {
      if (!playerFile.exists()) {
        Files.createParentDirs(playerFile);
        playerFile.createNewFile();
      }
    } catch (IOException exception) {
      exception.printStackTrace();
      return null;
    }

    return playerFile;
  }

  private Configuration loadConfiguration(File file) {
    Configuration configuration;
    try {
      configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }
    catch(IOException exception) {
      exception.printStackTrace();
      configuration = null;
    }

    return configuration;
  }
  private boolean saveConfiguration(File file, Configuration configuration) {
    try {
      ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
    } catch (IOException exception) {
      exception.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Gets a stored property from a player's data file
   *
   * @param uuid UUID for the player
   * @param path Path for the property you're wanting to save to
   * @param value Value for the path you're waiting to save
   */
  public boolean set(UUID uuid, String path, Object value) {
    File playerFile = createFile(uuid);
    if(playerFile == null) return false;

    Configuration configuration = loadConfiguration(playerFile);
    if(configuration == null) return false;

    configuration.set(path, value);

    return saveConfiguration(playerFile, configuration);
  }

  /**
   * Gets a stored property from a player's data file
   *
   * @param uuid UUID for the player
   * @param path Path for the property you're wanting to get
   */
  public Object get(UUID uuid, String path) {
    File playerFile = createFile(uuid);
    if(playerFile == null) return false;

    Configuration configuration = loadConfiguration(playerFile);
    if(configuration == null) return false;

    return configuration.get(path);
  }

}
