import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RouterModule } from '@angular/router';
import { OwlModule } from 'angular-owl-carousel';
import { GoogleMapsModule } from '@angular/google-maps';
import { HttpClientModule, HttpClientJsonpModule } from '@angular/common/http';
import { PagesRoutingModule } from './pages-routing.module';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ContactPageComponent } from './contact/contact.component';
import { AboutPageComponent } from './about/about.component';
import { ThemeModule } from 'src/app/@theme/theme.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule( {
	declarations: [
		AboutPageComponent,
		PageNotFoundComponent,
		ContactPageComponent,
	],

	imports: [
		CommonModule,
		PagesRoutingModule,
		ThemeModule,
		NgbModule,
		RouterModule,
		OwlModule,
		GoogleMapsModule,
		HttpClientModule,
		HttpClientJsonpModule,
    ReactiveFormsModule,
    FormsModule
	]
} )

export class PagesModule { }
