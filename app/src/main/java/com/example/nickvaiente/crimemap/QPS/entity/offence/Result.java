package com.example.nickvaiente.crimemap.QPS.entity.offence;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
        "MeshBlockId",
        "OffenceInfo",
        "GeometryWKT",
        "GeometryWKID"
})
public class Result {

    @JsonProperty("MeshBlockId")
    private Integer meshBlockId;
    @JsonProperty("OffenceInfo")
    private List<OffenceInfo> offenceInfo = null;
    @JsonProperty("GeometryWKT")
    private String geometryWKT;
    @JsonProperty("GeometryWKID")
    private Integer geometryWKID;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("MeshBlockId")
    public Integer getMeshBlockId() {
        return meshBlockId;
    }

    @JsonProperty("MeshBlockId")
    public void setMeshBlockId(Integer meshBlockId) {
        this.meshBlockId = meshBlockId;
    }

    @JsonProperty("OffenceInfo")
    public List<OffenceInfo> getOffenceInfo() {
        return offenceInfo;
    }

    @JsonProperty("OffenceInfo")
    public void setOffenceInfo(List<OffenceInfo> offenceInfo) {
        this.offenceInfo = offenceInfo;
    }

    @JsonProperty("GeometryWKT")
    public String getGeometryWKT() {
        return geometryWKT;
    }

    @JsonProperty("GeometryWKT")
    public void setGeometryWKT(String geometryWKT) {
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
