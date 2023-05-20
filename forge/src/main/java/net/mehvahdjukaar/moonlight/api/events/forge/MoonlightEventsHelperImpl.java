package net.mehvahdjukaar.moonlight.api.events.forge;

import net.mehvahdjukaar.moonlight.api.events.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public class MoonlightEventsHelperImpl {

    private static final Map<Class<? extends SimpleEvent>, Queue<Consumer<? extends SimpleEvent>>> LISTENERS = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends SimpleEvent> void addListener(Consumer<T> listener, Class<T> eventClass) {
        //hacky
        if (eventClass == IVillagerBrainEvent.class) {
            Consumer<VillagerBrainEvent> eventConsumer = e -> listener.accept((T) e);
            MinecraftForge.EVENT_BUS.addListener(eventConsumer);
        } else if (eventClass == IFireConsumeBlockEvent.class) {
            Consumer<FireConsumeBlockEvent> eventConsumer = e -> listener.accept((T) e);
            MinecraftForge.EVENT_BUS.addListener(eventConsumer);
        } else if (eventClass == IDropItemOnDeathEvent.class) {
            Consumer<DropItemOnDeathEvent> eventConsumer = e -> listener.accept((T) e);
            MinecraftForge.EVENT_BUS.addListener(eventConsumer);
        } else if (eventClass == ILightningStruckBlockEvent.class) {
            Consumer<LightningStruckBlockEvent> eventConsumer = e -> listener.accept((T) e);
            MinecraftForge.EVENT_BUS.addListener(eventConsumer);
        } else {
            //other 2 events dont work on forge bus for some reason... Randomly too
            LISTENERS.computeIfAbsent(eventClass, ev -> new ConcurrentLinkedDeque<>()).add(listener);
        }
    }

    public static <T extends SimpleEvent> void postEvent(T event, Class<T> eventClass) {
        if (event instanceof Event e) {
            MinecraftForge.EVENT_BUS.post(e);
        } else {
            var consumers = LISTENERS.get(eventClass);
            if (consumers != null) {
                ((Queue<Consumer<T>>) (Object) consumers).forEach(e -> e.accept(event));
            }
        }
    }

}
