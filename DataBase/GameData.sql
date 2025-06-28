-- Tạo bảng Level trước vì các bảng khác tham chiếu tới nó
CREATE TABLE Maze (
    LevelID INT PRIMARY KEY IDENTITY,
    MazeSeed INT,
    Width INT,
    Height INT
);

-- Tạo bảng Player
CREATE TABLE Player (
    PlayerID INT PRIMARY KEY IDENTITY,
    Name NVARCHAR(100),
    HP INT,
    Gold INT,
    PositionX INT,
    PositionY INT,
    CurrentLevelID INT,
    CONSTRAINT FK_Player_Level FOREIGN KEY (CurrentLevelID) REFERENCES Maze(LevelID)
);

-- Tạo bảng InventoryItem (vật phẩm trong túi đồ của người chơi)
CREATE TABLE InventoryItem (
    ItemID INT PRIMARY KEY IDENTITY,
    PlayerID INT,
    ItemType NVARCHAR(50), -- Key, Treasure, Weapon, Armor
    Name NVARCHAR(100),
    Value INT,
    Icon NVARCHAR(255),
    Quantity INT,
    CONSTRAINT FK_Inventory_Player FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID)
);

-- Tạo bảng Monster (quái vật)
CREATE TABLE Monster (
    MonsterID INT PRIMARY KEY IDENTITY,
    LevelID INT,
    HP INT,
    PositionX INT,
    PositionY INT,
    CONSTRAINT FK_Monster_Level FOREIGN KEY (LevelID) REFERENCES Maze(LevelID)
);

-- Tạo bảng LootTable (tỉ lệ rơi đồ khi quái chết)
CREATE TABLE LootTable (
    LootID INT PRIMARY KEY IDENTITY,
    MonsterID INT,
    ItemType NVARCHAR(50),
    Name NVARCHAR(100),
    DropRate FLOAT, -- 0.0 đến 1.0
    Value INT,
    Icon NVARCHAR(255),
    CONSTRAINT FK_Loot_Monster FOREIGN KEY (MonsterID) REFERENCES Monster(MonsterID)
);

-- Tạo bảng ItemOnMap (vật phẩm nằm trên bản đồ)
CREATE TABLE ItemOnMap (
    MapItemID INT PRIMARY KEY IDENTITY,
    LevelID INT,
    ItemType NVARCHAR(50),
    Name NVARCHAR(100),
    Value INT,
    Icon NVARCHAR(255),
    PositionX INT,
    PositionY INT,
    CONSTRAINT FK_MapItem_Level FOREIGN KEY (LevelID) REFERENCES Maze(LevelID)
);

-- Tạo bảng GameState (lưu trạng thái game)
CREATE TABLE GameState (
    GameStateID INT PRIMARY KEY IDENTITY,
    PlayerID INT,
    CurrentLevelID INT,
    MazeSeed INT,
    SaveTime DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_GameState_Player FOREIGN KEY (PlayerID) REFERENCES Player(PlayerID),
);