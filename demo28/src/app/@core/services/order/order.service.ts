import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { map } from 'rxjs/operators';

import { Order } from '../../models/order/order.model';
import { BaseURLService } from '../base-url.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AuthenticationService } from '../account/authentication.service';
import { FilterOrderCriteria } from '../../models/filter-order-criteria';
import { GetDTOByPages } from '../../models/product/get-dto-by-pages.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private orderChangeSubject = new Subject<void>();

  get orderChange$(): Observable<void> {
    return this.orderChangeSubject.asObservable();
  }

  notifyOrderChange(): void {
    this.orderChangeSubject.next();
  }

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient,
    private authenService: AuthenticationService
  ) { }


  findById(id: number): Observable<Order> {
    const url: string = `${this.baseUrlService.baseURL}/checkout/findOrderById/${id}`
    return this.httpClient.get<Order>(url)
  }

  findByPages(filter: FilterOrderCriteria) {
    const loggedInAccount = this.authenService.getAccountFromLocalCache()
    const url = `${this.baseUrlService.baseURL}/checkout/findOrders`;

    const queryParams = new HttpParams()
      .set('accountId', loggedInAccount.id)
      .set('page', (filter.page > 0 ? filter.page-1 : filter.page).toString() ?? "")
      .set('pageSize', filter.pageSize.toString() ?? "")
      .set('searchTerm', filter.searchTerm ?? "")
      .set('orderBy', filter.orderBy ?? "");

    const urlWithParams = url + '?' + queryParams.toString();
    return this.httpClient.get<GetDTOByPages<Order>>(urlWithParams);
  }

  findAll(): Observable<Order[]> {
    const loggedInAccount = this.authenService.getAccountFromLocalCache()
    const url: string = `${this.baseUrlService.baseURL}/checkout/findOrders?accountId=${loggedInAccount.id}`
    return this.httpClient.get<Order[]>(url)
  }

  placeOrder(order: Order): Observable<Order> {
    const url: string = `${this.baseUrlService.baseURL}/checkout/placeOrder`
    return this.httpClient.post<Order>(url, order);
  }


}
