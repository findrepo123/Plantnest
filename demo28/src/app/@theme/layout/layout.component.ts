import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { Router, NavigationStart, NavigationEnd, RouterOutlet } from '@angular/router';
import { Subscription } from 'rxjs';

import { routeAnimation } from '../data';
import { WishlistService } from 'src/app/@core/services/account/wishlist.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';

@Component({
	selector: 'molla-layout',
	templateUrl: './layout.component.html',
	styleUrls: ['./layout.component.scss'],
	animations: [
		routeAnimation
	]
})

export class LayoutComponent implements OnInit, OnDestroy {

	containerClass = 'container';
	isBottomSticky = false;
	current = "/";
  wishCount = 0
	private subscr: Subscription;

	constructor(
    private router: Router,
    public wishlistService: WishlistService,
    public authenService: AuthenticationService
  ) {
		this.subscr = this.router.events.subscribe(event => {
			if (event instanceof NavigationStart) {
				this.current = event.url;
				if (this.current.includes('fullwidth')) {
					this.containerClass = 'container-fluid';
				} else {
					this.containerClass = 'container';
				}

				if (this.current.includes('product/detail') && (window.innerWidth > 991)) {
					this.isBottomSticky = true;
				} else {
					this.isBottomSticky = false;
				}
			} else if (event instanceof NavigationEnd) {
				this.current = event.url;
				if (this.current.includes('fullwidth')) {
					this.containerClass = 'container-fluid';
				} else {
					this.containerClass = 'container';
				}

				if (this.current.includes('product/detail') && (window.innerWidth > 991)) {
					this.isBottomSticky = true;
				} else {
					this.isBottomSticky = false;
				}
			}
		});
	}

	ngOnInit(): void {
    this.wishlistService.wishlistQtyChangeSubject.subscribe(() => {
      this.loadWishlist()
    })
    this.loadWishlist()
	}

  loadWishlist() {
    if(this.authenService.isLoggedIn()) {
      this.wishlistService.countTotal().subscribe(qty => {
        this.wishCount = qty
      })
    }
  }

	ngOnDestroy(): void {
		this.subscr.unsubscribe();
	}

	@HostListener('window:resize', ['$event'])
	handleKeyDown(event: Event) {
		this.resizeHandle()
	}

	prepareRoute (outlet: RouterOutlet) {
		return outlet && outlet.isActivated && outlet.activatedRoute && outlet.activatedRoute.url;
	}

	resizeHandle() {
		if (this.current.includes('product/default') && window.innerWidth > 992)
			this.isBottomSticky = true;
		else
			this.isBottomSticky = false;
	}
}
