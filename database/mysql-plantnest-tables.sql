USE `railway`;
 
CREATE TABLE `Account`(
	`id` int AUTO_INCREMENT NOT NULL,
	`username` varchar(50) NOT NULL,
	`email` varchar(255) NOT NULL,
	`password` varchar(255) NOT NULL,
	`full_name` varchar(255) NOT NULL,
	`phone_number` varchar(20) NOT NULL,
	`image_id` int NULL,
	`active` Tinyint NULL,
	`role_id` int NOT NULL,
	`created_at` datetime(3) NULL DEFAULT now(3),
	`updated_at` datetime(3) NULL DEFAULT now(3),
PRIMARY KEY 
(
	`id` ASC
) ,
UNIQUE 
(
	`email` ASC
) ,
UNIQUE 
(
	`username` ASC
) 
);

CREATE TABLE `AccountAddress`(
	`address_id` int NOT NULL,
	`account_id` int NOT NULL,
PRIMARY KEY 
(
	`address_id` ASC,
	`account_id` ASC
) 
);

CREATE TABLE `AccountCoupon`(
	`coupon_id` int NOT NULL,
	`account_id` int NOT NULL,
	`is_used` Tinyint NOT NULL,
PRIMARY KEY 
(
	`coupon_id` ASC,
	`account_id` ASC
) 
);

CREATE TABLE `Address`(
	`address_id` int AUTO_INCREMENT NOT NULL,
	`road_name` varchar(255) NULL,
	`ward_code` varchar(20) NULL,
	`district_code` varchar(20) NULL,
	`province_code` varchar(20) NULL,
PRIMARY KEY 
(
	`address_id` ASC
) 
);

CREATE TABLE `AdministrativeUnit`(
	`id` int NOT NULL,
	`full_name` varchar(255) NULL,
	`full_name_en` varchar(255) NULL,
	`short_name` varchar(255) NULL,
	`short_name_en` varchar(255) NULL,
	`code_name` varchar(255) NULL,
	`code_name_en` varchar(255) NULL,
PRIMARY KEY 
(
	`id` ASC
) 
);

CREATE TABLE `Cart`(
	`cart_id` int NOT NULL,
	`created_at` datetime(3) NULL DEFAULT now(3),
	`updated_at` datetime(3) NULL DEFAULT now(3),
PRIMARY KEY 
(
	`cart_id` ASC
) 
);

CREATE TABLE `CartDetail`(
	`cart_detail_id` int AUTO_INCREMENT NOT NULL,
	`cart_id` int NOT NULL,
	`product_id` int NOT NULL,
	`product_variant_id` int NULL,
	`quantity` int NOT NULL,
PRIMARY KEY 
(
	`cart_detail_id` ASC
) 
);

CREATE TABLE `Catalog`(
	`catalog_id` int AUTO_INCREMENT NOT NULL,
	`catalog_name` varchar(100) NOT NULL,
	`description` varchar(500) NOT NULL,
	`image_id` int NULL,
	`catalog_parent_id` int NULL,
PRIMARY KEY 
(
	`catalog_id` ASC
) 
);

CREATE TABLE `Coupon`(
	`coupon_id` int AUTO_INCREMENT NOT NULL,
	`code` varchar(200) NOT NULL,
	`discount` int NOT NULL,
	`coupon_type_id` int NOT NULL,
	`description` varchar(200) NULL,
	`started_at` datetime(3) NULL DEFAULT now(3),
	`expired_at` datetime(3) NOT NULL,
PRIMARY KEY 
(
	`coupon_id` ASC
) 
);

CREATE TABLE `CouponType`(
	`coupon_type_id` int AUTO_INCREMENT NOT NULL,
	`type_name` varchar(50) NOT NULL,
PRIMARY KEY 
(
	`coupon_type_id` ASC
) 
);

CREATE TABLE `District`(
	`code` varchar(20) NOT NULL,
	`name` varchar(255) NOT NULL,
	`name_en` varchar(255) NULL,
	`full_name` varchar(255) NULL,
	`full_name_en` varchar(255) NULL,
	`code_name` varchar(255) NULL,
	`province_code` varchar(20) NULL,
	`administrative_unit_id` int NULL,
PRIMARY KEY 
(
	`code` ASC
) 
);

