import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Address } from 'src/app/@core/models/address/address.model';
import { District } from 'src/app/@core/models/address/districts.model';
import { Province } from 'src/app/@core/models/address/provinces.model';
import { Ward } from 'src/app/@core/models/address/wards.model';
import { AddressService } from 'src/app/@core/services/account/address.service';
import { OrderService } from 'src/app/@core/services/order/order.service';
import { CustomValidator } from 'src/app/@core/validators/custom-validator';
import { isCouponNotExisting } from 'src/app/@core/validators/is-coupon-not-existing';
import { isCouponCantBeUsed } from 'src/app/@core/validators/is-coupon-can-be-used';
import { ProductCouponService } from 'src/app/@core/services/product/product-coupon.service';
import { Coupon } from 'src/app/@core/models/coupon/coupon.model';
import { CartService } from 'src/app/@core/services/account/cart.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { Subscription } from 'rxjs';
declare var $: any;

const ADDRESS_OPTIONS = [
  { value: 'existing', label: 'Use Existing Address', disabled: false },
  { value: 'new', label: 'New Address', disabled: false },
];

@Component({
  selector: 'molla-billing-information',
  templateUrl: './billing-information.component.html',
  styleUrls: ['./billing-information.component.scss']
})

export class BillingInformationComponent implements OnInit {
  subscriptions: Subscription[] = []
  billingForm: FormGroup
  blurred: { [key: string]: boolean } = {}

  addressOptions = [...ADDRESS_OPTIONS]
  provinces: Province[]
  districts: District[]
  wards: Ward[]
  loadedAddresses: boolean = false
  existingAddresses: Address[]

  appliedCoupon: Coupon
  appliedCouponSuccessMessage: string

