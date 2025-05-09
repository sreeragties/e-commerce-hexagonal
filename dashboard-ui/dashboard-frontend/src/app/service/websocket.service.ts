import { Injectable } from '@angular/core';
import { RxStomp } from '@stomp/rx-stomp';
import { Subject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private rxStomp: RxStomp;
  private messageSubject: Subject<any> = new Subject<any>();
  public messages$: Observable<any> = this.messageSubject.asObservable();

  constructor() {
    this.rxStomp = new RxStomp();
    this.rxStomp.configure({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: {},
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      reconnectDelay: 5000,
      debug: (msg) => {
        console.log('STOMP Debug:', msg);
      }
    });

    this.rxStomp.activate();
  }

  connect(): void {
    if (!this.rxStomp.connected()) {
      this.rxStomp.activate();
    }
  }

  disconnect(): void {
    if (this.rxStomp.connected()) {
      this.rxStomp.deactivate();
    }
  }

  subscribe(topic: string): Observable<any> {
    return this.rxStomp.watch(topic).pipe(
      map(message => {
        console.log(`Received message on topic ${topic}:`, message.body);
        try {
          return JSON.parse(message.body);
        } catch (e) {
          console.error('Error parsing WebSocket message:', e);
          return null;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
