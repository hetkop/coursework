package ru.tisbi.furniture.data

/**
 * SQL-описание базы данных: таблицы, индексы, триггеры и начальные данные.
 * Бизнес-правила целостности (контроль остатков, пересчёт склада) выполняются
 * триггерами на стороне СУБД, а не кодом приложения.
 */
object Schema {

    val tables = listOf(
        """CREATE TABLE Categories (
            CategoryID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL UNIQUE)""",
        """CREATE TABLE Materials (
            MaterialID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL UNIQUE)""",
        """CREATE TABLE Styles (
            StyleID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL UNIQUE)""",
        """CREATE TABLE Statuses (
            StatusID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL UNIQUE)""",
        """CREATE TABLE Customers (
            CustomerID INTEGER PRIMARY KEY AUTOINCREMENT,
            FirstName TEXT NOT NULL,
            LastName TEXT NOT NULL,
            PhoneNumber TEXT NOT NULL,
            ShippingAddress TEXT NOT NULL)""",
        """CREATE TABLE Suppliers (
            SupplierID INTEGER PRIMARY KEY AUTOINCREMENT,
            CompanyName TEXT NOT NULL,
            ContactPerson TEXT NOT NULL,
            PhoneNumber TEXT NOT NULL)""",
        """CREATE TABLE Products (
            ProductID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT NOT NULL,
            Description TEXT,
            Price REAL NOT NULL CHECK (Price > 0),
            InStock INTEGER NOT NULL DEFAULT 0 CHECK (InStock >= 0),
            CategoryID INTEGER NOT NULL REFERENCES Categories(CategoryID),
            MaterialID INTEGER NOT NULL REFERENCES Materials(MaterialID),
            StyleID INTEGER NOT NULL REFERENCES Styles(StyleID))""",
        """CREATE TABLE Orders (
            OrderID INTEGER PRIMARY KEY AUTOINCREMENT,
            CustomerID INTEGER NOT NULL REFERENCES Customers(CustomerID),
            StatusID INTEGER NOT NULL REFERENCES Statuses(StatusID),
            OrderDate TEXT NOT NULL)""",
        """CREATE TABLE OrderItems (
            OrderItemID INTEGER PRIMARY KEY AUTOINCREMENT,
            OrderID INTEGER NOT NULL REFERENCES Orders(OrderID) ON DELETE CASCADE,
            ProductID INTEGER NOT NULL REFERENCES Products(ProductID),
            Quantity INTEGER NOT NULL CHECK (Quantity > 0))""",
        """CREATE TABLE Supplies (
            SupplyID INTEGER PRIMARY KEY AUTOINCREMENT,
            SupplyDate TEXT NOT NULL)""",
        """CREATE TABLE SupplyItems (
            SupplyItemID INTEGER PRIMARY KEY AUTOINCREMENT,
            SupplyID INTEGER NOT NULL REFERENCES Supplies(SupplyID) ON DELETE CASCADE,
            ProductID INTEGER NOT NULL REFERENCES Products(ProductID),
            SupplierID INTEGER NOT NULL REFERENCES Suppliers(SupplierID),
            Quantity INTEGER NOT NULL CHECK (Quantity > 0))"""
    )

    val indexes = listOf(
        "CREATE INDEX idx_products_name ON Products(Name)",
        "CREATE INDEX idx_customers_lastname ON Customers(LastName)",
        "CREATE INDEX idx_orders_date ON Orders(OrderDate)",
        "CREATE INDEX idx_orderitems_order ON OrderItems(OrderID)",
        "CREATE INDEX idx_supplyitems_supply ON SupplyItems(SupplyID)"
    )

