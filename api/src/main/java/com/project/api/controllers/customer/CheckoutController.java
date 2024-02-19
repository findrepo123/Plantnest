package com.project.api.controllers.customer;

import com.project.api.constants.PaginationConstant;
import com.project.api.dtos.OrderFindAllDTO;
import com.project.api.dtos.OrderFindDetailDTO;
import com.project.api.dtos.ProductFindAllDTO;
import com.project.api.dtos.request.OrderRequestDTO;
import com.project.api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private OrderService orderService;

    @Autowired
    public CheckoutController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("placeOrder")
    public ResponseEntity<Boolean> placeOrder(@RequestBody OrderRequestDTO order) {
        try {
            return new ResponseEntity<>(orderService.insert(order), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("findOrders")
    public ResponseEntity<Page<OrderFindAllDTO>> findOrders(
            @RequestParam Integer accountId,
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "orderBy", required = false) String orderBy) {
        final String PAGE_SIZE_DEFAULT = "5";
          page = page == "" || page == null ? PaginationConstant.PAGE_DEFAULT : page;
          pageSize = pageSize == "" || pageSize == null ? PAGE_SIZE_DEFAULT : pageSize;

          Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(pageSize));
          return new ResponseEntity<>(orderService.findOrders(pageable, accountId, orderBy, searchTerm), HttpStatus.OK);
    }

    @GetMapping("findOrderById/{orderId}")
    public ResponseEntity<OrderFindDetailDTO> findById(@PathVariable Integer orderId) {
        try {
            return new ResponseEntity<>(orderService.findById(orderId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("findByPages")
//    public ResponseEntity<Page<ProductFindAllDTO>> findByPages(
//            @RequestParam(name = "page", required = false) String page,
//            @RequestParam(name = "pageSize", required = false) String pageSize,
//            @RequestParam(name = "searchTerm", required = false) String searchTerm,
//            @RequestParam(name = "catalog", required = false) String catalog,
//            @RequestParam(name = "size", required = false) String size,
//            @RequestParam(name = "level", required = false) String level,
//            @RequestParam(name = "orderBy", required = false) String orderBy) {
//        page = page == "" || page == null ? PaginationConstant.PAGE_DEFAULT : page;
//        pageSize = pageSize == "" || pageSize == null ? PaginationConstant.PAGE_SIZE_DEFAULT : pageSize;
//
//        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(pageSize));
//        return new ResponseEntity<>(
//                productService.findByPages(pageable, catalog, size, level, orderBy, searchTerm), HttpStatus.OK);
//    }
}
