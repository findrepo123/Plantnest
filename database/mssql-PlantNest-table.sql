DROP DATABASE IF EXISTS PlantNest
CREATE DATABASE PlantNest
GO

USE PlantNest
GO


-- ADDRESS 
DROP TABLE IF EXISTS [AdministrativeUnit];
CREATE TABLE [AdministrativeUnit] (
	[id] INT PRIMARY KEY, 
	[full_name] NVARCHAR(255), 
	[full_name_en] NVARCHAR(255), 
	[short_name] NVARCHAR(255), 
	[short_name_en] NVARCHAR(255), 
	[code_name] NVARCHAR(255), 
	[code_name_en] NVARCHAR(255), 
) 

DROP TABLE IF EXISTS [Province]
CREATE TABLE [Province] (
	[code] NVARCHAR(20) PRIMARY KEY,
	[name] NVARCHAR(255) NOT NULL,
	[name_en] NVARCHAR(255),
	[full_name] NVARCHAR(255) NOT NULL,
	[full_name_en] NVARCHAR(255),
	[code_name] NVARCHAR(255),
	[administrative_unit_id] INT,
	CONSTRAINT [provinces_administrative_unit_id_fkey] FOREIGN KEY ([administrative_unit_id]) REFERENCES [AdministrativeUnit] ([id])
) 

DROP TABLE IF EXISTS [District];
CREATE TABLE [District] (
	[code] NVARCHAR(20) PRIMARY KEY, 
	[name] NVARCHAR(255) NOT NULL, 
	[name_en] NVARCHAR(255), 
	[full_name] NVARCHAR(255), 
	[full_name_en] NVARCHAR(255), 
	[code_name] NVARCHAR(255), 
	[province_code] NVARCHAR(20), 
	[administrative_unit_id] INT, 
	CONSTRAINT [districts_administrative_unit_id_fkey] FOREIGN KEY ([administrative_unit_id]) REFERENCES [AdministrativeUnit] ([id]), 
	CONSTRAINT [districts_province_code_fkey] FOREIGN KEY ([province_code]) REFERENCES [Province] ([code])
) 

DROP TABLE IF EXISTS [Ward];
CREATE TABLE [Ward] (
	[code] NVARCHAR(20) PRIMARY KEY,
	[name] NVARCHAR(255) NOT NULL,
	[name_en] NVARCHAR(255),
	[full_name] NVARCHAR(255),
	[full_name_en] NVARCHAR(255),
	[code_name] NVARCHAR(255),
	[district_code] NVARCHAR(20),
	[administrative_unit_id] INT,
	CONSTRAINT [wards_administrative_unit_id_fkey] FOREIGN KEY ([administrative_unit_id]) REFERENCES[AdministrativeUnit] ([id]),
	CONSTRAINT [wards_district_code_fkey] FOREIGN KEY ([district_code]) REFERENCES [District] ([code])
)

DROP TABLE IF EXISTS [Address]
CREATE TABLE [Address] (
	[address_id] INT PRIMARY KEY IDENTITY,
	[road_name] NVARCHAR(255),
	[ward_code] NVARCHAR(20),
	[district_code] NVARCHAR(20),
	[province_code] NVARCHAR(20),
	CONSTRAINT [address_ibfk_1] FOREIGN KEY ([ward_code]) REFERENCES [Ward] ([code]),
	CONSTRAINT [address_ibfk_2] FOREIGN KEY ([district_code]) REFERENCES [District] ([code]),
	CONSTRAINT [address_ibfk_3] FOREIGN KEY ([province_code]) REFERENCES [Province] ([code])
)






-- COUPON
DROP TABLE IF EXISTS [CouponType];
CREATE TABLE [CouponType] (
	[coupon_type_id] INT PRIMARY KEY IDENTITY,
	[type_name] NVARCHAR(50) NOT NULL,
)

