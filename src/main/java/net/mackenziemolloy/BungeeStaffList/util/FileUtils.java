package net.mackenziemolloy.BungeeStaffList.util;

import net.mackenziemolloy.BungeeStaffList.BungeeStaffList;

import java.io.*;

public class FileUtils {

  public static void copy(InputStream in, File file) {
    try {
      OutputStream out = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = in.read(buffer)) > 0) {
        out.write(buffer, 0, length);
      }
      out.close();
      in.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public static void mkdir(File file) {
    try {
      file.mkdir();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static File loadFile(String name) {
    if (!BungeeStaffList.getInstance().getDataFolder().exists()) {
      FileUtils.mkdir(BungeeStaffList.getInstance().getDataFolder());
    }

    File f = new File(BungeeStaffList.getInstance().getDataFolder(), name);
    if (!f.exists()) {
      FileUtils.copy(BungeeStaffList.getInstance().getResourceAsStream(name), f);
    }

    return f;
  }
}