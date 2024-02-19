import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { HeaderType } from 'src/app/@core/enum/header-type.enum';
import { Account } from 'src/app/@core/models/account/account.model';
import { AccountService } from 'src/app/@core/services/account/account.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { CustomValidator } from 'src/app/@core/validators/custom-validator';
import { isEmailExisting } from 'src/app/@core/validators/is-email-existing';
import { isUsernameExisting } from 'src/app/@core/validators/is-username-existing';

@Component({
  selector: 'molla-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss']
})

export class RegisterFormComponent implements OnInit {
  private subscriptions: Subscription[] = []
  registerFormGroup: FormGroup
  message: {
    state: string,
    message: string;
  };
  @Output() registerSuccess: EventEmitter<boolean>


  constructor(
    private formBuilder: FormBuilder,
    private accountService: AccountService,
    private authenService: AuthenticationService
  ) {
    this.message = {
      state: '',
      message: ''
    }

    this.registerFormGroup = this.formBuilder.group({
      username: [null,
        [CustomValidator.notBlank,
        Validators.minLength(3),
        Validators.maxLength(50),
        Validators.pattern("^[a-zA-Z0-9]+$")],
        [isUsernameExisting(this.accountService)]],
      email: [null,
        [CustomValidator.notBlank,
        Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
      password: [null,
        [CustomValidator.notBlank,
        Validators.minLength(6),
        Validators.maxLength(50)]],
      fullName: [null,
        [CustomValidator.notBlank,
        Validators.minLength(3),
        Validators.maxLength(100)]],
      phoneNumber: [null, [Validators.pattern(/^\s*\d{10}\s*$/)]]
    })

    this.registerSuccess = new EventEmitter()
  }

  ngOnInit(): void {
  }

  register() {
    if (this.registerFormGroup.invalid) {
      this.registerFormGroup.markAllAsTouched();
      return;
    }

    this.subscriptions.push(
      this.authenService.register(this.registerFormGroup.value).subscribe(
        (response: HttpResponse<Account>) => {
          if (response != null) {
            this.message = {
              state: 'success',
              message: "Register Successfully"
            }
            this.registerFormGroup.reset()
          }
        },
        (error) => {
          this.message = {
            state: 'danger',
            message: error.error.message
          }
        }
      )
    )
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe())
  }
}
