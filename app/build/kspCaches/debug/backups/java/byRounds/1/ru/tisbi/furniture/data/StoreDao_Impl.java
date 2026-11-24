package ru.tisbi.furniture.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Pair;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StoreDao_Impl extends StoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CategoryEntity> __insertionAdapterOfCategoryEntity;

  private final EntityInsertionAdapter<MaterialEntity> __insertionAdapterOfMaterialEntity;

  private final EntityInsertionAdapter<StyleEntity> __insertionAdapterOfStyleEntity;

  private final EntityInsertionAdapter<StatusEntity> __insertionAdapterOfStatusEntity;

  private final EntityInsertionAdapter<ProductEntity> __insertionAdapterOfProductEntity;

  private final EntityInsertionAdapter<CustomerEntity> __insertionAdapterOfCustomerEntity;

  private final EntityInsertionAdapter<SupplierEntity> __insertionAdapterOfSupplierEntity;

  private final EntityInsertionAdapter<OrderEntity> __insertionAdapterOfOrderEntity;

  private final EntityInsertionAdapter<OrderItemEntity> __insertionAdapterOfOrderItemEntity;

  private final EntityInsertionAdapter<SupplyEntity> __insertionAdapterOfSupplyEntity;

  private final EntityInsertionAdapter<SupplyItemEntity> __insertionAdapterOfSupplyItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCategory;

  private final SharedSQLiteStatement __preparedStmtOfUpdateMaterial;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStyle;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCategory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMaterial;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStyle;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateProduct;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProduct;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCustomer;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCustomer;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSupplier;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSupplier;

  private final SharedSQLiteStatement __preparedStmtOfSetOrderStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOrderItems;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOrderRow;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSupplyItems;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSupplyRow;

  public StoreDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategoryEntity = new EntityInsertionAdapter<CategoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Categories` (`CategoryID`,`Name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__insertionAdapterOfMaterialEntity = new EntityInsertionAdapter<MaterialEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Materials` (`MaterialID`,`Name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MaterialEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__insertionAdapterOfStyleEntity = new EntityInsertionAdapter<StyleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Styles` (`StyleID`,`Name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StyleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__insertionAdapterOfStatusEntity = new EntityInsertionAdapter<StatusEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Statuses` (`StatusID`,`Name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StatusEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__insertionAdapterOfProductEntity = new EntityInsertionAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Products` (`ProductID`,`Name`,`Description`,`Price`,`InStock`,`CategoryID`,`MaterialID`,`StyleID`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindDouble(4, entity.getPrice());
        statement.bindLong(5, entity.getInStock());
        statement.bindLong(6, entity.getCategoryId());
        statement.bindLong(7, entity.getMaterialId());
        statement.bindLong(8, entity.getStyleId());
      }
    };
    this.__insertionAdapterOfCustomerEntity = new EntityInsertionAdapter<CustomerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Customers` (`CustomerID`,`FirstName`,`LastName`,`PhoneNumber`,`ShippingAddress`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CustomerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getFirstName());
        statement.bindString(3, entity.getLastName());
        statement.bindString(4, entity.getPhone());
        statement.bindString(5, entity.getAddress());
      }
    };
    this.__insertionAdapterOfSupplierEntity = new EntityInsertionAdapter<SupplierEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Suppliers` (`SupplierID`,`CompanyName`,`ContactPerson`,`PhoneNumber`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SupplierEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCompany());
        statement.bindString(3, entity.getContact());
        statement.bindString(4, entity.getPhone());
      }
    };
    this.__insertionAdapterOfOrderEntity = new EntityInsertionAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Orders` (`OrderID`,`CustomerID`,`StatusID`,`OrderDate`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCustomerId());
        statement.bindLong(3, entity.getStatusId());
        statement.bindString(4, entity.getDate());
      }
    };
    this.__insertionAdapterOfOrderItemEntity = new EntityInsertionAdapter<OrderItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `OrderItems` (`OrderItemID`,`OrderID`,`ProductID`,`Quantity`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getOrderId());
        statement.bindLong(3, entity.getProductId());
        statement.bindLong(4, entity.getQuantity());
      }
    };
    this.__insertionAdapterOfSupplyEntity = new EntityInsertionAdapter<SupplyEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Supplies` (`SupplyID`,`SupplyDate`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SupplyEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
      }
    };
    this.__insertionAdapterOfSupplyItemEntity = new EntityInsertionAdapter<SupplyItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `SupplyItems` (`SupplyItemID`,`SupplyID`,`ProductID`,`SupplierID`,`Quantity`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SupplyItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSupplyId());
        statement.bindLong(3, entity.getProductId());
        statement.bindLong(4, entity.getSupplierId());
        statement.bindLong(5, entity.getQuantity());
      }
    };
    this.__preparedStmtOfUpdateCategory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Categories SET Name = ? WHERE CategoryID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateMaterial = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Materials SET Name = ? WHERE MaterialID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStyle = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Styles SET Name = ? WHERE StyleID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Statuses SET Name = ? WHERE StatusID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCategory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Categories WHERE CategoryID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMaterial = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Materials WHERE MaterialID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteStyle = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Styles WHERE StyleID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Statuses WHERE StatusID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateProduct = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Products SET Name = ?, Description = ?, Price = ?, InStock = ?,\n"
                + "                  CategoryID = ?, MaterialID = ?, StyleID = ? WHERE ProductID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteProduct = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Products WHERE ProductID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCustomer = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Customers SET FirstName = ?, LastName = ?, PhoneNumber = ?,\n"
                + "                  ShippingAddress = ? WHERE CustomerID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCustomer = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Customers WHERE CustomerID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSupplier = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Suppliers SET CompanyName = ?, ContactPerson = ?, PhoneNumber = ? WHERE SupplierID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSupplier = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Suppliers WHERE SupplierID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetOrderStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Orders SET StatusID = ? WHERE OrderID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOrderItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM OrderItems WHERE OrderID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOrderRow = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Orders WHERE OrderID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSupplyItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM SupplyItems WHERE SupplyID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSupplyRow = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Supplies WHERE SupplyID = ?";
        return _query;
      }
    };
  }

  @Override
  public long insertCategory(final CategoryEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfCategoryEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertMaterial(final MaterialEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfMaterialEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertStyle(final StyleEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfStyleEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertStatus(final StatusEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfStatusEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertProduct(final ProductEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfProductEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertCustomer(final CustomerEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfCustomerEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertSupplier(final SupplierEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfSupplierEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertOrder(final OrderEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfOrderEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertOrderItem(final OrderItemEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfOrderItemEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertSupply(final SupplyEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfSupplyEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long insertSupplyItem(final SupplyItemEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfSupplyItemEntity.insertAndReturnId(e);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void createOrder(final long customerId, final long statusId, final String date,
      final List<Pair<Long, Integer>> items) {
    __db.beginTransaction();
    try {
      StoreDao_Impl.super.createOrder(customerId, statusId, date, items);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteOrder(final long id) {
    __db.beginTransaction();
    try {
      StoreDao_Impl.super.deleteOrder(id);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void createSupply(final String date, final List<SupplyLine> lines) {
    __db.beginTransaction();
    try {
      StoreDao_Impl.super.createSupply(date, lines);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSupply(final long id) {
    __db.beginTransaction();
    try {
      StoreDao_Impl.super.deleteSupply(id);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCategory(final long id, final String name) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCategory.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, name);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateCategory.release(_stmt);
    }
  }

  @Override
  public void updateMaterial(final long id, final String name) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateMaterial.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, name);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateMaterial.release(_stmt);
    }
  }

  @Override
  public void updateStyle(final long id, final String name) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStyle.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, name);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStyle.release(_stmt);
    }
  }

  @Override
  public void updateStatus(final long id, final String name) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, name);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStatus.release(_stmt);
    }
  }

  @Override
  public void deleteCategory(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCategory.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteCategory.release(_stmt);
    }
  }

  @Override
  public void deleteMaterial(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMaterial.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteMaterial.release(_stmt);
    }
  }

  @Override
  public void deleteStyle(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStyle.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteStyle.release(_stmt);
    }
  }

  @Override
  public void deleteStatus(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStatus.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteStatus.release(_stmt);
    }
  }

  @Override
  public void updateProduct(final long id, final String name, final String description,
      final double price, final int inStock, final long categoryId, final long materialId,
      final long styleId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateProduct.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, name);
    _argIndex = 2;
    _stmt.bindString(_argIndex, description);
    _argIndex = 3;
    _stmt.bindDouble(_argIndex, price);
    _argIndex = 4;
    _stmt.bindLong(_argIndex, inStock);
    _argIndex = 5;
    _stmt.bindLong(_argIndex, categoryId);
    _argIndex = 6;
    _stmt.bindLong(_argIndex, materialId);
    _argIndex = 7;
    _stmt.bindLong(_argIndex, styleId);
    _argIndex = 8;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateProduct.release(_stmt);
    }
  }

  @Override
  public void deleteProduct(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProduct.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteProduct.release(_stmt);
    }
  }

  @Override
  public void updateCustomer(final long id, final String firstName, final String lastName,
      final String phone, final String address) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCustomer.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, firstName);
    _argIndex = 2;
    _stmt.bindString(_argIndex, lastName);
    _argIndex = 3;
    _stmt.bindString(_argIndex, phone);
    _argIndex = 4;
    _stmt.bindString(_argIndex, address);
    _argIndex = 5;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateCustomer.release(_stmt);
    }
  }

  @Override
  public void deleteCustomer(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCustomer.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteCustomer.release(_stmt);
    }
  }

  @Override
  public void updateSupplier(final long id, final String company, final String contact,
      final String phone) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSupplier.acquire();
    int _argIndex = 1;
    _stmt.bindString(_argIndex, company);
    _argIndex = 2;
    _stmt.bindString(_argIndex, contact);
    _argIndex = 3;
    _stmt.bindString(_argIndex, phone);
    _argIndex = 4;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateSupplier.release(_stmt);
    }
  }

  @Override
  public void deleteSupplier(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSupplier.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteSupplier.release(_stmt);
    }
  }

  @Override
  public void setOrderStatus(final long id, final long statusId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfSetOrderStatus.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, statusId);
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfSetOrderStatus.release(_stmt);
    }
  }

  @Override
  public void deleteOrderItems(final long orderId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOrderItems.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, orderId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteOrderItems.release(_stmt);
    }
  }

  @Override
  public void deleteOrderRow(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOrderRow.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteOrderRow.release(_stmt);
    }
  }

  @Override
  public void deleteSupplyItems(final long supplyId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSupplyItems.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, supplyId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteSupplyItems.release(_stmt);
    }
  }

  @Override
  public void deleteSupplyRow(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSupplyRow.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteSupplyRow.release(_stmt);
    }
  }

  @Override
  public List<Ref> categories() {
    final String _sql = "SELECT CategoryID AS id, Name AS name FROM Categories ORDER BY Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final List<Ref> _result = new ArrayList<Ref>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Ref _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item = new Ref(_tmpId,_tmpName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ref> materials() {
    final String _sql = "SELECT MaterialID AS id, Name AS name FROM Materials ORDER BY Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final List<Ref> _result = new ArrayList<Ref>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Ref _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item = new Ref(_tmpId,_tmpName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ref> styles() {
    final String _sql = "SELECT StyleID AS id, Name AS name FROM Styles ORDER BY Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final List<Ref> _result = new ArrayList<Ref>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Ref _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item = new Ref(_tmpId,_tmpName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Ref> statuses() {
    final String _sql = "SELECT StatusID AS id, Name AS name FROM Statuses ORDER BY StatusID";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final List<Ref> _result = new ArrayList<Ref>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Ref _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item = new Ref(_tmpId,_tmpName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Product> products() {
    final String _sql = "SELECT p.ProductID AS id, p.Name AS name, IFNULL(p.Description, '') AS description, p.Price AS price,\n"
            + "                  p.InStock AS inStock, p.CategoryID AS categoryId, p.MaterialID AS materialId, p.StyleID AS styleId,\n"
            + "                  c.Name AS categoryName, m.Name AS materialName, s.Name AS styleName\n"
            + "           FROM Products p\n"
            + "           JOIN Categories c ON c.CategoryID = p.CategoryID\n"
            + "           JOIN Materials m ON m.MaterialID = p.MaterialID\n"
            + "           JOIN Styles s ON s.StyleID = p.StyleID\n"
            + "           ORDER BY p.Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfDescription = 2;
      final int _cursorIndexOfPrice = 3;
      final int _cursorIndexOfInStock = 4;
      final int _cursorIndexOfCategoryId = 5;
      final int _cursorIndexOfMaterialId = 6;
      final int _cursorIndexOfStyleId = 7;
      final int _cursorIndexOfCategoryName = 8;
      final int _cursorIndexOfMaterialName = 9;
      final int _cursorIndexOfStyleName = 10;
      final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Product _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final double _tmpPrice;
        _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
        final int _tmpInStock;
        _tmpInStock = _cursor.getInt(_cursorIndexOfInStock);
        final long _tmpCategoryId;
        _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
        final long _tmpMaterialId;
        _tmpMaterialId = _cursor.getLong(_cursorIndexOfMaterialId);
        final long _tmpStyleId;
        _tmpStyleId = _cursor.getLong(_cursorIndexOfStyleId);
        final String _tmpCategoryName;
        _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
        final String _tmpMaterialName;
        _tmpMaterialName = _cursor.getString(_cursorIndexOfMaterialName);
        final String _tmpStyleName;
        _tmpStyleName = _cursor.getString(_cursorIndexOfStyleName);
        _item = new Product(_tmpId,_tmpName,_tmpDescription,_tmpPrice,_tmpInStock,_tmpCategoryId,_tmpMaterialId,_tmpStyleId,_tmpCategoryName,_tmpMaterialName,_tmpStyleName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Customer> customers() {
    final String _sql = "SELECT CustomerID AS id, FirstName AS firstName, LastName AS lastName, PhoneNumber AS phone,\n"
            + "                  ShippingAddress AS address FROM Customers ORDER BY LastName, FirstName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfFirstName = 1;
      final int _cursorIndexOfLastName = 2;
      final int _cursorIndexOfPhone = 3;
      final int _cursorIndexOfAddress = 4;
      final List<Customer> _result = new ArrayList<Customer>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Customer _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpFirstName;
        _tmpFirstName = _cursor.getString(_cursorIndexOfFirstName);
        final String _tmpLastName;
        _tmpLastName = _cursor.getString(_cursorIndexOfLastName);
        final String _tmpPhone;
        _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item = new Customer(_tmpId,_tmpFirstName,_tmpLastName,_tmpPhone,_tmpAddress);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Supplier> suppliers() {
    final String _sql = "SELECT SupplierID AS id, CompanyName AS company, ContactPerson AS contact, PhoneNumber AS phone FROM Suppliers ORDER BY CompanyName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfCompany = 1;
      final int _cursorIndexOfContact = 2;
      final int _cursorIndexOfPhone = 3;
      final List<Supplier> _result = new ArrayList<Supplier>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Supplier _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpCompany;
        _tmpCompany = _cursor.getString(_cursorIndexOfCompany);
        final String _tmpContact;
        _tmpContact = _cursor.getString(_cursorIndexOfContact);
        final String _tmpPhone;
        _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
        _item = new Supplier(_tmpId,_tmpCompany,_tmpContact,_tmpPhone);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<OrderRow> orders() {
    final String _sql = "\n"
            + "    SELECT o.OrderID AS id, o.CustomerID AS customerId, c.LastName || ' ' || c.FirstName AS customer,\n"
            + "           o.StatusID AS statusId, s.Name AS status, o.OrderDate AS date,\n"
            + "           IFNULL((SELECT SUM(p.Price * i.Quantity) FROM OrderItems i\n"
            + "                   JOIN Products p ON p.ProductID = i.ProductID WHERE i.OrderID = o.OrderID), 0) AS total,\n"
            + "           IFNULL((SELECT SUM(i.Quantity) FROM OrderItems i WHERE i.OrderID = o.OrderID), 0) AS qty\n"
            + "    FROM Orders o\n"
            + "    JOIN Customers c ON c.CustomerID = o.CustomerID\n"
            + "    JOIN Statuses s ON s.StatusID = o.StatusID\n"
            + " ORDER BY o.OrderDate DESC, o.OrderID DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfCustomerId = 1;
      final int _cursorIndexOfCustomer = 2;
      final int _cursorIndexOfStatusId = 3;
      final int _cursorIndexOfStatus = 4;
      final int _cursorIndexOfDate = 5;
      final int _cursorIndexOfTotal = 6;
      final int _cursorIndexOfQty = 7;
      final List<OrderRow> _result = new ArrayList<OrderRow>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final OrderRow _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpCustomerId;
        _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
        final String _tmpCustomer;
        _tmpCustomer = _cursor.getString(_cursorIndexOfCustomer);
        final long _tmpStatusId;
        _tmpStatusId = _cursor.getLong(_cursorIndexOfStatusId);
        final String _tmpStatus;
        _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        final double _tmpTotal;
        _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
        final int _tmpQty;
        _tmpQty = _cursor.getInt(_cursorIndexOfQty);
        _item = new OrderRow(_tmpId,_tmpCustomerId,_tmpCustomer,_tmpStatusId,_tmpStatus,_tmpDate,_tmpTotal,_tmpQty);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<OrderRow> ordersBetween(final String dateFrom, final String dateTo) {
    final String _sql = "\n"
            + "    SELECT o.OrderID AS id, o.CustomerID AS customerId, c.LastName || ' ' || c.FirstName AS customer,\n"
            + "           o.StatusID AS statusId, s.Name AS status, o.OrderDate AS date,\n"
            + "           IFNULL((SELECT SUM(p.Price * i.Quantity) FROM OrderItems i\n"
            + "                   JOIN Products p ON p.ProductID = i.ProductID WHERE i.OrderID = o.OrderID), 0) AS total,\n"
            + "           IFNULL((SELECT SUM(i.Quantity) FROM OrderItems i WHERE i.OrderID = o.OrderID), 0) AS qty\n"
            + "    FROM Orders o\n"
            + "    JOIN Customers c ON c.CustomerID = o.CustomerID\n"
            + "    JOIN Statuses s ON s.StatusID = o.StatusID\n"
            + " WHERE o.OrderDate BETWEEN ? AND ? ORDER BY o.OrderDate DESC, o.OrderID DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, dateFrom);
    _argIndex = 2;
    _statement.bindString(_argIndex, dateTo);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfCustomerId = 1;
      final int _cursorIndexOfCustomer = 2;
      final int _cursorIndexOfStatusId = 3;
      final int _cursorIndexOfStatus = 4;
      final int _cursorIndexOfDate = 5;
      final int _cursorIndexOfTotal = 6;
      final int _cursorIndexOfQty = 7;
      final List<OrderRow> _result = new ArrayList<OrderRow>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final OrderRow _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpCustomerId;
        _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
        final String _tmpCustomer;
        _tmpCustomer = _cursor.getString(_cursorIndexOfCustomer);
        final long _tmpStatusId;
        _tmpStatusId = _cursor.getLong(_cursorIndexOfStatusId);
        final String _tmpStatus;
        _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        final double _tmpTotal;
        _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
        final int _tmpQty;
        _tmpQty = _cursor.getInt(_cursorIndexOfQty);
        _item = new OrderRow(_tmpId,_tmpCustomerId,_tmpCustomer,_tmpStatusId,_tmpStatus,_tmpDate,_tmpTotal,_tmpQty);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<OrderLine> orderLines(final long orderId) {
    final String _sql = "SELECT p.ProductID AS productId, p.Name AS product, p.Price AS price, i.Quantity AS qty\n"
            + "           FROM OrderItems i JOIN Products p ON p.ProductID = i.ProductID\n"
            + "           WHERE i.OrderID = ? ORDER BY p.Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProductId = 0;
      final int _cursorIndexOfProduct = 1;
      final int _cursorIndexOfPrice = 2;
      final int _cursorIndexOfQty = 3;
      final List<OrderLine> _result = new ArrayList<OrderLine>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final OrderLine _item;
        final long _tmpProductId;
        _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
        final String _tmpProduct;
        _tmpProduct = _cursor.getString(_cursorIndexOfProduct);
        final double _tmpPrice;
        _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
        final int _tmpQty;
        _tmpQty = _cursor.getInt(_cursorIndexOfQty);
        _item = new OrderLine(_tmpProductId,_tmpProduct,_tmpPrice,_tmpQty);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SupplyRow> supplies() {
    final String _sql = "SELECT s.SupplyID AS id, s.SupplyDate AS date, COUNT(i.SupplyItemID) AS positions,\n"
            + "                  IFNULL(SUM(i.Quantity), 0) AS qty\n"
            + "           FROM Supplies s LEFT JOIN SupplyItems i ON i.SupplyID = s.SupplyID\n"
            + "           GROUP BY s.SupplyID ORDER BY s.SupplyDate DESC, s.SupplyID DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfDate = 1;
      final int _cursorIndexOfPositions = 2;
      final int _cursorIndexOfQty = 3;
      final List<SupplyRow> _result = new ArrayList<SupplyRow>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SupplyRow _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        final int _tmpPositions;
        _tmpPositions = _cursor.getInt(_cursorIndexOfPositions);
        final int _tmpQty;
        _tmpQty = _cursor.getInt(_cursorIndexOfQty);
        _item = new SupplyRow(_tmpId,_tmpDate,_tmpPositions,_tmpQty);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<SupplyLine> supplyLines(final long supplyId) {
    final String _sql = "SELECT p.ProductID AS productId, p.Name AS product, sp.SupplierID AS supplierId,\n"
            + "                  sp.CompanyName AS supplier, i.Quantity AS qty\n"
            + "           FROM SupplyItems i\n"
            + "           JOIN Products p ON p.ProductID = i.ProductID\n"
            + "           JOIN Suppliers sp ON sp.SupplierID = i.SupplierID\n"
            + "           WHERE i.SupplyID = ? ORDER BY p.Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, supplyId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProductId = 0;
      final int _cursorIndexOfProduct = 1;
      final int _cursorIndexOfSupplierId = 2;
      final int _cursorIndexOfSupplier = 3;
      final int _cursorIndexOfQty = 4;
      final List<SupplyLine> _result = new ArrayList<SupplyLine>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final SupplyLine _item;
        final long _tmpProductId;
        _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
        final String _tmpProduct;
        _tmpProduct = _cursor.getString(_cursorIndexOfProduct);
        final long _tmpSupplierId;
        _tmpSupplierId = _cursor.getLong(_cursorIndexOfSupplierId);
        final String _tmpSupplier;
        _tmpSupplier = _cursor.getString(_cursorIndexOfSupplier);
        final int _tmpQty;
        _tmpQty = _cursor.getInt(_cursorIndexOfQty);
        _item = new SupplyLine(_tmpProductId,_tmpProduct,_tmpSupplierId,_tmpSupplier,_tmpQty);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<PopularRow> popularProducts() {
    final String _sql = "SELECT p.Name AS product, c.Name AS category, SUM(i.Quantity) AS qty, SUM(i.Quantity * p.Price) AS revenue\n"
            + "           FROM OrderItems i\n"
            + "           JOIN Products p ON p.ProductID = i.ProductID\n"
            + "           JOIN Categories c ON c.CategoryID = p.CategoryID\n"
            + "           GROUP BY p.ProductID ORDER BY SUM(i.Quantity) DESC, p.Name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfProduct = 0;
      final int _cursorIndexOfCategory = 1;
      final int _cursorIndexOfQty = 2;
      final int _cursorIndexOfRevenue = 3;
      final List<PopularRow> _result = new ArrayList<PopularRow>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PopularRow _item;
        final String _tmpProduct;
        _tmpProduct = _cursor.getString(_cursorIndexOfProduct);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        final int _tmpQty;
        _tmpQty = _cursor.getInt(_cursorIndexOfQty);
        final double _tmpRevenue;
        _tmpRevenue = _cursor.getDouble(_cursorIndexOfRevenue);
        _item = new PopularRow(_tmpProduct,_tmpCategory,_tmpQty,_tmpRevenue);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
