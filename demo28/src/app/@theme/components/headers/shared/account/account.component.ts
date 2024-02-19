import { Component, OnInit, OnDestroy } from '@angular/core';
import { AuthenticationService } from 'src/app/@core/services/account/authentication.service';
import { ModalService } from 'src/app/@core/services/modal.service';


@Component({
	selector: 'molla-account',
	templateUrl: './account.component.html',
	styleUrls: ['./account.component.scss']
})

export class AccountComponent implements OnInit, OnDestroy {

	constructor(
    public authenService: AuthenticationService,
    public modalService: ModalService,
  ) { }

	ngOnInit(): void {
	}

	ngOnDestroy(): void {
	}

  showLoginModal(event: Event): void {
		event.preventDefault();
		this.modalService.showLoginModal();
	}
}
