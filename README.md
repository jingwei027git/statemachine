# README #

Spring StateMachine Choice

##

```
S1 --(E2)-> S4
 |
(E1)
 |       + first(condition: false) -> S2A -> S4
S2 ------| then (condition: true) -> S2B -> S1
 |       + else -> S3

S3
 |
(E3)
 |
S4
```

```
shell:>order create
Hibernate: insert into order_invoice (create_time, state, update_time, id) values (?, ?, ?, ?)
OrderInvoice(id=2253, createTime=2023-02-03T15:13:09.547994, updateTime=2023-02-03T15:13:09.548020, state=S1)
shell:>
shell:>sm event 2253 E1
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
2023-02-03T15:13:27.652+08:00  INFO 13531 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : ACTION S1-S2(E1)
2023-02-03T15:13:27.665+08:00  INFO 13531 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : ACTION S2-S2B (true)
2023-02-03T15:13:27.665+08:00  INFO 13531 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange S2B
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
2023-02-03T15:13:27.724+08:00  INFO 13531 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : ACTION S2B-S1
2023-02-03T15:13:27.725+08:00  INFO 13531 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange S1
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
SUBSCRIBE
shell:>
shell:>order read 2253
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
OrderInvoice(id=2253, createTime=2023-02-03T15:13:09.547994, updateTime=2023-02-03T15:13:27.737749, state=S1)
shell:>
shell:>sm event 2253 E2
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
2023-02-03T15:13:38.906+08:00  INFO 13531 --- [           main] n.j.s.s.config.OrderStateMachineConfig   : ACTION S1-S4(E2)
2023-02-03T15:13:38.906+08:00  INFO 13531 --- [           main] n.j.s.s.c.OrderStateMachineInterceptor   : preStateChange S4
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
Hibernate: update order_invoice set create_time=?, state=?, update_time=? where id=?
SUBSCRIBE
shell:>
shell:>order read 2253
Hibernate: select o1_0.id,o1_0.create_time,o1_0.state,o1_0.update_time from order_invoice o1_0 where o1_0.id=?
OrderInvoice(id=2253, createTime=2023-02-03T15:13:09.547994, updateTime=2023-02-03T15:13:38.917233, state=S4)
shell:>
```
