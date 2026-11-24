package ru.tisbi.furniture.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile StoreDao _storeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Categories` (`CategoryID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Name` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Categories_Name` ON `Categories` (`Name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Materials` (`MaterialID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Name` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Materials_Name` ON `Materials` (`Name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Styles` (`StyleID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Name` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Styles_Name` ON `Styles` (`Name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Statuses` (`StatusID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Name` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Statuses_Name` ON `Statuses` (`Name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Customers` (`CustomerID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `FirstName` TEXT NOT NULL, `LastName` TEXT NOT NULL, `PhoneNumber` TEXT NOT NULL, `ShippingAddress` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Customers_LastName` ON `Customers` (`LastName`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Suppliers` (`SupplierID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `CompanyName` TEXT NOT NULL, `ContactPerson` TEXT NOT NULL, `PhoneNumber` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Products` (`ProductID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Name` TEXT NOT NULL, `Description` TEXT, `Price` REAL NOT NULL, `InStock` INTEGER NOT NULL, `CategoryID` INTEGER NOT NULL, `MaterialID` INTEGER NOT NULL, `StyleID` INTEGER NOT NULL, FOREIGN KEY(`CategoryID`) REFERENCES `Categories`(`CategoryID`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`MaterialID`) REFERENCES `Materials`(`MaterialID`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`StyleID`) REFERENCES `Styles`(`StyleID`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Products_Name` ON `Products` (`Name`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Products_CategoryID` ON `Products` (`CategoryID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Products_MaterialID` ON `Products` (`MaterialID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Products_StyleID` ON `Products` (`StyleID`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Orders` (`OrderID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `CustomerID` INTEGER NOT NULL, `StatusID` INTEGER NOT NULL, `OrderDate` TEXT NOT NULL, FOREIGN KEY(`CustomerID`) REFERENCES `Customers`(`CustomerID`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`StatusID`) REFERENCES `Statuses`(`StatusID`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Orders_CustomerID` ON `Orders` (`CustomerID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Orders_StatusID` ON `Orders` (`StatusID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_Orders_OrderDate` ON `Orders` (`OrderDate`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `OrderItems` (`OrderItemID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `OrderID` INTEGER NOT NULL, `ProductID` INTEGER NOT NULL, `Quantity` INTEGER NOT NULL, FOREIGN KEY(`OrderID`) REFERENCES `Orders`(`OrderID`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`ProductID`) REFERENCES `Products`(`ProductID`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_OrderItems_OrderID` ON `OrderItems` (`OrderID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_OrderItems_ProductID` ON `OrderItems` (`ProductID`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Supplies` (`SupplyID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `SupplyDate` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `SupplyItems` (`SupplyItemID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `SupplyID` INTEGER NOT NULL, `ProductID` INTEGER NOT NULL, `SupplierID` INTEGER NOT NULL, `Quantity` INTEGER NOT NULL, FOREIGN KEY(`SupplyID`) REFERENCES `Supplies`(`SupplyID`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`ProductID`) REFERENCES `Products`(`ProductID`) ON UPDATE NO ACTION ON DELETE NO ACTION , FOREIGN KEY(`SupplierID`) REFERENCES `Suppliers`(`SupplierID`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_SupplyItems_SupplyID` ON `SupplyItems` (`SupplyID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_SupplyItems_ProductID` ON `SupplyItems` (`ProductID`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_SupplyItems_SupplierID` ON `SupplyItems` (`SupplierID`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0dd6da4e50f333be98c129b61507673d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Categories`");
        db.execSQL("DROP TABLE IF EXISTS `Materials`");
        db.execSQL("DROP TABLE IF EXISTS `Styles`");
        db.execSQL("DROP TABLE IF EXISTS `Statuses`");
        db.execSQL("DROP TABLE IF EXISTS `Customers`");
        db.execSQL("DROP TABLE IF EXISTS `Suppliers`");
        db.execSQL("DROP TABLE IF EXISTS `Products`");
        db.execSQL("DROP TABLE IF EXISTS `Orders`");
        db.execSQL("DROP TABLE IF EXISTS `OrderItems`");
        db.execSQL("DROP TABLE IF EXISTS `Supplies`");
        db.execSQL("DROP TABLE IF EXISTS `SupplyItems`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(2);
        _columnsCategories.put("CategoryID", new TableInfo.Column("CategoryID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("Name", new TableInfo.Column("Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(1);
        _indicesCategories.add(new TableInfo.Index("index_Categories_Name", true, Arrays.asList("Name"), Arrays.asList("ASC")));
        final TableInfo _infoCategories = new TableInfo("Categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "Categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "Categories(ru.tisbi.furniture.data.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsMaterials = new HashMap<String, TableInfo.Column>(2);
        _columnsMaterials.put("MaterialID", new TableInfo.Column("MaterialID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaterials.put("Name", new TableInfo.Column("Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaterials = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMaterials = new HashSet<TableInfo.Index>(1);
        _indicesMaterials.add(new TableInfo.Index("index_Materials_Name", true, Arrays.asList("Name"), Arrays.asList("ASC")));
        final TableInfo _infoMaterials = new TableInfo("Materials", _columnsMaterials, _foreignKeysMaterials, _indicesMaterials);
        final TableInfo _existingMaterials = TableInfo.read(db, "Materials");
        if (!_infoMaterials.equals(_existingMaterials)) {
          return new RoomOpenHelper.ValidationResult(false, "Materials(ru.tisbi.furniture.data.MaterialEntity).\n"
                  + " Expected:\n" + _infoMaterials + "\n"
                  + " Found:\n" + _existingMaterials);
        }
        final HashMap<String, TableInfo.Column> _columnsStyles = new HashMap<String, TableInfo.Column>(2);
        _columnsStyles.put("StyleID", new TableInfo.Column("StyleID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStyles.put("Name", new TableInfo.Column("Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStyles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStyles = new HashSet<TableInfo.Index>(1);
        _indicesStyles.add(new TableInfo.Index("index_Styles_Name", true, Arrays.asList("Name"), Arrays.asList("ASC")));
        final TableInfo _infoStyles = new TableInfo("Styles", _columnsStyles, _foreignKeysStyles, _indicesStyles);
        final TableInfo _existingStyles = TableInfo.read(db, "Styles");
        if (!_infoStyles.equals(_existingStyles)) {
          return new RoomOpenHelper.ValidationResult(false, "Styles(ru.tisbi.furniture.data.StyleEntity).\n"
                  + " Expected:\n" + _infoStyles + "\n"
                  + " Found:\n" + _existingStyles);
        }
        final HashMap<String, TableInfo.Column> _columnsStatuses = new HashMap<String, TableInfo.Column>(2);
        _columnsStatuses.put("StatusID", new TableInfo.Column("StatusID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatuses.put("Name", new TableInfo.Column("Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStatuses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStatuses = new HashSet<TableInfo.Index>(1);
        _indicesStatuses.add(new TableInfo.Index("index_Statuses_Name", true, Arrays.asList("Name"), Arrays.asList("ASC")));
        final TableInfo _infoStatuses = new TableInfo("Statuses", _columnsStatuses, _foreignKeysStatuses, _indicesStatuses);
        final TableInfo _existingStatuses = TableInfo.read(db, "Statuses");
        if (!_infoStatuses.equals(_existingStatuses)) {
          return new RoomOpenHelper.ValidationResult(false, "Statuses(ru.tisbi.furniture.data.StatusEntity).\n"
                  + " Expected:\n" + _infoStatuses + "\n"
                  + " Found:\n" + _existingStatuses);
        }
        final HashMap<String, TableInfo.Column> _columnsCustomers = new HashMap<String, TableInfo.Column>(5);
        _columnsCustomers.put("CustomerID", new TableInfo.Column("CustomerID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("FirstName", new TableInfo.Column("FirstName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("LastName", new TableInfo.Column("LastName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("PhoneNumber", new TableInfo.Column("PhoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCustomers.put("ShippingAddress", new TableInfo.Column("ShippingAddress", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCustomers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCustomers = new HashSet<TableInfo.Index>(1);
        _indicesCustomers.add(new TableInfo.Index("index_Customers_LastName", false, Arrays.asList("LastName"), Arrays.asList("ASC")));
        final TableInfo _infoCustomers = new TableInfo("Customers", _columnsCustomers, _foreignKeysCustomers, _indicesCustomers);
        final TableInfo _existingCustomers = TableInfo.read(db, "Customers");
        if (!_infoCustomers.equals(_existingCustomers)) {
          return new RoomOpenHelper.ValidationResult(false, "Customers(ru.tisbi.furniture.data.CustomerEntity).\n"
                  + " Expected:\n" + _infoCustomers + "\n"
                  + " Found:\n" + _existingCustomers);
        }
        final HashMap<String, TableInfo.Column> _columnsSuppliers = new HashMap<String, TableInfo.Column>(4);
        _columnsSuppliers.put("SupplierID", new TableInfo.Column("SupplierID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSuppliers.put("CompanyName", new TableInfo.Column("CompanyName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSuppliers.put("ContactPerson", new TableInfo.Column("ContactPerson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSuppliers.put("PhoneNumber", new TableInfo.Column("PhoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSuppliers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSuppliers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSuppliers = new TableInfo("Suppliers", _columnsSuppliers, _foreignKeysSuppliers, _indicesSuppliers);
        final TableInfo _existingSuppliers = TableInfo.read(db, "Suppliers");
        if (!_infoSuppliers.equals(_existingSuppliers)) {
          return new RoomOpenHelper.ValidationResult(false, "Suppliers(ru.tisbi.furniture.data.SupplierEntity).\n"
                  + " Expected:\n" + _infoSuppliers + "\n"
                  + " Found:\n" + _existingSuppliers);
        }
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(8);
        _columnsProducts.put("ProductID", new TableInfo.Column("ProductID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("Name", new TableInfo.Column("Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("Description", new TableInfo.Column("Description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("Price", new TableInfo.Column("Price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("InStock", new TableInfo.Column("InStock", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("CategoryID", new TableInfo.Column("CategoryID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("MaterialID", new TableInfo.Column("MaterialID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("StyleID", new TableInfo.Column("StyleID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysProducts.add(new TableInfo.ForeignKey("Categories", "NO ACTION", "NO ACTION", Arrays.asList("CategoryID"), Arrays.asList("CategoryID")));
        _foreignKeysProducts.add(new TableInfo.ForeignKey("Materials", "NO ACTION", "NO ACTION", Arrays.asList("MaterialID"), Arrays.asList("MaterialID")));
        _foreignKeysProducts.add(new TableInfo.ForeignKey("Styles", "NO ACTION", "NO ACTION", Arrays.asList("StyleID"), Arrays.asList("StyleID")));
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(4);
        _indicesProducts.add(new TableInfo.Index("index_Products_Name", false, Arrays.asList("Name"), Arrays.asList("ASC")));
        _indicesProducts.add(new TableInfo.Index("index_Products_CategoryID", false, Arrays.asList("CategoryID"), Arrays.asList("ASC")));
        _indicesProducts.add(new TableInfo.Index("index_Products_MaterialID", false, Arrays.asList("MaterialID"), Arrays.asList("ASC")));
        _indicesProducts.add(new TableInfo.Index("index_Products_StyleID", false, Arrays.asList("StyleID"), Arrays.asList("ASC")));
        final TableInfo _infoProducts = new TableInfo("Products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "Products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "Products(ru.tisbi.furniture.data.ProductEntity).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsOrders = new HashMap<String, TableInfo.Column>(4);
        _columnsOrders.put("OrderID", new TableInfo.Column("OrderID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("CustomerID", new TableInfo.Column("CustomerID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("StatusID", new TableInfo.Column("StatusID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("OrderDate", new TableInfo.Column("OrderDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrders = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysOrders.add(new TableInfo.ForeignKey("Customers", "NO ACTION", "NO ACTION", Arrays.asList("CustomerID"), Arrays.asList("CustomerID")));
        _foreignKeysOrders.add(new TableInfo.ForeignKey("Statuses", "NO ACTION", "NO ACTION", Arrays.asList("StatusID"), Arrays.asList("StatusID")));
        final HashSet<TableInfo.Index> _indicesOrders = new HashSet<TableInfo.Index>(3);
        _indicesOrders.add(new TableInfo.Index("index_Orders_CustomerID", false, Arrays.asList("CustomerID"), Arrays.asList("ASC")));
        _indicesOrders.add(new TableInfo.Index("index_Orders_StatusID", false, Arrays.asList("StatusID"), Arrays.asList("ASC")));
        _indicesOrders.add(new TableInfo.Index("index_Orders_OrderDate", false, Arrays.asList("OrderDate"), Arrays.asList("ASC")));
        final TableInfo _infoOrders = new TableInfo("Orders", _columnsOrders, _foreignKeysOrders, _indicesOrders);
        final TableInfo _existingOrders = TableInfo.read(db, "Orders");
        if (!_infoOrders.equals(_existingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "Orders(ru.tisbi.furniture.data.OrderEntity).\n"
                  + " Expected:\n" + _infoOrders + "\n"
                  + " Found:\n" + _existingOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsOrderItems = new HashMap<String, TableInfo.Column>(4);
        _columnsOrderItems.put("OrderItemID", new TableInfo.Column("OrderItemID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("OrderID", new TableInfo.Column("OrderID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("ProductID", new TableInfo.Column("ProductID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("Quantity", new TableInfo.Column("Quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrderItems = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysOrderItems.add(new TableInfo.ForeignKey("Orders", "CASCADE", "NO ACTION", Arrays.asList("OrderID"), Arrays.asList("OrderID")));
        _foreignKeysOrderItems.add(new TableInfo.ForeignKey("Products", "NO ACTION", "NO ACTION", Arrays.asList("ProductID"), Arrays.asList("ProductID")));
        final HashSet<TableInfo.Index> _indicesOrderItems = new HashSet<TableInfo.Index>(2);
        _indicesOrderItems.add(new TableInfo.Index("index_OrderItems_OrderID", false, Arrays.asList("OrderID"), Arrays.asList("ASC")));
        _indicesOrderItems.add(new TableInfo.Index("index_OrderItems_ProductID", false, Arrays.asList("ProductID"), Arrays.asList("ASC")));
        final TableInfo _infoOrderItems = new TableInfo("OrderItems", _columnsOrderItems, _foreignKeysOrderItems, _indicesOrderItems);
        final TableInfo _existingOrderItems = TableInfo.read(db, "OrderItems");
        if (!_infoOrderItems.equals(_existingOrderItems)) {
          return new RoomOpenHelper.ValidationResult(false, "OrderItems(ru.tisbi.furniture.data.OrderItemEntity).\n"
                  + " Expected:\n" + _infoOrderItems + "\n"
                  + " Found:\n" + _existingOrderItems);
        }
        final HashMap<String, TableInfo.Column> _columnsSupplies = new HashMap<String, TableInfo.Column>(2);
        _columnsSupplies.put("SupplyID", new TableInfo.Column("SupplyID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplies.put("SupplyDate", new TableInfo.Column("SupplyDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSupplies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSupplies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSupplies = new TableInfo("Supplies", _columnsSupplies, _foreignKeysSupplies, _indicesSupplies);
        final TableInfo _existingSupplies = TableInfo.read(db, "Supplies");
        if (!_infoSupplies.equals(_existingSupplies)) {
          return new RoomOpenHelper.ValidationResult(false, "Supplies(ru.tisbi.furniture.data.SupplyEntity).\n"
                  + " Expected:\n" + _infoSupplies + "\n"
                  + " Found:\n" + _existingSupplies);
        }
        final HashMap<String, TableInfo.Column> _columnsSupplyItems = new HashMap<String, TableInfo.Column>(5);
        _columnsSupplyItems.put("SupplyItemID", new TableInfo.Column("SupplyItemID", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplyItems.put("SupplyID", new TableInfo.Column("SupplyID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplyItems.put("ProductID", new TableInfo.Column("ProductID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplyItems.put("SupplierID", new TableInfo.Column("SupplierID", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSupplyItems.put("Quantity", new TableInfo.Column("Quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSupplyItems = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysSupplyItems.add(new TableInfo.ForeignKey("Supplies", "CASCADE", "NO ACTION", Arrays.asList("SupplyID"), Arrays.asList("SupplyID")));
        _foreignKeysSupplyItems.add(new TableInfo.ForeignKey("Products", "NO ACTION", "NO ACTION", Arrays.asList("ProductID"), Arrays.asList("ProductID")));
        _foreignKeysSupplyItems.add(new TableInfo.ForeignKey("Suppliers", "NO ACTION", "NO ACTION", Arrays.asList("SupplierID"), Arrays.asList("SupplierID")));
        final HashSet<TableInfo.Index> _indicesSupplyItems = new HashSet<TableInfo.Index>(3);
        _indicesSupplyItems.add(new TableInfo.Index("index_SupplyItems_SupplyID", false, Arrays.asList("SupplyID"), Arrays.asList("ASC")));
        _indicesSupplyItems.add(new TableInfo.Index("index_SupplyItems_ProductID", false, Arrays.asList("ProductID"), Arrays.asList("ASC")));
        _indicesSupplyItems.add(new TableInfo.Index("index_SupplyItems_SupplierID", false, Arrays.asList("SupplierID"), Arrays.asList("ASC")));
        final TableInfo _infoSupplyItems = new TableInfo("SupplyItems", _columnsSupplyItems, _foreignKeysSupplyItems, _indicesSupplyItems);
        final TableInfo _existingSupplyItems = TableInfo.read(db, "SupplyItems");
        if (!_infoSupplyItems.equals(_existingSupplyItems)) {
          return new RoomOpenHelper.ValidationResult(false, "SupplyItems(ru.tisbi.furniture.data.SupplyItemEntity).\n"
                  + " Expected:\n" + _infoSupplyItems + "\n"
                  + " Found:\n" + _existingSupplyItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0dd6da4e50f333be98c129b61507673d", "96884daffed188e4243cee061a91a1e6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Categories","Materials","Styles","Statuses","Customers","Suppliers","Products","Orders","OrderItems","Supplies","SupplyItems");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `Categories`");
      _db.execSQL("DELETE FROM `Materials`");
      _db.execSQL("DELETE FROM `Styles`");
      _db.execSQL("DELETE FROM `Statuses`");
      _db.execSQL("DELETE FROM `Customers`");
      _db.execSQL("DELETE FROM `Suppliers`");
      _db.execSQL("DELETE FROM `Products`");
      _db.execSQL("DELETE FROM `Orders`");
      _db.execSQL("DELETE FROM `OrderItems`");
      _db.execSQL("DELETE FROM `Supplies`");
      _db.execSQL("DELETE FROM `SupplyItems`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(StoreDao.class, StoreDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public StoreDao dao() {
    if (_storeDao != null) {
      return _storeDao;
    } else {
      synchronized(this) {
        if(_storeDao == null) {
          _storeDao = new StoreDao_Impl(this);
        }
        return _storeDao;
      }
    }
  }
}
