import { ACCOUNT_IMAGE_DIRECTORY } from './../../../../@core/services/image-storing-directory';
import { map } from 'rxjs/operators';
import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Product } from 'src/app/@core/models/product/product.model';
import { Accordion } from '../accordion-data';
import { AccountService } from 'src/app/@core/services/account/account.service';
import { ProductReview } from 'src/app/@core/models/product/product-review.model';
import { ProductService } from 'src/app/@core/services/product/product.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomValidator } from 'src/app/@core/validators/custom-validator';
import { Account } from 'src/app/@core/models/account/account.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'molla-info-tabs',
  templateUrl: './info-tabs.component.html',
  styleUrls: ['./info-tabs.component.scss'],
})
export class InfoTabsComponent implements OnInit, OnChanges {
  @Input() product: Product;
  ACCOUNT_IMAGE_DIRECTORY = ACCOUNT_IMAGE_DIRECTORY
  reviews: ProductReview[]
  loadedReview: boolean = false

  loggedInAccount: Account
  commentFormGroup: FormGroup

  accordion: Accordion = {
    cards: []
  }
  constructor(
    public accountService: AccountService,
    public productService: ProductService,
    public authenService: AuthenticationService,
    public formBuilder: FormBuilder,
    public toastrService: ToastrService
  ) {
    this.loggedInAccount = this.authenService.getAccountFromLocalCache();
    this.commentFormGroup = this.formBuilder.group({
      fullName: [this.loggedInAccount.fullName],
      email: [this.loggedInAccount.email],
      ratingStar: [, Validators.required],
      content: [,
        [CustomValidator.notBlank,
        Validators.minLength(3),
        Validators.maxLength(300)]]
    })
    this.commentFormGroup.get('fullName').disable()
    this.commentFormGroup.get('email').disable()

  }

  ngOnInit(): void {
    this.productService.productChange$.subscribe(() => {
      this.loadReviews()
    })
    this.loadReviews
  }

  ngOnChanges(): void {
    const mappedCards = Object.keys(this.product.productCareGuide)
      .filter(key => key !== 'productId')
      .map(key => ({
        title: key.charAt(0).toUpperCase() + key.slice(1),
        icon: 'icon-heart-o',
        body: this.product.productCareGuide[key]
      }));
    this.accordion.cards = mappedCards
  }

  setRating = (event: any, num: number) => {
    event.preventDefault();

    if (event.currentTarget.parentNode.querySelector('.active')) {
      event.currentTarget.parentNode
        .querySelector('.active')
        .classList.remove('active');
    }
    this.commentFormGroup.get('ratingStar').setValue(num);
    event.currentTarget.classList.add('active');
  };

  loadReviews() {
    this.productService.findReviews(this.product.productId).subscribe(reviews => {
      this.reviews = reviews.reverse()
      this.loadedReview = true
    })
  }

  getCommentTitle(ratingStart: number): string {
    if (ratingStart <= 0 || ratingStart > 5) return "OUT MOVE STANDING";
    const titles = ["Very Poor", "Not that bad", "Average", "Good", "Perfect"]
    return titles[ratingStart - 1]
  }

  submitComment() {
    if (this.commentFormGroup.invalid) {
      this.commentFormGroup.markAllAsTouched()
      return;
    }

    this.productService.submitComment(this.product.productId, this.loggedInAccount.username,
      this.commentFormGroup.get('ratingStar').value, this.commentFormGroup.get('content').value)
      .subscribe(
        result => {
          if (result) {
            this.productService.notifyProductChange()
            this.toastrService.success("Publish comment successfully!")
            this.reset()
            this.product.totalRating += 1
          }
        },
        error => {
          console.log(error);
        }
      )
  }

  reset() {
    this.commentFormGroup.get('content').setValue(null)
    this.commentFormGroup.get('content').setErrors(null)
    this.commentFormGroup.get('ratingStar').setValue(null)
  }
}
