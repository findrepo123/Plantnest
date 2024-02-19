import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

import { iconBoxes, counters, brands, members } from './about-data';

@Component({
	selector: 'pages-about-page',
	templateUrl: './about.component.html',
	styleUrls: ['./about.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})

export class AboutPageComponent implements OnInit {

	iconBoxes = iconBoxes;
	brands = brands;
	members = members;
	counters = counters;

	constructor(public sanitizer: DomSanitizer) {
	}

	ngOnInit(): void {
	}
}
