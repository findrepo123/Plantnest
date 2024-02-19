import { Injectable } from '@angular/core';
import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { CouponType } from '../../models/coupon/coupon-type.model';
import { Coupon, GetCouponResponse } from '../../models/coupon/coupon.model';

@Injectable({
  providedIn: 'root'
})
export class ProductCouponService {

  appliedCouponChange: Subject<void> = new Subject()

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient
  ) {
  }

  findCouponTypeById(id: number): CouponType {
    if(id == 1) {
      return { couponTypeId: 1, typeName: 'Fixed' }
    } else {
      return { couponTypeId: 2, typeName: 'Percent' }
    }
  }

  isCouponExists(couponCode: string): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/coupons/isCouponExist?code=${couponCode}`
    return this.httpClient.get<boolean>(url);
  }

  isCouponCanBeUsed(couponCode: string): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/coupons/isCouponCanBeUsed?code=${couponCode}`
    return this.httpClient.get<boolean>(url);
  }

  findByCode(couponCode: string): Observable<Coupon> {
    const url: string = `${this.baseUrlService.baseURL}/coupons/findByCode?code=${couponCode}`
    return this.httpClient.get<Coupon>(url);
  }

  getDiscountValue(coupon: Coupon): string {
    if(coupon == null) return "COUPON IS NULL";
    return (coupon.couponType.typeName == 'Fixed') ? "$" + coupon.discount :  coupon.discount + "%"
  }
}
