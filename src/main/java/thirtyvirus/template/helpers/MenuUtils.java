package thirtyvirus.template.helpers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import thirtyvirus.multiversion.API;
import thirtyvirus.multiversion.XMaterial;
import thirtyvirus.template.TemplatePlugin;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    private TemplatePlugin main = null;
    public MenuUtils(TemplatePlugin main) { this.main = main; }

    public static void tutorialMenu(Player player) {
        ItemStack book = new ItemStack(XMaterial.WRITTEN_BOOK.parseMaterial());
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.setAuthor("HexiaMC (DrPuffy)");
        meta.setTitle("Welcome to HexiaMC Prestige!");

        List<String> pages = new ArrayList<String>();

        // exmaple main menu
        pages.add(ChatColor.translateAlternateColorCodes('&',
                "      &7&lWelcome to:" + "\n" +
                        "   &c&lHexia&5&lPrestige&r" + "\n" +
                        "This guide book will show you everything you need to know about prestiges! Happy reading!" + "\n" +
                        "" + "\n" +
                        " - HexiaMC" + "\n" +
                        "" + "\n" +
                        "&7&lGo to the next page for info on a second page!"));

        // example secondary page
        pages.add(ChatColor.translateAlternateColorCodes('&',
                "&c&lPrestiges&r" + "\n" +
                        "" + "\n" +
                        "We have 1000 prestiges on this server along with Mastery!" + "\n" +
                        "" + "\n" +
                        "Upon reaching prestige master your data will be reset but you will get" + "\n" +
                        "" + "\n" +
                        "the prestige master pickaxe!" + "\n"));


        meta.setPages(pages);
        book.setItemMeta(meta);

        Utilities.playSound(ActionSound.CLICK, player);
        API.openBook(book, player);
    }

}
