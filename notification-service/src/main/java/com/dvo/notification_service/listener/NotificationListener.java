package com.dvo.notification_service.listener;

import com.dvo.notification_service.event.DeliveryAssignmentsEvent;
import com.dvo.notification_service.event.OrderChangedOrderItem;
import com.dvo.notification_service.event.OrderStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationListener {
    @KafkaListener(topics = "order-status-topic", groupId = "notification-group", containerFactory = "orderStatusChangedKafkaListenerContainerFactory")
    public void listenOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Notification: заказ {} изменил статус на {}. Уведомляем клиента {}",
                event.getOrderId(), event.getStatus(), event.getCustomerId());

    }

    @KafkaListener(topics = "order-changed-order-item-topic", groupId = "notification-group", containerFactory = "orderItemAddedKafkaListenerContainerFactory")
    public void listenOrderChangedOrderItem(OrderChangedOrderItem event){
        log.info("Notification: в заказе {} {} подзаказ {}. Уведомляем клиента {}",
                event.getOrderId(), event.getAction(), event.getOrderItemId(), event.getCustomerId());
    }

    @KafkaListener(topics = "delivery-assignment-topic", groupId = "notification-group", containerFactory = "deliveryAssignmentsKafkaListenerContainerFactory")
    public void listenDeliveryAssignments(DeliveryAssignmentsEvent event){
        log.info("Notification: курьеру {} назначена доставка {} в {}. Уведомляем клиента {}",
                event.getCourierId(), event.getOrderId(), event.getAssignedAt(), event.getCustomerId());
    }
}
