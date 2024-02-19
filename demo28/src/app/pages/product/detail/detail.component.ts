import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from 'src/app/@core/models/product/product.model';
import { PRODUCT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';
import { ProductService } from 'src/app/@core/services/product/product.service';

@Component({
  selector: 'product-detail-page',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
})
export class DetailPageComponent {
  PRODUCT_IMAGE_DIRECTORY = PRODUCT_IMAGE_DIRECTORY;
  product: Product;
  loaded = false;

  constructor(
    private activeRoute: ActivatedRoute,
    public router: Router,
    private productService: ProductService
  ) {
    activeRoute.params.subscribe((params) => {
      this.productService.findBySlug(params['slug']).subscribe((result) => {
        if (result === null) {
          this.router.navigate(['/pages/404']);
        }
        this.product = result;
        this.loaded = true;
      });
    })
  }
}

