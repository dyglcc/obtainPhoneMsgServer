package com.dx168.patchserver.manager.web;

import com.dx168.patchserver.core.domain.BasicUser;
import com.dx168.patchserver.core.domain.Order;
import com.dx168.patchserver.core.utils.BeanMapConvertUtil;
import com.dx168.patchserver.core.utils.BizException;
import com.dx168.patchserver.core.utils.VStringUtils;
import com.dx168.patchserver.manager.common.*;
import com.dx168.patchserver.manager.mail.ValidateService;
import com.dx168.patchserver.manager.payperiod.PayPeridUtils;
import com.dx168.patchserver.manager.payperiod.PayPeriod;
import com.dx168.patchserver.manager.service.AccountService;
import com.dx168.patchserver.manager.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tong on 15/10/24.
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ValidateService validateService;
    @Autowired
    private AccountService accountService;
    @Value("${spring.mail.username}")
    private String from;
    @Value("${spring.mail.password}")
    private String password;

    // 新创建订单 // or renewal
    @RequestMapping(value = "/api/v1/createOrder", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse createOrder(String period, String email, String parentid) {
        RestResponse restResponse = new RestResponse();
        Order order = new Order();
//        2。年付。3。两年付款4。三年付款
//        annually 年付
//        biennially两年
//        triennially三年付费

        BasicUser basicUser = accountService.findByEmail(email);
        if (basicUser == null) {
            restResponse.setMessage("email not exist");
            return restResponse;
        }
        if (VStringUtils.isEmpty(period)) {
            restResponse.setMessage("error param period is null");
            return restResponse;
        }
        int period_value = 4;
        String descPeriod = "";
        if (period.equals("annually")) {
            period_value = 2;
            descPeriod = "年付";
        } else if (period.equals("biennially")) {
            period_value = 3;
            descPeriod = "两年付";
        } else if (period.equals("triennially")) {
            period_value = 4;
            descPeriod = "三年付";
        }else if(period.equals("halfYearly")){
            period_value = 5;
            descPeriod="半年付";
        }else if(period.equals("quarterly")){
            period_value=6;
            descPeriod = "季付";
        }else if(period.equals("monthly")){
            period_value=1;
            descPeriod="月付";
        }else if(period.equals("weekly")){
            period_value =7;
            descPeriod="周付";
        }else if(period.equals("day")){
            period_value=8;
            descPeriod="天付";
        }
        try {
            if (!VStringUtils.isEmpty(parentid)) {
                Order parentOrder = orderService.findById(Integer.parseInt(parentid));
                if (parentOrder != null) {
                    order.setAmount(parentOrder.getAmount());
                    order.setEmail(parentOrder.getEmail());
                    order.setParent_id(Integer.parseInt(parentid));
                    order.setCreated_at(parentOrder.getCreated_at());
                } else {
                    restResponse.setMessage("error param parentid is empty");
                    return restResponse;
                }
            } else { //create new order
                PayPeriod payPeriod = Constants.PayMethod.mapPrice.get(period_value);
                order.setAmount(payPeriod.getPrice());
                order.setEmail(email);
                order.setCreated_at(new Date());
            }
            order.setStart_at(new Date());
            order.setPay_period(period_value);
//            order.setCharge_at(new Date());

            order.setProduct_type(Constants.Product.PRODUCT_TYPE_PERSIONAL);
            order.setStatus(Constants.Status.order_status_unpayed);
//            order.setUuid();
//            order.setParent_id(-1);
            order.setUpdated_at(new Date());
            Calendar calendar = Calendar.getInstance();
            setEndTime(order, calendar, period_value);
            orderService.insert(order);
            SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd");
            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("order_id", order.getId());
            restResponse.getData().put("amount", order.getAmount());
            restResponse.getData().put("paid", 0);
            restResponse.getData().put("detail", "购买个人云加速服务器 " + descPeriod);
            restResponse.getData().put("orderTime", format.format(order.getCreated_at()));

//            OrderID:
//            201910201538584
//            AMT¥200.00
//            Paid0
//            Detailcloud service dyglcc 续费 12 月 dyglcc 续费 12 月
//            Order Time2019-10-20 15:38:58

            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }

    // 获取正在运行的order
    @RequestMapping(value = "/api/v1/getProducts", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getActiveOrders(HttpServletRequest req, HttpServletResponse res, String period, String email, String type) {
        RestResponse restResponse = new RestResponse();
//        2。年付。3。两年付款4。三年付款
//        annually 年付
//        biennially两年
//        triennially三年付费

        BasicUser basicUser = accountService.findByEmail(email);
        if (basicUser == null) {
            restResponse.setMessage("email not exist");
            return restResponse;
        }
        DecimalFormat df1 = new DecimalFormat("¥0.00");
        try {
            SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd");
            List<Order> orderList = orderService.findRunningOrders(email);
            List<OrderData> products = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                Order orderProduct = orderList.get(i);
                OrderData orderData = new OrderData();

                orderData.setCreateAt(format.format(orderProduct.getCharge_at()));
                this.setNextPaymentTime(orderData, orderProduct);
                orderData.setPeriod(getperiodString(orderProduct.getPay_period()));
                orderData.setpId(orderProduct.getId());

                orderData.setStatus(orderProduct.getStatus() == Constants.Status.order_status_opening ? "开通中" : "有效的");

                PayPeriod payPeriod = Constants.PayMethod.mapPrice.get(orderProduct.getPay_period());
                orderData.setPrice(df1.format(payPeriod.getPrice()));
                //product type is default string
                products.add(orderData);
            }

            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("products", products);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }

    // 获取正在运行的order
    @RequestMapping(value = "/api/v1/getAllOrders", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getAllOrders(String email, String type) {
        RestResponse restResponse = new RestResponse();
//        2。年付。3。两年付款4。三年付款
//        annually 年付
//        biennially两年
//        triennially三年付费

        BasicUser basicUser = accountService.findByEmail(email);
        if (basicUser == null) {
            restResponse.setMessage("email not exist");
            return restResponse;
        }
        DecimalFormat df1 = new DecimalFormat("0.00");
        try {
            SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd");
            List<Order> orderList = orderService.findAllOrders(email);
            List<OrderAll> products = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                Order orderProduct = orderList.get(i);
                OrderAll orderData = new OrderAll();

                orderData.setOrder_id(orderProduct.getId());
                PayPeriod payPeriod = Constants.PayMethod.mapPrice.get(orderProduct.getPay_period());
                orderData.setAmount(df1.format(payPeriod.getPrice()));
                String statusString = "";
                int status = orderProduct.getStatus();
                if (status == Constants.Status.order_status_unpayed) {
                    statusString = "未支付";
                } else if (status == Constants.Status.order_status_using) {
                    statusString = "已支付";
                } else if (status == Constants.Status.order_status_opening) {
                    statusString = "开通中";
                } else if (status == Constants.Status.order_status_done) {
                    statusString = "已过期";
                }
                orderData.setPaid(statusString);
                orderData.setDetail("购买个人云加速服务 " + getperiodString(orderProduct.getPay_period()));
                orderData.setOrderTime(format.format(orderProduct.getCreated_at()));
                orderData.setPeriod(getperiodString(orderProduct.getPay_period()));
                orderData.setExpiredAt(format.format(orderProduct.getCreated_at()));

                //product type is default string
                products.add(orderData);
            }

            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("orders", products);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }
    // 获取正在运行的order
    @RequestMapping(value = "/api/v1/getPeriods", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getPayPeriods() {
        RestResponse restResponse = new RestResponse();
        try{
            List<PayPeriod> list = PayPeridUtils.getPeriodList();

            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("periods", list);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }

    // 获取可以续费的订单，status 》 1
    @RequestMapping(value = "/api/v1/getNewalList", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse getCanNewal(HttpServletRequest req, HttpServletResponse res, String period, String email) {
        RestResponse restResponse = new RestResponse();
//        2。年付。3。两年付款4。三年付款
//        annually 年付
//        biennially两年
//        triennially三年付费

        BasicUser basicUser = accountService.findByEmail(email);
        if (basicUser == null) {
            restResponse.setMessage("email not exist");
            return restResponse;
        }
        DecimalFormat df1 = new DecimalFormat("¥0.00");
        try {
            List<Order> orderList = orderService.findPaidOrders(email);
            List<OrderCanNewal> products = new ArrayList<>();
            for (int i = 0; i < orderList.size(); i++) {
                Order orderProduct = orderList.get(i);
                OrderCanNewal orderData = new OrderCanNewal();

                this.setNextPaymentTime(orderData, orderProduct);
                PayPeriod payPeriod = Constants.PayMethod.mapPrice.get(orderProduct.getPay_period());
                orderData.setAmount(df1.format(payPeriod.getPrice()));
                orderData.setId(orderProduct.getId());
                //product type is default string
                products.add(orderData);
            }


            restResponse.setMessage(RestResponse.OK);
            restResponse.getData().put("list", products);
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }

    @RequestMapping(value = "/api/v1/purchase", method = RequestMethod.POST)
    public @ResponseBody
    RestResponse purchase(HttpServletRequest req, HttpServletResponse res, String email, int orderid) {
        RestResponse restResponse = new RestResponse();
        BasicUser basicUser = accountService.findByEmail(email);
        if (basicUser == null) {
            restResponse.setMessage("email not exist");
            return restResponse;
        }
        Order order = orderService.findById(orderid);
        try {
            Order parentOrder = orderService.findOrderByParentId(order.getParent_id());
            order.setCharge_at(new Date());
            order.setStatus(Constants.Status.order_status_opening);
            order.setCharge_at(new Date());
            int period = order.getPay_period();
            if (parentOrder != null) {
                order.setUuid(parentOrder.getUuid());
                order.setStart_at(parentOrder.getStart_at());
                if (parentOrder.getStatus() == Constants.Status.order_status_done) {
                    Calendar calendar = Calendar.getInstance();
                    setEndTime(order, calendar, period);
                } else if (parentOrder.getStatus() == Constants.Status.order_status_using) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(parentOrder.getEnd_at());
                    setEndTime(order, calendar, period);
                } else if (parentOrder.getStatus() == Constants.Status.order_status_unpayed) {
                    restResponse.setMessage("请先支付未支付订单");
                    return restResponse;
                } else if (parentOrder.getStatus() == Constants.Status.order_status_opening) {
                    restResponse.setMessage("订单正在开通中，开通以后可以续费");
                    return restResponse;
                }
                orderService.setDone2LastOrder(parentOrder);
            } else {
                order.setUuid(UUID.randomUUID().toString());
                order.setStart_at(new Date());
                Calendar calendar = Calendar.getInstance();
                setEndTime(order, calendar, period);
            }
            order.setUpdated_at(new Date());
            orderService.purchase(order);
            // sendemail to administor
            gmailSender(order.getEmail(), orderid, order.getAmount(), res, req);

            // sendemail

            restResponse.setMessage(RestResponse.OK);
            restResponse.setData(BeanMapConvertUtil.convertBean2Map(order));
            return restResponse;
        } catch (BizException e) {
            e.printStackTrace();
            restResponse.setMessage(e.getMessage());
            return restResponse;
        } finally {
            restResponse.toString();
        }
    }


    private void setEndTime(Order order, Calendar calendar, int period) {
//        public static final int pay_period_year = 2;
//        public static final int pay_period_2year = 3;
//        public static final int pay_period_3year = 4;
//        public static final int pay_period_half_year = 5;
//        public static final int pay_period_season_year = 6;
//        public static final int pay_period_month = 1;
//        public static final int pay_period_week = 7;
//        public static final int pay_period_day = 8;
        if (period == Constants.PayMethod.pay_period_year) {
            calendar.add(Calendar.YEAR, 1);
        } else if (period == Constants.PayMethod.pay_period_2year) {
            calendar.add(Calendar.YEAR, 2);
        } else if (period == Constants.PayMethod.pay_period_3year) {
            calendar.add(Calendar.YEAR, 3);
        } else if(period == Constants.PayMethod.pay_period_half_year){
            calendar.add(Calendar.MONTH,6);
        } else if(period == Constants.PayMethod.pay_period_season_year){
            calendar.add(Calendar.MONTH,3);
        } else if (period == Constants.PayMethod.pay_period_month) {
            calendar.add(Calendar.MONTH, 1);
        } else if(period == Constants.PayMethod.pay_period_week){
            calendar.add(Calendar.DATE,7);
        }else if(period == Constants.PayMethod.pay_period_day){
            calendar.add(Calendar.DATE,1);
        }
        order.setEnd_at(calendar.getTime());
    }

    private void setNextPaymentTime(OrderData orderData, Order order) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getEnd_at());
        orderData.setNextPayment(dateFormat.format(calendar.getTime()));
    }

    private void setNextPaymentTime(OrderCanNewal orderData, Order order) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getEnd_at());
        orderData.setNextPayment(dateFormat.format(calendar.getTime()));
    }

    public String getperiodString(int period) {
        if (period == 2) {
            return "年付";
        } else if (period == 3) {
            return "两年付";
        } else if (period == 4) {
            return "三年付";
        }
        return "年付";
    }


    //gmail邮箱的TLS方式
    public static void gmailtls(Properties props) {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /*
     * 通过gmail邮箱发送邮件
     */
    public void gmailSender(String email, int orderid, BigDecimal amount, HttpServletResponse res, HttpServletRequest req) {

        // Get a Properties object
        Properties props = new Properties();
        //选择ssl方式
        gmailtls(props);

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        // -- Create a new message --
        Message msg = new MimeMessage(session);
        String appUrl = req.getScheme() + "://" + req.getServerName();
        // -- Set the FROM and TO fields --
        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(from));
            msg.setSubject("有用户付款：" + email);
            msg.setText("用户已付款，金额是：" + amount + "点击此链接通过审核: \n" + appUrl + "/confirm.html?orderid=" + orderid);
            msg.setSentDate(new Date());
            validateService.sendUserPaidEmail(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Message sent.");
    }


}
