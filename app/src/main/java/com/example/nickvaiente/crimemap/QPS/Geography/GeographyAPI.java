package com.example.nickvaiente.crimemap.QPS.Geography;

import android.util.Log;

import org.springframework.web.client.RestTemplate;

import entity.geography.Success;

import static java.lang.String.format;

public class GeographyAPI {

    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%d&longitude=%d&maxresults=";

    public static void Main(String what){
        RestTemplate restTemplate = new RestTemplate();

        String name = "Rock";
        Double latitude = -27.5;
        Double longitude = 153.0;
        int maxResults = 10;
        System.out.println("Hello");

        //uses the Name_Url constant declared above and adds the variables I stated just above too
        //Using this getForObject Method it should translate whatevers in the API to Objects that I've
        //created in the entity package.
        String url = format(NAME_URL, name, maxResults);
        try {
            //this line crashes my Android VM but try it on you machines and see if it works.
            Success success = restTemplate.getForObject(url, Success.class);
            Log.i("Message", success.getResultCount().toString());

            //Just another example for the getForObject Method but with location entered with lats and longs
            url = format(LOCATION_URL, latitude, longitude, maxResults);
            Success success2 = restTemplate.getForObject(url, Success.class);
            Log.i("Message2", success2.getResultCount().toString());
        } catch (Exception ex){
            Log.i("Error", ex.getMessage());
            ex.printStackTrace();
        }
//        OffenceAPI offenceAPI = new OffenceAPI();
//        offenceAPI.offenceResults(name, maxResults);
    }
}