  constructor(
    public formBuilder: FormBuilder,
    public orderService: OrderService,
    public addressService: AddressService,
    public couponService: ProductCouponService,
    public cartService: CartService,
    public authenService: AuthenticationService
  ) {
    this.billingForm = this.formBuilder.group({
      fullName: ['', [CustomValidator.notBlank, Validators.minLength(3), Validators.maxLength(100)]],
      email: ['', Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      phoneNumber: ['', [Validators.pattern(/^\s*\d{10}\s*$/)]],

      addressOption: ['', Validators.required],
      // for address option = new
      province: [null, Validators.required],
      district: [null, Validators.required],
      ward: [null, Validators.required],
      roadName: [null, [CustomValidator.notBlank, Validators.maxLength(50)]],

      // for address option = existing
      address: [null, [Validators.required]],

      couponCode: ['', [Validators.required],
        [isCouponNotExisting(this.couponService),
        isCouponCantBeUsed(this.couponService)]]
    })
    this.billingForm.get('email').disable()
  }

  setBlurredAll() {
    this.blurred.fullName = true
    this.blurred.phoneNumber = true
    this.blurred.roadName = true
    this.blurred.couponCode = true
  }

  validateField(controlName: string) {
    this.blurred[controlName] = true;
  }

  ngOnInit(): void {
    this.setDefaultAddressOption()
    this.fillFormValues();
    this.onAddressOptionChange()
    this.onAddressChange()
  }

  setDefaultAddressOption() {
    this.subscriptions.push(
      this.addressService.findAllAddress().subscribe(addresses => {
        this.existingAddresses = addresses
        this.loadedAddresses = true
        if(addresses.length == 0) {
          this.billingForm.get('addressOption').setValue('new')
        } else {
          this.billingForm.get('addressOption').setValue('existing')
        }
      })
    )
  }

  fillFormValues() {
    const loggedInAccount = this.authenService.getAccountFromLocalCache();
    this.billingForm.get('fullName').setValue(loggedInAccount.fullName)
    this.billingForm.get('email').setValue(loggedInAccount.email)
    this.billingForm.get('phoneNumber').setValue(loggedInAccount.phoneNumber)
    this.appliedCoupon = this.cartService.appliedCoupon
    if(this.appliedCoupon != null) {
      this.billingForm.get('couponCode').setValue(this.appliedCoupon.code)
      this.appliedCouponSuccessMessage = "Applied coupon successfully!<br/>" +
          "This coupon sale off " + this.couponService.getDiscountValue(this.appliedCoupon);
    }

  }

  onAddressOptionChange() {
    this.subscriptions.push(
      this.billingForm.get('addressOption').valueChanges.subscribe(data => {
        if(data == 'new') {
          this.loadProvinces()
          this.resetAddressOptions(true, true, true, true)
        } else if(data == 'existing'){
          if(this.existingAddresses != null) {
            this.loadAddresses()
          }
        }
      })
    )
  }

  onAddressChange() {
    this.subscriptions.push(
      this.billingForm.get('province').valueChanges.subscribe(data => {
        this.resetAddressOptions(true, true)
      })
    )
    this.subscriptions.push(
      this.billingForm.get('district').valueChanges.subscribe(data => {
        this.resetAddressOptions(true)
      })
    )
  }

  onApplyCoupon() {
    this.subscriptions.push(
      this.billingForm.get('couponCode').valueChanges.subscribe((data) => {
        this.applyCoupon()
      })
    )
  }

  loadAddresses() {
    this.subscriptions.push(
      this.addressService.findAllAddress().subscribe(addresses => this.existingAddresses = addresses)
    )
  }

  loadProvinces() {
    this.addressService.findAllProvinces().subscribe(
      data => {
        this.provinces = data._embedded.provinces
      }
    )
  }

  loadDistricts() {
    const selectedProvince: Province = this.billingForm.get('province').value;
    this.subscriptions.push(
      this.addressService.findAllDistrictByProvince(selectedProvince.code).subscribe(
        data => {
          this.districts = data._embedded.districts
        }
      )
    )
  }

  loadWards() {
    const selectedDistrict: District = this.billingForm.get('district').value;
    this.subscriptions.push(
      this.addressService.findAllWardByDistrict(selectedDistrict.code).subscribe(
        data => {
          this.wards = data._embedded.wards
        }
      )
    )
  }

  resetAddressOptions(resetWard: boolean = false, resetDistrict: boolean = false,
      resetProvince: boolean = false, resetRoadName: boolean = false): void {
    if(resetWard) this.billingForm.get('ward').setValue('');
    if(resetDistrict) this.billingForm.get('district').setValue('');
    if(resetProvince) this.billingForm.get('province').setValue('');
    if(resetRoadName) this.billingForm.get('roadName').setValue('');
  }

  applyCoupon() {
    if(this.billingForm.get('couponCode').invalid) {
      this.appliedCoupon = null
      this.cartService.appliedCoupon = null
      this.appliedCouponSuccessMessage = null
      return
    }

    this.subscriptions.push(
      this.couponService.findByCode(this.billingForm.get('couponCode').value)
        .subscribe(coupon => {
          this.appliedCoupon = coupon
          this.cartService.appliedCoupon = coupon
          this.appliedCouponSuccessMessage = "Applied coupon successfully!<br/>" +
            "This coupon sale off " + this.couponService.getDiscountValue(coupon);
          this.couponService.appliedCouponChange.next()
        })
    )
  }

  getFormValue(): any | null {
    this.setBlurredAll()


    if(this.billingForm.get('couponCode').hasError('required')) {
      this.billingForm.get('couponCode').setErrors(null)
    }
    if(this.billingForm.get('addressOption').value == 'new') {
      this.billingForm.get('address').setErrors(null)
    } else if(this.billingForm.get('addressOption').value == 'existing') {
      this.billingForm.get('province').setErrors(null)
      this.billingForm.get('district').setErrors(null)
      this.billingForm.get('ward').setErrors(null)
      this.billingForm.get('roadName').setErrors(null)
    }

    if(this.billingForm.invalid) {
      this.billingForm.markAllAsTouched()
      return null;
    }

    return this.billingForm.value;
  }
}
