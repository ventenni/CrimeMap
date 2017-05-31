package com.example.nickvaiente.crimemap.QPS.Offence;

import android.util.Log;

import org.springframework.web.client.RestTemplate;

import com.example.nickvaiente.crimemap.QPS.QueenslandPoliceService;
import com.example.nickvaiente.crimemap.QPS.entity.offence.OffenceBoundary;

import static java.lang.String.format;

/**
 * Created by cherr on 3/05/2017.
 */

public class RetrieveOffenceJSONTask
{
    public void doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate(true);
        try {
            // Performs QPS API call to retrieve offence details and put it directly into a offenceBoundary object
            OffenceBoundary offenceBoundary = restTemplate.getForObject(params[0], OffenceBoundary.class);

            // Stores the result in an offenceBoundary object in the QPS single instance to be accessed by other classes
            QueenslandPoliceService.getInstance().setOffenceBoundary(offenceBoundary);
            if ( offenceBoundary == null) {
                Log.i("OffenceBoundaryPostcode", "No postcode found or Multiple Postcodes");
            }

        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
    }
}
