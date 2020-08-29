package thirtyvirus.template.commands;

import me.clip.ezprestige.PrestigeManager;
import me.clip.ezprestige.objects.Prestige;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import thirtyvirus.template.TemplatePlugin;
import thirtyvirus.template.helpers.MenuUtils;
import thirtyvirus.template.helpers.Utilities;

import java.util.Arrays;

public class MainPluginCommand implements CommandExecutor{

    private TemplatePlugin main = null;
    public MainPluginCommand(TemplatePlugin main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // verify that the user has proper permissions
        if (!sender.hasPermission("hexia.user")) {
            Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-permissions-message")));
            return true;
        }

        try {

            switch (args[0].toLowerCase()) {
                case "help":
                    help(sender);
                    break;
                case "info":
                    info(sender);
                    break;
                case "tutorial":
                    if (sender instanceof Player) MenuUtils.tutorialMenu((Player) sender);
                    else Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-console-message")));
                    break;
                case "getpres":
                    getPres(sender);
                    break;
                case "master":
                    presMaster(sender);
                    break;
                case "reset":
                    resetPres(sender);
                    break;
                case "setpres":
                    if(args[1].length() == 0)
                    {
                        Player target = (Player) sender;
                        setPres(target, args[2]);
                        sender.sendMessage(TemplatePlugin.prefix + ChatColor.GRAY + "args[2]");
                    }
                    else {
                        Player target = Bukkit.getPlayer(args[1]);
                        setPres(target, args[2]);
                        sender.sendMessage(TemplatePlugin.prefix + ChatColor.GRAY + "args[2]");
                    }
                    break;

                // put plugin specific commands here

                case "reload":
                    if (sender.hasPermission("hexia.admin")) reload(sender);
                    else Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-permissions-message")));
                    break;
                default:
                    Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("not-a-command-message")));
                    help(sender);
                    break;
            }

        } catch(Exception e) {
            Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("formatting-error-message")));
        }

        return true;
    }

    private void setPres(Player target, String arg) {
        Prestige pres = PrestigeManager.getCurrentPrestige(target);
        pres.setPrestige(Integer.parseInt(arg));
        target.sendMessage(TemplatePlugin.prefix + ChatColor.GREEN + "Your prestige was set to "+arg);
    }

    private void info(CommandSender sender) {
        sender.sendMessage(TemplatePlugin.prefix + ChatColor.GRAY + "Plugin Info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GREEN + "Version " + main.getVersion() + " - By HexiaMC (DrPuffy)");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
    }

    private void help(CommandSender sender) {
        sender.sendMessage(TemplatePlugin.prefix + ChatColor.GRAY + "Commands");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres help");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres info");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres tutorial");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres getpres");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres setpres");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres reset");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres master");
        sender.sendMessage(ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/hexpres reload");
        sender.sendMessage(ChatColor.DARK_PURPLE + "------------------------------");
    }

    private void getPres(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Prestige pres = PrestigeManager.getCurrentPrestige(player);
            sender.sendMessage(TemplatePlugin.prefix + ChatColor.GRAY + pres.getPrestige());
        }
        else Utilities.warnPlayer(sender, Arrays.asList(main.getPhrase("no-console-message")));
    }

    private void presMaster(CommandSender sender) {
        Prestige pres = getPresObject(sender);
        if(pres.getPrestige() == 1000) {
            sender.sendMessage(TemplatePlugin.prefix + ChatColor.GREEN + "you are prestige 1000 - starting mastery!");
            //do prestige master stuff here
        }
        else sender.sendMessage(TemplatePlugin.prefix + ChatColor.LIGHT_PURPLE + "you are not the correct prestige to start mastery.");
    }

    private void resetPres(CommandSender sender) {
        Prestige pres = getPresObject(sender);
        pres.setPrestige(0);
    }

    private Prestige getPresObject(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Prestige pres = PrestigeManager.getCurrentPrestige(player);
            return pres;
        }
        else
            return null;
    }

    private void reload(CommandSender sender) {
        main.reloadConfig();
        main.loadConfiguration();

        main.loadLangFile();

        Utilities.informPlayer(sender, Arrays.asList("configuration, values, and language settings reloaded"));
    }

}
