import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DetailPageComponent } from './detail/detail.component';


const routes: Routes = [
    {
        path: 'detail/:slug',
        component: DetailPageComponent
    },

]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class ProductRoutingModule { };
