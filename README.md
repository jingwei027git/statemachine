# README #

Spring StateMachine CASE: parent

## 狀態流程

READY -(DEPLOY)-> DEPLOY { DEPLOY_PREPARE(initial) -> DEPLOY_EXECUTE } -(DONE)-> DONE

DEPLOY_EXECUTE -> READY

READY -(DONE)-> DONE

### 說明

如果 transitions 只有定義 source , target 沒指定 event 代表了一進到 source state 就會馬上轉成 target state

由於 DEPLOY_PREPARE 是 DEPLOY 的子初始狀態，所以當狀態 轉到 DEPLOY 時會馬上切成 DEPLOY_PREPARE 但這不會觸發狀態改變，因為 initial 是屬於虛擬狀態。

如果使用子狀態，將狀態進到 DEPLOY 時會變成 DEPLOY_PREPARE 由於其為虛擬狀態所以不會觸發 changeState 也就不會保存到 DB，這不符合使用場景。
所以最好再多個無需事件觸發的過渡狀態 DEPLOY_PREPARE -> DEPLOY_EXECUTE, 這樣就會觸發 changeState, 總的來說當狀態轉到 DEPLOY 時最終狀態會改變至 DEPLOY_EXECUTE 且與 DB 狀態一致。

```
shell:>order create
Hibernate: select next_val as id_val from order_invoice_seq for update
Hibernate: update order_invoice_seq set next_val= ? where next_val=?
Hibernate: insert into order_invoice (create_time, state, update_time, id) values (?, ?, ?, ?)
OrderInvoice(id=1652, createTime=2023-02-03T13:48:47.783782, updateTime=2023-02-03T13:48:47.783817, state=READY)
shell:>
shell:>sm event 1652 DEPLOY
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
2023-02-03T13:49:02.179+08:00  INFO 12434 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preEvent DEPLOY
2023-02-03T13:49:02.208+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : 001 ready to deploy (deploy)
2023-02-03T13:49:02.210+08:00  INFO 12434 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange DEPLOY
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
2023-02-03T13:49:02.254+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DEPLOY entry
2023-02-03T13:49:02.258+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : REPLOYPREPARE initial
2023-02-03T13:49:02.260+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DEPLOYPREPARE entry
2023-02-03T13:49:02.266+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : 002 prepare to execute (auto)
2023-02-03T13:49:02.267+08:00  INFO 12434 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange DEPLOYEXECUTE
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
2023-02-03T13:49:02.291+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DEPLOYPREPARE exit
2023-02-03T13:49:02.295+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DEPLOYEXECUTE entry
2023-02-03T13:49:02.298+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : 003 execute to ready (auto)
2023-02-03T13:49:02.300+08:00  INFO 12434 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange READY
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
2023-02-03T13:49:02.330+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DEPLOYEXECUTE exit
2023-02-03T13:49:02.331+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DEPLOY exit
SUBSCRIBE
shell:>
shell:>order read 1652
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
OrderInvoice(id=1652, createTime=2023-02-03T13:48:47.783782, updateTime=2023-02-03T13:49:02.308027, state=READY)
shell:>
shell:>sm event 1652 DONE
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
2023-02-03T13:49:37.011+08:00  INFO 12434 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preEvent DONE
2023-02-03T13:49:37.013+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : 00A ready to done (done)
2023-02-03T13:49:37.013+08:00  INFO 12434 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange DONE
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
2023-02-03T13:49:37.062+08:00  INFO 12434 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : DONE entry
SUBSCRIBE
shell:>
shell:>order read 1652
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
OrderInvoice(id=1652, createTime=2023-02-03T13:48:47.783782, updateTime=2023-02-03T13:49:37.025980, state=DONE)
shell:>
```