    val triggers = listOf(
        // Нельзя заказать больше, чем есть на складе
        """CREATE TRIGGER trg_orderitems_check_stock BEFORE INSERT ON OrderItems
        BEGIN
            SELECT RAISE(ABORT, 'Недостаточно товара на складе')
            WHERE (SELECT InStock FROM Products WHERE ProductID = NEW.ProductID) < NEW.Quantity;
        END""",
        // Списание со склада при добавлении позиции заказа
        """CREATE TRIGGER trg_orderitems_after_insert AFTER INSERT ON OrderItems
        BEGIN
            UPDATE Products SET InStock = InStock - NEW.Quantity WHERE ProductID = NEW.ProductID;
        END""",
        // Возврат на склад при удалении позиции заказа
        """CREATE TRIGGER trg_orderitems_after_delete AFTER DELETE ON OrderItems
        BEGIN
            UPDATE Products SET InStock = InStock + OLD.Quantity WHERE ProductID = OLD.ProductID;
        END""",
        // Оприходование товара при добавлении позиции поставки
        """CREATE TRIGGER trg_supplyitems_after_insert AFTER INSERT ON SupplyItems
        BEGIN
            UPDATE Products SET InStock = InStock + NEW.Quantity WHERE ProductID = NEW.ProductID;
        END""",
        // Нельзя удалить поставку, если товар из неё уже продан
        """CREATE TRIGGER trg_supplyitems_check_delete BEFORE DELETE ON SupplyItems
        BEGIN
            SELECT RAISE(ABORT, 'Нельзя удалить поставку: товар из неё уже продан')
            WHERE (SELECT InStock FROM Products WHERE ProductID = OLD.ProductID) < OLD.Quantity;
        END""",
        // Списание остатка при удалении позиции поставки
        """CREATE TRIGGER trg_supplyitems_after_delete AFTER DELETE ON SupplyItems
        BEGIN
            UPDATE Products SET InStock = InStock - OLD.Quantity WHERE ProductID = OLD.ProductID;
        END"""
    )

