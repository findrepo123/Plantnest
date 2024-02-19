import { Component, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { ViewportScroller } from '@angular/common';
import { filter, first } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Store } from '@ngrx/store';

import { environment } from 'src/environments/environment';
import { StoreService } from './@core/store/store.service';
import { UtilsService } from './@core/services/utils.service';
import { RefreshStoreAction } from './@core/actions/actions';

declare var $: any;

@Component({
	selector: 'molla-root',
	templateUrl: './app.component.html',
})

export class AppComponent {

	constructor(
		public store: Store<any>,
		public router: Router,
		public viewPort: ViewportScroller,
		public storeService: StoreService,
		public utilsService: UtilsService,
		public modalService: NgbModal
	) {
		const navigationEnd = this.router.events.pipe(
			filter(event => event instanceof NavigationEnd)
		);

		navigationEnd.pipe(first()).subscribe(() => {
			document.querySelector('body')?.classList.add('loaded');
			var timer = setInterval(() => {
				if( window.getComputedStyle( document.querySelector('body') ).visibility == 'visible') {
					clearInterval(timer);
					$('.owl-carousel').trigger('refresh.owl.carousel');
				}
			}, 300);
		});

		navigationEnd.subscribe((event: any) => {
			if (!event.url.includes('/shop/sidebar') && !event.url.includes('/shop/nosidebar') && !event.url.includes('/shop/market') && !event.url.includes('/blog')) {
				this.viewPort.scrollToPosition([0, 0]);
			}

			if (event.url == '/') {
				document.querySelector('.header').classList.remove('position-relative');
			} else {
				document.querySelector('.header').classList.add('position-relative');
			}

			if (event.url.startsWith('/product') || (event.url.startsWith('/pages') && !event.url.includes('faq'))) {
				document.querySelector('.header').classList.remove('border-0');
			} else {
				document.querySelector('.header').classList.add('border-0');
			}

			this.modalService.dismissAll();
		})
	}

	@HostListener('window: scroll', ['$event'])
	onWindowScroll(e: Event) {
		this.utilsService.setStickyHeader();
	}

	hideMobileMenu() {
		document.querySelector('body').classList.remove('mmenu-active');
		document.querySelector('html').style.overflowX = 'unset';
	}
}
