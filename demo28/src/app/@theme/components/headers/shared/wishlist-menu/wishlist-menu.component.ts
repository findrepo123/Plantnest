import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';


@Component({
	selector: 'molla-wishlist-menu',
	templateUrl: './wishlist-menu.component.html',
	styleUrls: ['./wishlist-menu.component.scss']
})
export class WishlistMenuComponent implements OnInit {

  @Input() wishlistQty = null

	constructor(
    public authenService: AuthenticationService,
    private router: Router,
    private toastrService: ToastrService
  ) { }

	ngOnInit(): void {
	}

  loadWishlist() {
  }

  goToWishlist() {
    if(this.authenService.isLoggedIn()) {
      this.router.navigateByUrl("/shop/wishlist")
    } else {
      this.toastrService.error('You have to login to access this page');
    }
  }
}
