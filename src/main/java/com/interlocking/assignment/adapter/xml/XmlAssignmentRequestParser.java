package com.interlocking.assignment.adapter.xml;

import com.interlocking.assignment.domain.model.assignment.TrainRequest;
import com.interlocking.assignment.domain.model.time.TimeInterval;
import com.interlocking.assignment.domain.model.track.Track;
import com.interlocking.assignment.domain.model.track.TrackOperationalStatus;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

public final class XmlAssignmentRequestParser {

    public XmlAssignmentInput parse(String xml) {
        try {
            var documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactory.setExpandEntityReferences(false);

            var document = documentBuilderFactory
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            var root = document.getDocumentElement();
            root.normalize();

            var request = new TrainRequest(
                    requiredAttribute(root, "id"),
                    new TimeInterval(
                            instantAttribute(root, "start"),
                            instantAttribute(root, "end")));

            return new XmlAssignmentInput(
                    request,
                    tracks(root),
                    optionalAttribute(root, "lastAssignedTrackId"));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid assignment XML", exception);
        }
    }

    private static List<Track> tracks(Element root) {
        var trackNodes = root.getElementsByTagName("track");
        var tracks = new ArrayList<Track>();
        for (var index = 0; index < trackNodes.getLength(); index++) {
            var trackElement = (Element) trackNodes.item(index);
            tracks.add(new Track(
                    requiredAttribute(trackElement, "id"),
                    TrackOperationalStatus.valueOf(requiredAttribute(trackElement, "status")),
                    occupations(trackElement)));
        }
        return tracks;
    }

    private static List<TimeInterval> occupations(Element trackElement) {
        var occupationNodes = trackElement.getElementsByTagName("occupation");
        var occupations = new ArrayList<TimeInterval>();
        for (var index = 0; index < occupationNodes.getLength(); index++) {
            var occupationElement = (Element) occupationNodes.item(index);
            occupations.add(new TimeInterval(
                    instantAttribute(occupationElement, "start"),
                    instantAttribute(occupationElement, "end")));
        }
        return occupations;
    }

    private static Instant instantAttribute(Element element, String name) {
        return Instant.parse(requiredAttribute(element, name));
    }

    private static Optional<String> optionalAttribute(Element element, String name) {
        if (!element.hasAttribute(name) || element.getAttribute(name).isBlank()) {
            return Optional.empty();
        }
        return Optional.of(element.getAttribute(name));
    }

    private static String requiredAttribute(Element element, String name) {
        if (!element.hasAttribute(name) || element.getAttribute(name).isBlank()) {
            throw new IllegalArgumentException("Missing required XML attribute: " + name);
        }
        return element.getAttribute(name);
    }
}
