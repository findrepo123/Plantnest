package com.project.api.services.impl;

import com.project.api.dtos.OrderDetailDTO;
import com.project.api.dtos.OrderFindAllDTO;
import com.project.api.dtos.OrderFindDetailDTO;
import com.project.api.dtos.ProductFindAllDTO;
import com.project.api.dtos.request.OrderRequestDTO;
import com.project.api.dtos.request.ProductRequestDTO;
import com.project.api.entities.*;
import com.project.api.repositories.AddressRepository;
import com.project.api.repositories.OrderRepository;
import com.project.api.repositories.OrderStatusRepository;
import com.project.api.services.AccountService;
import com.project.api.services.AddressService;
import com.project.api.services.OrderService;
import com.project.api.specifications.OrderSpecification;
import com.project.api.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Override
    public List<OrderFindAllDTO> findAll() {
        try {
            return orderRepository.findAll().stream()
                    .map(OrderFindAllDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public OrderFindDetailDTO findById(Integer orderId) {
        try {
            Order order = orderRepository.findById(orderId).get();
            List<OrderDetailDTO> orderDetailDTOS = order.getOrderDetails().stream()
                    .map(OrderDetailDTO::new)
                    .collect(Collectors.toList());
            OrderFindDetailDTO orderFindDetailDTO = new OrderFindDetailDTO(order);
            orderFindDetailDTO.setOrderDetails(orderDetailDTOS);

            return orderFindDetailDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean insert(OrderRequestDTO order) {
        try {
            Order insertingOrder = new Order();
            insertingOrder.setAccountEmail(order.getAccountEmail());

            Account account = accountService.findByEmail(order.getAccountEmail());
            insertingOrder.setAccount(new Account(){{setId(account.getId());}});

//            insert address
            Address insertAddress = new Address(order.getAddress());
            if(insertAddress.getAddressId() == null) {
                insertAddress.getAccounts().add(account);
                Address insertedAddress = addressRepository.save(insertAddress);

                account.getAddresses().add(insertedAddress);
                accountService.save(account);
            }
            insertingOrder.setAddress(insertAddress.toString());

            if(order.getCoupon() != null) {
                insertingOrder.setCoupon(new Coupon(order.getCoupon()));
                insertingOrder.setCouponCode(order.getCoupon().getCode());

                insertingOrder.setCouponDiscount(
                        order.getCoupon().getCouponType().getTypeName().equals("Fixed") ? "$" + order.getCoupon().getDiscount() : order.getCoupon().getDiscount() + "%"
                );
            }

            insertingOrder.setOrderStatus(new OrderStatus(order.getOrderStatus()));
            insertingOrder.setPaymentMethod(new PaymentMethod(order.getPaymentMethod()));
            insertingOrder.setTotalPrice(order.getTotalPrice());
            insertingOrder.setTotalQuantity(order.getTotalQuantity());

            String trackingNumber = UUID.randomUUID().toString().replace("-", "");
            insertingOrder.setOrderTrackingNumber(trackingNumber);
            insertingOrder.setCreatedAt(new Date());
            insertingOrder.setUpdatedAt(new Date());

            Set<OrderDetail> orderDetails = new HashSet<>();
            for (ProductRequestDTO product : order.getProducts()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(new Product() {{setProductId(product.getProductId());}});
                orderDetail.setProductName(product.getProductName());
                orderDetail.setQuantity(product.getQuantity());
                orderDetail.setSize(product.getProductVariant().getProductSize().getSizeName());
                orderDetail.setPrice(product.getPrice());
                orderDetail.setOrder(insertingOrder);
                orderDetails.add(orderDetail);
            }
            insertingOrder.setOrderDetails(orderDetails);
            orderRepository.save(insertingOrder);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateStatus(Order order, OrderStatus orderStatus) {
        try {
            Order oldOrder = orderRepository.findById(order.getOrderId()).get();
            oldOrder.setOrderStatus(orderStatus);
            orderRepository.save(oldOrder);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateStatus(List<Order> orders, OrderStatus orderStatus) {
        try {
            Boolean result = true;
            for (Order order : orders) {
                if(!updateStatus(order, orderStatus)) {
                    result = false;
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long countCompleted() {
        try {
            return (long) orderRepository.findAll()
                    .stream()
                    .filter(order -> order.getOrderStatus().getStatusName().equals("Completed"))
                    .collect(Collectors.toList())
                    .size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long countProductSold() {
        try {
            return (long) orderRepository.findAll()
                    .stream()
                    .filter(order -> order.getOrderStatus().getStatusName().equals("Completed"))
                    .mapToInt(order -> {
                        return order.getOrderDetails().stream()
                                .mapToInt(OrderDetail::getQuantity)
                                .sum();
                    })
                    .sum();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long countOrdersLastMonth() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfMonth = calendar.getTime();

        calendar.add(Calendar.MONTH, 1);
        Date startOfNextMonth = calendar.getTime();

        return orderRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(startOfMonth, startOfNextMonth);
    }

    @Override
    public Long countOrdersThisWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startOfWeek = calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = calendar.getTime();

        return orderRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(startOfWeek, endOfWeek);
    }

    @Override
    public Long countOrdersToday() {
        return orderRepository.countByCreatedAt(new Date());
    }

    @Override
    public Page<OrderFindAllDTO> findOrders(Pageable pageable, Integer accountId, String orderBy, String searchTerm) {
        Page<Order> orderPages = orderRepository.findAll(OrderSpecification.withFilters(accountId, searchTerm, orderBy), pageable);

        List<OrderFindAllDTO> dtoList = orderPages.stream()
                .map(OrderFindAllDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, orderPages.getTotalElements());
    }

    private BigDecimal calcPriceAfterSale(BigDecimal rootPrice, ProductSale productSale) {
        if(productSale == null) return rootPrice;

        if(productSale.getSaleName().equals("Fixed")) {
            return (rootPrice.subtract(BigDecimal.valueOf((long) productSale.getDiscount())));
        } else if(productSale.getSaleName().equals("Percent")) {
            return (rootPrice.multiply(BigDecimal.valueOf(1-productSale.getDiscount()/100)));
        }
        return rootPrice;
    }
}
