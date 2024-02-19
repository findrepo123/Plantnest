import { ProductVariant } from "../product/product-variant.model";
import { Product } from "../product/product.model";
import { Cart } from "./cart.model";

export class CartDetail {
    cartDetailId: number;
    product?: Product;
    productVariant?: ProductVariant
    quantity: number
}
