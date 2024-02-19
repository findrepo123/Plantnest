import { map, catchError } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { AbstractControl, ValidationErrors } from "@angular/forms";
import { ProductCouponService } from '../services/product/product-coupon.service';

export function isCouponCantBeUsed(couponService: ProductCouponService) {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return couponService.isCouponCanBeUsed(control.value).pipe(
          map((canUse: boolean) => !canUse ? { couponCantUse: true } : null),
          catchError(() => of(null))
      );
  };
}
