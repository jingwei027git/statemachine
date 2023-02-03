package net.jingwei027.ssm.shell;

import java.time.LocalDateTime;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jingwei027.ssm.sample.config.OrderStateMachineInterceptor;
import net.jingwei027.ssm.sample.consts.OrderEvents;
import net.jingwei027.ssm.sample.consts.OrderStates;
import net.jingwei027.ssm.sample.domain.entity.OrderInvoice;
import net.jingwei027.ssm.sample.domain.repository.OrderRepository;
import reactor.core.publisher.Mono;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class MyCommands {

    private final StateMachineFactory<OrderStates, OrderEvents> smFty;
    private final OrderRepository orderRepo;
    private final OrderStateMachineInterceptor orderStateMachineInterceptor;
    
    @ShellMethod(key = "order create", value = "Create new order")
    public String createOrder()
    {
        var order = new OrderInvoice();
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            order.setState(OrderStates.S1.name());
            
        orderRepo.save(order);
        
        return order.toString();
    }
    
    @ShellMethod(key = "order read", value = "Create new order")
    public String readOrder(
        @ShellOption(help = "orderId") Long orderId)
    {
        var orderOpt = orderRepo.findById(orderId);
        
        return orderOpt.isPresent()
            ? orderOpt.get().toString()
            : "(null)"
            ;
    }

    
    @ShellMethod(key = "sm event", value = "StateMachine send event")
    public String sendEvent(
        @ShellOption(help = "orderId") Long orderId,
        @ShellOption(help = "event") String event,
        @ShellOption(help = "payload", defaultValue = "") String payload)
    {
        var orderEvent = OrderEvents.valueOf(event);
        
        var message = newOrderEventBuilder(orderEvent, orderId).build();
        
        var sm = newStateMachine(orderId);
            sm.sendEvent(Mono.just(message)).subscribe();
        
        return "SUBSCRIBE";
    }
    
    private MessageBuilder<OrderEvents> newOrderEventBuilder(OrderEvents orderEvent, Long orderId) {
        return MessageBuilder
            .withPayload(orderEvent)
            .setHeader("orderId", orderId)
            ;
    }
    
    private StateMachine<OrderStates, OrderEvents> newStateMachine(Long orderId) {
        var order = orderRepo.findById(orderId).get();
        var sourceState = OrderStates.valueOf(order.getState());
        var dsmc = new DefaultStateMachineContext<OrderStates, OrderEvents>(sourceState, null, null, null);
        
        var sm = smFty.getStateMachine(Long.toString(orderId));
            sm.stopReactively().block();
            sm.getStateMachineAccessor().doWithAllRegions(sma -> {
                sma.addStateMachineInterceptor(orderStateMachineInterceptor);
                sma.resetStateMachineReactively(dsmc).block();
            });
            sm.startReactively().block();
        
        return sm;
    }
    
}
