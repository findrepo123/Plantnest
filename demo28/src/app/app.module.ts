import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { OwlModule } from 'angular-owl-carousel';

// NGRX
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreRouterConnectingModule } from '@ngrx/router-store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

import { AppRoutingModule } from './app-routing.module';
import { PagesModule } from './pages/others/pages.module';
import { HomeModule } from './pages/home/home.module';

// reducers
import { appReducers, metaReducers } from './@core/reducers/app.reducer';
import { cartReducer } from './@core/reducers/cart.reducer';

import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { wishlistReducer } from './@core/reducers/wishlist.reducer';
import { LayoutComponent } from './@theme/layout/layout.component';
import { ThemeModule } from './@theme/theme.module';
import { DashboardModule } from './pages/dashboard/dashboard.module';

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    OwlModule,
    PagesModule,
    ThemeModule,
    HomeModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 2000,
      progressBar: false,
      enableHtml: true,
    }),
    StoreModule.forRoot(appReducers, { metaReducers }),
    StoreModule.forFeature('cart', cartReducer),
    StoreModule.forFeature('wishlist', wishlistReducer),
    EffectsModule.forRoot([]),
    StoreRouterConnectingModule.forRoot({ stateKey: 'router' }),
    StoreDevtoolsModule.instrument(),
    DashboardModule
  ],
  bootstrap: [AppComponent],
  providers: []
})

export class AppModule { }
