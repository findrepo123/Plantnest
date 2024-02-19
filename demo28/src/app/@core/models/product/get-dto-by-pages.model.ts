import { Pageable } from "../pageable"
import { Product } from "./product.model"

export class GetDTOByPages<T> {
  content: T[]
  pageable: Pageable
  last: boolean
  totalElements: number
  totalPages: number
  size: number
  number: number
  sort: {
      empty: boolean,
      sorted: boolean,
      unsorted: boolean
  }
  numberOfElements: number
  first: boolean
  empty: boolean
}
