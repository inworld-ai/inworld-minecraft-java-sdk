package ai.inworld.minecraftsdk.services;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import ai.inworld.minecraftsdk.command.commands.ChatCommand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static ai.inworld.minecraftsdk.Constants.CONSOLE_UID;
import static ai.inworld.minecraftsdk.Constants.PLUGIN_NAME;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LOG;
import static ai.inworld.minecraftsdk.utils.logger.Logger.LogType;


public final class ChatService implements CommandExecutor, TabCompleter {

    private final String permission;
    private final List<ai.inworld.minecraftsdk.command.Command> commands;

    private final String noPermissionMessage = "You do not have permission to execute this command!";

    public ChatService(JavaPlugin plugin) {

        plugin.getCommand("chat").setExecutor(this);
        plugin.getCommand("chat").setTabCompleter(this);

        this.permission = "chat.command";
        this.commands = new ArrayList<>();
        this.commands.add(new ChatCommand());

    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

        if (! sender.hasPermission(this.getPermission())) {
            
            messageSender(sender, noPermissionMessage);
            return true;

        } else if (args.length > 0) {

            String commandName = args[0];

            for (ai.inworld.minecraftsdk.command.Command cmd : commands) {
                if (cmd.getName().equalsIgnoreCase("chat")) {
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

    public String getPermission() {
        return permission;
    }

    public static String getUidHelper(CommandSender commandSender) {

        if (commandSender instanceof Player) {
            return ((Player) commandSender).getUniqueId().toString();
        }
        return CONSOLE_UID;

    }

    private void messageSender(CommandSender sender, String message) {
        // TODO Use Bukkit ChatColor instead of §
        MessageService.sendPlayerMessage(sender, "§c§l" + PLUGIN_NAME + " §7§l» §7" + message);
    }
    
}
