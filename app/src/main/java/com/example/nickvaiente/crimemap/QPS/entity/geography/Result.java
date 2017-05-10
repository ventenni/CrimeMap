
package com.example.nickvaiente.crimemap.QPS.entity.geography;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "QldPostcodeId",
        "Postcode",
        "Locality",
        "GeometryWKT",
        "GeometryWKID",
        "QldSuburbId",
        "Name",
        "LGA",
        "Status",
        "LocalGovtAreaId",
        "Code",
        "QpsAreaId",
        "QpsAreaType",
        "ParentId",
})
public class Result {

    @JsonProperty("QldPostcodeId")
    private Integer qldPostcodeId;
    @JsonProperty("Postcode")
    private String postcode;
    @JsonProperty("Locality")
    private String locality;
    @JsonProperty("GeometryWKT")
    private Object geometryWKT;
    @JsonProperty("GeometryWKID")
    private Integer geometryWKID;
    @JsonProperty("QldSuburbId")
    private Integer qldSuburbId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("LGA")
    private String lGA;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("LocalGovtAreaId")
    private Integer localGovtAreaId;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("QpsAreaId")
    private Integer qpsAreaId;
    @JsonProperty("QpsAreaType")
    private Integer qpsAreaType;
    @JsonProperty("ParentId")
    private Integer parentId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("QldPostcodeId")
    public Integer getQldPostcodeId() {
        return qldPostcodeId;
    }

    @JsonProperty("QldPostcodeId")
    public void setQldPostcodeId(Integer qldPostcodeId) {
        this.qldPostcodeId = qldPostcodeId;
    }

    @JsonProperty("Postcode")
    public String getPostcode() {
        return postcode;
    }

    @JsonProperty("Postcode")
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @JsonProperty("Locality")
    public String getLocality() {
        return locality;
    }

    @JsonProperty("Locality")
    public void setLocality(String locality) {
        this.locality = locality;
    }

    @JsonProperty("GeometryWKT")
    public Object getGeometryWKT() {
        return geometryWKT;
    }

    @JsonProperty("GeometryWKT")
    public void setGeometryWKT(Object geometryWKT) {
        this.geometryWKT = geometryWKT;
    }

    @JsonProperty("GeometryWKID")
    public Integer getGeometryWKID() {
        return geometryWKID;
    }

    @JsonProperty("GeometryWKID")
    public void setGeometryWKID(Integer geometryWKID) {
        this.geometryWKID = geometryWKID;
    }

    @JsonProperty("QldSuburbId")
    public Integer getQldSuburbId() {
        return qldSuburbId;
    }

    @JsonProperty("QldSuburbId")
    public void setQldSuburbId(Integer qldSuburbId) {
        this.qldSuburbId = qldSuburbId;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("LGA")
    public String getLGA() {
        return lGA;
    }

    @JsonProperty("LGA")
    public void setLGA(String lGA) {
        this.lGA = lGA;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("LocalGovtAreaId")
    public Integer getLocalGovtAreaId() {
        return localGovtAreaId;
    }

    @JsonProperty("LocalGovtAreaId")
    public void setLocalGovtAreaId(Integer localGovtAreaId) {
        this.localGovtAreaId = localGovtAreaId;
    }

    @JsonProperty("Code")
    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("QpsAreaId")
    public Integer getQpsAreaId() {
        return qpsAreaId;
    }

    @JsonProperty("QpsAreaId")
    public void setQpsAreaId(Integer qpsAreaId) {
        this.qpsAreaId = qpsAreaId;
    }

    @JsonProperty("QpsAreaType")
    public Integer getQpsAreaType() {
        return qpsAreaType;
    }

    @JsonProperty("QpsAreaType")
    public void setQpsAreaType(Integer qpsAreaType) {
        this.qpsAreaType = qpsAreaType;
    }

    @JsonProperty("ParentId")
    public Integer getParentId() {
        return parentId;
    }

    @JsonProperty("ParentId")
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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