DROP TABLE IF EXISTS [Coupon];
CREATE TABLE [Coupon] (
	[coupon_id] INT PRIMARY KEY IDENTITY,
	[code] NVARCHAR(200) NOT NULL,
	[discount] INT NOT NULL,
	[coupon_type_id] INT NOT NULL,
	[description] NVARCHAR(200),
	[started_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[expired_at] DATETIME NOT NULL,
	CONSTRAINT [coupon_ibfk_1] FOREIGN KEY ([coupon_type_id]) REFERENCES [CouponType] ([coupon_type_id])
)



DROP TABLE IF EXISTS [Image];
CREATE TABLE [Image] (
	[image_id] INT  PRIMARY KEY IDENTITY,
	[image_url] NVARCHAR(200) NOT NULL,
)



-- ACCOUNT
DROP TABLE IF EXISTS [Role];
CREATE TABLE [Role] (
	[role_id] INT PRIMARY KEY IDENTITY,
	[authorities] NVARCHAR(50),
	[name] NVARCHAR(50) UNIQUE NOT NULL,
)


DROP TABLE IF EXISTS [Account];
CREATE TABLE [Account] (
	[id] INT PRIMARY KEY IDENTITY,
	[username] NVARCHAR(50) UNIQUE NOT NULL,
	[email] NVARCHAR(255) UNIQUE NOT NULL,
	[password] NVARCHAR(255) NOT NULL,
	[full_name] NVARCHAR(255) NOT NULL,
	[phone_number] NVARCHAR(20) NOT NULL,
	[image_id] INT,
	[active] BIT,
	[role_id] INT NOT NULL,
	[created_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[updated_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT [account_ibfk_1] FOREIGN KEY ([role_id]) REFERENCES [Role] ([role_id]),
	CONSTRAINT [account_ibfk_2] FOREIGN KEY ([image_id]) REFERENCES [Image] ([image_id])
)

DROP TABLE IF EXISTS [AccountAddress];
CREATE TABLE [AccountAddress] (
	[address_id] INT,
	[account_id] INT,
	PRIMARY KEY ([address_id], [account_id]),
	CONSTRAINT [account_address_ibfk_1] FOREIGN KEY ([address_id]) REFERENCES [Address] ([address_id]),
	CONSTRAINT [account_address_ibfk_2] FOREIGN KEY ([account_id]) REFERENCES [Account] ([id])
) 

DROP TABLE IF EXISTS [AccountCoupon];
CREATE TABLE [AccountCoupon] (
	[coupon_id] INT NOT NULL,
	[account_id] INT NOT NULL,
	[is_used] BIT NOT NULL,
	PRIMARY KEY  ([coupon_id],[account_id]),
	CONSTRAINT [account_coupon_ibfk_1] FOREIGN KEY ([coupon_id]) REFERENCES [Coupon] ([coupon_id]),
	CONSTRAINT [account_coupon_ibfk_2] FOREIGN KEY ([account_id]) REFERENCES [Account] ([id])
)














-- PRODUCT
DROP TABLE IF EXISTS [Catalog];
CREATE TABLE [Catalog] (
	[catalog_id] INT PRIMARY KEY IDENTITY,
	[catalog_name] NVARCHAR(100) NOT NULL,
	[description] NVARCHAR(500) NOT NULL,
	[image_id] INT,
	[catalog_parent_id] INT,
	CONSTRAINT [catalog_ibfk_1] FOREIGN KEY ([image_id]) REFERENCES [Image] ([image_id]),
	CONSTRAINT [catalog_ibfk_2] FOREIGN KEY ([catalog_parent_id]) REFERENCES [Catalog] ([catalog_id])
)


DROP TABLE IF EXISTS [ProductSaleType];
CREATE TABLE [ProductSaleType] (
	[product_sale_type_id] INT PRIMARY KEY IDENTITY,
	[type_name] NVARCHAR(50),
)

DROP TABLE IF EXISTS [ProductSale];
CREATE TABLE [ProductSale] (
	[product_sale_id] INT PRIMARY KEY IDENTITY,
	[sale_name] NVARCHAR(50) NOT NULL,
	[discount] INT NOT NULL,
	[active] BIT NOT NULL,
	[product_sale_type_id] INT NOT NULL,
	[description] NVARCHAR(200),
	[started_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[expired_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT [sale_ibfk_1] FOREIGN KEY ([product_sale_type_id]) REFERENCES [ProductSaleType] ([product_sale_type_id])
)

DROP TABLE IF EXISTS [ProductCareGuide]
CREATE TABLE [ProductCareGuide] (	
	[product_id] INT PRIMARY KEY IDENTITY,
	[watering] NVARCHAR(500),
	[light] NVARCHAR(500),
	[nutrition] NVARCHAR(500),
	[cleaning] NVARCHAR(500),
	[pruning] NVARCHAR(500),
	[bugs] NVARCHAR(500),
	[trouble] NVARCHAR(500),
	[warning] NVARCHAR(500),
)
GO

DROP TABLE IF EXISTS [PlantingDifficultyLevel]	
CREATE TABLE [PlantingDifficultyLevel] (
	[planting_difficulty_level_id] INT PRIMARY KEY IDENTITY,
	[level] NVARCHAR(50) NOT NULL,
)
GO



DROP TABLE IF EXISTS [Product];
CREATE TABLE [Product] (
	[product_id] INT PRIMARY KEY IDENTITY,
	[product_name] NVARCHAR(200) NOT NULL,
	[slug] NVARCHAR(200) UNIQUE NOT NULL,
	[description] NVARCHAR(1000),
	[active] BIT DEFAULT 1,
	[sale] BIT DEFAULT 0,
	[top] BIT DEFAULT 0,
	[new] BIT DEFAULT 1,
	[product_sale_id] INT,
	[image_size_guide_id] INT,
	[planting_difficulty_level_id] INT,
	[catalog_id] INT,
	[created_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[updated_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT [product_ibfk_1] FOREIGN KEY ([catalog_id]) REFERENCES [Catalog] ([catalog_id]),
	CONSTRAINT [product_ibfk_4] FOREIGN KEY ([product_sale_id]) REFERENCES [ProductSale] ([product_sale_id]),
	CONSTRAINT [product_ibfk_3] FOREIGN KEY ([product_id]) REFERENCES [ProductCareGuide] ([product_id]),
	CONSTRAINT [product_ibfk_2] FOREIGN KEY ([image_size_guide_id]) REFERENCES [Image] ([image_id]),
	CONSTRAINT [product_ibfk_5] FOREIGN KEY ([planting_difficulty_level_id]) REFERENCES [PlantingDifficultyLevel] ([planting_difficulty_level_id]),
) 




DROP TABLE IF EXISTS [ProductImage];
CREATE TABLE [ProductImage] (
	[product_id] INT NOT NULL,
	[image_id] INT NOT NULL,
	PRIMARY KEY ([product_id], [image_id]),
	CONSTRAINT [product_image_ibfk_1] FOREIGN KEY ([product_id]) REFERENCES [Product] ([product_id]),
	CONSTRAINT [product_image_ibfk_2] FOREIGN KEY ([image_id]) REFERENCES [Image] ([image_id])
)

DROP TABLE IF EXISTS [ProductReview];
CREATE TABLE [ProductReview] (
	[product_review_id] INT PRIMARY KEY IDENTITY,
	[account_id] INT NOT NULL,
	[product_id] INT NOT NULL,
	[content] NVARCHAR(1000) NOT NULL,
	[rating] INT NOT NULL,
	[created_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[updated_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT [product_review_ibfk_1] FOREIGN KEY ([account_id]) REFERENCES[Account] ([id]),
	CONSTRAINT [product_review_ibfk_2] FOREIGN KEY ([product_id]) REFERENCES[Product] ([product_id])
)


DROP TABLE IF EXISTS [ProductSize];
CREATE TABLE [ProductSize] (
	[product_size_id] INT PRIMARY KEY IDENTITY,
	[size_name] NVARCHAR(50)
)

DROP TABLE IF EXISTS [ProductVariant];
CREATE TABLE [ProductVariant] (
	[product_variant_id] INT PRIMARY KEY IDENTITY,
	[product_id] INT NOT NULL,
	[size_id] INT NOT NULL,
	[height] INT,
	[width] INT,
	[quantity] INT NOT NULL,
	[price] decimal(18,2) NOT NULL,
	[image_id] INT,
	CONSTRAINT [product_variant_ibfk_1] FOREIGN KEY ([product_id]) REFERENCES [Product] ([product_id]),
	CONSTRAINT [product_variant_ibfk_3] FOREIGN KEY ([image_id]) REFERENCES [Image] ([image_id]),
	CONSTRAINT [product_variant_ibfk_4] FOREIGN KEY ([size_id]) REFERENCES [ProductSize] ([product_size_id])
)


DROP TABLE IF EXISTS [Wishlist];
CREATE TABLE [Wishlist] (
	[account_id] INT NOT NULL,
	[product_id] INT NOT NULL,
	PRIMARY KEY ([account_id], [product_id]),
	CONSTRAINT [wishlist_ibfk_1] FOREIGN KEY ([account_id]) REFERENCES [Account] ([id]),
	CONSTRAINT [wishlist_ibfk_2] FOREIGN KEY ([product_id]) REFERENCES [Product] ([product_id])
) 

-- CART
DROP TABLE IF EXISTS [Cart];
CREATE TABLE [Cart] (
	[cart_id] INT PRIMARY KEY,
	[created_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[updated_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT [cart_ibfk_1] FOREIGN KEY ([cart_id]) REFERENCES [Account] ([id])
) 

DROP TABLE IF EXISTS [CartDetail];
CREATE TABLE [CartDetail] (
	[cart_detail_id] INT PRIMARY KEY IDENTITY,
	[cart_id] INT NOT NULL,
	[product_id] INT NOT NULL,
	[product_variant_id] INT,
	[quantity] INT NOT NULL,
	CONSTRAINT [cart_detail_ibfk_1] FOREIGN KEY ([product_variant_id]) REFERENCES [ProductVariant] ([product_variant_id]),
	CONSTRAINT [cart_detail_ibfk_2] FOREIGN KEY ([cart_id]) REFERENCES [Cart] ([cart_id]),
	CONSTRAINT [cart_detail_ibfk_3] FOREIGN KEY ([product_id]) REFERENCES [Product] ([product_id])
)


-- ORDER
DROP TABLE IF EXISTS [PaymentMethod];
CREATE TABLE [PaymentMethod] (
	[payment_method_id] INT PRIMARY KEY IDENTITY,
	[method_name] NVARCHAR(100) NOT NULL,
)

DROP TABLE IF EXISTS [OrderStatus];
CREATE TABLE [OrderStatus] (
	[order_status_id] INT PRIMARY KEY IDENTITY,
	[status_name] NVARCHAR(50) NOT NULL,
	[description] NVARCHAR(100) NOT NULL
)

DROP TABLE IF EXISTS [Order];
CREATE TABLE [Order] (
	[order_id] INT PRIMARY KEY IDENTITY,
	[order_tracking_number] NVARCHAR(50),
	[account_id] INT,
	[account_email] NVARCHAR(255),
	[coupon_id] INT,
	[coupon_code] NVARCHAR(200),
	[coupon_discount] NVARCHAR(50),
	[total_price] decimal(18,2) NOT NULL,
	[total_quantity] INT NOT NULL,
	[order_status_id] INT NOT NULL,
	[payment_method_id] INT NOT NULL,
	[address] NVARCHAR(300),
	[created_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	[updated_at] DATETIME DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT [order_ibfk_1] FOREIGN KEY ([payment_method_id]) REFERENCES [PaymentMethod] ([payment_method_id]),
	CONSTRAINT [order_ibfk_2] FOREIGN KEY ([account_id]) REFERENCES [Account] ([id]),
	CONSTRAINT [order_ibfk_3] FOREIGN KEY ([order_status_id]) REFERENCES [OrderStatus] ([order_status_id]),
	CONSTRAINT [order_ibfk_4] FOREIGN KEY ([coupon_id]) REFERENCES [Coupon] ([coupon_id]),
) 

DROP TABLE IF EXISTS [OrderDetail];
CREATE TABLE [OrderDetail] (
	[order_detail_id] INT PRIMARY KEY IDENTITY,
	[order_id] INT NOT NULL,
	[product_name] NVARCHAR(250),
	[product_id] INT,
	[quantity] INT NOT NULL,
	[size] NVARCHAR(50),
	[price] decimal(18,2) NOT NULL,
	CONSTRAINT [order_detail_ibfk_1] FOREIGN KEY ([order_id]) REFERENCES [Order] ([order_id]),
	CONSTRAINT [order_detail_ibfk_2] FOREIGN KEY ([product_id]) REFERENCES [Product] ([product_id])
) 
