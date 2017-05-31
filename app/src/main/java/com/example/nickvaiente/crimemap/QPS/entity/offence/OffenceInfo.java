
package com.example.nickvaiente.crimemap.QPS.entity.offence;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

//  Store the offence information for a single offence.
//  The json object containing the offence details are retrieved form QPS.
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
        "QpsOffenceId",
        "OccurrenceNo",
        "QpsOffenceCode",
        "ReportDate",
        "StartDate",
        "EndDate",
        "OffenceCount",
        "Meshblockid",
        "Suburb",
        "Postcode",
        "Solved"
})
public class OffenceInfo {

    @JsonProperty("QpsOffenceId")
    private Integer qpsOffenceId;
    @JsonProperty("OccurrenceNo")
    private String occurrenceNo;
    @JsonProperty("QpsOffenceCode")
    private Integer qpsOffenceCode;
    @JsonProperty("ReportDate")
    private String reportDate;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("OffenceCount")
    private Integer offenceCount;
    @JsonProperty("Meshblockid")
    private Integer meshblockid;
    @JsonProperty("Suburb")
    private String suburb;
    @JsonProperty("Postcode")
    private String postcode;
    @JsonProperty("Solved")
    private Boolean solved;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("QpsOffenceId")
    public Integer getQpsOffenceId() {
        return qpsOffenceId;
    }

    @JsonProperty("QpsOffenceId")
    public void setQpsOffenceId(Integer qpsOffenceId) {
        this.qpsOffenceId = qpsOffenceId;
    }

    @JsonProperty("OccurrenceNo")
    public String getOccurrenceNo() {
        return occurrenceNo;
    }

    @JsonProperty("OccurrenceNo")
    public void setOccurrenceNo(String occurrenceNo) {
        this.occurrenceNo = occurrenceNo;
    }

    @JsonProperty("QpsOffenceCode")
    public Integer getQpsOffenceCode() {
        return qpsOffenceCode;
    }

    @JsonProperty("QpsOffenceCode")
    public void setQpsOffenceCode(Integer qpsOffenceCode) {
        this.qpsOffenceCode = qpsOffenceCode;
    }

    @JsonProperty("ReportDate")
    public String getReportDate() {
        return reportDate;
    }

    @JsonProperty("ReportDate")
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @JsonProperty("StartDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("StartDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("EndDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("EndDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("OffenceCount")
    public Integer getOffenceCount() {
        return offenceCount;
    }

    @JsonProperty("OffenceCount")
    public void setOffenceCount(Integer offenceCount) {
        this.offenceCount = offenceCount;
    }

    @JsonProperty("Meshblockid")
    public Integer getMeshblockid() {
        return meshblockid;
    }

    @JsonProperty("Meshblockid")
    public void setMeshblockid(Integer meshblockid) {
        this.meshblockid = meshblockid;
    }

    @JsonProperty("Suburb")
    public String getSuburb() {
        return suburb;
    }

    @JsonProperty("Suburb")
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    @JsonProperty("Postcode")
    public String getPostcode() {
        return postcode;
    }

    @JsonProperty("Postcode")
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @JsonProperty("Solved")
    public Boolean getSolved() {
        return solved;
    }

    @JsonProperty("Solved")
    public void setSolved(Boolean solved) {
        this.solved = solved;
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
