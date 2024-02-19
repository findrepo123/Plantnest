import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Address } from '../../models/address/address.model';
import { GetProvinceResponse } from '../../models/address/provinces.model';
import { GetDistrictResponse } from '../../models/address/districts.model';
import { GetWardResponse } from '../../models/address/wards.model';
import { AuthenticationService } from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient,
    private authenService: AuthenticationService
  ) {
  }

  findAllAddress(): Observable<Address[]> {
    const loggedInAccount = this.authenService.getAccountFromLocalCache()
    const url: string = `${this.baseUrlService.baseURL}/${loggedInAccount.id}/addresses`
    return this.httpClient.get<Address[]>(url)
  }

  findAllProvinces(): Observable<GetProvinceResponse> {
    const url: string = `${this.baseUrlService.baseURL}/admin/provinces?size=63`
    return this.httpClient.get<GetProvinceResponse>(url);
  }

  findAllDistrictByProvince(provinceCode: string ): Observable<GetDistrictResponse> {
    const url: string = `${this.baseUrlService.baseURL}/admin/provinces/${provinceCode}/districts`
    return this.httpClient.get<GetDistrictResponse>(url);
  }


  findAllWardByDistrict(districtCode: string): Observable<GetWardResponse> {
    const url: string = `${this.baseUrlService.baseURL}/admin/districts/${districtCode}/wards`
    return this.httpClient.get<GetWardResponse>(url);
  }

  getAddressStringFormAddress(address: Address) {
    const wardName = address.ward != null ? address.ward.fullName : null
    const districtName = address.district != null ? address.district.fullName : null
    const provinceName = address.province != null ? address.province.fullName : null

    return `${address.roadName}, ${wardName}, ${districtName}, ${provinceName}`
  }
}
