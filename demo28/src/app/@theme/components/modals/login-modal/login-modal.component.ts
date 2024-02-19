import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbNav } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { HeaderType } from 'src/app/@core/enum/header-type.enum';
import { Account } from 'src/app/@core/models/account/account.model';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';

@Component({
	selector: 'molla-login-modal',
	templateUrl: './login-modal.component.html',
	styleUrls: ['./login-modal.component.scss']
})

export class LoginModalComponent implements OnInit {
  private subscriptions: Subscription[] = []
  @ViewChild(NgbNav) nav: NgbNav

	constructor(
    private formBuilder: FormBuilder,
    private authenService: AuthenticationService
  ) { }

	ngOnInit(): void {
	}

	closeModal() {
    let modal = document.querySelector('.login-modal') as HTMLElement;
    if (modal)
      modal.click();
	}

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe())
  }
}
