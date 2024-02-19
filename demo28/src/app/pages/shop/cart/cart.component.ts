import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { CartService } from 'src/app/@core/services/account/cart.service';
import { PRODUCT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';
import { CartDetail } from 'src/app/@core/models/cart/cart-detail.model';
import { Cart } from 'src/app/@core/models/cart/cart.model';
import { ToastrService } from 'ngx-toastr';
import { ProductService } from 'src/app/@core/services/product/product.service';
import { Coupon } from 'src/app/@core/models/coupon/coupon.model';

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

@Component({
  selector: 'shop-cart-page',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})

export class CartComponent implements OnDestroy {
  private subscriptions: Subscription[] = [];
  PRODUCT_IMAGE_DIRECTORY = PRODUCT_IMAGE_DIRECTORY
  shippingData = SHIPPING_DATA
  cart: Cart;

  appliedCoupon: Coupon;
  subTotalPrice = 0; // with products and coupon
  shippingCost = 0;
  totalPrice = 0; // products, coupon & shipping cost

  constructor(
    public cartService: CartService,
    public toastrService: ToastrService,
    public productService: ProductService
  ) { }

  ngOnInit() {
    this.subscriptions.push(
      this.cartService.cartChangeSubject.subscribe(() => {
        this.loadCartItems()
      })
    )
    this.loadCartItems()
  }

  loadCartItems() {
    this.subscriptions.push(
      this.cartService.findAll().subscribe(cart => {
        this.cart = cart
        this.subTotalPrice = this.cartService.getTotalPriceAndQty(this.cart).totalPrice
        this.calcTotalPrice()
      })
    );
  }

  calcTotalPrice() {
    if(this.appliedCoupon == null) {
      this.totalPrice = this.subTotalPrice + this.shippingCost
      return;
    }

    this.totalPrice = this.cartService
      .calcPriceAfterAppliedCoupon(this.subTotalPrice, this.appliedCoupon) + this.shippingCost;
  }

  ngOnDestroy() {
    this.subscriptions.forEach((subcr) => subcr.unsubscribe())
  }

  updateCart(event: any) {
    event.preventDefault()
    this.loadCartItems()
  }

  remove(cartDetail: CartDetail) {
    this.cartService.remove(cartDetail.product, cartDetail.productVariant).subscribe(result => {
      if(result) {
        this.toastrService.success("Product removed successfully!")
        this.cartService.cartChangeSubject.next()
        this.loadCartItems()
      } else {
        this.toastrService.error("Product removed failed! Some error happened")
      }
    })
  }

  onChangeQty(newQty: number, cartDetail: CartDetail) {
    document.querySelector('.btn-cart-update.disabled') &&
      document.querySelector('.btn-cart-update.disabled').classList.remove('disabled');

    this.cartService.addOrUpdateCartItem(cartDetail.product, cartDetail.productVariant, newQty).subscribe(
      result => {
        if(result) {
          cartDetail.quantity = newQty
          this.loadCartItems()
        }
      }
    )
  }

  changeShipping(shipping: Shipping) {
    this.cartService.appliedShipping = shipping
    this.shippingCost = shipping.cost
    this.calcTotalPrice()
  }

  applyCoupon(coupon: Coupon) {
    this.appliedCoupon = coupon
    this.calcTotalPrice()
  }
}
