import { Account } from "../account/account.model";
import { CartDetail } from "./cart-detail.model";

export class Cart {
    cartId: number;
    account?: Account
    createdAt: Date;
    updatedAt: Date
    cartDetails: CartDetail[]
}
