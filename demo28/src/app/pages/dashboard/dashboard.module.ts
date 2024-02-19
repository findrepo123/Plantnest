import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { HomeComponent } from './home/home.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountDetailsTabComponent } from './shared/account-details-tab/account-details-tab.component';
import { OrdersTabComponent } from './shared/orders-tab/orders-tab.component';
import { ThemeModule } from 'src/app/@theme/theme.module';
import { RouterModule } from '@angular/router';

@NgModule({
	declarations: [
		HomeComponent,
    AccountDetailsTabComponent,
    OrdersTabComponent,
	],

	imports: [
    NgbModule,
    FormsModule,
    CommonModule,
    ThemeModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule
	]
})

export class DashboardModule { }
