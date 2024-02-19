import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AboutPageComponent } from './about/about.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { ContactPageComponent } from './contact/contact.component';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'about',
        pathMatch: 'full'
    },
    {
        path: 'about',
        component: AboutPageComponent
    },
    {
        path: '404',
        component: PageNotFoundComponent
    },
    {
        path: 'contact',
        component: ContactPageComponent
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class PagesRoutingModule { };
