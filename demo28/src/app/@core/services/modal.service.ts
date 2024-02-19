import Cookie from 'js-cookie';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgbModalOptions, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginModalComponent } from 'src/app/@theme/components/modals/login-modal/login-modal.component';


@Injectable({
	providedIn: 'root'
})

export class ModalService {
	products = [];
	timer: any;

	private modalOption2: NgbModalOptions = {
		centered: true,
		size: 'lg',
		windowClass: 'login-modal',
		beforeDismiss: async () => {
			document.querySelector('body')?.classList.remove('modal-open');

			await new Promise((resolve) => {
				setTimeout(() => {
					resolve('success');
				}, 300)
			});

			(document.querySelector('.logo') as HTMLElement).focus({ preventScroll: true });

			return true;
		}
	}

	constructor(private modalService: NgbModal, private router: Router, private http: HttpClient) {
	}

	// Show login modal
	showLoginModal() {
		(document.querySelector('.logo') as HTMLElement).focus({ preventScroll: true });
		this.modalService.open(
			LoginModalComponent,
			this.modalOption2
		)
	}
}
