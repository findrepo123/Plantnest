import { PRODUCT_IMAGE_DIRECTORY } from './../../../../../@core/services/image-storing-directory';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from 'src/app/@core/models/product/product.model';
import { ProductSale } from 'src/app/@core/models/sale/product-sale.model';
import { ProductService } from 'src/app/@core/services/product/product.service';
import { UtilsService } from 'src/app/@core/services/utils.service';

@Component({
  selector: 'molla-header-search',
  templateUrl: './header-search.component.html',
  styleUrls: ['./header-search.component.scss']
})

export class HeaderSearchComponent implements OnInit, OnDestroy {

  searchTerm = "";
  suggestions = [];
  PRODUCT_IMAGE_DIRECTORY = PRODUCT_IMAGE_DIRECTORY

  constructor(
    public activeRoute: ActivatedRoute,
    public router: Router,
    public utilsService: UtilsService,
    public productService: ProductService,
  ) { }

  ngOnInit(): void {
    document.querySelector('body').addEventListener('click', this.closeSearchForm);
  }

  ngOnDestroy(): void {
    document.querySelector('body').removeEventListener('click', this.closeSearchForm);
  }

  searchProducts(event: any) {
    this.searchTerm = event.target.value;

    if (this.searchTerm.length >= 2) {
      this.productService.findByNameKeyword(this.searchTerm).subscribe(result => {
        this.suggestions = result.content
      })
    }
  }

  matchEmphasize(name: string) {
    var regExp = new RegExp(this.searchTerm, 'i');
    return name.replace(
      regExp,
      match => '<strong>' + match + '</strong>'
    );
  }

  goProductPage() {
    this.searchTerm = '';
    this.suggestions = [];
    var inputElement: any = document.querySelector('.header-search .form-control');
    inputElement.value = "";
  }

  onSearchToggle(e: Event) {
    e.stopPropagation();
    document
      .querySelector('.header .search-toggle')
      .classList.toggle('active');
    document
      .querySelector('.header .header-search-wrapper')
      .classList.toggle('show');
  }

  showSearchForm(e: Event) {
    document
      .querySelector('.header .header-search')
      .classList.add('show');
    e.stopPropagation();
  }

  closeSearchForm(e: any) {
    if (!e.target.closest('.header-search')) {
      document
        .querySelector('.header .search-toggle')
        .classList.remove('active');
      document
        .querySelector('.header .header-search-wrapper')
        .classList.remove('show');
    }
  }

  submitSearchForm(e: Event) {
    e.preventDefault();
    this.router.navigate(['/shop/list'], { queryParams: { searchTerm: this.searchTerm } });
  }

  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }
}
