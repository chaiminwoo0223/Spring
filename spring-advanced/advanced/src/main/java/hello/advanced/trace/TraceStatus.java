package hello.advanced.trace;

public record TraceStatus(
        TraceId traceId,
        Long startTimeMs,
        String message
) {
    public static TraceStatus from(TraceId traceId, Long startTimeMs, String message) {
        return new TraceStatus(traceId, startTimeMs, message);
    }
}
