import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {FormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';
import {OrderTrackerService} from '../../service/order-tracker.service';
import {WebSocketService} from '../../service/websocket.service';
import {StompSubscription} from '@stomp/stompjs';
import {Subject, takeUntil} from 'rxjs';

export enum OrderState {
  CREATED = 'CREATED',
  OFFER_APPLIED = 'OFFER_APPLIED',
  PAYMENT_APPROVED = 'PAYMENT_APPROVED',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  COMPLETED = 'COMPLETED',
}

@Component({
  selector: 'app-order-tracker',
  standalone: true,
  imports: [
    NgIf,
    FormsModule,
    MatIcon,
    NgClass,
  ],
  templateUrl: './order-tracker.component.html',
  styleUrl: './order-tracker.component.scss'
})
export class OrderTrackerComponent implements OnInit, OnDestroy {

  constructor(private orderTrackerService: OrderTrackerService,
              private webSocketService: WebSocketService) {}

  @Input() currentOrderState: OrderState | null = null;
  @Input() orderId: string | null = null;

  private orderStatusSubscription: StompSubscription | null = null;
  private destroy$ = new Subject<void>();

  itemId: string = '';
  customerId: string = '';

  isLoading: boolean = false;

  OrderState = OrderState;

  isStepComplete(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex >= stepStateIndex;
  }

  isStepActive(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex === stepStateIndex - 1;
  }

  isLineComplete(stepState: OrderState): boolean {
    if (this.currentOrderState === null) {
      return false;
    }
    const currentStateIndex = Object.values(OrderState).indexOf(this.currentOrderState);
    const stepStateIndex = Object.values(OrderState).indexOf(stepState);
    return currentStateIndex > stepStateIndex;
  }

  createOrderAndTrack(): void {
    this.orderTrackerService.createOrder(this.customerId, this.itemId).subscribe({
      next: (order: any) => {
        console.log('Order created successfully:', order);
        this.orderId = order.id;
        this.currentOrderState = order.orderState;
        if (this.orderId) {
          this.subscribeToOrderStatus(this.orderId);
        }
      },
    });
  }

  private subscribeToOrderStatus(orderId: string): void {
    this.unsubscribeFromOrderStatus();

    const topic = `/topic/orders/status/${orderId}`;
    console.log(`Attempting to subscribe to WebSocket topic: ${topic}`);

    this.orderStatusSubscription = this.webSocketService.subscribe(topic);

    this.webSocketService.messages$
      .pipe(takeUntil(this.destroy$))
      .subscribe((newState: OrderState) => {
        console.log('Received order state update via WebSocket:', newState);
        this.currentOrderState = newState;
      });
  }

  private unsubscribeFromOrderStatus(): void {
    if (this.orderStatusSubscription) {
      console.log('Unsubscribing from WebSocket topic.');
      this.orderStatusSubscription.unsubscribe();
      this.orderStatusSubscription = null;
    }
  }

  ngOnInit(): void {
    if (this.orderId) {
      this.subscribeToOrderStatus(this.orderId);
    }
  }

  ngOnDestroy(): void {
    this.unsubscribeFromOrderStatus();
    this.destroy$.next();
    this.destroy$.complete();
  }

}
