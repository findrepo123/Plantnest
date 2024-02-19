import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from "@angular/router";
import { TranslateModule } from '@ngx-translate/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlModule } from 'angular-owl-carousel';
import { LazyLoadImageModule } from 'ng-lazyload-image';

// Header Element
import { CartMenuComponent } from './components/headers/shared/cart-menu/cart-menu.component';
import { WishlistMenuComponent } from './components/headers/shared/wishlist-menu/wishlist-menu.component';
import { CategoryMenuComponent } from './components/headers/shared/category-menu/category-menu.component';
import { MainMenuComponent } from './components/headers/shared/main-menu/main-menu.component';
import { HeaderSearchComponent } from './components/headers/shared/header-search/header-search.component';
import { MobileButtonComponent } from './components/headers/shared/mobile-button/mobile-button.component';
import { MobileMenuComponent } from './components/headers/shared/mobile-menu/mobile-menu.component';

// Header Component
import { HeaderComponent } from './components/headers/header/header.component';

// // Product Component
import { ProductNineComponent } from './components/product/product-nine/product-nine.component';
import { ProductTwelveComponent } from './components/product/product-twelve/product-twelve.component';

// Footer Component
import { FooterComponent } from './components/footer/footer.component';
// // Page Element
import { BreadcrumbComponent } from './components/breadcrumb/breadcrumb.component';
import { PageHeaderComponent } from './components/page-header/page-header.component';
import { PaginationComponent } from '../pages/shop/shared/pagination/pagination.component';
import { CardComponent } from './components/accordion/card/card.component';
import { AccordionComponent } from './components/accordion/accordion.component';

// Product Element
import { QuantityInputComponent } from './components/quantity-input/quantity-input.component';
import { CountDownComponent } from './components/count-down/count-down.component';
import { CountToComponent } from './components/count-to/count-to.component';

// // single use component
import { LoginModalComponent } from './components/modals/login-modal/login-modal.component';
import { IsotopeGridComponent } from './components/isotope-grid/isotope-grid.component';
import { ImageComponent } from './components/image/image.component';

// // Custom Directives
import { BgParallaxDirective } from './directives/bg-parallax.directive';
import { TabClickDirective } from './directives/custom-tab-click.directive';
import { ProductHoverDirective } from './directives/product-hover.directive';
import { ContentAnimDirective } from './directives/content-anim.directive';

// Pipes
import { CatFilterPipe } from './pipes/cat-filter.pipe';
import { AttrFilterPipe } from './pipes/attr-filter.pipe';
import { SafeContentPipe } from './pipes/safe-content.pipe';
import { CoreModule } from '../@core/core.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountComponent } from './components/headers/shared/account/account.component';
import { LoginFormComponent } from './components/modals/shared/login-form/login-form.component';
import { RegisterFormComponent } from './components/modals/shared/register-form/register-form.component';

const COMPONENTS = [
  // header
  CartMenuComponent,
  WishlistMenuComponent,
  CategoryMenuComponent,
  MainMenuComponent,
  HeaderSearchComponent,
  MobileButtonComponent,
  MobileMenuComponent,

  HeaderComponent,
  FooterComponent,

  // product
  ProductNineComponent,
  ProductTwelveComponent,

  // single-use components
  BreadcrumbComponent,
  PageHeaderComponent,

  LoginModalComponent,
  LoginFormComponent,
  RegisterFormComponent,

  QuantityInputComponent,
  CountDownComponent,
  AccordionComponent,
  CardComponent,
  PaginationComponent,
  IsotopeGridComponent,
  ImageComponent,

  // directives
  BgParallaxDirective,
  TabClickDirective,
  ProductHoverDirective,
  ContentAnimDirective,

  // pipes
  CatFilterPipe,
  AttrFilterPipe,
  SafeContentPipe,
  CountDownComponent,

  CountToComponent,
  AccountComponent
]

@NgModule({
	declarations: [
		...COMPONENTS
	],

	imports: [
		CommonModule,
		RouterModule,
		NgbModule,
		TranslateModule,
		OwlModule,
		LazyLoadImageModule,
    CoreModule,
    FormsModule,
    ReactiveFormsModule
	],

	exports: [
    ...COMPONENTS
	],
	entryComponents: [
		LoginModalComponent
	]
})

export class ThemeModule { }
