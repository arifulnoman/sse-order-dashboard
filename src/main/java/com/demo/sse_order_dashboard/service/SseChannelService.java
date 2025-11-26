package com.demo.sse_order_dashboard.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseChannelService {

	private final Map<String, Set<SseEmitter>> channels = new ConcurrentHashMap<>();

	public SseEmitter subscribe(String channel, long timeoutMs) {
        return createEmitter(channel, timeoutMs);
    }

	private SseEmitter createEmitter(String channel, long timeoutMs) {
        SseEmitter emitter = new SseEmitter(timeoutMs);

        channels
            .computeIfAbsent(channel, c -> ConcurrentHashMap.newKeySet())
            .add(emitter);

        emitter.onCompletion(() -> removeEmitter(channel, emitter, "completion"));
        emitter.onTimeout(() -> removeEmitter(channel, emitter, "timeout"));
        emitter.onError(e -> removeEmitter(channel, emitter, "error: " + e));

        return emitter;
    }

	private void removeEmitter(String channel, SseEmitter emitter, String reason) {
        Set<SseEmitter> emitterSet = channels.get(channel);
        if (emitterSet != null) {
            emitterSet.remove(emitter);
        }
    }

	/**
     * Send an event with a custom name to all subscribers of a channel.
     */
    public void sendToChannel(String channel, String eventName, Object data) {
        Set<SseEmitter> emitterSet = channels.get(channel);
        if (emitterSet == null || emitterSet.isEmpty()) {
            return;
        }

        for (SseEmitter emitter : emitterSet.toArray(new SseEmitter[0])) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                // client likely disconnected; clean it up
                removeEmitter(channel, emitter, "send-failed: " + e.getMessage());
            }
        }
    }

    public int getSubscriberCount(String channel) {
        Set<SseEmitter> emitterSet = channels.get(channel);
        return emitterSet != null ? emitterSet.size() : 0;
    }
}
