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
            //OffenceBoundary class example
            OffenceBoundary offenceBoundary = restTemplate.getForObject(params[0], OffenceBoundary.class);
            QueenslandPoliceService.getInstance().setOffenceBoundary(offenceBoundary);
            if ( offenceBoundary != null && offenceBoundary.getResult().get(0).getOffenceInfo().get(0).getPostcode() != null) {
                Log.i("OffenceBoundaryPostcode", offenceBoundary.getResult().get(0).getOffenceInfo().get(0).getPostcode());
            } else {
                Log.i("OffenceBoundaryPostcode", "No postcode found or Multiple Postcodes");
            }

        } catch(
                Exception ex)

        {
            Log.e("Error", ex.getMessage());
            ex.printStackTrace();
        }
    }
}
