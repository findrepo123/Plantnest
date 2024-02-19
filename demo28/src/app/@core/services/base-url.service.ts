import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root'
})
export class BaseURLService {
    private _baseURL: string = environment.apiUrl + "/api"
    get baseURL(): string{
        return this._baseURL ;
    }
}