CREATE TABLE `Image`(
	`image_id` int AUTO_INCREMENT NOT NULL,
	`image_url` varchar(200) NOT NULL,
PRIMARY KEY 
(
	`image_id` ASC
) 
);

CREATE TABLE `Order`(
	`order_id` int AUTO_INCREMENT NOT NULL,
	`order_tracking_number` varchar(50) NULL,
	`account_id` int NULL,
	`account_email` varchar(255) NULL,
	`coupon_id` int NULL,
	`coupon_code` varchar(200) NULL,
	`total_price` decimal(18, 2) NOT NULL,
	`total_quantity` int NOT NULL,
	`order_status_id` int NOT NULL,
	`payment_method_id` int NOT NULL,
	`address` varchar(300) NULL,
	`created_at` datetime(3) NULL DEFAULT now(3),
	`updated_at` datetime(3) NULL DEFAULT now(3),
	`coupon_discount` varchar(50) NULL,
PRIMARY KEY 
(
	`order_id` ASC
) 
);

CREATE TABLE `OrderDetail`(
	`order_detail_id` int AUTO_INCREMENT NOT NULL,
	`order_id` int NOT NULL,
	`product_name` varchar(250) NULL,
	`product_id` int NULL,
	`quantity` int NOT NULL,
	`size` varchar(50) NULL,
	`price` decimal(18, 2) NOT NULL,
PRIMARY KEY 
(
	`order_detail_id` ASC
) 
);

CREATE TABLE `OrderStatus`(
	`order_status_id` int AUTO_INCREMENT NOT NULL,
	`status_name` varchar(50) NOT NULL,
	`description` varchar(100) NOT NULL,
PRIMARY KEY 
(
	`order_status_id` ASC
) 
);

CREATE TABLE `PaymentMethod`(
	`payment_method_id` int AUTO_INCREMENT NOT NULL,
	`method_name` varchar(100) NOT NULL,
PRIMARY KEY 
(
	`payment_method_id` ASC
) 
);

CREATE TABLE `PlantingDifficultyLevel`(
	`planting_difficulty_level_id` int AUTO_INCREMENT NOT NULL,
	`level` varchar(50) NOT NULL,
PRIMARY KEY 
(
	`planting_difficulty_level_id` ASC
) 
);

CREATE TABLE `Product`(
	`product_id` int AUTO_INCREMENT NOT NULL,
	`product_name` varchar(200) NOT NULL,
	`slug` varchar(200) NOT NULL,
	`description` varchar(1000) NULL,
	`active` Tinyint NULL DEFAULT 1,
	`sale` Tinyint NULL DEFAULT 0,
	`top` Tinyint NULL DEFAULT 0,
	`new` Tinyint NULL DEFAULT 1,
	`product_sale_id` int NULL,
	`image_size_guide_id` int NULL,
	`planting_difficulty_level_id` int NULL,
	`catalog_id` int NULL,
	`created_at` datetime(3) NULL DEFAULT now(3),
	`updated_at` datetime(3) NULL DEFAULT now(3),
PRIMARY KEY 
(
	`product_id` ASC
) ,
UNIQUE 
(
	`slug` ASC
) 
);

CREATE TABLE `ProductCareGuide`(
	`product_id` int AUTO_INCREMENT NOT NULL,
	`watering` varchar(500) NULL,
	`light` varchar(500) NULL,
	`nutrition` varchar(500) NULL,
	`cleaning` varchar(500) NULL,
	`pruning` varchar(500) NULL,
	`bugs` varchar(500) NULL,
	`trouble` varchar(500) NULL,
	`warning` varchar(500) NULL,
PRIMARY KEY 
(
	`product_id` ASC
) 
);

CREATE TABLE `ProductImage`(
	`product_id` int NOT NULL,
	`image_id` int NOT NULL,
PRIMARY KEY 
(
	`product_id` ASC,
	`image_id` ASC
) 
);

