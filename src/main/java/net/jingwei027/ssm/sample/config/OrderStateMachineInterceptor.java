package net.jingwei027.ssm.sample.config;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jingwei027.ssm.sample.OrderPersistService;
import net.jingwei027.ssm.sample.consts.OrderEvents;
import net.jingwei027.ssm.sample.consts.OrderStates;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStateMachineInterceptor extends StateMachineInterceptorAdapter<OrderStates, OrderEvents> {
    
    private final OrderPersistService orderPersistService;
    
    @Override
    public Message<OrderEvents> preEvent(Message<OrderEvents> message, StateMachine<OrderStates, OrderEvents> stateMachine) {
        log.info("preEvent {}", message.getPayload());
        return message;
    }
    
    @Override
    public void preStateChange(
        State<OrderStates, OrderEvents> state,
        Message<OrderEvents> message,
        Transition<OrderStates, OrderEvents> transition,
        StateMachine<OrderStates, OrderEvents> stateMachine,
        StateMachine<OrderStates, OrderEvents> rootStateMachine)
    {
        log.info("preStateChange {}", getOrderState(state));
        orderPersistService.persistState(state, message, transition, rootStateMachine);
    }
    
    private OrderStates getOrderState(State<OrderStates, OrderEvents> state) {
        return state == null ? null : state.getId();
    }
    
}
