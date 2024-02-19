import { Injectable } from '@angular/core';
import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs-compat';
import { BehaviorSubject, Subject } from 'rxjs';
import { GetProductSaleResponse, ProductSale } from '../../models/sale/product-sale.model';
import { ProductSaleType } from '../../models/sale/product-sale-type.model';

@Injectable({
  providedIn: 'root'
})
export class ProductSaleService {

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient
  ) {
  }

  findAll(): Observable<GetProductSaleResponse> {
    const url: string = `${this.baseUrlService.baseURL}/product-sales`
    return this.httpClient.get<GetProductSaleResponse>(url)
  }

  findProductSaleTypeById(id: number): ProductSaleType {
    if(id == 1) {
      return { productSaleTypeId: 1, typeName: 'Fixed' }
    } else {
      return { productSaleTypeId: 2, typeName: 'Percent' }
    }
  }
}
