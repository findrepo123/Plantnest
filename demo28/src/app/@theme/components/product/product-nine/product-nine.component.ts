import { Subscription } from 'rxjs';
import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from 'src/app/@core/models/product/product.model';
import { WishlistService } from 'src/app/@core/services/account/wishlist.service';
import { ProductSale } from 'src/app/@core/models/sale/product-sale.model';
import { PRODUCT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';

@Component({
  selector: 'molla-product-nine',
  templateUrl: './product-nine.component.html',
  styleUrls: ['./product-nine.component.scss'],
})
export class ProductNineComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = []
  @Input() product: Product;
  PRODUCT_IMAGE_DIRECTORY: string = PRODUCT_IMAGE_DIRECTORY
  inWishlist: boolean = false;

  constructor(
    private router: Router,
    private wishlistService: WishlistService,
    private toastrService: ToastrService,
    private authenService: AuthenticationService
  ) {}

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscr => subscr.unsubscribe())
  }

  loadIsInWishlist() {
    if(this.authenService.isLoggedIn()) {
      this.subscriptions.push(
        this.wishlistService.isInWishlist(this.product).subscribe(result => {
          this.inWishlist = result
        })
      )
    }
  }

  addToWishlist(event: Event) {
    event.preventDefault();
    if(this.inWishlist) {
      this.router.navigateByUrl("/shop/wishlist")
      return
    }

    this.subscriptions.push(
      this.wishlistService.addToWishList(this.product).subscribe(
        (result: boolean) => {
          if (result) {
            this.inWishlist = result
            this.product.totalLikes += 1
            this.toastrService.success('Product added to Wishlist.');
            this.wishlistService.notifyWishlistChange()
          }
        },
        (error) => {
          console.error('Error while adding product to Wishlist:', error);
        }
      )
    )
  }

  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }
}
