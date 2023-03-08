package ai.inworld.minecraftsdk.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;

public abstract class CommandBase {

    protected String permission = "";
    protected String name = "";
    protected String description = "";
    protected String version = "";
    protected String syntax = "";
    protected HashMap<Integer, List<String>> tabCompletes = new HashMap<>();
    protected int minArgs = 0;
    protected int maxArgs = 0;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

    public List<String> getTabComplete(int index) {
        return tabCompletes.get(index);
    }

    public void perform(Player player, String[] args) {
        Player sender = player;
        if (args.length < minArgs || args.length > maxArgs) {
            performHelp(sender);
            return;
        }
        processCommand(player, args);
    }

    public void performConsole(CommandSender sender, String[] args) {
        if (args.length < minArgs || args.length > maxArgs) {
            performHelp(sender);
            return;
        }
        processCommand(sender, args);
    }

    public void performHelp(CommandSender sender) {
        sender.sendMessage("§c§l" + PLUGIN_NAME + " §7§l» §7" + this.syntax);
    }

    protected abstract void processCommand(CommandSender commandSender, String[] args);

}
