import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { HeaderType } from 'src/app/@core/enum/header-type.enum';
import { Account } from 'src/app/@core/models/account/account.model';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { CartService } from 'src/app/@core/services/account/cart.service';
import { WishlistService } from 'src/app/@core/services/account/wishlist.service';

@Component({
	selector: 'molla-login-form',
	templateUrl: './login-form.component.html',
	styleUrls: ['./login-form.component.scss']
})

export class LoginFormComponent implements OnInit {
  private subscriptions: Subscription[] = []
  loginFormGroup: FormGroup
  errorMessage: string = '';
  @Output() loginSuccess: EventEmitter<boolean>

	constructor(
    private formBuilder: FormBuilder,
    private authenService: AuthenticationService,
    private wishlistService: WishlistService,
    private cartService: CartService
  ) {
    this.loginFormGroup = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })
    this.loginSuccess = new EventEmitter()
  }

	ngOnInit(): void {
	}

  login() {
    if(this.loginFormGroup.invalid) {
      this.loginFormGroup.markAllAsTouched();
      return;
    }

    this.subscriptions.push(
      this.authenService.login(this.loginFormGroup.value).subscribe(
        (response: HttpResponse<Account>) => {
          const token = response.headers.get(HeaderType.JWT_TOKEN)
          this.authenService.saveToken(token)
          this.authenService.addAccountToLocalCache(response.body)
          this.authenService.authChange()
          this.wishlistService.wishlistQtyChangeSubject.next()
          this.cartService.cartChangeSubject.next()
          this.loginSuccess.emit(true)
        },
        (error) => {
          this.errorMessage = error.error.message
        }
      )
    )
  }

  saveToken(token: string) {
    localStorage.setItem('token', token)
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe())
  }
}
