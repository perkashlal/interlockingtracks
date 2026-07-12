package com.interlocking.assignment.adapter.web;

import com.interlocking.assignment.adapter.xml.XmlAssignmentRequestParser;
import com.interlocking.assignment.application.assignment.AssignmentService;
import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.time.TimeInterval;
import com.interlocking.assignment.domain.model.track.Track;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String assignmentHelpPage() {
        return """
                <!doctype html>
                <html lang=\"en\">
                <head>
                    <meta charset=\"utf-8\">
                    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">
                    <title>Assignment API</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; background: #f6f8fb; color: #1f2937; }
                        main { max-width: 880px; margin: auto; background: white; padding: 32px; border-radius: 18px; box-shadow: 0 12px 32px rgba(15,23,42,.10); }
                        code, pre { background: #111827; color: #e5e7eb; border-radius: 10px; }
                        code { padding: 2px 6px; }
                        pre { padding: 18px; overflow-x: auto; }
                        a { color: #2563eb; }
                    </style>
                </head>
                <body>
                <main>
                    <h1>Train Assignment API</h1>
                    <p>This endpoint is working. It accepts train assignment requests using <strong>POST</strong>.</p>
                    <p>For the visual project page, open <a href=\"/\">localhost:8080</a>.</p>
                    <p>Health check: <a href=\"/actuator/health\">/actuator/health</a></p>
                    <h2>CMD test command</h2>
                    <pre>curl -X POST http://localhost:8080/api/assignments ^
  -H "Content-Type: application/xml" ^
  -H "Accept: application/json" ^
  --data-binary @src/main/resources/examples/assignment-request.xml</pre>
                </main>
                </body>
                </html>
                """;
    }

    private final AssignmentService assignmentService;
    private final XmlAssignmentRequestParser xmlParser;

    public AssignmentController(
            AssignmentService assignmentService,
            XmlAssignmentRequestParser xmlParser) {
        this.assignmentService = assignmentService;
        this.xmlParser = xmlParser;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AssignmentResponse assign(@RequestBody AssignmentRequest request) {
        var decision = assignmentService.assign(
                new TrainRequest(
                        request.requestId(),
                        new TimeInterval(request.start(), request.end())),
                toDomainTracks(request.tracks()),
                Optional.ofNullable(request.lastAssignedTrackId()));

        return new AssignmentResponse(
                decision.outcome().name(),
                decision.assignedTrackId().orElse(null),
                decision.reasons());
    }

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AssignmentResponse assignXml(@RequestBody String xml) {
        var input = xmlParser.parse(xml);
        var decision = assignmentService.assign(
                input.request(),
                input.tracks(),
                input.lastAssignedTrackId());

        return new AssignmentResponse(
                decision.outcome().name(),
                decision.assignedTrackId().orElse(null),
                decision.reasons());
    }

    private static List<Track> toDomainTracks(List<AssignmentRequest.TrackCandidate> tracks) {
        return tracks.stream()
                .map(track -> new Track(
                        track.id(),
                        track.status(),
                        toTimeIntervals(track.occupations())))
                .toList();
    }

    private static List<TimeInterval> toTimeIntervals(List<AssignmentRequest.Occupation> occupations) {
        return occupations.stream()
                .map(occupation -> new TimeInterval(occupation.start(), occupation.end()))
                .toList();
    }
}

