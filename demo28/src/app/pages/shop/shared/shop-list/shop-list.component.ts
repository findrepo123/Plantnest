import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'molla-shop-list',
	templateUrl: './shop-list.component.html',
	styleUrls: ['./shop-list.component.scss']
})

export class ShopListComponent implements OnInit {

	@Input() products = [];
	@Input() loaded = false;

	constructor(
  ){ }

	ngOnInit(): void {
	}

}
