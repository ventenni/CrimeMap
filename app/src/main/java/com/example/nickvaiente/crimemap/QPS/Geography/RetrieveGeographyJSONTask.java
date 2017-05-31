package com.example.nickvaiente.crimemap.QPS.Geography;

import android.util.Log;

import org.springframework.web.client.RestTemplate;

import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.geography.Success;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class RetrieveGeographyJSONTask
{
    private RestTemplate restTemplate;

    public void doInBackground(String... params) {
        restTemplate = new RestTemplate(true);
        try {
//          Performs API call to the QPS servers and put it directly into a Success object
            Success success = restTemplate.getForObject(params[0], Success.class);

//          Storing the result in the QPS singleton instance success variable to be accessed by
//          other classes.
            QueenslandPoliceService.getInstance().setSuccess(success);

        } catch(Exception ex) {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
    }
}
