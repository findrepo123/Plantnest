import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { BaseURLService } from '../base-url.service';
import { HttpClient, HttpEvent } from '@angular/common/http';
import { Account } from '../../models/account/account.model';
import { Address } from '../../models/address/address.model';
import { ProductReview } from '../../models/product/product-review.model';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient,
  ) {
  }

  countTotalComments(id: number): Observable<number> {
    const url: string = `${this.baseUrlService.baseURL}/accounts/${id}/countComments`
    return this.httpClient.get<number>(url);
  }

  isUsernameExists(username: string): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/isUsernameExist?username=${username}`
    return this.httpClient.get<boolean>(url);
  }

  isEmailExists(email: string): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/isEmailExist?email=${email}`
    return this.httpClient.get<boolean>(url);
  }

  findByUsername(username: string): Observable<Account> {
    const url: string = `${this.baseUrlService.baseURL}/find/${username}`
    return this.httpClient.get<Account>(url);
  }

  findAllAddress(): Observable<Address[]> {
    const url: string = `${this.baseUrlService.baseURL}/address`
    return this.httpClient.get<Address[]>(url)
  }

  updateInformation(username: string, fullName: string, currentPassword: string, newPassword: string): Observable<boolean> {
    const url: string = `${this.baseUrlService.baseURL}/updateInformation`
    let formData = new FormData()
    formData.append("username", username)
    formData.append("fullName", fullName)
    formData.append("currentPassword", currentPassword ?? "")
    formData.append("newPassword", newPassword ?? "")
    return this.httpClient.post<boolean>(url, formData)
  }

  updateProfileImage(formData: FormData): Observable<HttpEvent<Account>> {
    return this.httpClient.post<Account>(`${this.baseUrlService.baseURL}/updateProfileImage`, formData,
    {reportProgress: true,
      observe: 'events'
    });
  }

}
