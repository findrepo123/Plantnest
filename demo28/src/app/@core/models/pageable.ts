export class Pageable {
  sort: {
    empty: boolean,
    sorted: boolean,
    unsorted: boolean
  }
  offset: number
  pageNumber: number
  pageSize: number
  paged: boolean
  unpaged: boolean
}
