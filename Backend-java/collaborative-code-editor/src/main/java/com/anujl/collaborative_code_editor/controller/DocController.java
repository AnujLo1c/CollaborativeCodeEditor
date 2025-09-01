package com.anujl.collaborative_code_editor.controller;


import com.anujl.collaborative_code_editor.dto.wc.DocSnapshot;
import com.anujl.collaborative_code_editor.dto.wc.EditMessage;
import com.anujl.collaborative_code_editor.dto.wc.ServerAck;
import com.anujl.collaborative_code_editor.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class DocController {


    private final DocumentService service;


    private final SimpMessagingTemplate broker;

    @Autowired
    public DocController(DocumentService service, SimpMessagingTemplate broker) {
        this.service = service;
        this.broker = broker;
    }

    // STOMP: send to /app/doc/{docId}/edit
    @MessageMapping("/doc/{docId}/edit")
    public void edit(@DestinationVariable String docId, @Payload @Valid EditMessage payload) {
        try {
            DocSnapshot updated = service.apply(docId, payload);
            if (updated == null) {
                var snap = service.get(docId);
                var ack = new ServerAck(docId, snap.getVersion(), "resync", "version mismatch", snap);
                broker.convertAndSend("/topic/doc/" + docId, ack);
                return;
            }
            var ack = new ServerAck(docId, updated.getVersion(), "ok", "applied", null);
            broker.convertAndSend("/topic/doc/" + docId, ack);
            broker.convertAndSend("/topic/doc/" + docId + "/snapshot", updated);
        } catch (Exception e) {
            var ack = new ServerAck(docId, -1, "error", e.getMessage(), null);
            broker.convertAndSend("/topic/doc/" + docId, ack);
        }
    }

    // REST bootstrap/resync
    @GetMapping("/api/doc/{docId}")
    @ResponseBody
    public DocSnapshot get(@PathVariable String docId) {
        return service.get(docId);
    }
}
