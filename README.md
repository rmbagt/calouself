# CaLouselF 🛍️

CaLouselF is a Java-based e-commerce application that facilitates buying and selling of items between users. It features a robust authentication system, role-based access control, and comprehensive item management capabilities.

## Features

### User Management

- **Authentication System**: Secure login and registration functionality
- **Role-Based Access**: Distinct interfaces for buyers, sellers, and administrators
- **User Validation**: Comprehensive input validation for registration

### Seller Features

![Seller Dashboard](/[Documentation_SQL]/img/seller_dashboard.png)

- **Item Management**: Add, edit, and delete items
- **Offer Management**: View and respond to buyer offers
- **Status Tracking**: Monitor item status (pending, approved, declined, sold)

### Buyer Features

![Buyer Dashboard](/[Documentation_SQL]/img/buyer_dashboard.png)

- **Item Browsing**: View available items
- **Wishlist**: Save items for later
- **Purchase History**: Track all transactions
- **Offer System**: Make offers on items

### Admin Features

![Admin Dashboard](/[Documentation_SQL]/img/admin_dashboard.png)

- **Item Approval**: Review and approve/decline new items
- **Moderation**: Provide reasons for declined items
- **System Overview**: Monitor all pending items

### Additional Features

- **Secure Authentication**: Password validation and secure storage
- **Transaction Management**: Complete purchase tracking
- **Offer System**: Comprehensive offer management
- **Status Updates**: Real-time item status tracking

## Screenshots

### User Authentication

| Login                                               | Registration                                                  |
| --------------------------------------------------- | ------------------------------------------------------------- |
| ![Login Screen](/[Documentation_SQL]/img/login.png) | ![Registration Screen](/[Documentation_SQL]/img/register.png) |

### Buyer Interface

| Wishlist                                                 | Purchase History                                                | Make Offer                                              |
| -------------------------------------------------------- | --------------------------------------------------------------- | ------------------------------------------------------- |
| ![Wishlist](/[Documentation_SQL]/img/buyer_wishlist.png) | ![Purchase History](/[Documentation_SQL]/img/buyer_history.png) | ![Make Offer](/[Documentation_SQL]/img/buyer_offer.png) |

### Seller Interface

| Upload Item                                                | Edit Item                                              | View Offers                                               |
| ---------------------------------------------------------- | ------------------------------------------------------ | --------------------------------------------------------- |
| ![Upload Item](/[Documentation_SQL]/img/seller_upload.png) | ![Edit Item](/[Documentation_SQL]/img/seller_edit.png) | ![View Offers](/[Documentation_SQL]/img/seller_offer.png) |

## Technical Requirements

- Eclipse Version 4.34.0 (2024-12)
- Java 23.0.1 (2024-10-15)
- JavaFX 21.0.4
- MySQL Java Connection Library 8.0.24
- XAMPP 8.2.4-0
- Build on macOS Sequoia 15.2 (24C101)

## Database Schema

The application uses the following main tables:

- users
- items
- wishlist
- transactions
- offers

## Installation

1. Clone the repository

```bash
git clone https://github.com/rmbagt/calouself.git
```

2. Add JavaFX and MySQL Java Connection Library to the project

   Using Eclipse:

- Right click on project folder -> Build Path -> Configure Build Path...
- On Libraries tab, click on Modulepath -> Add Library -> User Library
- Check JavaFX and MySQL and then apply changes

3. Set up the MySQL database

```shellscript
mysql -u root -p < /[Documentation_SQL]/calouself.sql
```

4. Configure database connection

- Update the database connection settings in `connect/Connect.java`

5. Compile and run

```shellscript
javac Main.java
java Main
```

## Usage

### For Buyers

1. Register a new account with "Buyer" role
2. Browse available items
3. Add items to wishlist
4. Make offers or purchase items directly
5. View purchase history

### For Sellers

1. Register a new account with "Seller" role
2. Upload items for sale
3. Wait for admin approval
4. Manage item listings
5. Accept or decline offers

### For Administrators

1. Login with admin credentials
2. Review pending items
3. Approve or decline items with reasons
4. Monitor system activity

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- JavaFX for the GUI
- MySQL for database management
- All contributors who have helped with the project

## Contributors

<a href="https://github.com/rmbagt/calouself/graphs/contributors">
    <img src="https://contrib.rocks/image?repo=rmbagt/calouself"/>
</a>
