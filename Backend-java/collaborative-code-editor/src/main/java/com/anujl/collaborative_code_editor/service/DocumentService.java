package com.anujl.collaborative_code_editor.service;


import com.anujl.collaborative_code_editor.dto.wc.DocSnapshot;
import com.anujl.collaborative_code_editor.dto.wc.EditMessage;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DocumentService {

    public static class DocState {
        public final AtomicLong version = new AtomicLong(0);
        public volatile StringBuilder text = new StringBuilder();
    }

    private final Map<String, DocState> docs = new ConcurrentHashMap<>();

    public DocSnapshot get(String docId) {
        DocState s = docs.computeIfAbsent(docId, k -> new DocState());
        return new DocSnapshot(docId, s.text.toString(), s.version.get());
    }

    public synchronized DocSnapshot apply(String docId, @Valid EditMessage msg) throws IllegalArgumentException {
        DocState s = docs.computeIfAbsent(docId, k -> new DocState());
        long curVer = s.version.get();
        if (msg.getBaseVersion() != curVer) {
            return null; // caller should resync
        }
        msg.getOps().forEach(
                a->{
                    System.out.println("op: " + a.getType() + " index: " + a.getIndex() + " length: " + a.getLength() + " text: " + a.getText());
                }
        );
        for (EditMessage.Op op : msg.getOps()) {
            switch (op.getType()) {
                case "insert" -> insert(s.text, op.getIndex(), nullSafe(op.getText()));
                case "delete" -> delete(s.text, op.getIndex(), op.getLength());
                case "replace" -> { delete(s.text, op.getIndex(), op.getLength()); insert(s.text, op.getIndex(), nullSafe(op.getText())); }
                default -> throw new IllegalArgumentException("bad op");
            }
        }
        long newVer = s.version.incrementAndGet();
        System.out.println("Document " + docId + " updated to version " + newVer + ": " + s.text);
        return new DocSnapshot(docId, s.text.toString(), newVer);
    }

    private static String nullSafe(String t) { return t == null ? "" : t; }

    private static void insert(StringBuilder sb, Integer index, String text) {
        if (index == null || index < 0 || index > sb.length()) throw new IllegalArgumentException("bad index");
        sb.insert(index, text);
    }

    private static void delete(StringBuilder sb, Integer index, Integer length) {
        if (index == null || length == null) throw new IllegalArgumentException("bad delete");
        int end = index + length;
        if (index < 0 || end > sb.length()) throw new IllegalArgumentException("range");
        sb.delete(index, end);
    }
}
