package com.furkan.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class YouTubeSearchController2 {

    @Value("${youtube.api.key}")
    private String apiKey;

    @GetMapping("/get")
    public ResponseEntity<List<YouTubeVideoInformation>> getVideoIds(@RequestParam String q) throws IOException {
        String apiUrl = "https://www.googleapis.com/youtube/v3/search";
        String type = "video";

        String searchKey = q.replace(" ", "+");

        String url = String.format("%s?part=snippet&key=%s&q=%s&type=%s", apiUrl, apiKey, searchKey, type);

        //System.out.println("url: " + url);

        String responseJSON = IOUtils.toString(URI.create(url), StandardCharsets.UTF_8);

        //System.out.println(responseJSON);

        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        YouTubeSearchListResponse response = objectMapper.readValue(responseJSON, YouTubeSearchListResponse.class);

        List<YouTubeVideoInformation> videoInformations = new ArrayList<>();

        for (YouTubeSearchResult youTubeSearchResult : response.getItems()) {
            //System.out.println(youTubeSearchResult.getId().getVideoId());

            //System.out.println(youTubeSearchResult.getSnippet().getTitle());

            String videoID = youTubeSearchResult.getId().getVideoId();
            String videoURL = youTubeSearchResult.getSnippet().getThumbnails().getDefaultThumbnail().getUrl();
            String videoTitle = youTubeSearchResult.getSnippet().getTitle();
            YouTubeVideoInformation youTubeVideoInformation = new YouTubeVideoInformation(videoID, videoURL, videoTitle);
            videoInformations.add(youTubeVideoInformation);
        }

        for (YouTubeVideoInformation item: videoInformations) {
            System.out.println(item.toString());
        }

        return new ResponseEntity<>(videoInformations, HttpStatus.OK);
    }
}