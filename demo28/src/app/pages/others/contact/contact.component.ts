import { ToastrService } from 'ngx-toastr';
import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { Account } from 'src/app/@core/models/account/account.model';
import { CustomValidator } from 'src/app/@core/validators/custom-validator';
import { ContactService } from 'src/app/@core/services/account/contact.service';
import { Contact } from 'src/app/@core/models/account/contact.model';

@Component({
  selector: 'pages-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class ContactPageComponent implements OnInit {
  contactForm: FormGroup
  blurred: {
    [key: string]: boolean
  }
  constructor(
    public formBuilder: FormBuilder,
    public authenService: AuthenticationService,
    public contactService: ContactService,
    public toastrService: ToastrService
  ) {
    this.contactForm = this.formBuilder.group({
      fullName: [null,
        [CustomValidator.notBlank,
        Validators.minLength(3),
        Validators.maxLength(100)]],
      email: [null,
        [CustomValidator.notBlank,
        Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
      phoneNumber: [, [Validators.pattern(/^\s*\d{10}\s*$/)]],
      subject: [null,
        [Validators.minLength(3),
        Validators.maxLength(100)]],
      message: [,
        [CustomValidator.notBlank,
        Validators.minLength(3),
        Validators.maxLength(200)]],
    })

    this.fillFormValues()
  }

  fillFormValues() {
    if (this.authenService.isLoggedIn()) {
      const loggedInAccount: Account = this.authenService.getAccountFromLocalCache()
      this.contactForm.setValue({
        fullName: loggedInAccount.fullName,
        email: loggedInAccount.email,
        phoneNumber: loggedInAccount.phoneNumber,
        subject: null,
        message: null
      })
    } else {
      this.contactForm.setValue({
        fullName: null,
        email: null,
        phoneNumber: null,
        subject: null,
        message: null
      })
    }
    this.contactForm.get('email').disable()
  }

  ngOnInit(): void {
  }

  submitContact() {
    if (this.contactForm.invalid) {
      this.contactForm.markAllAsTouched()
      return;
    }

    let contact: Contact = this.contactForm.value as Contact
    contact.email = this.contactForm.get('email').value
    this.contactService.insert(contact).subscribe(
      data => {
        if (data) {
          this.toastrService.success("Message Received! We'll be in touch soon")
          this.fillFormValues()
        }
      },
      error => console.log(error)
    )
  }
}
