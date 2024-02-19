export class CartRequest {

    constructor(
      public accountId: number,
      public productId: number,
      public productVariantId: number,
      public quantity?: number
    ) { }
}
