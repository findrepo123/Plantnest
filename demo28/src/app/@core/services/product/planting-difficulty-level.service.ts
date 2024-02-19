import { Injectable } from '@angular/core';
import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs-compat';
import { GetPlantDifficultyLevelResponse, PlantingDifficultyLevel } from '../../models/product/planting-difficulty-level.model';

@Injectable({
  providedIn: 'root'
})
export class PlantingDifficultyLevelService {
  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient
  ) { }

  findAll(): Observable<PlantingDifficultyLevel[]> {
    const url: string = `${this.baseUrlService.baseURL}/products/findAllLevels`
    return this.httpClient.get<PlantingDifficultyLevel[]>(url)
  }
}
