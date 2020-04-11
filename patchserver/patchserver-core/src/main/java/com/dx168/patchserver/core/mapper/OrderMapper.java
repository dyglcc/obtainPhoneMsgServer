package com.dx168.patchserver.core.mapper;

import com.dx168.patchserver.core.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by tong on 16/10/26.
 */
@Mapper
public interface OrderMapper {

    Order findById(Integer id);
    Order findOrderByParentId(Integer id);

    Integer insert(Order order);

    List<Order> findAllOrders(String email);
    List<Order> findRunningOrders(String email);
    List<Order> findUnpaysOrder(String email);
    List<Order> findPaidOrders(String email);

    void deleteById(Integer id);

    void setDone2LastOrder(Order order);
    // 更新流量
    void updateTraffic(Order order);

    void purchase(Order order);

    void updateOrder2Using(Order order);
}
