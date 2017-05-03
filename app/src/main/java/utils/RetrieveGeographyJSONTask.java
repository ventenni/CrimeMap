package utils;

import android.os.AsyncTask;
import android.util.Log;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import entity.geography.Success;
import entity.offence.Boundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class RetrieveGeographyJSONTask extends AsyncTask<String,Void,Void>
{
    private static final String NAME_URL = "https://data.police.qld.gov.au/api/boundary?name=%s&returngeometry=true&maxresults=%s";
    private static final String LOCATION_URL = "https://data.police.qld.gov.au/api/boundary?latitude=%d&longitude=%d&maxresults=";
    public static final String OFFENCE_URL = "https://data.police.qld.gov.au/api/qpsmeshblock?boundarylist=1_6989,1_6428&startdate=1364466223&enddate=1372415023&offences=8,14";

    @Override
    protected Void doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate(true);
//        restTemplate.getMessageConverters().add(messageConverter);
//        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());



        try

        {
            //this line crashes my Android VM but try it on you machines and see if it works.
            Success success = restTemplate.getForObject(params[0], Success.class);
            Log.i("Result", success.getResult().get(0).getLGA());

            //Just another example for the getForObject Method but with location entered with lats and longs
            Success success2 = restTemplate.getForObject(params[1], Success.class);
            Log.i("Result2", success2.getResult().get(0).getLocality());

            //Boundary class example
            Boundary boundary = restTemplate.getForObject(OFFENCE_URL, Boundary.class);
            Log.i("Boundary", boundary.getResult().get(0).getOffenceInfo().get(0).getPostcode());
        } catch(
                Exception ex)

        {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
