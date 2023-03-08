package ai.inworld.minecraftsdk.commands;

import ai.inworld.minecraftsdk.Constants;
// import ai.inworld.commands.inworld.InworldHelpCommand;
// import ai.inworld.commands.inworld.InworldMessageCommand;
// import ai.inworld.commands.inworld.InworldOpenCommand;
// import ai.inworld.commands.inworld.InworldSummonCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// import static ai.inworld.Constants.consoleUid;

public class CommandService implements CommandExecutor, TabCompleter {

    private final String permission;
    private final List<SubCommand> subCommands;

    private final String noPermissionMessage = "You do not have permission to execute this command!";

    public CommandService(String permission) {
        this.permission = permission;
        this.subCommands = new ArrayList<>();
        // this.subCommands.add(new InworldMessageCommand());
        // this.subCommands.add(new InworldOpenCommand());
        // this.subCommands.add(new InworldSummonCommand());

        // this.subCommands.add(new InworldHelpCommand(subCommands));
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (! sender.hasPermission(this.getPermission())) {
            messageSender(sender, noPermissionMessage);
            return true;
        } else
        if (args.length > 0) {
            String subCommand = args[0];

            for (SubCommand sub : subCommands) {
                if (subCommand.equalsIgnoreCase(sub.getName())) {
                    if (sender.hasPermission(sub.getPermission())) {
                        if (sender instanceof Player) {
                            sub.perform((Player) sender, args);
                        } else {
                            sub.performConsole(sender, args);
                        }
                    } else {
                        messageSender(sender, noPermissionMessage);
                    }
                }
            }
        } else {
            subCommands.get(subCommands.size() - 1).performHelp(sender);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length > 0) {
            String subCommand = args[0];
            boolean foundSubCommand = false;

            for (SubCommand sub : subCommands) {
                if (subCommand.equalsIgnoreCase(sub.getName())) {
                    foundSubCommand = true;
                    if (sender.hasPermission(sub.getPermission())) {
                        return sub.getTabComplete(args.length);
                    }
                }
            }

            if (!foundSubCommand) {
                List<String> subCommands = new ArrayList<>();
                for (SubCommand sub : this.subCommands) {
                    if (sender.hasPermission(sub.getPermission())) {
                        subCommands.add(sub.getName());
                    }
                }

                return subCommands;
            }
        }

        return null;
    }

    public String getPermission() {
        return permission;
    }

    // public static String getUidHelper(CommandSender commandSender) {
    //     if (commandSender instanceof Player) {
    //         return ((Player) commandSender).getUniqueId().toString();
    //     }
    //     return consoleUid;
    // }

    private void messageSender(CommandSender sender, String message) {
        // TODO Use Bukkit ChatColor instead of §
        sender.sendMessage("§c§l" + Constants.pluginName + " §7§l» §7" + message);
    }
}