CREATE TABLE `ProductReview`(
	`product_review_id` int AUTO_INCREMENT NOT NULL,
	`account_id` int NOT NULL,
	`product_id` int NOT NULL,
	`content` varchar(1000) NOT NULL,
	`rating` int NOT NULL,
	`created_at` datetime(3) NULL DEFAULT now(3),
	`updated_at` datetime(3) NULL DEFAULT now(3),
PRIMARY KEY 
(
	`product_review_id` ASC
) 
);

CREATE TABLE `ProductSale`(
	`product_sale_id` int AUTO_INCREMENT NOT NULL,
	`sale_name` varchar(50) NOT NULL,
	`discount` int NOT NULL,
	`active` Tinyint NOT NULL,
	`product_sale_type_id` int NOT NULL,
	`description` varchar(200) NULL,
	`started_at` datetime(3) NULL DEFAULT now(3),
	`expired_at` datetime(3) NULL DEFAULT now(3),
PRIMARY KEY 
(
	`product_sale_id` ASC
) 
);

CREATE TABLE `ProductSaleType`(
	`product_sale_type_id` int AUTO_INCREMENT NOT NULL,
	`type_name` varchar(50) NULL,
PRIMARY KEY 
(
	`product_sale_type_id` ASC
) 
);

CREATE TABLE `ProductSize`(
	`product_size_id` int AUTO_INCREMENT NOT NULL,
	`size_name` varchar(50) NULL,
PRIMARY KEY 
(
	`product_size_id` ASC
) 
);

CREATE TABLE `ProductVariant`(
	`product_variant_id` int AUTO_INCREMENT NOT NULL,
	`product_id` int NOT NULL,
	`size_id` int NOT NULL,
	`height` int NULL,
	`width` int NULL,
	`quantity` int NOT NULL,
	`price` decimal(18, 2) NOT NULL,
	`image_id` int NULL,
PRIMARY KEY 
(
	`product_variant_id` ASC
) 
);

CREATE TABLE `Province`(
	`code` varchar(20) NOT NULL,
	`name` varchar(255) NOT NULL,
	`name_en` varchar(255) NULL,
	`full_name` varchar(255) NOT NULL,
	`full_name_en` varchar(255) NULL,
	`code_name` varchar(255) NULL,
	`administrative_unit_id` int NULL,
PRIMARY KEY 
(
	`code` ASC
) 
);

CREATE TABLE `Role`(
	`role_id` int AUTO_INCREMENT NOT NULL,
	`name` varchar(50) NOT NULL,
	`authorities` varbinary(255) NULL,
PRIMARY KEY 
(
	`role_id` ASC
) ,
UNIQUE 
(
	`name` ASC
) 
);

CREATE TABLE `Ward`(
	`code` varchar(20) NOT NULL,
	`name` varchar(255) NOT NULL,
	`name_en` varchar(255) NULL,
	`full_name` varchar(255) NULL,
	`full_name_en` varchar(255) NULL,
	`code_name` varchar(255) NULL,
	`district_code` varchar(20) NULL,
	`administrative_unit_id` int NULL,
PRIMARY KEY 
(
	`code` ASC
) 
);

CREATE TABLE `Wishlist`(
	`account_id` int NOT NULL,
	`product_id` int NOT NULL,
PRIMARY KEY 
(
	`account_id` ASC,
	`product_id` ASC
) 
);

CREATE TABLE `Contact`(
	`contact_id` int AUTO_INCREMENT NOT NULL,
	`full_name` varchar(20) NOT NULL,
	`email` varchar(255) NOT NULL,
	`phone_number` varchar(255) NULL,
	`subject` varchar(255) NULL,
	`message` varchar(200) NOT NULL,
	`created_at` datetime(3) NULL DEFAULT now(3),
PRIMARY KEY 
(
	`contact_id` ASC
) 
);




ALTER TABLE `Account` ADD  CONSTRAINT `account_ibfk_1` FOREIGN KEY(`role_id`)
REFERENCES `Role` (`role_id`);
  
