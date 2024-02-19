import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SidebarPageComponent } from './sidebar/sidebar.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { CartComponent } from './cart/cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { AuthenticationGuard } from 'src/app/@core/guard/authentication.guard';

const routes: Routes = [
	{
		path: 'list',
		component: SidebarPageComponent
	},
	{
		path: 'list',
		pathMatch: 'full',
		redirectTo: 'list'
	},
	{
		path: 'wishlist',
		component: WishlistComponent,
    canActivate: [AuthenticationGuard]
	},
	{
		path: 'cart',
		component: CartComponent,
    canActivate: [AuthenticationGuard]
	},{
		path: 'checkout',
		component: CheckoutComponent,
    canActivate: [AuthenticationGuard]
	},

];

@NgModule( {
	imports: [ RouterModule.forChild( routes ) ],
	exports: [ RouterModule ]
} )



export class ShopRoutingModule { };
