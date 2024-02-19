import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Contact, GetContactResponse } from '../../models/account/contact.model';

@Injectable({
  providedIn: 'root'
})
export class ContactService {

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient,
  ) {
  }

  insert(contact: Contact): Observable<Contact> {
    const url: string = `${this.baseUrlService.baseURL}/admin/contacts/`
    return this.httpClient.post<Contact>(url, contact);
  }

}
