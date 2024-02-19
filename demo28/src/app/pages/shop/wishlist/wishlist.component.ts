import { Component, OnInit, OnDestroy } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { Product } from 'src/app/@core/models/product/product.model';
import { ProductSale } from 'src/app/@core/models/sale/product-sale.model';
import { WishlistService } from 'src/app/@core/services/account/wishlist.service';
import { PRODUCT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';

import { environment } from 'src/environments/environment';

@Component({
  selector: 'shop-wishlist-page',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})

export class WishlistComponent implements OnInit, OnDestroy {

  wishlist: Product[] = [];
  PRODUCT_IMAGE_DIRECTORY = PRODUCT_IMAGE_DIRECTORY
  private subscr: Subscription;

  constructor(
    public wishlistService: WishlistService,
    private toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this.loadWishlist()
  }

  loadWishlist() {
    this.subscr = this.wishlistService.findAll().subscribe(items => {
      this.wishlist = items;
    });
  }

  ngOnDestroy(): void {
    this.subscr.unsubscribe();
  }

  remove(product) {
    this.wishlistService.removeFromWishList(product).subscribe(
      (result) => {
        if(result) {
          this.wishlistService.notifyWishlistChange()
          this.toastrService.success('Product was removed from wishlist.');
          this.loadWishlist()
        }
      },
      (error) => {
        console.error('Error while removing product from Wishlist:', error);
      }
    );
  }

  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }
}
