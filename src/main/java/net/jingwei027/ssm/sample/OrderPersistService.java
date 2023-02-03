package net.jingwei027.ssm.sample;

import java.time.LocalDateTime;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import net.jingwei027.ssm.sample.consts.OrderEvents;
import net.jingwei027.ssm.sample.consts.OrderStates;
import net.jingwei027.ssm.sample.domain.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPersistService {
    
    private final OrderRepository orderRepo;
    
    @Transactional
    public void persistState(
        State<OrderStates, OrderEvents> state,
        Message<OrderEvents> message,
        Transition<OrderStates, OrderEvents> transition,
        StateMachine<OrderStates, OrderEvents> stateMachine)
    {
        val orderId = message.getHeaders().get("orderId", Long.class);
        val orderDb = orderRepo.findById(orderId).get();
            orderDb.setState(state.getId().name());
            orderDb.setUpdateTime(LocalDateTime.now());
        
        orderRepo.save(orderDb);
    }

}
