import { ProductVariant } from "./product-variant.model";

export class Product {
  productId: number
  productName: string
  productVariant: ProductVariant
  price: number
  quantity: number
}
