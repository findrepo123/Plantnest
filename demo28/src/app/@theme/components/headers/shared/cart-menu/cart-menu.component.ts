import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CartDetail } from 'src/app/@core/models/cart/cart-detail.model';
import { Cart } from 'src/app/@core/models/cart/cart.model';
import { ProductSale } from 'src/app/@core/models/sale/product-sale.model';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { CartService } from 'src/app/@core/services/account/cart.service';
import { PRODUCT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';

@Component({
  selector: 'molla-cart-menu',
  templateUrl: './cart-menu.component.html',
  styleUrls: ['./cart-menu.component.scss']
})

export class CartMenuComponent implements OnInit {
  PRODUCT_IMAGE_DIRECTORY = PRODUCT_IMAGE_DIRECTORY

  cart: Cart
  totalPrice = 0;
  totalQuantity = 0;

  constructor(
    public cartService: CartService,
    public authenService: AuthenticationService,
    private toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this.cartService.cartChangeSubject.subscribe((data) => {
      console.log(data);

      this.loadMenu()
    })
    this.loadMenu()
  }

  loadMenu() {
    if(this.authenService.isLoggedIn()){
      this.cartService.findAll().subscribe(cart => {
        this.cart = cart
        const totalPriceAndQty = this.cartService.getTotalPriceAndQty(cart);
        this.totalPrice = totalPriceAndQty.totalPrice;
        this.totalQuantity = totalPriceAndQty.totalQuantity
      })
    }
  }

  removeFromCart(event: Event, cartDetail: CartDetail) {
    event.preventDefault();


    this.cartService.remove(cartDetail.product, cartDetail.productVariant).subscribe(result => {
      if(result) {
        this.cartService.cartChangeSubject.next()
        this.toastrService.success("Remove product successfully!")
      }
    })
  }

  showErrorMessage() {
    if (!this.authenService.isLoggedIn()) {
      this.toastrService.error('You have to login to access this page');
    }
  }

  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale == null) return rootPrice;

    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }
}
