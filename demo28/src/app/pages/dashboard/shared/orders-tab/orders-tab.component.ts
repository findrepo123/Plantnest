import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FilterOrderCriteria } from 'src/app/@core/models/filter-order-criteria';
import { Order } from 'src/app/@core/models/order/order.model';
import { OrderService } from 'src/app/@core/services/order/order.service';
import { ProductService } from 'src/app/@core/services/product/product.service';


@Component({
  selector: 'dashboard-orders-tab',
  templateUrl: './orders-tab.component.html',
})

export class OrdersTabComponent implements OnInit {
  private subscriptions: Subscription[] = []
  orders: Order[]
  loadedOrders: boolean = false
  selectedOrder: Order
  filters: FilterOrderCriteria = {
    page: 1,
    pageSize: 4,
    totalElements: null,
    searchTerm: null,
    orderBy: 'default'
  };

  searchFormGroup: FormGroup

  constructor(
    private orderService: OrderService,
    private productService: ProductService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
  ) {
    this.searchFormGroup = formBuilder.group({
      searchTerm: [''],
      sort: [null],
    })
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      this.filters.page = params['page'] ?? 1;
      this.filters.pageSize = params['pageSize'] ?? 4;
      this.filters.searchTerm = params['searchTerm'] ?? null;
      this.filters.orderBy = params['orderBy'] ?? 'default';

      this.loadOrders()
    })
  }

  loadOrders() {
    this.subscriptions.push(
      this.orderService.findByPages(this.filters).subscribe((result) => {
        this.loadedOrders = true
        this.orders = result.content;
        this.filters.totalElements = result.totalElements;
      })
    )
  }

  selectOrderDetail(orderId: number) {
    this.subscriptions.push(
      this.orderService.findById(orderId).subscribe(orderDetail => {
        this.selectedOrder = orderDetail
      })
    )
  }

  navigateProductDetailById(productId: number) {
    if (productId != null) {
      this.productService.findById(productId).subscribe(product => {
        this.router.navigateByUrl("/product/detail/" + product.slug)
      })
    }
  }

  searchOrder() {
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      fragment: 'orders',
      queryParams: {
        page: 1,
        searchTerm: this.searchFormGroup.get('searchTerm').value,
        orderBy: this.searchFormGroup.get('sort').value
      },
      queryParamsHandling: 'merge'
    });
  }

  reset(event: Event) {
    event.preventDefault()
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      fragment: 'orders',
    })

  }
}
