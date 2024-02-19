import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OwlModule } from 'angular-owl-carousel';


import { IndexComponent } from './index/index.component';
import { TrendyCollectionComponent } from './trendy-collection/trendy-collection.component';
import { ThemeModule } from 'src/app/@theme/theme.module';

@NgModule({
	declarations: [
		IndexComponent,
		TrendyCollectionComponent,
	],

	imports: [
		CommonModule,
		RouterModule,
		NgbModule,
		OwlModule,
    ThemeModule
	]
})

export class HomeModule { }
