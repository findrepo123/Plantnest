export class Shipping {
  shippingName: string;
  cost: number;
}

export const SHIPPING_DATA: Shipping[] =  [
  {
    shippingName: "Free Ship",
    cost: 0
  },
  {
    shippingName: "Standard",
    cost: 10
  },
  {
    shippingName: "Express",
    cost: 20
  }
]
