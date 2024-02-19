import { Injectable } from '@angular/core';
import { BaseURLService } from '../base-url.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs-compat';
import { Catalog } from '../../models/product/catalog.model';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  constructor(
    private baseUrlService: BaseURLService,
    private httpClient: HttpClient
  ) { }

  findAll(): Observable<Catalog[]> {
    const url: string = `${this.baseUrlService.baseURL}/products/findAllCatalogs`
    return this.httpClient.get<Catalog[]>(url)
  }

  findById(catalogId: number): Observable<Catalog> {
    const url: string = `${this.baseUrlService.baseURL}/catalogs/findById/${catalogId}`
    return this.httpClient.get<Catalog>(url)
  }

  flattenCatalogs(catalogs, parent = null) {
    const flattened = [];

    catalogs.forEach(catalog => {
      const flatCatalog = {
        catalogId: catalog.catalogId,
        catalogName: catalog.catalogName,
        description: catalog.description,
        image: {
          imageId: catalog.image.imageId,
          imageUrl: catalog.image.imageUrl
        },
        hasParent: catalog.hasParent,
        parentCatalog: parent
      };

      flattened.push(flatCatalog);

      if (catalog.childCatalogs.length > 0) {
        const childFlattened = this.flattenCatalogs(catalog.childCatalogs, catalog.catalogId);
        flattened.push(...childFlattened);
      }
    });

    return flattened;
  }
}
