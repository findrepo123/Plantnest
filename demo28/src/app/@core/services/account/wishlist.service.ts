import { Wishlist } from '../../models/account/wishlist.model';
import { Injectable } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';
import { Subject, BehaviorSubject, Observable, ReplaySubject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { Product } from '../../models/product/product.model';
import { HttpClient } from '@angular/common/http';
import { BaseURLService } from '../base-url.service';
import { AuthenticationService } from './authentication.service';
import { Account } from '../../models/account/account.model';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  wishlistQtyChangeSubject: Subject<number> = new ReplaySubject(1);

  notifyWishlistChange(value = null): void {
    this.wishlistQtyChangeSubject.next(value);
  }

  constructor(
    private toastrService: ToastrService,
    private httpClient: HttpClient,
    private baseUrlService: BaseURLService,
    private authenService: AuthenticationService
  ) { }

  findAll(): Observable<Product[]> {
    const loggedInAccount: Account = this.authenService.getAccountFromLocalCache()
    const url = `${this.baseUrlService.baseURL}/wishlists/findAll/${loggedInAccount.id}`;
    return this.httpClient.get<Product[]>(url);
  }

  countTotal(): Observable<number> {
    const loggedInAccount: Account = this.authenService.getAccountFromLocalCache()
    const url = `${this.baseUrlService.baseURL}/wishlists/count/${loggedInAccount.id}`;
    return this.httpClient.get<number>(url);
  }

  isInWishlist(product: Product): Observable<boolean> {
    const loggedInAccount: Account = this.authenService.getAccountFromLocalCache()
    const url = `${this.baseUrlService.baseURL}/wishlists/isExists?accountId=${loggedInAccount.id}&productId=${product.productId}`;
    return this.httpClient.get<boolean>(url);
  }

  addToWishList(product): Observable<boolean> | null {
    if (!this.authenService.isLoggedIn()) {
      this.toastrService.error("You have to log in to add this product to wishlist")
      return null;
    }

    const loggedInAccount: Account = this.authenService.getAccountFromLocalCache()
    const url = `${this.baseUrlService.baseURL}/wishlists/add?accountId=${loggedInAccount.id}&productId=${product.productId}`;
    return this.httpClient.get<boolean>(url)
  }

  removeFromWishList(product): Observable<boolean> {
    const loggedInAccount: Account = this.authenService.getAccountFromLocalCache()
    const url = `${this.baseUrlService.baseURL}/wishlists/remove?accountId=${loggedInAccount.id}&productId=${product.productId}`;
    return this.httpClient.get<boolean>(url)
  }

}
