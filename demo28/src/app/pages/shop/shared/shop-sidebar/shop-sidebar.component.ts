import { Subscription } from 'rxjs';
import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlantingDifficultyLevel } from 'src/app/@core/models/product/planting-difficulty-level.model';
import { Catalog } from 'src/app/@core/models/product/catalog.model';
import { CatalogService } from 'src/app/@core/services/product/product-catalog.service';
import { PlantingDifficultyLevelService } from 'src/app/@core/services/product/planting-difficulty-level.service';
import { ProductSize } from 'src/app/@core/models/product/product-size.model';
import { ProductService } from 'src/app/@core/services/product/product.service';

@Component({
	selector: 'molla-shop-sidebar',
	templateUrl: './shop-sidebar.component.html',
	styleUrls: ['./shop-sidebar.component.scss']
})

export class ShopSidebarComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = []
	@Input() toggle = false;
	params = {};
  @Input() catalogs: Catalog[]
  @Input() levels: PlantingDifficultyLevel[]
  @Input() sizes: ProductSize[]

	constructor(
    public activeRoute: ActivatedRoute,
    public router: Router,
  ) {
	}

	ngOnInit(): void {

	}

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscr => subscr.unsubscribe())
  }

	containsAttrInUrl(type: string, value: string) {
		const currentQueries = this.params[type] ? this.params[type].split(',') : [];
		return currentQueries && currentQueries.includes(value);
	}

	getUrlForAttrs(type: string, value: string) {
		let currentQueries = this.params[type] ? this.params[type].split(',') : [];
		currentQueries = this.containsAttrInUrl(type, value) ? currentQueries.filter(item => item !== value) : [...currentQueries, value];
		return currentQueries.join(',');
	}
}
