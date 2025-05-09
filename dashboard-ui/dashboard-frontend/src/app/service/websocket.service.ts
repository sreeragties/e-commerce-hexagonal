import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';
import { Client, Message, StompSubscription } from '@stomp/stompjs';
import {OrderState} from '../pages/order-tracker/order-tracker.component';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private stompClient: Client;
  private messageSubject: Subject<any> = new Subject<any>();
  public messages$: Observable<any> = this.messageSubject.asObservable();

  private readonly websocketEndpoint = 'ws://localhost:8080/ws';

  constructor() {
    this.stompClient = new Client({
      brokerURL: this.websocketEndpoint,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => {
        console.log('STOMP Debug:', str);
      },
    });

    this.stompClient.onConnect = (frame) => {
      console.log('Connected to WebSocket:', frame);
    };

    this.stompClient.onDisconnect = (frame) => {
      console.log('Disconnected from WebSocket:', frame);
    };

    this.stompClient.onWebSocketError = (event) => {
      console.error('WebSocket error:', event);
    };

    this.stompClient.onStompError = (frame) => {
      console.error('STOMP error:', frame);
      console.error('Broker reported:', frame.headers['message']);
      console.error('Additional details:', frame.body);
    };

    this.stompClient.activate();
  }

  connect(): void {
    if (!this.stompClient.active) {
      this.stompClient.activate();
    }
  }
  disconnect(): void {
    if (this.stompClient.active) {
      this.stompClient.deactivate();
    }
  }

  subscribe(topic: string): StompSubscription | null {
    if (this.stompClient.active) {
      return this.stompClient.subscribe(topic, (message: Message) => {
        console.log(`Received message on topic ${topic}:`, message.body);
        try {
          const orderState: OrderState = JSON.parse(message.body);
          this.messageSubject.next(orderState);
        } catch (e) {
          console.error('Error parsing WebSocket message:', e);
        }
      });
    } else {
      console.warn('STOMP client not active. Cannot subscribe.');
      return null;
    }
  }
  ngOnDestroy(): void {
    this.disconnect();
  }
}
