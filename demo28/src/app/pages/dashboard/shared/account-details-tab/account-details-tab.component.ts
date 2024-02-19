import { HttpErrorResponse, HttpEvent, HttpEventType } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { Account } from 'src/app/@core/models/account/account.model';
import { AccountService } from 'src/app/@core/services/account/account.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { ACCOUNT_IMAGE_DIRECTORY } from 'src/app/@core/services/image-storing-directory';
import { CustomValidator } from 'src/app/@core/validators/custom-validator';


@Component({
  selector: 'dashboard-account-details-tab',
  templateUrl: './account-details-tab.component.html',
  styleUrls: ['./account-details-tab.component.scss']
})

export class AccountDetailsTabComponent implements OnInit {

  ACCOUNT_IMAGE_DIRECTORY = ACCOUNT_IMAGE_DIRECTORY
  private subscriptions: Subscription[] = [];
  loggedInAccount: any;
  refreshing: boolean;
  fileName: string;
  profileImage: File;
  fileStatus = new FileUploadStatus();
  changeProfileFormGroup: FormGroup
  isFormChanged: boolean = false;

  errorMessage: string;

  constructor(
    private accountService: AccountService,
    private authenService: AuthenticationService,
    private toastrService: ToastrService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.loggedInAccount = this.authenService.getAccountFromLocalCache()
    this.changeProfileFormGroup = this.formBuilder.group({
      username: [this.loggedInAccount.username],
      email: [this.loggedInAccount.email],
      fullName: [this.loggedInAccount.fullName,
      [Validators.maxLength(255),
      Validators.minLength(3),
      CustomValidator.notBlank]],
      currentPassword: [],
      newPassword: [],
    })
    this.changeProfileFormGroup.get('username').disable()
    this.changeProfileFormGroup.get('email').disable()
    this.isFormChange()
    this.changeProfileFormGroup.get('currentPassword').valueChanges.subscribe((data) => {
      if (data != null || data != '') {
        this.changeProfileFormGroup.get('newPassword').setValidators([
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(50)
        ])
      } else {
        this.changeProfileFormGroup.get('newPassword').clearValidators();
      }

      this.changeProfileFormGroup.get('newPassword').updateValueAndValidity();
    })
  }

  isFormChange() {
    this.changeProfileFormGroup.valueChanges.subscribe((data) => {
      this.isFormChanged = true
    })
  }

  changeProfile() {
    if (this.changeProfileFormGroup.invalid) {
      this.changeProfileFormGroup.markAllAsTouched()
      return;
    }

    this.subscriptions.push(
      this.accountService.updateInformation(this.loggedInAccount.username,
        this.changeProfileFormGroup.get('fullName').value,
        this.changeProfileFormGroup.get('currentPassword').value,
        this.changeProfileFormGroup.get('newPassword').value)
        .subscribe(
          (response) => {
            if (response) {
              this.accountService.findByUsername(this.loggedInAccount.username).subscribe(data => {
                this.loggedInAccount = data
                this.authenService.addAccountToLocalCache(data)
                this.toastrService.success('Updated account information successfully')
                this.fileStatus.status = 'done';
                this.errorMessage = null
              })
            }
          },
          (error: HttpErrorResponse) => {
            this.errorMessage = error.error.message
            this.toastrService.error('Updated account information failed')

          }
        )
    );
  }

  focusCurrentPw() {
    this.changeProfileFormGroup.get('currentPassword').setValue(null)
    this.errorMessage = null
  }

  public updateProfileImage(): void {
    document.getElementById('profile-image-input').click();
  }

  public onProfileImageChange(event): void {
    this.fileName = event.target.files[0].name;
    this.profileImage = event.target.files[0];
  }

  public onUpdateProfileImage(): void {
    const formData = new FormData();
    formData.append('username', this.loggedInAccount.username);
    formData.append('profileImage', this.profileImage);
    this.subscriptions.push(
      this.accountService.updateProfileImage(formData).subscribe(
        (event: HttpEvent<any>) => {
          this.reportUploadProgress(event);
        },
        (errorResponse: HttpErrorResponse) => {
          this.toastrService.error(errorResponse.error.message)
          this.fileStatus.status = 'done';
        }
      )
    );
  }

  private reportUploadProgress(event: HttpEvent<any>): void {
    switch (event.type) {
      case HttpEventType.UploadProgress:
        this.fileStatus.percentage = Math.round(100 * event.loaded / event.total);
        this.fileStatus.status = 'progress';
        break;
      case HttpEventType.Response:
        if (event.status === 200) {
          this.subscriptions.push(
            this.accountService.findByUsername(this.loggedInAccount.username).subscribe(data => {
              this.loggedInAccount = data
              this.authenService.addAccountToLocalCache(data)

              this.toastrService.success(`${this.loggedInAccount.fullName}\'s profile image updated successfully`);
              this.fileStatus.status = 'done';
            }))
          break;
        } else {
          this.toastrService.error(`Unable to upload image`);
          break;
        }
      default:
        `Finished all processes`;
    }
  }
}

export class FileUploadStatus {
  public status: string;
  public percentage: number;

  constructor() {
    this.status = '';
    this.percentage = 0;
  }
}
