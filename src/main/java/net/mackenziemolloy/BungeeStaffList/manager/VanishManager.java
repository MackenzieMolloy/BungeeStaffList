package net.mackenziemolloy.BungeeStaffList.manager;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;
import net.mackenziemolloy.BungeeStaffList.exception.group.GroupProviderAlreadyRegistered;

import java.util.UUID;

public class VanishManager {

  /*

    Same as GroupManager, just instead it's storing event listeners
    There is a constant InternalVanish system where the vanish state of players is altered by the listeners

    VanishManager#getPlayerState(Player)
    VanishManager#setPlayerState(Player, Boolean)

   */

  PlayerManager playerManager;

  public VanishManager(PlayerManager playerManager) {
    playerManager = this.playerManager;
  }

  /**
   * Gets the vanished state of a player from their UUID
   *
   * @param uuid Unique ID of the player
   * @return boolean vanished state
   */
  public boolean getPlayerState(UUID uuid) {
    return (boolean) playerManager.get(uuid, "hidden");
  }

  /**
   * Sets the vanished state of a player from their UUID
   *
   * @param uuid Unique ID of the player
   * @param state boolean vanished state
   * @return boolean success state
   */
  public boolean setPlayerState(UUID uuid, boolean state) {
    return playerManager.set(uuid, "hidden", state);
  }

}
