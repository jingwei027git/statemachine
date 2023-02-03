package net.jingwei027.ssm.sample.config;

import static net.jingwei027.ssm.sample.consts.OrderEvents.E1;
import static net.jingwei027.ssm.sample.consts.OrderEvents.E3;
import static net.jingwei027.ssm.sample.consts.OrderStates.S1;
import static net.jingwei027.ssm.sample.consts.OrderStates.S2;
import static net.jingwei027.ssm.sample.consts.OrderStates.S2A;
import static net.jingwei027.ssm.sample.consts.OrderStates.S2B;
import static net.jingwei027.ssm.sample.consts.OrderStates.S3;
import static net.jingwei027.ssm.sample.consts.OrderStates.S4;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import lombok.extern.slf4j.Slf4j;
import net.jingwei027.ssm.sample.OrderStateMachineListener;
import net.jingwei027.ssm.sample.consts.OrderEvents;
import net.jingwei027.ssm.sample.consts.OrderStates;

@Slf4j
@Configuration
@EnableStateMachineFactory(contextEvents = false)
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
        config
                .withConfiguration()
                .listener(new OrderStateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(S1)
                .junction(S2)
                .state(S3)
                .end(S4)
                .states(EnumSet.allOf(OrderStates.class))
                ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(S1)
                    .target(S2)
                    .event(E1)
                    .action(action -> {
                        log.info("ACTION S1-S2(E1)");
                    })
                    .and()
                .withChoice()
                    .source(S2)
                    .first(S2A, context -> false, action -> {
                        log.info("ACTION S2-S2A");
                    })
                    .then(S2B, context -> false, action -> {
                        log.info("ACTION S2-S2B");
                    })
                    .last(S3, action -> {
                        log.info("ACTION S2-S3");
                    })
                    .and()
                .withExternal()
                    .source(S3)
                    .target(S4)
                    .event(E3)
                ;
    }

}
