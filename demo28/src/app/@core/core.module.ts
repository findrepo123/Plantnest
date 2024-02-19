import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AuthenticationGuard } from './guard/authentication.guard';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth-interceptor.service';

@NgModule({
  imports: [
    CommonModule,
  ],
  exports: [],
  declarations: [],
  providers: [
    AuthenticationGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class CoreModule { }
