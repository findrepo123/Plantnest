import { Component, OnInit, Renderer2, ElementRef, ViewChild, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbNav } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { Address } from 'src/app/@core/models/address/address.model';
import { FilterOrderCriteria } from 'src/app/@core/models/filter-order-criteria';
import { Order } from 'src/app/@core/models/order/order.model';
import { AccountService } from 'src/app/@core/services/account/account.service';
import { AddressService } from 'src/app/@core/services/account/address.service';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { OrderService } from 'src/app/@core/services/order/order.service';
import { UtilsService } from 'src/app/@core/services/utils.service';


class Tab {
  fragment: string
  label: string
}

@Component({
  selector: 'molla-dashboard-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild(NgbNav, { static: true }) ngbNavCpn: NgbNav;
  private subscriptions: Subscription[] = []


  tabs: Tab[] = [
    {
      fragment: "orders",
      label: "Orders"
    },
    {
      fragment: "account-details",
      label: "Account Details"
    },
  ]
  currentFragment: string;

  constructor(
    public activatedRoute: ActivatedRoute,
    public orderService: OrderService,
    public el: ElementRef,
    public renderer: Renderer2,
    public utilsService: UtilsService,
    private router: Router,
    public accountService: AccountService,
    public addressService: AddressService,
    public authenService: AuthenticationService,
    public toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.activatedRoute.fragment.subscribe(fragment => {
        let tab = this.tabs.find(tab => tab.fragment == fragment)
        tab = (tab) ? tab : this.tabs[0]
        this.ngbNavCpn.select(tab.fragment);

        if(this.currentFragment != null && this.currentFragment != tab.fragment) {
          this.router.navigate(['/dashboard'],{
            fragment: tab.fragment,
            queryParamsHandling: 'merge'
          })
        }
        this.currentFragment = tab.fragment
      })
    )
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscr => subscr.unsubscribe());
  }

  logout() {
    this.authenService.logout()
    this.authenService.authChange()
    this.toastrService.success("Log out successfully!")
    this.router.navigateByUrl("/")
  }
}
