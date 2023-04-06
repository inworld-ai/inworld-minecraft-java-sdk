package ai.inworld.minecraftsdk.services;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ai.inworld.minecraftsdk.command.commands.APICommand;
import ai.inworld.minecraftsdk.command.commands.CharacterCommand;
import ai.inworld.minecraftsdk.command.commands.HelpCommand;
import ai.inworld.minecraftsdk.command.commands.RegisterCommand;
import ai.inworld.minecraftsdk.command.commands.SceneCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static ai.inworld.minecraftsdk.Constants.CONSOLE_UID;
import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;

/**
 * This service class loads and manages the admin commands
 */
public final class CommandService implements CommandExecutor, TabCompleter {

    private final String permission;
    private final List<ai.inworld.minecraftsdk.command.Command> commands;

    private final String noPermissionMessage = "You do not have permission to execute this command!";

    /**
     * This method is called upon enabling the plugin.
     * @param plugin A reference to the Minecraft plugin
     */
    public CommandService(JavaPlugin plugin) {

        plugin.getCommand("inworld").setExecutor(this);
        plugin.getCommand("inworld").setTabCompleter(this);

        // Load the admin commands into this service
        this.permission = "inworld.command";
        this.commands = new ArrayList<>();
        this.commands.add(new APICommand());
        this.commands.add(new CharacterCommand());
        // this.commands.add(new RegisterCommand());
        this.commands.add(new SceneCommand());
        this.commands.add(new HelpCommand(this.commands));

    }

    /**
     * This method handles processing the command. It confirms the player has permission and if it's a player or not.
     */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

        if (! sender.hasPermission(this.getPermission())) {
            messageSender(sender, noPermissionMessage);
            return true;
        } else if (args.length > 0) {

            String commandName = args[0];

            for (ai.inworld.minecraftsdk.command.Command cmd : commands) {
                if (commandName.equalsIgnoreCase(cmd.getName())) {
                    if (sender.hasPermission(cmd.getPermission())) {
                        if (sender instanceof Player) {
                            cmd.perform((Player) sender, args);
                        } else {
                            cmd.performConsole((Player) sender, args);
                        }
                    } else {
                        messageSender(sender, noPermissionMessage);
                    }
                }
            }

        } else {
            commands.get(commands.size() - 1).performHelp((Player) sender);
        }

        return true;

    }

    /**
     * This method handles processing the tab autocomplete for the command as the user enters it
     */
    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        
        // LOG(LogType.Info, "onTabComplete " + args.length);

        if (args.length > 0) {

            String commandName = args[0];
            boolean foundCommand = false;

            for (ai.inworld.minecraftsdk.command.Command cmd : commands) {
                if (commandName.equalsIgnoreCase(cmd.getName())) {
                    foundCommand = true;
                    if (sender.hasPermission(cmd.getPermission())) {
                        return cmd.getTabComplete(args, args.length);
                    }
                }
            }

            if (!foundCommand) {
                List<String> authorizedCommands = new ArrayList<>();
                for (ai.inworld.minecraftsdk.command.Command cmd : this.commands) {
                    if (sender.hasPermission(cmd.getPermission())) {
                        authorizedCommands.add(cmd.getName());
                    }
                }

                return authorizedCommands;
            }

        }

        return null;
    }

    /**
     * This method returns the permission level of the command service
     * @return String the permission level
     */
    public String getPermission() {
        return permission;
    }

    /**
     * This method returns the string UID of the Player sending the command
     * @param commandSender
     * @return String the UID of the Player
     */
    public static String getUidHelper(CommandSender commandSender) {

        if (commandSender instanceof Player) {
            return ((Player) commandSender).getUniqueId().toString();
        }
        return CONSOLE_UID;

    }

    /**
     * This method sends a message to the Player that send the command
     * @param sender The Player who sent the command
     * @param message The message to send
     */
    private void messageSender(CommandSender sender, String message) {
        // TODO Use Bukkit ChatColor instead of §
        MessageService.sendPlayerMessage(sender, "§c§l" + PLUGIN_NAME + " §7§l» §7" + message);
    }
    
}
