package ai.inworld.minecraftsdk.command;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;

/**
 * The base class that all commands should extend. 
 * Not to be used by itself
 */
public abstract class CommandBase {

    protected String permission = "";
    protected String name = "";
    protected String description = "";
    protected String version = "";
    protected String syntax = "";
    protected HashMap<Integer, List<String>> tabCompletes = new HashMap<>();
    protected int minArgs = 0;
    protected int maxArgs = 0;

    // Gets the command name
    public String getName() {
        return name;
    }

    // Gets the comamnd's description
    public String getDescription() {
        return description;
    }

    // Gets the version of the command
    public String getVersion() {
        return version;
    }

    // Gets the syntax for the command. Used in help output.
    public String getSyntax() {
        return syntax;
    }

    // Gets the permission of the command
    public String getPermission() {
        return permission;
    }

    /**
     * Checks if the player has permission to use the command
     * @param player The player to check if they have permission
     */ 
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }

    /**
     * Gets the tab autocomplete of the command
     * @param args Not used
     * @param index The position in the command list
     */ 
    public List<String> getTabComplete(String[] args, int index) {
        return tabCompletes.get(index);
    }

    /**
     * Performs the command
     * @param player The player issuing the command
     * @param args The arguments for the command
     */
    public void perform(Player player, String[] args) {
        Player sender = player;
        if (args.length < minArgs || args.length > maxArgs) {
            performHelp(sender);
            return;
        }
        processCommand(player, args);
    }

    /**
     * Not used
     */
    public void performConsole(Player sender, String[] args) {
        if (args.length < minArgs || args.length > maxArgs) {
            performHelp(sender);
            return;
        }
        processCommand(sender, args);
    }

    /**
     * Outputs the command syntax to the user. Used in help
     * and if the command is entered wrong.
     * @param sender The player to send the help to
     */
    public void performHelp(Player sender) {
        sender.sendMessage("§c§l" + PLUGIN_NAME + " §7§l» §7" + this.syntax);
    }

    protected abstract void processCommand(Player commandSender, String[] args);

}
