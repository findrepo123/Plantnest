import { PRODUCT_IMAGE_DIRECTORY, VARIANT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';
import { Component, OnInit, Input } from '@angular/core';
import { Lightbox } from 'ngx-lightbox';

import { sliderOpt } from 'src/app/@theme/data';
import { Product } from 'src/app/@core/models/product/product.model';


@Component( {
	selector: 'product-gallery',
	templateUrl: './gallery.component.html',
	styleUrls: [ './gallery.component.scss' ]
} )

export class GalleryComponent implements OnInit {
  PRODUCT_IMAGE_DIRECTORY = PRODUCT_IMAGE_DIRECTORY;
  VARIANT_IMAGE_DIRECTORY = VARIANT_IMAGE_DIRECTORY

	@Input() product: Product;
	@Input() loaded = false;
  album = [];

	options = {
		...sliderOpt,
		nav: true,
		dots: false,
		items: 3,
		margin: 20,
		loop: false,
		responsive: {
			0: {
				items: 1
			},
			576: {
				items: 2
			},
			992: {
				items: 3
			}
		}
	};


	constructor( public lightBox: Lightbox ) { }

	ngOnInit (): void {
	}

  ngOnChanges() {
    this.album = this.product.imageUrls.map(url => {
      return {
        src: this.PRODUCT_IMAGE_DIRECTORY + url,
        thumb: this.PRODUCT_IMAGE_DIRECTORY + url,
        caption: this.product.productName,
      }
    })
    this.product.productVariants.forEach(variant => {
      this.album.push({
        src: this.VARIANT_IMAGE_DIRECTORY + variant.imageUrl,
        thumb: this.VARIANT_IMAGE_DIRECTORY + variant.imageUrl,
        caption: "Variant Size: " + variant.productSize.sizeName,
      })
    })
  }
}
