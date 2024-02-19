import { map } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { HttpParams } from '@angular/common/http';

import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';
import { Product } from '../../models/product/product.model';
import { ProductSize } from '../../models/product/product-size.model';
import { GetDTOByPages } from '../../models/product/get-dto-by-pages.model';
import { FilterCriteria } from '../../models/filter-criteria';
import { ProductSale } from '../../models/sale/product-sale.model';
import { ProductReview } from '../../models/product/product-review.model';

export class ToastState {
  bahavior: String;
  model: string;
  status: string;
}
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private productChangeSubject = new Subject<void>();

  // Getter for the subject as an observable
  get productChange$(): Observable<void> {
    return this.productChangeSubject.asObservable();
  }

  // Call this method whenever a change occurs in the product list
  notifyProductChange(): void {
    this.productChangeSubject.next();
  }

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient
  ) {}

  findByNameKeyword(searchTerm: string): Observable<GetDTOByPages<Product>> {
    const url = `${this.baseUrlService.baseURL}/products/findByPages?searchTerm=${searchTerm}&page=0&pageSize=5`;
    return this.httpClient.get<GetDTOByPages<Product>>(url);
  }

  findAll(): Observable<Product[]> {
    const url = `${this.baseUrlService.baseURL}/products/findAll`;
    return this.httpClient.get<Product[]>(url);
  }

  findByPages(filter: FilterCriteria): Observable<GetDTOByPages<Product>> {
    const url = `${this.baseUrlService.baseURL}/products/findByPages`;

    const queryParams = new HttpParams()
      .set('page', (filter.page > 0 ? filter.page-1 : filter.page).toString() ?? "")
      .set('pageSize', filter.pageSize.toString() ?? "")
      .set('searchTerm', filter.searchTerm ?? "")
      .set('level', filter.level ?? "")
      .set('size', filter.size ?? "")
      .set('catalog', filter.catalog ?? "")
      .set('orderBy', filter.orderBy ?? "");

    const urlWithParams = url + '?' + queryParams.toString();
    return this.httpClient.get<GetDTOByPages<Product>>(urlWithParams);
  }

  findAllSizes(): Observable<ProductSize[]> {
    const url = `${this.baseUrlService.baseURL}/products/findAllSizes`;
    return this.httpClient.get<ProductSize[]>(url);
  }

  find10SaleProduct(): Observable<Product[]> {
    const url = `${this.baseUrlService.baseURL}/products/find10SaleProduct`;
    return this.httpClient.get<Product[]>(url);
  }

  findTop10Products(): Observable<Product[]> {
    const url = `${this.baseUrlService.baseURL}/products/findTop10Product`;
    return this.httpClient.get<Product[]>(url);
  }

  findById(id: number): Observable<Product> {
    const url: string = `${this.baseUrlService.baseURL}/products/${id}`;
    return this.httpClient.get<Product>(url);
  }

  findBySlug(slug: string): Observable<Product> {
    const url: string = `${this.baseUrlService.baseURL}/products/findBySlug?slug=${slug}`;
    return this.httpClient.get<Product>(url);
  }

  countTotalComments(id: number): Observable<number> {
    const url: string = `${this.baseUrlService.baseURL}/products/${id}/countComments`;
    return this.httpClient.get<number>(url);
  }

  findReviews(id: number): Observable<ProductReview[]> {
    const url: string = `${this.baseUrlService.baseURL}/products/${id}/productReviews`;
    return this.httpClient.get<ProductReview[]>(url);
  }

  isExist(productId: number): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/products/isExist?productId=${productId}`;
    return this.httpClient.get<boolean>(url);
  }

  // FOR ADDING ORDER
  findSizesFromProductId(id: number): Observable<ProductSize[]> {
    const url: string = `${this.baseUrlService.baseURL}/products/${id}/sizes/`;
    return this.httpClient
      .get<ProductSize[]>(url)
      .pipe(map((colors) => removeDuplicateSize(colors)));
  }

  findPrice(
    productId: number,
    productSize: ProductSize,
  ): Observable<number | null> {
    let formData: FormData = new FormData();
    formData.append('productId', productId.toString());
    formData.append('sizeJson', JSON.stringify(productSize));

    const url: string = `${this.baseUrlService.baseURL}/products/findPrice`;
    return this.httpClient.post<number>(url, formData);
  }


  calcPriceAfterSale(rootPrice, productSale: ProductSale): number {
    if(productSale == null) return rootPrice;
    if(productSale.productSaleType.typeName == "Fixed") {
      return (rootPrice - productSale.discount > 0) ? rootPrice - productSale.discount : 0
    } else {
      return (rootPrice * (1 - productSale.discount/100))
    }
  }

  submitComment(productId, username, rating, content): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/products/comment`;
    let formData = new FormData();
    formData.append("productId", productId)
    formData.append("username", username)
    formData.append("ratingStar", rating)
    formData.append("content", content)

    return this.httpClient.post<boolean>(url, formData)
  }
}

export function removeDuplicateSize(sizes: ProductSize[]): ProductSize[] {
  return sizes.filter((value, index) => {
    const _value = JSON.stringify(value);
    return (
      index ===
      sizes.findIndex((obj) => {
        return JSON.stringify(obj) === _value;
      })
    );
  });



}
