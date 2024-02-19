import { map, catchError } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { AbstractControl, ValidationErrors } from "@angular/forms";
import { AccountService } from "../services/account/account.service";

export function isUsernameExisting(accountService: AccountService) {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return accountService.isUsernameExists(control.value).pipe(
          map((exists: boolean) => (exists ? { usernameExists: true } : null)),
          catchError(() => of(null))
      );
  };
}
