import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlModule } from 'angular-owl-carousel';
import { NouisliderModule } from 'ng2-nouislider';

import { ShopRoutingModule } from './shop-routing.module';

import { SidebarPageComponent } from './sidebar/sidebar.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { ShopSidebarComponent } from './shared/shop-sidebar/shop-sidebar.component';
import { ShopListComponent } from './shared/shop-list/shop-list.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ThemeModule } from 'src/app/@theme/theme.module';
import { PaginationComponent } from './shared/pagination/pagination.component';
import { ApplyCouponComponent } from './shared/apply-coupon/apply-coupon.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaymentMethodsComponent } from './shared/payment-methods/payment-methods.component';
import { BillingInformationComponent } from './shared/billing-information/billing-information.component';

@NgModule( {
	declarations: [
		SidebarPageComponent,
		WishlistComponent,
		ShopSidebarComponent,
		ShopListComponent,
    ApplyCouponComponent,
    CartComponent,
	  CheckoutComponent,
    PaymentMethodsComponent,
    BillingInformationComponent
	],
	imports: [
		CommonModule,
		ThemeModule,
		ShopRoutingModule,
		RouterModule,
		NgbModule,
		OwlModule,
		NouisliderModule,
    FormsModule,
    ReactiveFormsModule
	]
} )

export class ShopModule { }
