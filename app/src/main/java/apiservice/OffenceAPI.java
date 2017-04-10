package apiservice;

import org.springframework.web.client.RestTemplate;

import entity.offence.Boundary;

import static java.lang.String.format;

/**
 * Created by cherr on 5/04/2017.
 */

public class OffenceAPI {
    private static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%d";
    private RestTemplate restTemplate = new RestTemplate();

    public void offenceResults(String name, int maxResults){
        String url = format(OFFENCE_URL, name, maxResults);
        Boundary boundary = restTemplate.getForObject(url, Boundary.class);
        System.out.println(boundary.getResultCount());
    }
}
