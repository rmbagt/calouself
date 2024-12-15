package view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import model.User;

public class NavigationBar extends MenuBar {
    private Menu userMenu;
    private MenuItem logoutMenuItem;
    private Menu itemsMenu;
    private MenuItem browseItemsMenuItem;
    private MenuItem uploadItemMenuItem;
    private Menu wishlistMenu;
    private MenuItem viewWishlistMenuItem;
    private Menu historyMenu;
    private MenuItem viewPurchaseHistoryMenuItem;
    private Menu offersMenu;
    private MenuItem viewOffersMenuItem;

    public NavigationBar(User user) {
        createMenus(user);
    }

    private void createMenus(User user) {
        // User Menu
        userMenu = new Menu(user != null ? user.getUsername() : "Guest");
        logoutMenuItem = new MenuItem("Logout");
        userMenu.getItems().add(logoutMenuItem);

        // Items Menu
        itemsMenu = new Menu("Items");
        browseItemsMenuItem = new MenuItem("Browse Items");
        itemsMenu.getItems().add(browseItemsMenuItem);

        if (user != null && "Seller".equals(user.getRole())) {
            uploadItemMenuItem = new MenuItem("Upload Item");
            itemsMenu.getItems().add(uploadItemMenuItem);

            offersMenu = new Menu("Offers");
            viewOffersMenuItem = new MenuItem("View Offers");
            offersMenu.getItems().add(viewOffersMenuItem);
        }

        // Wishlist Menu (only for logged-in Buyers)
        if (user != null && "Buyer".equals(user.getRole())) {
            wishlistMenu = new Menu("Wishlist");
            viewWishlistMenuItem = new MenuItem("View Wishlist");
            wishlistMenu.getItems().add(viewWishlistMenuItem);
        }

        // History Menu (only for logged-in Buyers)
        if (user != null && "Buyer".equals(user.getRole())) {
            historyMenu = new Menu("History");
            viewPurchaseHistoryMenuItem = new MenuItem("View Purchase History");
            historyMenu.getItems().add(viewPurchaseHistoryMenuItem);
        }

        // Offers Menu (only for logged-in Sellers)
        

        // Add all menus to the menu bar
        this.getMenus().addAll(userMenu, itemsMenu);
        if (wishlistMenu != null) {
            this.getMenus().add(wishlistMenu);
        }
        if (historyMenu != null) {
            this.getMenus().add(historyMenu);
        }
        if (offersMenu != null) {
            this.getMenus().add(offersMenu);
        }
    }

    // Getters for menu items
    public MenuItem getLogoutMenuItem() { return logoutMenuItem; }
    public MenuItem getBrowseItemsMenuItem() { return browseItemsMenuItem; }
    public MenuItem getUploadItemMenuItem() { return uploadItemMenuItem; }
    public MenuItem getViewWishlistMenuItem() { return viewWishlistMenuItem; }
    public MenuItem getViewPurchaseHistoryMenuItem() { return viewPurchaseHistoryMenuItem; }
    public MenuItem getViewOffersMenuItem() { return viewOffersMenuItem; }
}

