import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { FilterCriteria } from 'src/app/@core/models/filter-criteria';
import { Catalog } from 'src/app/@core/models/product/catalog.model';
import { PlantingDifficultyLevel } from 'src/app/@core/models/product/planting-difficulty-level.model';
import { ProductSize } from 'src/app/@core/models/product/product-size.model';
import { Product } from 'src/app/@core/models/product/product.model';
import { PlantingDifficultyLevelService } from 'src/app/@core/services/product/planting-difficulty-level.service';
import { CatalogService } from 'src/app/@core/services/product/product-catalog.service';
import { ProductService } from 'src/app/@core/services/product/product.service';
import { UtilsService } from 'src/app/@core/services/utils.service';

@Component({
  selector: 'shop-sidebar-page',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarPageComponent implements OnInit {
  catalogs: Catalog[]
  levels: PlantingDifficultyLevel[]
  sizes: ProductSize[]
  products: Product[] = [];
  pageTitle = 'List';

  filters: FilterCriteria = {
    page: 1,
    pageSize: 10,
    totalElements: 0,
    searchTerm: null,
    catalog: null,
    size: null,
    level: null,
    orderBy: 'default'
  }
  toggle = false;
  loaded = false;
  filtersLoaded = false;

  constructor(
    public activeRoute: ActivatedRoute,
    public router: Router,
    public utilsService: UtilsService,
    public productService: ProductService,
    private catalogService: CatalogService,
    private levelService: PlantingDifficultyLevelService
  ) {
    this.activeRoute.queryParams.subscribe((params) => {
      this.loaded = false;

      this.filters.page = params['page'] ?? 1;
      this.filters.pageSize = params['pageSize'] ?? 10;
      this.filters.searchTerm = params['searchTerm'] ?? null;
      this.filters.catalog = params['catalog'] ?? null;
      this.filters.size = params['size'] ?? null;
      this.filters.level = params['level'] ?? null;
      this.filters.orderBy = params['orderBy'] ?? 'default';

      this.loadProductsAndFilters()
    });
  }

  ngOnInit(): void {
    if (window.innerWidth > 991) this.toggle = false;
    else this.toggle = true;
  }

  resetFilter() {

  }

  loadProductsAndFilters() {
    const catalog$ = this.catalogService.findAll()
    const level$ = this.levelService.findAll()
    const sizes$ = this.productService.findAllSizes()
    const productPage$ = this.productService.findByPages(this.filters);

    forkJoin([catalog$, level$, sizes$, productPage$]).subscribe(
      ([catalogData, levelData, sizesData, productPageData]) => {
        this.catalogs = this.catalogService.flattenCatalogs(catalogData)
        this.levels = levelData
        this.sizes = sizesData
        if (!this.filtersLoaded) {
          this.filtersLoaded = true;
        }

        this.products = productPageData.content
        this.filters.totalElements = productPageData.totalElements;
        this.loaded = true;
        this.utilsService.scrollToPageContent();
      })
  }

  @HostListener('window: resize', ['$event'])
  onResize(event: Event) {
    if (window.innerWidth > 991) this.toggle = false;
    else this.toggle = true;
  }

  changeOrderBy(event: any) {
    this.router.navigate([], {
      queryParams: { orderBy: event.currentTarget.value, page: 1 },
      queryParamsHandling: 'merge',
    });
  }

  toggleSidebar() {
    if (document.querySelector('body').classList.contains('sidebar-filter-active'))
      document.querySelector('body').classList.remove('sidebar-filter-active');
    else document.querySelector('body').classList.add('sidebar-filter-active');
  }

  hideSidebar() {
    document.querySelector('body').classList.remove('sidebar-filter-active');
  }
}
