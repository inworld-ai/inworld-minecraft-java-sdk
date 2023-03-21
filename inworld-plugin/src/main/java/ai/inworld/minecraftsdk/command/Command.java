package ai.inworld.minecraftsdk.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface Command {

    void perform(Player player, String[] args);
    void performConsole(Player sender, String[] args);
    void performHelp(Player sender);
    String getName();
    String getDescription();
    String getVersion();
    String getSyntax();
    String getPermission();
    boolean hasPermission(Player player);
    List<String> getTabComplete(String[] args, int index);

}
