package net.jingwei027.ssm.sample;

import java.util.function.Function;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.monitor.AbstractStateMachineMonitor;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import lombok.extern.slf4j.Slf4j;
import net.jingwei027.ssm.sample.consts.OrderEvents;
import net.jingwei027.ssm.sample.consts.OrderStates;
import reactor.core.publisher.Mono;

@Slf4j
public class OrderStateMachineMonitor extends AbstractStateMachineMonitor<OrderStates, OrderEvents> {
    
    @Override
    public void transition(
            StateMachine<OrderStates, OrderEvents> stateMachine,
            Transition<OrderStates, OrderEvents> transition,
            long duration)
    {
        log.info("monitor-transition state: {}, transition: {} {}",
                getOrderState(stateMachine.getState()),
                getOrderState(transition.getSource()),
                getOrderState(transition.getTarget())
                );
    }

    @Override
    public void action(
            StateMachine<OrderStates, OrderEvents> stateMachine,
            Function<StateContext<OrderStates, OrderEvents>, Mono<Void>> action,
            long duration)
    {
        log.info("monitor-transition state: {}, duration: {}",
                getOrderState(stateMachine.getState()),
                duration
                );
    }
    
    private OrderStates getOrderState(State<OrderStates, OrderEvents> state) {
        return state == null ? null : state.getId();
    }

}
