package net.jingwei027.ssm.sample.config;

import static net.jingwei027.ssm.sample.consts.OrderEvents.E1;
import static net.jingwei027.ssm.sample.consts.OrderEvents.E2;
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
import net.jingwei027.ssm.sample.consts.OrderEvents;
import net.jingwei027.ssm.sample.consts.OrderStates;

@Slf4j
@Configuration
@EnableStateMachineFactory(contextEvents = false)
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(S1)
                .choice(S2)
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
                    .action(
                        c -> { log.info("ACTION S1-S2(E1)"); throw new RuntimeException("S1-S2(E1) failure"); },
                        c -> { log.error("ACTION S1-S2(E1): " + c.getException().getMessage()); }) // handle transition action exception
                    .and()
                .withExternal()
                    .source(S1)
                    .target(S4)
                    .event(E2)
                    .action(c -> log.info("ACTION S1-S4(E2)"))
                    .and()
                .withChoice()
                    .source(S2)
                    .first(S2A,
                        c -> false,
                        c -> log.info("ACTION S2-S2A (true)"))
                    .then(S2B,
                        c -> true,
                        c -> log.info("ACTION S2-S2B (true)"))
                    .last(S3,
                        c -> log.info("ACTION S2-S3 (true)"))
                    .and()
                .withExternal()
                    .source(S2A)
                    .target(S4)
                    .action(c -> log.info("ACTION S2A-S4"))
                    .and()
                .withExternal()
                    .source(S2B)
                    .target(S1)
                    .action(c -> log.info("ACTION S2B-S1"))
                    .and()
                .withExternal()
                    .source(S3)
                    .target(S4)
                    .event(E3)
                    .action(c -> log.info("ACTION S3-S4"))
                ;
    }

}
