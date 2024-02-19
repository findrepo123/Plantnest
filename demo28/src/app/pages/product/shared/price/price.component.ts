import { Component, OnInit, HostListener, Input, SimpleChanges } from '@angular/core';
import { Product } from 'src/app/@core/models/product/product.model';
import { ProductSale } from 'src/app/@core/models/sale/product-sale.model';


@Component({
	selector: 'product-price',
	templateUrl: './price.component.html',
	styleUrls: ['./price.component.scss']
})

export class PriceComponent implements OnInit {

	@Input() product: Product;
  @Input() selectedVariant: any

  hasOnlyOnePrice: boolean = false;

	constructor() { }

	ngOnInit(): void {
	}

  ngOnChanges(changes: SimpleChanges): void {
    if(this.product != null) {
      this.hasOnlyOnePrice = this.product.minPrice == this.product.maxPrice
      if(this.selectedVariant != null) {

      }
    }
  }

  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }
}
