package id.ac.ui.cs.advprog.rating_service.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionManager {
    private UUID currentSessionId;

    public synchronized UUID getCurrentSessionId() {
        if (currentSessionId == null) {
            currentSessionId = UUID.randomUUID();
        }
        return currentSessionId;
    }

    public synchronized UUID checkout() {
        UUID oldSession = currentSessionId;
        currentSessionId = UUID.randomUUID();
        return oldSession;
    }
}
