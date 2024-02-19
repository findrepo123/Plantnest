import { takeUntil } from 'rxjs/operators';
import { Subject, Subscription } from 'rxjs';
import { Component, OnInit, Input, NgZone, OnDestroy} from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ProductVariant } from 'src/app/@core/models/product/product-variant.model';
import { Product } from 'src/app/@core/models/product/product.model';
import { ProductSale } from 'src/app/@core/models/sale/product-sale.model';
import { CartService } from 'src/app/@core/services/account/cart.service';
import { WishlistService } from 'src/app/@core/services/account/wishlist.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';

@Component({
	selector: 'product-detail-informations',
	templateUrl: './detail-informations.component.html',
	styleUrls: ['./detail-informations.component.scss']
})

export class DetailInformationsComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = []
  private destroy$ = new Subject<void>();

	@Input() product: Product;

	selectedVariant: ProductVariant
  inWishlist: boolean = false;
  maxQuantity = 1
	qty = 1;

	constructor(
		public cartService: CartService,
		public wishlistService: WishlistService,
    private toastrService: ToastrService,
		public router: Router,
    private ngZone: NgZone,
    private authenService: AuthenticationService
    )
  { }

  ngOnInit() {
    this.subscriptions.push(
      this.authenService.authChange$
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.loadIsInWishlist();
        })
    );
    this.loadIsInWishlist();
  }

  loadIsInWishlist() {
    if (this.authenService.isLoggedIn()) {
      this.subscriptions.push(
        this.wishlistService.isInWishlist(this.product)
          .pipe(takeUntil(this.destroy$))
          .subscribe((result) => {
            this.inWishlist = result;
          })
      );
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.subscriptions.forEach(subscr => subscr.unsubscribe())
  }

	addCart(event: Event) {
		event.preventDefault();
		if ((event.currentTarget as HTMLElement).classList.contains('btn-disabled')) return;

    this.subscriptions.push(
      this.cartService.addOrUpdateCartItem(this.product, this.selectedVariant, this.qty).subscribe(
        result => {
          if(result) {
            this.toastrService.success("Add product to cart successfully!")
            console.log("Before emitting value");
            this.ngZone.run(() => {
              this.cartService.cartChangeSubject.next();
            });
            console.log("After emitting value");
          } else {
            this.toastrService.error("Some errors happened, please try again")
          }
        },
        error => console.log(error)
      )
    )
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
            this.wishlistService.notifyWishlistChange()
            this.inWishlist = result
            this.product.totalLikes += 1
            this.toastrService.success('Product added to Wishlist.');
          }
        },
        (error) => {
          console.error('Error while adding product to Wishlist:', error);
        }
      )
    )
  }

	selectVariant(event) {
    if(event != null) {
      this.maxQuantity = event.quantity
    }
  }

	onChangeQty(current: number) {
		this.qty = current;
	}

  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }
}
