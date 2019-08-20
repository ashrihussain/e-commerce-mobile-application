package com.example.eea.eea.Model;

import java.util.List;


public class Order {


        private Integer id;
        private String order_date;
        private String status;
        private String comment;

        private User user;
        private List<OrderItem> orderItems;


        public Order() {
        }

        public Order(String order_date, String status, String comment, User user, List<OrderItem> orderItems) {
            this.order_date = order_date;
            this.status = status;
            this.comment = comment;
            this.user = user;
            this.orderItems = orderItems;
        }

        public Order(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
    }
