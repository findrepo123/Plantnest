import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LightboxModule } from 'ngx-lightbox';
import { OwlModule } from 'angular-owl-carousel';

import { ProductRoutingModule } from './product-routing.module';

import { DetailPageComponent } from './detail/detail.component';


import { InfoTabsComponent } from './shared/info-tabs/info-tabs.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ThemeModule } from 'src/app/@theme/theme.module';
import { PriceComponent } from './shared/price/price.component';
import { DetailInformationsComponent } from './shared/detail-informations/detail-informations.component';
import { GalleryComponent } from './shared/gallery/gallery.component';

@NgModule({
  declarations: [
    DetailPageComponent,
    PriceComponent,

    GalleryComponent,
    DetailInformationsComponent,
    InfoTabsComponent,
  ],

  imports: [
    CommonModule,
    ProductRoutingModule,
    ThemeModule,
    RouterModule,
    NgbModule,
    OwlModule,
    LightboxModule,
    FormsModule,
    ReactiveFormsModule
  ],

  exports: [],

  providers: [NgbModal],
})
export class ProductModule {}
