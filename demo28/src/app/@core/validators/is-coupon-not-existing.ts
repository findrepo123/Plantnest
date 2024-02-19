import { map, catchError } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { AbstractControl, ValidationErrors } from "@angular/forms";
import { ProductCouponService } from '../services/product/product-coupon.service';

export function isCouponNotExisting(couponService: ProductCouponService) {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (control.value.trim().length > 0) {
      return couponService.isCouponExists(control.value).pipe(
        map((exists: boolean) => !exists ? { couponNotExist: true } : null),
        catchError(() => of(null))
      );
    } else {
      return null
    }
  };
}