ALTER TABLE `Account` ADD  CONSTRAINT `account_ibfk_2` FOREIGN KEY(`image_id`)
REFERENCES `Image` (`image_id`);
 
ALTER TABLE `AccountAddress` ADD  CONSTRAINT `account_address_ibfk_1` FOREIGN KEY(`address_id`)
REFERENCES `Address` (`address_id`);
 
 
ALTER TABLE `AccountAddress` ADD  CONSTRAINT `account_address_ibfk_2` FOREIGN KEY(`account_id`)
REFERENCES `Account` (`id`);
 
 
ALTER TABLE `AccountCoupon` ADD  CONSTRAINT `account_coupon_ibfk_1` FOREIGN KEY(`coupon_id`)
REFERENCES `Coupon` (`coupon_id`);
 
 
ALTER TABLE `AccountCoupon` ADD  CONSTRAINT `account_coupon_ibfk_2` FOREIGN KEY(`account_id`)
REFERENCES `Account` (`id`);
 
 
ALTER TABLE `Address` ADD  CONSTRAINT `address_ibfk_1` FOREIGN KEY(`ward_code`)
REFERENCES `Ward` (`code`);
 
 
ALTER TABLE `Address` ADD  CONSTRAINT `address_ibfk_2` FOREIGN KEY(`district_code`)
REFERENCES `District` (`code`);
 
 
ALTER TABLE `Address` ADD  CONSTRAINT `address_ibfk_3` FOREIGN KEY(`province_code`)
REFERENCES `Province` (`code`);
 
 
ALTER TABLE `Cart` ADD  CONSTRAINT `cart_ibfk_1` FOREIGN KEY(`cart_id`)
REFERENCES `Account` (`id`);
 
 
ALTER TABLE `CartDetail` ADD  CONSTRAINT `cart_detail_ibfk_1` FOREIGN KEY(`product_variant_id`)
REFERENCES `ProductVariant` (`product_variant_id`);
 
 
ALTER TABLE `CartDetail` ADD  CONSTRAINT `cart_detail_ibfk_2` FOREIGN KEY(`cart_id`)
REFERENCES `Cart` (`cart_id`);
 
 
ALTER TABLE `CartDetail` ADD  CONSTRAINT `cart_detail_ibfk_3` FOREIGN KEY(`product_id`)
REFERENCES `Product` (`product_id`);
 
 
ALTER TABLE `Catalog` ADD  CONSTRAINT `catalog_ibfk_1` FOREIGN KEY(`image_id`)
REFERENCES `Image` (`image_id`);
 
 
ALTER TABLE `Catalog` ADD  CONSTRAINT `catalog_ibfk_2` FOREIGN KEY(`catalog_parent_id`)
REFERENCES `Catalog` (`catalog_id`);
 
 
ALTER TABLE `Coupon` ADD  CONSTRAINT `coupon_ibfk_1` FOREIGN KEY(`coupon_type_id`)
REFERENCES `CouponType` (`coupon_type_id`);
 
 
ALTER TABLE `District` ADD  CONSTRAINT `districts_administrative_unit_id_fkey` FOREIGN KEY(`administrative_unit_id`)
REFERENCES `AdministrativeUnit` (`id`);
 
 
ALTER TABLE `District` ADD  CONSTRAINT `districts_province_code_fkey` FOREIGN KEY(`province_code`)
REFERENCES `Province` (`code`);
 
 
ALTER TABLE `Order` ADD  CONSTRAINT `order_ibfk_1` FOREIGN KEY(`payment_method_id`)
REFERENCES `PaymentMethod` (`payment_method_id`);
 
 
ALTER TABLE `Order` ADD  CONSTRAINT `order_ibfk_2` FOREIGN KEY(`account_id`)
REFERENCES `Account` (`id`);
 
 
ALTER TABLE `Order` ADD  CONSTRAINT `order_ibfk_3` FOREIGN KEY(`order_status_id`)
REFERENCES `OrderStatus` (`order_status_id`);
 
 
ALTER TABLE `Order` ADD  CONSTRAINT `order_ibfk_4` FOREIGN KEY(`coupon_id`)
REFERENCES `Coupon` (`coupon_id`);
 
 
ALTER TABLE `OrderDetail` ADD  CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY(`order_id`)
REFERENCES `Order` (`order_id`);
 
 
ALTER TABLE `OrderDetail` ADD  CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY(`product_id`)
REFERENCES `Product` (`product_id`);
 
 
ALTER TABLE `Product` ADD  CONSTRAINT `product_ibfk_1` FOREIGN KEY(`catalog_id`)
REFERENCES `Catalog` (`catalog_id`);
 
 
ALTER TABLE `Product` ADD  CONSTRAINT `product_ibfk_2` FOREIGN KEY(`image_size_guide_id`)
REFERENCES `Image` (`image_id`);
 
 
ALTER TABLE `Product` ADD  CONSTRAINT `product_ibfk_3` FOREIGN KEY(`product_id`)
REFERENCES `ProductCareGuide` (`product_id`);
 
 
ALTER TABLE `Product` ADD  CONSTRAINT `product_ibfk_4` FOREIGN KEY(`product_sale_id`)
REFERENCES `ProductSale` (`product_sale_id`);
 
 
ALTER TABLE `Product` ADD  CONSTRAINT `product_ibfk_5` FOREIGN KEY(`planting_difficulty_level_id`)
REFERENCES `PlantingDifficultyLevel` (`planting_difficulty_level_id`);
 
 
ALTER TABLE `ProductImage` ADD  CONSTRAINT `product_image_ibfk_1` FOREIGN KEY(`product_id`)
REFERENCES `Product` (`product_id`);
 
 
ALTER TABLE `ProductImage` ADD  CONSTRAINT `product_image_ibfk_2` FOREIGN KEY(`image_id`)
REFERENCES `Image` (`image_id`);
 
 
ALTER TABLE `ProductReview` ADD  CONSTRAINT `product_review_ibfk_1` FOREIGN KEY(`account_id`)
REFERENCES `Account` (`id`);
 
 
ALTER TABLE `ProductReview` ADD  CONSTRAINT `product_review_ibfk_2` FOREIGN KEY(`product_id`)
REFERENCES `Product` (`product_id`);
 
 
ALTER TABLE `ProductSale` ADD  CONSTRAINT `sale_ibfk_1` FOREIGN KEY(`product_sale_type_id`)
REFERENCES `ProductSaleType` (`product_sale_type_id`);
 
 
ALTER TABLE `ProductVariant` ADD  CONSTRAINT `product_variant_ibfk_1` FOREIGN KEY(`product_id`)
REFERENCES `Product` (`product_id`);
 
 
ALTER TABLE `ProductVariant` ADD  CONSTRAINT `product_variant_ibfk_3` FOREIGN KEY(`image_id`)
REFERENCES `Image` (`image_id`);
 
 
ALTER TABLE `ProductVariant` ADD  CONSTRAINT `product_variant_ibfk_4` FOREIGN KEY(`size_id`)
REFERENCES `ProductSize` (`product_size_id`);
 
 
ALTER TABLE `Province` ADD  CONSTRAINT `provinces_administrative_unit_id_fkey` FOREIGN KEY(`administrative_unit_id`)
REFERENCES `AdministrativeUnit` (`id`);
 
 
ALTER TABLE `Ward` ADD  CONSTRAINT `wards_administrative_unit_id_fkey` FOREIGN KEY(`administrative_unit_id`)
REFERENCES `AdministrativeUnit` (`id`);
 
 
ALTER TABLE `Ward` ADD  CONSTRAINT `wards_district_code_fkey` FOREIGN KEY(`district_code`)
REFERENCES `District` (`code`);
 
 
ALTER TABLE `Wishlist` ADD  CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY(`account_id`)
REFERENCES `Account` (`id`);
 
 
ALTER TABLE `Wishlist` ADD  CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY(`product_id`)
REFERENCES `Product` (`product_id`);
 
 
 
 
