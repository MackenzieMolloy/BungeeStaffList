package net.mackenziemolloy.BungeeStaff.Utils;

import net.mackenziemolloy.BungeeStaff.StaffMember;

import java.util.Comparator;
import java.util.List;

public class PlayerComparator implements Comparator<StaffMember> {
    @Override
    public int compare(StaffMember player1, StaffMember player2) {
        int weight1 = player1.getGroupWeight();
        int weight2 = player2.getGroupWeight();
        if(weight1 == weight2) {
            String name1 = player1.getUsername();
            String name2 = player2.getUsername();
            return name1.compareTo(name2);
        }

        return Integer.compare(weight2, weight1);
    }

    public static void sort(List<StaffMember> playerList) {
        PlayerComparator playerComparator = new PlayerComparator();
        playerList.sort(playerComparator);
    }
}