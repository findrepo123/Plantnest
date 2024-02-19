import { Image } from "../Image";
import { ProductSize } from "./product-size.model";

export class ProductVariant {
    productVariantId: number;
    productSize: ProductSize;
    height: number;
    width: number;
    quantity: number;
    price: number;
    image?: Image;
    imageUrl: string;
}
