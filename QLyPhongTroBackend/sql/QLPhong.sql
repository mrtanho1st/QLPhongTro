USE master;
GO
IF DB_ID('QuanLyPhongTro') IS NOT NULL
BEGIN
    ALTER DATABASE QuanLyPhongTro SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyPhongTro;
END
GO

CREATE DATABASE QuanLyPhongTro;
GO

USE QuanLyPhongTro;
GO

CREATE TABLE Districts
(
    DistrictId INT IDENTITY PRIMARY KEY,
    DistrictName NVARCHAR(50) NOT NULL UNIQUE
);
GO

CREATE TABLE TypeRooms
(
    TypeRoomId INT IDENTITY PRIMARY KEY,
    TypeRoomName NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Buildings
(
    BuildingId INT IDENTITY PRIMARY KEY,
    TrueAddress NVARCHAR(300) NOT NULL,
    FakeAddress NVARCHAR(300) NOT NULL,
    DistrictId INT,
    Note NVARCHAR(MAX),
    OwnerPhone NVARCHAR(20),
    Latitude DECIMAL(10,8),
    Longitude DECIMAL(11,8),
    FOREIGN KEY (DistrictId) REFERENCES Districts(DistrictId)
);
GO

CREATE TABLE Rooms
(
    RoomId INT IDENTITY PRIMARY KEY,
    BuildingId INT NOT NULL,
    TypeRoomId INT,
    RoomCode NVARCHAR(20),
    Price DECIMAL(12,0),
    Bedroom INT,
    PersonLimit INT,
    Area DECIMAL(5,2),
    Locked BIT,
    AvailableDate DATE NULL,
    Note NVARCHAR(MAX),
    FOREIGN KEY(BuildingId) REFERENCES Buildings(BuildingId),
    FOREIGN KEY(TypeRoomId) REFERENCES TypeRooms(TypeRoomId)
);
GO

CREATE TABLE BuildingFees
(
    FeeId INT IDENTITY PRIMARY KEY,
    BuildingId INT,
    ElectricityPrice DECIMAL(10,0),
    WaterPrice DECIMAL(10,0),
    ServiceFee DECIMAL(10,0),
    ParkingFee DECIMAL(10,0),
    OtherFee DECIMAL(10,0),
    FreeParking INT,
    FOREIGN KEY(BuildingId) REFERENCES Buildings(BuildingId)
);
GO

CREATE TABLE Amenities
(
    AmenityId INT IDENTITY PRIMARY KEY,
    Name NVARCHAR(100)
);
GO

CREATE TABLE RoomAmenities
(
    RoomId INT,
    AmenityId INT,
    PRIMARY KEY(RoomId, AmenityId),
    FOREIGN KEY(RoomId) REFERENCES Rooms(RoomId),
    FOREIGN KEY(AmenityId) REFERENCES Amenities(AmenityId)
);
GO

CREATE TABLE Commissions
(
    CommissionId INT IDENTITY PRIMARY KEY,
    BuildingId INT,
    ContractMonth INT,
    CommissionPercent DECIMAL(5,2),
    Deposit DECIMAL(3,1),
    FOREIGN KEY(BuildingId) REFERENCES Buildings(BuildingId)
);
GO

CREATE TABLE RoomMedia
(
    MediaId INT IDENTITY PRIMARY KEY,
    RoomId INT NOT NULL,
    MediaType TINYINT NOT NULL,   -- 1=Image, 2=Video
    Url NVARCHAR(1000) NOT NULL,
    SortOrder INT DEFAULT 0,
    FOREIGN KEY(RoomId) REFERENCES Rooms(RoomId)
);
GO

CREATE TABLE Landmarks
(
    LandmarkId INT IDENTITY PRIMARY KEY,

    Name NVARCHAR(200) NOT NULL,

    LandmarkTypesId INT NOT NULL,

    Address NVARCHAR(300),

    Latitude DECIMAL(10,8) NOT NULL,

    Longitude DECIMAL(11,8) NOT NULL,

    Description NVARCHAR(500),

    IsActive BIT DEFAULT 1

    FOREIGN KEY(LandmarkTypesId) REFERENCES LandmarkTypes(LandmarkTypesId)
);
GO

CREATE TABLE LandmarkTypes
(
    LandmarkTypesId INT IDENTITY PRIMARY KEY,
    Name NVARCHAR(100)
);
GO

CREATE TABLE SaleOffs
(
    SaleOffId INT IDENTITY(1,1) PRIMARY KEY,
	RoomId INT NOT NULL,
    SaleOffName NVARCHAR(100) NOT NULL,      -- Ví dụ: Khuyến mãi tháng 8
    DiscountAmount DECIMAL(18,2) NOT NULL,   -- Giảm theo số tiền
    StartDate DATETIME NOT NULL,
    EndDate DATETIME NOT NULL,
    IsActive BIT NOT NULL DEFAULT 1,         -- Bật/Tắt khuyến mãi
    FOREIGN KEY(RoomId) REFERENCES Rooms(RoomId)
);