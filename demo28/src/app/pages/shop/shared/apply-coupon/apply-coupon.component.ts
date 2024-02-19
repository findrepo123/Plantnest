import { Subscription } from 'rxjs';
import { Component, OnInit, Input, OnChanges, Output, EventEmitter, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Coupon } from 'src/app/@core/models/coupon/coupon.model';
import { CartService } from 'src/app/@core/services/account/cart.service';
import { ProductCouponService } from 'src/app/@core/services/product/product-coupon.service';
import { CustomValidator } from 'src/app/@core/validators/custom-validator';
import { isCouponCantBeUsed } from 'src/app/@core/validators/is-coupon-can-be-used';
import { isCouponNotExisting } from 'src/app/@core/validators/is-coupon-not-existing';

@Component({
	selector: 'molla-apply-coupon',
	templateUrl: './apply-coupon.component.html',
	styleUrls: ['./apply-coupon.component.scss']
})

export class ApplyCouponComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = []
  @Output() availableCoupon: EventEmitter<Coupon>;
  couponForm: FormGroup
  hasAnyErrors: boolean = false
  successMessage: string;
  appliedCoupon: Coupon

	constructor(
    public formBuilder: FormBuilder,
    public couponService: ProductCouponService,
    public cartService: CartService
  ) {
    this.availableCoupon = new EventEmitter()
    this.couponForm = this.formBuilder.group({
      code: ['', [CustomValidator.notBlank],
        [isCouponNotExisting(this.couponService),
        isCouponCantBeUsed(this.couponService)]]
    })
	}

  get code() {return this.couponForm.get('code')}
	ngOnInit(): void {
	}

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscr => subscr.unsubscribe())
  }

  onFocus(event) {
    if(this.appliedCoupon == null) {
      this.hasAnyErrors = false
      this.code.reset()
    }
  }

  applyCode() {
    if(this.code.invalid) {
      this.hasAnyErrors = true
      this.successMessage = null
      this.appliedCoupon = null
      this.cartService.appliedCoupon = null
      this.availableCoupon.emit(null)
      return
    }

    this.subscriptions.push(
      this.couponService.findByCode(this.code.value).subscribe(coupon => {
        this.appliedCoupon = coupon
        this.cartService.appliedCoupon = coupon
        this.successMessage = "Applied coupon successfully!<br/>" +
          "This coupon sale off " + this.couponService.getDiscountValue(coupon);
        this.availableCoupon.emit(this.appliedCoupon)
      })
    )
  }

}
