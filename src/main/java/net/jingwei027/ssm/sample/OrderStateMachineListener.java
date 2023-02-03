//package net.jingwei027.ssm.sample;
//
//import org.springframework.statemachine.listener.StateMachineListenerAdapter;
//import org.springframework.statemachine.state.State;
//
//import lombok.extern.slf4j.Slf4j;
//import net.jingwei027.ssm.sample.consts.OrderEvents;
//import net.jingwei027.ssm.sample.consts.OrderStates;
//
//@Slf4j
//public class OrderStateMachineListener extends StateMachineListenerAdapter<OrderStates, OrderEvents> {
//
//    @Override
//    public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
//        log.info("stateChanged {} to {}", getOrderState(from), getOrderState(to));
//    }
//    
//    private OrderStates getOrderState(State<OrderStates, OrderEvents> state) {
//        return state == null ? null : state.getId();
//    }
//    
//}
