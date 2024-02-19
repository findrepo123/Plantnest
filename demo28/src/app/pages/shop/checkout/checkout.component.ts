import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { Cart } from 'src/app/@core/models/cart/cart.model';
import { Coupon } from 'src/app/@core/models/coupon/coupon.model';
import { CartService } from 'src/app/@core/services/account/cart.service';
import { Shipping } from '../shared/shipping-data';
import { ProductCouponService } from 'src/app/@core/services/product/product-coupon.service';
import { ProductService } from 'src/app/@core/services/product/product.service';
import { BillingInformationComponent } from '../shared/billing-information/billing-information.component';
import { OrderService } from 'src/app/@core/services/order/order.service';
import { Order } from 'src/app/@core/models/order/order.model';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { AddressService } from 'src/app/@core/services/account/address.service';
import { Address } from 'src/app/@core/models/address/address.model';
import { PaymentMethodsComponent } from '../shared/payment-methods/payment-methods.component';
import { PaymentMethod } from 'src/app/@core/models/order/payment-method.model';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
	selector: 'shop-checkout-page',
	templateUrl: './checkout.component.html',
	styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit, OnDestroy {
	private subscriptions: Subscription[] = [];

  @ViewChild(PaymentMethodsComponent) paymentMethodsCpn: PaymentMethodsComponent
  @ViewChild(BillingInformationComponent) billingInformationCpn: BillingInformationComponent
  cart: Cart

  subTotalPrice = 0; // with products and coupon
  shippingCost = 0;
  totalPrice = 0; // products, coupon & shipping cost

  appliedCoupon: Coupon;
  appliedShipping: Shipping

	constructor(
    public cartService: CartService,
    public couponService: ProductCouponService,
    public productService: ProductService,
    public orderService: OrderService,
    public authenService: AuthenticationService,
    public addressService: AddressService,
    public toastrService: ToastrService,
    public router: Router
  ) {
    this.subscriptions.push(
      this.couponService.appliedCouponChange.subscribe(() => {
        this.appliedCoupon = this.cartService.appliedCoupon
        this.calcTotalPrice()
      })
    )
  }


	ngOnInit(): void {
    this.subscriptions.push(
      this.cartService.findAll().subscribe(cart => {
        this.cart = cart;
        this.subTotalPrice = this.cartService.getTotalPriceAndQty(this.cart).totalPrice
        this.appliedCoupon = this.cartService.appliedCoupon
        this.appliedShipping = this.cartService.appliedShipping

        this.calcTotalPrice()
      })
    )

		document.querySelector('body').addEventListener("click", () => this.clearOpacity())
	}

	ngOnDestroy(): void {
		this.subscriptions.forEach(subscr => subscr.unsubscribe())
		document.querySelector('body').removeEventListener("click", () => this.clearOpacity())
	}

	clearOpacity() {
		let input: any = document.querySelector('#checkout-discount-input');
		if (input && input.value == "") {
			let label: any = document.querySelector('#checkout-discount-form label');
			label.removeAttribute('style');
		}
	}

	addOpacity(event: any) {
		event.target.parentElement.querySelector("label").setAttribute("style", "opacity: 0");
		event.stopPropagation();
	}

  calcTotalPrice() {
    if(this.appliedCoupon == null) {
      this.totalPrice = this.subTotalPrice + this.shippingCost
      return;
    }

    this.totalPrice = this.cartService
      .calcPriceAfterAppliedCoupon(this.subTotalPrice, this.appliedCoupon) + this.shippingCost;
  }

  placeOrder() {
    let formValue = this.billingInformationCpn.getFormValue()
    let paymentMethod: PaymentMethod = this.paymentMethodsCpn.getPaymentMethod()

    if(formValue == null || paymentMethod == null) {
      return;
    }

    let order: Order = this.mapFormValue(formValue, paymentMethod)

    this.subscriptions.push(
      this.orderService.placeOrder(order).subscribe(result => {
        if(result) {
          this.toastrService.success("Place order successfully! Your order are handling")
          this.cartService.resetCart()
          this.router.navigateByUrl('/')
        } else {
          this.toastrService.error("Place order failed! Please try again")
        }
      })
    )



  }

  mapFormValue(formValue: any, paymentMethod: PaymentMethod): Order {
    const loggedInAccount = this.authenService.getAccountFromLocalCache()
    let order: any = new Order()
    order.accountEmail = loggedInAccount.email
    if(formValue.addressOption == 'existing') {
      order.address = formValue.address
    } else {
      let newAddress = new Address()
      newAddress.roadName = formValue.roadName
      newAddress.ward = formValue.ward
      newAddress.district = formValue.district
      newAddress.province = formValue.province
      order.address = newAddress
    }
    order.coupon = this.cartService.appliedCoupon ?? null
    order.orderStatus = {orderStatusId: 2, statusName: 'Handling', description: 'The order was paid, and admin are handling for shipping'}
    order.paymentMethod = paymentMethod

    order.products = this.cart.cartDetails.map(cartDetail => {
      return {
        productId: cartDetail.product.productId,
        productName: cartDetail.product.productName,
        productVariant: cartDetail.productVariant,
        price: this.productService.calcPriceAfterSale(cartDetail.productVariant.price, cartDetail.product.productSale),
        quantity: cartDetail.quantity
      }
    })
    this.calcTotalPrice()
    order.totalPrice = this.totalPrice
    order.totalQuantity = this.cartService.getTotalPriceAndQty(this.cart).totalQuantity
    return order;
  }
}