    val seed = listOf(
        """INSERT INTO Categories(Name) VALUES ('Диваны'),('Кресла'),('Столы'),('Стулья'),('Шкафы'),
            ('Кровати'),('Комоды'),('Тумбы'),('Стеллажи'),('Пуфы')""",
        """INSERT INTO Materials(Name) VALUES ('Дерево'),('Металл'),('Ткань'),('Кожа'),('Стекло'),
            ('ДСП'),('МДФ'),('Ротанг'),('Пластик'),('Массив дуба')""",
        """INSERT INTO Styles(Name) VALUES ('Классика'),('Модерн'),('Лофт'),('Скандинавский'),('Минимализм'),
            ('Прованс'),('Барокко'),('Хай-тек'),('Эко'),('Ар-деко')""",
        """INSERT INTO Statuses(Name) VALUES ('В ожидании'),('В обработке'),('Выполнен'),('Доставлен'),('Отменён')""",
        """INSERT INTO Customers(FirstName,LastName,PhoneNumber,ShippingAddress) VALUES
            ('Анна','Иванова','+7 (917) 234-56-78','г. Казань, ул. Баумана, д. 12, кв. 5'),
            ('Дмитрий','Петров','+7 (987) 123-45-67','г. Казань, пр. Победы, д. 45, кв. 112'),
            ('Елена','Сидорова','+7 (903) 555-12-34','г. Казань, ул. Чистопольская, д. 8, кв. 33'),
            ('Игорь','Кузнецов','+7 (927) 444-98-76','г. Казань, ул. Пушкина, д. 21, кв. 9'),
            ('Мария','Смирнова','+7 (960) 321-65-43','г. Казань, ул. Декабристов, д. 90, кв. 71'),
            ('Алексей','Попов','+7 (905) 777-33-22','г. Казань, ул. Файзи, д. 14, кв. 2'),
            ('Ольга','Васильева','+7 (919) 888-44-11','г. Казань, пр. Ямашева, д. 63, кв. 48'),
            ('Сергей','Михайлов','+7 (937) 222-10-90','г. Казань, ул. Кремлёвская, д. 7, кв. 15'),
            ('Наталья','Фёдорова','+7 (950) 666-77-88','г. Казань, ул. Ершова, д. 33, кв. 27'),
            ('Артём','Морозов','+7 (996) 101-20-30','г. Казань, ул. Гвардейская, д. 5, кв. 64')""",
        """INSERT INTO Suppliers(CompanyName,ContactPerson,PhoneNumber) VALUES
            ('ООО «МебельТорг»','Захаров Павел Иванович','+7 (843) 210-11-22'),
            ('ИП Гайнуллин','Гайнуллин Ренат Маратович','+7 (987) 654-32-10'),
            ('ООО «Диванный мир»','Белова Светлана Петровна','+7 (843) 233-44-55'),
            ('АО «Уральская фабрика мебели»','Орлов Андрей Викторович','+7 (343) 355-66-77'),
            ('ООО «Скандик Групп»','Николаев Денис Олегович','+7 (495) 120-30-40'),
            ('ООО «Дубовъ»','Ткачук Игорь Сергеевич','+7 (843) 290-80-70')""",
        """INSERT INTO Products(Name,Description,Price,InStock,CategoryID,MaterialID,StyleID) VALUES
            ('Диван «Комфорт»','Прямой трёхместный диван с механизмом «книжка»',45900,12,1,3,4),
            ('Диван «Лофт» угловой','Угловой диван на металлическом каркасе',68500,8,1,4,3),
            ('Кресло «Оскар»','Мягкое кресло с высокой спинкой',18700,15,2,3,2),
            ('Кресло-качалка «Эко»','Плетёное кресло-качалка из ротанга',14200,10,2,8,9),
            ('Стол обеденный «Дуб»','Раздвижной стол из массива дуба',32400,9,3,10,1),
            ('Стол журнальный «Стекло»','Журнальный столик со стеклянной столешницей',9800,14,3,5,8),
            ('Стул «Скандик»','Деревянный стул в скандинавском стиле',3900,40,4,1,4),
            ('Шкаф-купе «Модерн»','Двухстворчатый шкаф-купе с зеркалом',37800,6,5,7,2),
            ('Кровать «Прованс»','Двуспальная кровать 160×200 см',41500,7,6,1,6),
            ('Комод «Минимал»','Комод с четырьмя ящиками',12600,11,7,6,5),
            ('Тумба прикроватная «Классик»','Тумба с одним ящиком',5400,20,8,7,1),
            ('Стеллаж «Хай-тек»','Металлический стеллаж с пятью полками',8900,13,9,2,8)""",
        """INSERT INTO Supplies(SupplyDate) VALUES ('2026-01-15'),('2026-02-10'),('2026-03-05'),('2026-03-28'),
            ('2026-04-18'),('2026-05-12'),('2026-06-03'),('2026-07-09'),('2026-08-14'),('2026-09-21')""",
        """INSERT INTO SupplyItems(SupplyID,ProductID,SupplierID,Quantity) VALUES
            (1,1,3,10),(2,5,6,6),(3,7,5,30),(4,8,1,4),(5,3,3,8),
            (6,9,2,5),(7,12,1,10),(8,2,3,5),(9,6,2,8),(10,10,1,6)""",
        """INSERT INTO Orders(CustomerID,StatusID,OrderDate) VALUES
            (1,4,'2026-02-03'),(2,3,'2026-03-14'),(3,4,'2026-04-02'),(4,5,'2026-04-20'),(5,3,'2026-05-17'),
            (6,4,'2026-06-25'),(7,2,'2026-09-30'),(8,2,'2026-10-12'),(9,1,'2026-11-05'),(10,1,'2026-11-15')""",
        """INSERT INTO OrderItems(OrderID,ProductID,Quantity) VALUES
            (1,1,1),(1,7,4),(2,5,1),(2,7,6),(3,3,2),(4,9,1),(5,8,1),(5,10,1),
            (6,2,1),(6,6,1),(7,4,1),(8,12,2),(8,11,2),(9,1,1),(10,7,4)"""
    )
}
