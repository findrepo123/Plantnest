import { filter } from 'rxjs/operators';
import { Component, OnInit, OnDestroy } from '@angular/core';

import { UtilsService } from 'src/app/@core/services/utils.service';

import { introSlider, brandSlider, serviceSlider, bannerSlider } from '../data';
import { ProductService } from 'src/app/@core/services/product/product.service';
import { Subscription } from 'rxjs'
@Component({
  selector: 'molla-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.scss'],
})
export class IndexComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = []
  topProducts = [];
  loadedTopProduct = false;

  saleProducts = [];
  loadedSaleProduct = false;

  introSlider = introSlider;
  brandSlider = brandSlider;
  serviceSlider = serviceSlider;
  bannerSlider = bannerSlider;

  constructor(
    public utilsService: UtilsService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.productService.find10SaleProduct().subscribe((result) => {
        this.saleProducts = result
        this.loadedSaleProduct = true
      })
    )

    this.subscriptions.push(
      this.productService.findTop10Products().subscribe((result) => {
        this.topProducts = result
        this.loadedTopProduct = true
      })
    )
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscr => subscr.unsubscribe())
  }
}
