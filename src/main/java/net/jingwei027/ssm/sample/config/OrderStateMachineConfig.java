package net.jingwei027.ssm.sample.config;

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
        config
                .withConfiguration()
                ;
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
        .withStates()
            .initial(OrderStates.READY, (context) -> log.info("READ initial"))
            .state(OrderStates.DEPLOY, (context) -> log.info("DEPLOY entry"), (context) -> log.info("DEPLOY exit"))
            .state(OrderStates.DONE, (context) -> log.info("DONE entry"), (context) -> log.info("DONE exit"))
            .end(OrderStates.DONE)
            .and()
            .withStates()
                .parent(OrderStates.DEPLOY)
                .initial(OrderStates.DEPLOYPREPARE, (context) -> log.info("REPLOYPREPARE initial"))
                .state(OrderStates.DEPLOYPREPARE, (context) -> log.info("DEPLOYPREPARE entry"), (context) -> log.info("DEPLOYPREPARE exit"))
                .state(OrderStates.DEPLOYEXECUTE, (context) -> log.info("DEPLOYEXECUTE entry"), (context) -> log.info("DEPLOYEXECUTE exit"));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
        .withExternal()
            .source(OrderStates.READY).target(OrderStates.DEPLOY)
            .event(OrderEvents.DEPLOY)
            .action(action -> log.info("001 ready to deploy (deploy)"))
            .and()
        .withExternal()
            .source(OrderStates.DEPLOYPREPARE)
            .target(OrderStates.DEPLOYEXECUTE)
            .action(action -> log.info("002 prepare to execute (auto)"))
            .and()
        .withExternal()
            .source(OrderStates.DEPLOYEXECUTE)
            .target(OrderStates.READY)
            .action(action -> log.info("003 execute to ready (auto)"))
            .and()
        .withExternal()
            .source(OrderStates.READY)
            .target(OrderStates.DONE)
            .event(OrderEvents.DONE)
            .action(action -> log.info("00A ready to done (done)"))
            .and()
        .withExternal()
            .source(OrderStates.DEPLOY)
            .target(OrderStates.DONE)
            .event(OrderEvents.DONE)
            .action(action -> log.info("00B deploy to done (done)"))
        ;
    }

}
