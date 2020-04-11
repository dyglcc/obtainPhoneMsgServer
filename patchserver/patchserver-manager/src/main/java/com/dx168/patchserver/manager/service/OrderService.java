package com.dx168.patchserver.manager.service;

import com.dx168.patchserver.core.domain.Order;
import com.dx168.patchserver.core.mapper.OrderMapper;
import com.dx168.patchserver.core.mapper.UserMapper;
import com.dx168.patchserver.core.utils.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**SubmitTicket
 * Created by tong on 16/10/25.
 */
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public void setDone2LastOrder(Order order){
        orderMapper.setDone2LastOrder(order);
    }
    public void updateTraffic(Order order){
        orderMapper.updateTraffic(order);
    }

    public Order findById(Integer id){
        return orderMapper.findById(id);
    }
    public Order findOrderByParentId(Integer id){
       return orderMapper.findOrderByParentId(id);
    }

    public Integer insert(Order order){
        return orderMapper.insert(order);
    }

    public List<Order> findAllOrders(String email){
        return orderMapper.findAllOrders(email);
    }
    public List<Order> findPaidOrders(String email){
        return orderMapper.findPaidOrders(email);
    }

    public void deleteById(Integer id){
        orderMapper.deleteById(id);
    }


    public void purchase(Order order){
        orderMapper.purchase(order);
    }


    public  List<Order> findUnpaysOrder(String email) {
        return orderMapper.findUnpaysOrder(email);
    }
    public  List<Order> findRunningOrders(String email) {
        return orderMapper.findRunningOrders(email);
    }

    public void updateOrder2Using(Order order) {
        orderMapper.updateOrder2Using(order);
    }
}
