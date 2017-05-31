
package com.example.nickvaiente.crimemap.QPS.entity.geography;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  Store the confirmation details for a searched location.
//  The json object containing the confirmation details are retrieved form QPS.
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Success",
    "Message",
    "Result",
    "ResultCount"
})
//  Store the details even if some items are missing or there are attributes in the JSON that are
//  not specified in the in the class below
@JsonIgnoreProperties(ignoreUnknown = true)
public class Success {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonProperty("Success")
    private Boolean success;
    @JsonProperty("Message")
    private Object message;
    @JsonProperty("Result")
    private List<Result> result = null; //  Contains matches based on area eg. suburb, local government area etc.
    @JsonProperty("ResultCount")
    private Integer resultCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("Success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("Message")
    public Object getMessage() {
        return message;
    }

    @JsonProperty("Message")
    public void setMessage(Object message) {
        this.message = message;
    }

    @JsonProperty("Result")
    public List<Result> getResult() {
        return result;
    }

    @JsonProperty("Result")
    public void setResult(List<Result> result) {
        this.result = result;
    }

    @JsonProperty("ResultCount")
    public Integer getResultCount() {
        return resultCount;
    }

    @JsonProperty("ResultCount")
    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
